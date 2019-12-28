package eatyourbeets.utilities;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MethodInfo
{
    private final Method method;

    public void Invoke(Object instance, Object... args) throws RuntimeException
    {
        try
        {
            method.invoke(instance, args);
        }
        catch (InvocationTargetException | IllegalAccessException e)
        {
            throw new RuntimeException(e);
        }
    }

    public MethodInfo(Method method)
    {
        this.method = method;
    }
}