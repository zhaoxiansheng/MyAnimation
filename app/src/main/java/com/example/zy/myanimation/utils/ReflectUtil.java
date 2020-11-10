package com.example.zy.myanimation.utils;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by lenovo on 2019/1/9.
 */

public class ReflectUtil {

    /**
     * @param   clazz the class to check
     * @param   fildName the filedID to check
     * @return  this object member
     *
     */
    public static Field getClassFiled(Class<?> clazz, String fildName)
    {
        Field paramField = null;
        try {
            paramField = clazz.getDeclaredField(fildName);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        return paramField;
    }

    /**
     * @param   obj the object to check
     * @param   fildName the filedname to check
     * @return  this object member
     *
     */
    public static Object getObjectByFiled(Object obj, String fildName)
    {
        if(obj == null)
            return null;

        Class<?> clazz = obj.getClass();

        Field paramField = null;
        try {
            paramField = clazz.getDeclaredField(fildName);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        if(paramField == null)
            return null;

        paramField.setAccessible(true);
        try {
            return paramField.get(obj);// 获取gDefault实例，由于是静态类型的属性，所以这里直接传递null参数
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param   clazz the class contained fildName
     * @param   obj the object to check
     * @param   fildName the filedID to check
     * @return  this object member
     *
     */
    public static Object getObjectByFiled(Class<?> clazz, Object obj, String fildName)
    {
        Field paramField = null;
        try {
            paramField = clazz.getDeclaredField(fildName);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        if(paramField == null)
            return null;

        paramField.setAccessible(true);
        try {
            return paramField.get(obj);// 获取gDefault实例，由于是静态类型的属性，所以这里直接传递null参数
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param   obj the object to check
     * @param   fildName the filedID to check
     * @return  this object member
     *
     */
    public static Object getSuperObjectByFiled(Object obj, String fildName)
    {
        if(obj == null)
            return null;
        Field paramField = null;
        Class<?> clazz = obj.getClass().getSuperclass();
        if(clazz == null)
            return null;
        try {
            paramField = clazz.getDeclaredField(fildName);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        if(paramField == null)
            return null;

        paramField.setAccessible(true);
        try {
            return paramField.get(obj);// 获取gDefault实例，由于是静态类型的属性，所以这里直接传递null参数
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param   obj the object to check
     * @param   paramField the filedID to check
     * @return  this object member
     *
     */
    public static Object getObjectByFiled(Object obj, Field paramField)
    {
        if(paramField == null)
            return null;

        paramField.setAccessible(true);
        try {
            return paramField.get(obj);// 获取gDefault实例，由于是静态类型的属性，所以这里直接传递null参数
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param   obj the object to check
     * @param   val the value to check
     * @param   fildName the filedID to check
     * @return  if true set succeed
     *
     */
    public static boolean setObjectByFiled(Object obj, Object val, String fildName)
    {
        if(obj == null)
            return false;

        Class<?> clazz = obj.getClass();

        Field paramField = null;
        try {
            paramField = clazz.getDeclaredField(fildName);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        if(paramField == null)
            return false;

        paramField.setAccessible(true);
        try {
            paramField.set(obj, val);// 获取gDefault实例，由于是静态类型的属性，所以这里直接传递null参数
            return true;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @param   clazz the class to check
     * @param   obj the object to check
     * @param   val the value to check
     * @param   fildName the filedID to check
     * @return  if true set succeed
     *
     */
    public static boolean setObjectByFiled(Class<?> clazz, Object obj, Object val, String fildName)
    {
        Field paramField = null;
        try {
            paramField = clazz.getDeclaredField(fildName);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        if(paramField == null)
            return false;

        paramField.setAccessible(true);
        try {
            paramField.set(obj, val);// 获取gDefault实例，由于是静态类型的属性，所以这里直接传递null参数
            return true;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @param   obj the object to check
     * @param   val the value to check
     * @param   fildName the filedID to check
     * @return  if true set succeed
     *
     */
    public static boolean setSuperObjectByFiled(Object obj, Object val, String fildName)
    {
        Field paramField = null;
        Class<?> clazz = obj.getClass().getSuperclass();
        if(clazz == null)
            return false;
        try {
            paramField = clazz.getDeclaredField(fildName);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        if(paramField == null)
            return false;

        paramField.setAccessible(true);
        try {
            paramField.set(obj, val);// 获取gDefault实例，由于是静态类型的属性，所以这里直接传递null参数
            return true;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @param   obj the object to check
     * @param   val the value to check
     * @param   paramField the filedID to check
     * @return  if true set succeed
     *
     */
    public static boolean setObjectByFiled(Object obj, Object val, Field paramField)
    {
        if(paramField == null)
            return false;

        paramField.setAccessible(true);
        try {
            paramField.set(obj, val);// 获取gDefault实例，由于是静态类型的属性，所以这里直接传递null参数
            return true;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @param   className the class to check
     * @return  Class named className
     *
     * construct a class with initilize the class
     */
    public static Class<?> getClazz(String className)
    {
        Class<?> clazz = null;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return clazz;
    }

    /**
     * @param   className the class to check
     * @param   loader the classloader to load class
     * @return  Class named className
     *
     */
    public static Class<?> getClazz(String className, boolean bInit, ClassLoader loader)
    {
        Class<?> clazz = null;
        try {
            clazz = Class.forName(className, bInit, loader);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return clazz;
    }

    /**
     * @param cls         此方法所在类
     * @param methodName  方法名称
     * @param params      调用的这个方法的参数参数类列表
     */
    public static Method getMethod(Class<?> cls, String methodName, Class<?> params[])
    {
        Method method;
        try {
            method = cls.getDeclaredMethod(methodName, params);
            return method;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Constructor getConstructor(Class<?> cls, Class<?> clazzs[])
    {
        if(cls == null)
            return null;
        //获取构造器
        Constructor constructor = null;
        try {
            constructor = cls.getDeclaredConstructor(clazzs);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return constructor;
    }

    /**
     * @param methodName  方法名称
     * @param cls         此方法所在类
     * @param obj         调用此方法的对象
     * @param args       调用的这个方法的参数参数列表
     */
    public static boolean callMethod(String methodName, Class<?> cls, Object obj, Object[] args, Object[] ret){
        Class<?> clazzs[] = null;

        //1.参数存在
        if(args != null){
            int len = args.length;
            clazzs = new Class[len];

            //2.根据参数得到相应的 Class的类对象
            for(int i=0; i<len; ++i){
                clazzs[i] = args[i].getClass();
            }
        }else
        {
            clazzs = new Class[0];
            args = new Object[]{};//无参数调用
        }

        //3.根据方法名，参数Class类对象数组，得到Method
        Method method = null;
        try {
            method = cls.getDeclaredMethod(methodName, clazzs);
            method.setAccessible(true);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return false;
        }

        //4.通过方法所在的类，和具体的参数值，调用相应的方法
        try {
            if(ret != null)
                ret[0] = method.invoke(obj, args);//调用o对象的方法
            else
                method.invoke(obj, args);//调用o对象的方法
            return true;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @param methodName  方法名称
     * @param obj           调用此方法的对象
     * @param args       调用的这个方法的参数参数列表
     */
    public static boolean callMethod(String methodName, Object obj, Object[] args, Object[] ret){
        Class<?> clazzs[] = null;

        //1.参数存在
        if(args != null){
            int len = args.length;
            clazzs = new Class[len];

            //2.根据参数得到相应的 Class的类对象
            for(int i=0; i<len; ++i){
                clazzs[i] = args[i].getClass();
            }
        }else
        {
            clazzs = new Class[0];
            args = new Object[]{};//无参数调用
        }

        //3.根据方法名，参数Class类对象数组，得到Method
        Method method = null;
        try {
            method = obj.getClass().getDeclaredMethod(methodName, clazzs);
            method.setAccessible(true);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return false;
        }

        //4.通过方法所在的类，和具体的参数值，调用相应的方法
        try {
            if(ret != null)
                ret[0] = method.invoke(obj, args);//调用o对象的方法
            else
                method.invoke(obj, args);//调用o对象的方法
            return true;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @param methodName  方法名称
     * @param obj           调用此方法的对象
     * @param args       调用的这个方法的参数参数列表
     */
    public static boolean callMethod(String methodName, Class<?> cls, Object obj, Class<?> clazzs[], Object[] args, Object[] ret){
        //1.参数不存在
        if(args == null){
            clazzs = new Class[0];
            args = new Object[]{};//无参数调用
        }

        //3.根据方法名，参数Class类对象数组，得到Method
        Method method = null;
        try {
            method = cls.getDeclaredMethod(methodName, clazzs);
            method.setAccessible(true);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return false;
        }

        //4.通过方法所在的类，和具体的参数值，调用相应的方法
        try {
            if(ret != null)
                ret[0] = method.invoke(obj, args);//调用o对象的方法
            else
                method.invoke(obj, args);//调用o对象的方法
            return true;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @param methodName  方法名称
     * @param obj           调用此方法的对象
     * @param args       调用的这个方法的参数参数列表
     */
    public static boolean callMethod(String methodName, Object obj, Class<?> clazzs[], Object[] args, Object[] ret){
        //1.参数不存在
        if(args == null){
            clazzs = new Class[0];
            args = new Object[]{};//无参数调用
        }

        //3.根据方法名，参数Class类对象数组，得到Method
        Method method = null;
        try {
            method = obj.getClass().getDeclaredMethod(methodName, clazzs);
            method.setAccessible(true);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return false;
        }

        //4.通过方法所在的类，和具体的参数值，调用相应的方法
        try {
            if(ret != null)
                ret[0] = method.invoke(obj, args);//调用o对象的方法
            else
                method.invoke(obj, args);//调用o对象的方法
            return true;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @param method        此方法
     * @param obj           调用此方法的对象
     * @param args          调用的这个方法的参数参数列表
     * @param ret           此方法返回值
     */
    public static boolean callMethod(Method method, Object obj, Object[] args, Object[] ret){

        if(method == null)
            return false;

        method.setAccessible(true);

        //3.通过方法所在的类，和具体的参数值，调用相应的方法
        try {
            if(ret != null)
                ret[0] = method.invoke(obj, args);//调用o对象的方法
            else
                method.invoke(obj, args);//调用o对象的方法
            return true;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Object createObject(String clsName, Object[] args)
    {
        if(clsName == null)
            return null;

        Class<?> cls = getClazz(clsName);
        if(cls == null)
            return null;

        Object newSubject = null;

        Class clazzs[] = null;
        if(args != null) {
            int len = args.length;
            clazzs = new Class[len];

            //1.根据参数得到相应的 Class的类对象
            for (int i = 0; i < len; ++i) {
                clazzs[i] = args[i].getClass();
            }
        }

        //获取构造器
        Constructor constructor = null;
        try {
            constructor = cls.getDeclaredConstructor(clazzs);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        if(constructor != null) {
            constructor.setAccessible(true);
            try {
                newSubject = constructor.newInstance(args);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return newSubject;
    }

    public static Object createObject(String clsName, Class<?> clazzs[], Object[] args)
    {
        if(clsName == null)
            return null;

        Class<?> cls = getClazz(clsName);
        if(cls == null)
            return null;

        Object newSubject = null;
        //获取构造器
        Constructor constructor = null;
        try {
            constructor = cls.getDeclaredConstructor(clazzs);
        } catch (NoSuchMethodException e) {
            /*Constructor<?> cons[] = cls.getConstructors();//获取构造函数的数组
            //打印
            for (Constructor<?> constructor_ : cons) {
                Log.e(TAG, "JJJ constructor: " + constructor_);
            }*/
            e.printStackTrace();
        }
        if(constructor != null) {
            constructor.setAccessible(true);
            try {
                newSubject = constructor.newInstance(args);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return newSubject;
    }

    public static Object createObject(Class<?> cls, Object[] args)
    {
        if(cls == null)
            return null;
        Object newSubject = null;

        Class<?> clazzs[] = null;

        if(args != null) {
            int len = args.length;
            clazzs = new Class[len];

            //1.参数存在 根据参数得到相应的 Class的类对象
            for (int i = 0; i < len; ++i) {
                clazzs[i] = args[i].getClass();
            }
        }

        //获取构造器
        Constructor constructor = null;
        try {
            constructor = cls.getDeclaredConstructor(clazzs);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        if(constructor != null) {
            constructor.setAccessible(true);
            try {
                newSubject = constructor.newInstance(args);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return newSubject;
    }

    public static Object createObject(Class<?> cls, Class<?> clazzs[], Object[] args)
    {
        if(cls == null)
            return null;
        Object newSubject = null;

        //获取构造器
        Constructor constructor = null;
        try {
            constructor = cls.getDeclaredConstructor(clazzs);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        if(constructor != null) {
            constructor.setAccessible(true);
            try {
                newSubject = constructor.newInstance(args);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        return newSubject;
    }

    public static Object createObject(Constructor constructor, Object[] args)
    {
        Object newSubject = null;

        if (constructor != null) {
            constructor.setAccessible(true);
            try {
                newSubject = constructor.newInstance(args);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        return newSubject;
    }

    public static Object[] newArray(Object array, int length) {
        Class<?> componentType;
        if(array instanceof Object[]) {
            componentType = array.getClass().getComponentType();
        }else {
            componentType = array.getClass();
        }
        Object newArray = Array.newInstance(componentType, length);
        return (Object[]) newArray;
    }

    public static boolean isAncestry(Class<?> ancestor, Class<?> posterity){
        if(ancestor != null && posterity != null) {
            return ancestor.isAssignableFrom(posterity);
        }
        return false;
    }

}
