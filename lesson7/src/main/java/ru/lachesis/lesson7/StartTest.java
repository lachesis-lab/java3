package ru.lachesis.lesson7;

import org.apache.commons.lang3.ClassUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class StartTest {
    static Map<Method, Integer> invokeMethodMap = new HashMap<>();
    static Object aClassObj;

    static void start(Class testClass) throws RuntimeException {

        try {
            aClassObj = testClass.getConstructor().newInstance();
            Method[] allMethods = testClass.getDeclaredMethods();
            addSuitMethods(allMethods, BeforeSuit.class);
            addTestMethods(allMethods);
            addSuitMethods(allMethods, AfterSuit.class);
            invokeMethods();
        } catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException | RuntimeException e) {
            e.printStackTrace();
        }
    }

    private static void invokeMethods() {
        invokeMethodMap.entrySet().stream().sorted(Map.Entry.comparingByValue()).forEach(entry -> {
            try {
                invokeMethod(entry.getKey());
            } catch (InvocationTargetException | IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }
        });


    }

    private static void invokeMethod(Method method) throws InvocationTargetException, IllegalAccessException, InstantiationException {
        List<Object> args = getArgsForMethod(method);
        getArgsForMethod(method);
        method.invoke(aClassObj, args.toArray());

    }

    private static ArrayList<Object> getArgsForMethod(Method method) throws IllegalAccessException, InstantiationException {
        ArrayList<Object> args = new ArrayList<>();
        Class<?>[] argTypes = method.getParameterTypes();
        Object argInstance = null;
        for (Class<?> argClass : argTypes) {
            if (ClassUtils.isPrimitiveOrWrapper(argClass)) {
                if (argClass.isPrimitive())
                    argClass = ClassUtils.primitiveToWrapper(argClass);
                args.add(initPrimitive(argClass.getSimpleName()));
            } else {
                argInstance = argClass.newInstance();
                args.add(argInstance);
            }
        }
        return args;
    }

    private static Object initPrimitive(String nameClass) {
        switch (nameClass) {
            case "Byte", "Short", "Integer", "Double" -> {return(0);}
            case "Long" -> {return(0L);}
            case "Float" -> {return(0F);}
            case "Character" -> {return('\u0000');}
            case "Boolean" -> {return false;}
        }
        return 0;
    }

    private static void addTestMethods(Method[] allMethods) {
        Object[] methodArr = Arrays.stream(allMethods).filter(m -> m.isAnnotationPresent(Test.class)).toArray();
        Method method;
        for (Object obj : methodArr) {
            method = (Method) obj;
            method.setAccessible(true);
            int priority = method.getAnnotation(Test.class).priority();
            invokeMethodMap.put(method, priority);
        }
    }

    private static void addSuitMethods(Method[] allMethods, Class<? extends Annotation> annotationClass) {
        Object[] methodArr = Arrays.stream(allMethods).filter(m -> m.isAnnotationPresent(annotationClass)).toArray();
        if (methodArr.length == 1) {
            invokeMethodMap.put((Method) methodArr[0], invokeMethodMap.size());
        } else if (methodArr.length != 0) throw new RuntimeException();
    }
}


