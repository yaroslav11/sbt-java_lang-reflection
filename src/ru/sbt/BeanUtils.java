package ru.sbt;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

public class BeanUtils {
    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException {

    }
    /**
     * Scans object "from" for all getters. If object "to"
     * contains correspondent setter, it will invoke it
     * to set property value for "to" which equals to the property
     * of "from".
     * <p/>
     * The type in setter should be compatible to the value returned
     * by getter (if not, no invocation performed).
     * Compatible means that parameter type in setter should
     * be the same or be superclass of the return type of the getter.
     * <p/>
     * The method takes care only about public methods.
     *
     * @param to   Object which properties will be set.
     * @param from Object which properties will be used to get values.
     */
    public static void assign(Object to, Object from) throws InvocationTargetException, IllegalAccessException {

        Class sourceClass = from.getClass();
        System.out.println("sourceClass = " + sourceClass.getName());
        System.out.println("sourceClass.superclass = " + sourceClass.getSuperclass().getName());

        Class destinationClass = to.getClass();
        System.out.println("destinationClass = " + destinationClass.getName());

        Method[] sourceMethods = sourceClass.getMethods();
        for (Method m:sourceMethods){
            if(isGetter(m))
            System.out.println("\tsourceMethod = " + m.getName());
        }

        Method[] destinationMethods = destinationClass.getMethods();
        for (Method m:destinationMethods){
            if(isSetter(m))
                System.out.println("\tdestinationMethod = " + m.getName());
        }

        Set<Method> sourceGetters = new HashSet<>();
        for (Method method: sourceMethods){
            if (isGetter(method)) sourceGetters.add(method);
        }

        Set<Method> destinationSetters = new HashSet<>();
        for (Method method: destinationMethods){
            if (isSetter(method)) destinationSetters.add(method);
        }

        for (Method gettterMethod: sourceGetters){
            String requiredSetterName = "set" + gettterMethod.getName().substring(3);
            System.out.println("requiredSetterName = " + requiredSetterName);
            for (Method setterMethod: destinationSetters){
                if (! setterMethod.getName().equals(requiredSetterName)) continue;
                if (! compatibleParameters(
                        gettterMethod.getReturnType(),
                        setterMethod.getParameterTypes()[0])
                        ) continue;
                System.out.println("CORRESPONDING_Method = " + setterMethod.getName());
//                Double d = (Double) method.invoke(obj, args);
                setterMethod.invoke(to, gettterMethod.invoke(from));
            }
        }

    }

    public static boolean isGetter(Method method){
        if(!method.getName().startsWith("get"))      return false;
        if(method.getParameterTypes().length != 0)   return false;
        if(void.class.equals(method.getReturnType())) return false;
        return true;
    }

    public static boolean isSetter(Method method){
        if(!method.getName().startsWith("set")) return false;
        if(method.getParameterTypes().length != 1) return false;
        return true;
    }

    public static boolean compatibleParameters(Class fromGetter, Class fromSetter){
        if (fromSetter == fromGetter) return true;
        if (fromSetter == fromGetter.getSuperclass()) return true;
        return false;
    }
}