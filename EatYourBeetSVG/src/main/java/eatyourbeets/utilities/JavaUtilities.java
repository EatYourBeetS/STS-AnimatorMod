package eatyourbeets.utilities;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.random.Random;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public class JavaUtilities
{
    public static final Logger Logger = LogManager.getLogger(JavaUtilities.class.getName());

    private static final ArrayList<String> classNames = new ArrayList<>();
    private static final WeightedList<AbstractOrb> orbs = new WeightedList<>();

    public static <T> T SafeCast(Object o, Class<T> type)
    {
        return type.isInstance(o) ? type.cast(o) : null;
    }

    public static <T> T GetRandomElement(ArrayList<T> list)
    {
        return GetRandomElement(list, AbstractDungeon.cardRandomRng);
    }

    public static <T> T GetRandomElement(ArrayList<T> list, Random rng)
    {
        int size = list.size();
        if (size > 0)
        {
            return list.get(rng.random(list.size() - 1));
        }

        return null;
    }

    public static MethodInfo GetPrivateMethod(String methodName, Class<?> type, Class<?>... parameterTypes) throws RuntimeException
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

    public static <T> FieldInfo<T> GetPrivateField(String fieldName, Class<?> type) throws RuntimeException
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

    public static <T> ArrayList<T> Filter(ArrayList<T> list, Predicate<T> predicate)
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

    public static String Format(String format, Object... args)
    {
        int index = 0;
        for (Object val : args)
        {
            format = format.replace("{" + index + "}", val.toString());
            index += 1;
        }

        return format;
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
}