package eatyourbeets.utilities;

import java.lang.reflect.InvocationTargetException;

public class Method
{
    private final java.lang.reflect.Method method;

    public void Invoke(Object instance, Object... args)
    {
        try
        {
            method.invoke(instance, args);
        }
        catch (InvocationTargetException | IllegalAccessException e)
        {
            e.printStackTrace();
        }
    }

    public Method(java.lang.reflect.Method method)
    {
        this.method = method;
    }
}