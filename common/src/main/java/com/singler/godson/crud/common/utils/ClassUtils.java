package com.singler.godson.crud.common.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 与类相关的工具方法
 *
 * @author maenfang1
 * @version 1.0
 * @date 2020/11/3 10:57
 */
public class ClassUtils extends org.springframework.util.ClassUtils {

    public static final Class<?>[] EMPTY_CLASSES = new Class<?>[0];

    public static Class<?> getGenericClass(Class<?> clazz) {
        Class<?>[] genericClasses = getGenericClasses(clazz);
        if (genericClasses.length == 1) {
            return genericClasses[0];
        }
        return null;
    }

    public static Class<?>[] getGenericClasses(Class<?> clazz) {
        return getGenericClasses(clazz.getGenericSuperclass());
    }

    public static Class<?> getGenericClass(Class<?> clazz, Class<?> superInterfaceClass) {
        Class<?>[] genericClasses = getGenericClasses(clazz, superInterfaceClass);
        if (genericClasses.length == 1) {
            return genericClasses[0];
        }
        return null;
    }

    public static Class<?>[] getGenericClasses(Class<?> clazz, Class<?> superInterfaceClass) {
        Type[] types = clazz.getGenericInterfaces();
        for (Type type : types) {
            if (type instanceof Class) {
                Class<?> typeClass = (Class) type;
                if (typeClass.equals(superInterfaceClass)) {
                    return getGenericClasses(type);
                }
            }
        }
        return EMPTY_CLASSES;
    }


    public static Class<?>[] getGenericClasses(Type type) {
        if  (type != null && type instanceof ParameterizedType) {
            ParameterizedType pType = (ParameterizedType) type;
            Type[] types = pType.getActualTypeArguments();
            Class<?>[] classes = new Class<?>[types.length];
            for (int i = 0; i < types.length; i++) {
                if (types[i] instanceof Class) {
                    classes[i] = (Class) types[i];
                }
            }
            return classes;
        }
        return EMPTY_CLASSES;
    }
}
