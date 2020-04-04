package eatyourbeets.utilities;

import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.random.Random;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.function.Predicate;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public class JavaUtilities
{
    public static final Logger Logger = LogManager.getLogger(JavaUtilities.class.getName());

    private static final MessageFormat formatter = new MessageFormat("");
    private static final ArrayList<String> classNames = new ArrayList<>();
    private static final WeightedList<AbstractOrb> orbs = new WeightedList<>();

    public static <T> T SafeCast(Object o, Class<T> type)
    {
        return type.isInstance(o) ? (T)o : null;
    }

    public static <T> T GetRandomElement(List<T> list)
    {
        return GetRandomElement(list, GameUtilities.GetRNG());
    }

    public static <T> T GetRandomElement(List<T> list, Random rng)
    {
        int size = list.size();
        if (size > 0)
        {
            return list.get(rng.random(list.size() - 1));
        }

        return null;
    }

    public static MethodInfo GetMethod(String methodName, Class<?> type, Class<?>... parameterTypes) throws RuntimeException
    {
        try
        {
            Method method = type.getDeclaredMethod(methodName, parameterTypes);
            method.setAccessible(true);
            return new MethodInfo(method);
        }
        catch (NoSuchMethodException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static <T> FieldInfo<T> GetField(String fieldName, Class<?> type) throws RuntimeException
    {
        try
        {
            Field field = type.getDeclaredField(fieldName);
            field.setAccessible(true);
            return new FieldInfo<>(field);
        }
        catch (NoSuchFieldException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static <T> void ChangeIndex(T item, List<T> list, int index)
    {
        if (list.remove(item))
        {
            list.add(Math.max(0, Math.min(index, list.size())), item);
        }
    }

    public static <T> int Count(Iterable<T> list, Predicate<T> predicate)
    {
        int count = 0;
        for (T t : list)
        {
            if (predicate.test(t))
            {
                count += 1;
            }
        }

        return count;
    }

    public static <T> ArrayList<T> Filter(Iterable<T> list, Predicate<T> predicate)
    {
        ArrayList<T> res = new ArrayList<>();
        for (T t : list)
        {
            if (predicate.test(t))
            {
                res.add(t);
            }
        }

        return res;
    }

    public static <T> T Find(Iterable<T> list, Predicate<T> predicate)
    {
        for (T t : list)
        {
            if (predicate.test(t))
            {
                return t;
            }
        }

        return null;
    }

    public static String Format(String format, Object... args)
    {
        formatter.applyPattern(format);
        return formatter.format(args);
    }

    public static ArrayList<String> GetClassNamesFromJarFile(String prefix)
    {
        if (classNames.size() == 0)
        {
            try
            {
                String path = JavaUtilities.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
                JarInputStream jarFile = new JarInputStream(new FileInputStream(path));

                while (true)
                {
                    JarEntry entry = jarFile.getNextJarEntry();
                    if (entry == null)
                    {
                        break;
                    }

                    String name = entry.getName();
                    if (name.endsWith(".class") && name.indexOf('$') == -1)
                    {
                        classNames.add(name.replaceAll("/", "\\."));
                    }
                }
            }
            catch (IOException | URISyntaxException e)
            {
                throw new RuntimeException(e);
            }
        }

        ArrayList<String> result = new ArrayList<>();
        for (String entry : classNames)
        {
            if (entry.startsWith(prefix))
            {
                result.add(entry.substring(0, entry.lastIndexOf('.')));
            }
        }

        return result;
    }

    public static Logger GetLogger(Class c)
    {
        return LogManager.getLogger(c.getName());
    }

    public static Logger GetLogger(Object instance)
    {
        return GetLogger(instance.getClass());
    }

    public static void Log(Class source, Object message)
    {
        GetLogger(source).info(message);
    }

    public static void Log(Object source, Object message)
    {
        GetLogger(source).info(message);
    }

    public static void Log(Object source, String format, Object... values)
    {
        GetLogger(source).info(Format(format, values));
    }

    public static float Round(float value, int precision)
    {
        final float scale = (float) Math.pow(10, precision);
        return Math.round(value * scale) / scale;
    }

    public static float ParseFloat(String value, float defaultValue)
    {
        try
        {
            return Float.parseFloat(value);
        }
        catch (NumberFormatException ex)
        {
            return defaultValue;
        }
    }

    public static int ParseInt(String value, int defaultValue)
    {
        try
        {
            return Integer.parseInt(value);
        }
        catch (NumberFormatException ex)
        {
            return defaultValue;
        }
    }

    public static <T> String JoinStrings(String delimiter, T[] values)
    {
        StringJoiner sj = new StringJoiner(delimiter);
        for (T value : values)
        {
            sj.add(value.toString());
        }

        return sj.toString();
    }
}