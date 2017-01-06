package com.commons.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class ClassUtil {

    /**
     * 根据class的name取得对象实例
     * @param className
     * @return
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static Object getClassInstance(String className)
            throws ClassNotFoundException, IllegalAccessException,
            InstantiationException {
        Class clazz = getDefaultClassLoader().loadClass(className);
        return clazz.newInstance();
    }

    /**
     * 根据传入的对象实例,方法名称,方法参数取得对象的方法

     * @param obj
     * @param methodName
     * @param param
     * @return
     * @throws NoSuchMethodException
     */
    public static Method getMethod(Object obj, String methodName,
                                   Class... param) throws NoSuchMethodException {

        return obj.getClass().getMethod(methodName, param);

    }
    /**
     * 取得当前默认的classLoader
     * @return
     */
    public static ClassLoader getDefaultClassLoader() {
        ClassLoader classLoader = null;
        try {
            classLoader = Thread.currentThread().getContextClassLoader();
        }
        catch (Throwable ex) {
            return null;
        }
        if (classLoader == null) {
            classLoader = ClassUtil.class.getClassLoader();
        }
        return classLoader;
    }

    public static boolean hasMethod(Class clazz, String methodName, Class[] paramTypes) {
        return (getMethodIfAvailable(clazz, methodName, paramTypes) != null);
    }


    public static Method getMethodIfAvailable(Class clazz, String methodName, Class[] paramTypes) {

        try {
            return clazz.getMethod(methodName, paramTypes);
        }
        catch (NoSuchMethodException ex) {
            return null;
        }
    }

    /**
     * 通过反射绕过java的访问控制，向对象中SET属性。
     * @param target
     * @param fname
     * @param ftype
     * @param fvalue
     */
    public static void setFieldValue(Object target, String fname, Class ftype, Object fvalue) {
        if (target == null || fname == null || "".equals(fname)
                || (fvalue != null && !ftype.isAssignableFrom(fvalue.getClass()))) {
            return;
        }
        Class clazz = target.getClass();
        try {
            Method method = clazz.getDeclaredMethod("set" + Character.toUpperCase(fname.charAt(0)) + fname.substring(1), ftype);
            if (!Modifier.isPublic(method.getModifiers())) {
                method.setAccessible(true);
            }
            method.invoke(target, fvalue);

        } catch (Exception me) {
            try {
                Field field = clazz.getDeclaredField(fname);
                if (!Modifier.isPublic(field.getModifiers())) {
                    field.setAccessible(true);
                }
                field.set(target, fvalue);
            } catch (Exception fe) {
                fe.printStackTrace();
            }
        }
    }
}
