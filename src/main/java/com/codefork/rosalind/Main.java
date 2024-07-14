package com.codefork.rosalind;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Main {

    public static Map<String, Problem> getProblems() throws IOException {
        // adapted from https://stackoverflow.com/questions/1810614/getting-all-classes-from-a-package/1811120#1811120
        Map<String, Problem> problems = new HashMap<>();

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(
                null, null, null);

        StandardLocation location = StandardLocation.CLASS_PATH;
        String packageName = "com.codefork.rosalind.problems";
        Set<JavaFileObject.Kind> kinds = new HashSet<>();
        kinds.add(JavaFileObject.Kind.CLASS);
        boolean recurse = false;

        Iterable<JavaFileObject> list = fileManager.list(location, packageName,
                kinds, recurse);

        for (JavaFileObject classFile : list) {
            String name = classFile.getName().replaceAll(".*/|[.]class.*","");

            // skip inner classes
            if(name.contains("$"))
                continue;

            Class clazz;
            try {
                clazz = Class.forName(packageName + "." + name);
            } catch(ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

            // skip non-Problem classes
            if(!Problem.class.isAssignableFrom(clazz))
                continue;

            try {
                var problem = (Problem) clazz.getConstructors()[0].newInstance(null);
                problems.put(problem.getID(), problem);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return problems;
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: rosalind-solutions problem-id");
            System.exit(1);
        }
        String problemId = args[0];

        Map<String, Problem> problems = null;
        try {
            problems = getProblems();
        } catch(IOException e) {
            System.out.println("Error loading problems: " + e);
            System.exit(1);
        }
        var problem = problems.get(problemId);
        if(problem == null) {
            System.out.println("Error: Problem " + problemId + " doesn't exist ");
            System.exit(1);
        }
        problem.run();
    }
}