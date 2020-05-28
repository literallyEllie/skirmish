package net.mcskirmish.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class UtilReflect {

    public static Field getField(Class<?> clazz, String fieldName) {
        try {
            Field f = clazz.getDeclaredField(fieldName);
            f.setAccessible(true);
            return f;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Object getField(Class<?> clazz, String fieldName, Object instance) {
        try {
            Field f = clazz.getDeclaredField(fieldName);
            f.setAccessible(true);
            return f.get(instance);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Object getField(Object instance, Field field) {
        try {
            return field.get(instance);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void setField(Object instance, String fieldName, Object value) {
        try {
            Field field = instance.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(instance, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static void setField(Object instance, Field field, Object value) {
        try {
            field.set(instance, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static Method getMethod(Class<?> clazz, String methodName, Class<?>... arguments) {
        try {
            final Method declaredMethod = clazz.getDeclaredMethod(methodName, arguments);
            declaredMethod.setAccessible(true);
            return declaredMethod;
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    public static void invokeMethod(Method method, Object instance, Object... parameters) {
        try {
            method.invoke(instance, parameters);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

}
