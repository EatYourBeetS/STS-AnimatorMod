package eatyourbeets.utilities;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MethodInfo
{
    private final Method method;

    public Object Invoke(Object instance, Object... args) throws RuntimeException
    {
        try
        {
            return method.invoke(instance, args);
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