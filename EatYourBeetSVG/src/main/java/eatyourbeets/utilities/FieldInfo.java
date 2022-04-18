package eatyourbeets.utilities;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class FieldInfo<T>
{
    public final Field field;

    public T Set(Object instance, T value) throws RuntimeException
    {
        try
        {
            field.set(instance, value);
            return value;
        }
        catch (IllegalAccessException e)
        {
            throw new RuntimeException(e);
        }
    }

    public T Get(Object instance) throws RuntimeException
    {
        try
        {
            return (T)field.get(instance);
        }
        catch (IllegalAccessException e)
        {
            throw new RuntimeException(e);
        }
        catch (Exception e)
        {
            JUtils.LogError(this, field.getName());
            throw e;
        }
    }

    public boolean IsStatic()
    {
        return Modifier.isStatic(field.getModifiers());
    }

    public FieldInfo(Field field)
    {
        this.field = field;
    }
}