package us.core_network.cornel.java;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import javax.annotation.Nullable;

/**
 * Created by Matej on 23.2.2014.
 */
public class ReflectionUtils {
    /**
     * Method that is used to retrieve value of a private variable in an object.
     * This method can only be used if variable is declared in provided object, not in its subclass.
     * @param obj Object that contains the variable.
     * @param field Name of the field.
     * @return Value of the field or <code>null if field does not exist.</code>
     */
    public static Object get(Object obj, String field)
    {
        return get(obj.getClass(), obj, field);
    }

    /**
     * Method that is used to retrieve value of a private variable in an object.
     * @param cls Class that contains definition ot this variable.
     * @param obj Object that contains variable value.
     * @param field Name of the field.
     * @return Value of the field or <code>null</code> if field does not exist or something went wrong.
     */
    public static Object get(Class cls, Object obj, String field)
    {
        try {
            Field fieldObj = cls.getDeclaredField(field);
            fieldObj.setAccessible(true);
            return fieldObj.get(obj);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Method that is used to retrieve value of a private static variable of a class.
     * @param cls Class that contains definition ot this variable.
     * @param field Name of the field.
     * @return Value of the field or <code>null</code> if field does not exist or something went wrong.
     */
    public static Object getStatic(Class cls, String field)
    {
        try {
            Field fieldObj = cls.getDeclaredField(field);
            fieldObj.setAccessible(true);
            return fieldObj.get(null);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Method that is used to set value of a private variable in an object.
     * This method can only be used if variable is declared in provided object, not in its subclass.
     * @param obj Object that contains the variable.
     * @param field Name of the field.
     * @param value Value to set.
     */
    public static void set(Object obj, String field, Object value)
    {
        set(obj.getClass(), obj, field, value);
    }

    /**
     * Method that is used to set value of a private variable in an object.
     * @param cls Class that contains definition ot this variable.
     * @param obj Object that contains the variable.
     * @param field Name of the field.
     * @param value Value to set.
     */
    public static void set(Class cls, Object obj, String field, Object value)
    {
        try {
            Field fieldObj = cls.getDeclaredField(field);
            fieldObj.setAccessible(true);
            fieldObj.set(obj, value);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Method that is used to retrieve private method of the class that can be later executed using {@link us.core_network.cornel.java.ReflectionUtils#executeMethod(java.lang.reflect.Method, Object, Object...)}.
     * @param cls Class that contains definition ot this variable.
     * @param methodName Name of the field.
     * @param argumentTypes Types of arguments of this method.
     */
    public static Method getMethod(Class cls, String methodName, Class... argumentTypes)
    {
        try {
            Method method = cls.getDeclaredMethod(methodName, argumentTypes);
            method.setAccessible(true);
            return method;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Execude {@link java.lang.reflect.Method}.
     * @param method Method, retrieved using {@link us.core_network.cornel.java.ReflectionUtils#getMethod(Class, String, Class[])}.
     * @param object Which object to execute method on. Use <code>null</code> for static methods.
     * @param arguments Arguments to this method.
     */

    public static Object executeMethod(Method method, @Nullable Object object, Object... arguments)
    {
        try {
            return method.invoke(object, arguments);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
