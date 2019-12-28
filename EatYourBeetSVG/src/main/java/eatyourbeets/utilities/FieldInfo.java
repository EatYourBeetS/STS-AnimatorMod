package eatyourbeets.utilities;

public class FieldInfo<T>
{
    private final java.lang.reflect.Field field;

    public void Set(Object instance, T value)
    {
        try
        {
            field.set(instance, value);
        }
        catch (IllegalAccessException e)
        {
            JavaUtilities.ThrowAndShutdown(e, getClass());
        }
    }

    @SuppressWarnings("unchecked")
    public T Get(Object instance)
    {
        try
        {
            return (T)field.get(instance);
        }
        catch (IllegalAccessException e)
        {
            JavaUtilities.ThrowAndShutdown(e, getClass());
        }

        return null;
    }

    public FieldInfo(java.lang.reflect.Field field)
    {
        this.field = field;
    }
}