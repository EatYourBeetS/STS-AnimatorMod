package eatyourbeets.utilities;

import java.lang.reflect.Field;

public class FieldInfo<T>
{
    private final Field field;

    public void Set(Object instance, T value) throws RuntimeException
    {
        try
        {
            field.set(instance, value);
        }
        catch (IllegalAccessException e)
        {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
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
    }

    public FieldInfo(Field field)
    {
        this.field = field;
    }
}