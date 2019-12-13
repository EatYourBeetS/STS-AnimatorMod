package eatyourbeets.utilities;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.random.Random;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public class JavaUtilities
{
    public static final Logger Logger = LogManager.getLogger(JavaUtilities.class.getName());

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

    public static MethodInfo GetPrivateMethod(String methodName, Class<?> type, Class<?>... parameterTypes)
    {
        java.lang.reflect.Method method = null;
        try
        {
            method = type.getDeclaredMethod(methodName, parameterTypes);
            method.setAccessible(true);
        }
        catch (NoSuchMethodException e)
        {
            e.printStackTrace();
        }

        return new MethodInfo(method);
    }

    public static <T> FieldInfo<T> GetPrivateField(String fieldName, Class<?> type)
    {
        java.lang.reflect.Field field = null;
        try
        {
            field = type.getDeclaredField(fieldName);
            field.setAccessible(true);
        }
        catch (NoSuchFieldException e)
        {
            e.printStackTrace();
        }

        return new FieldInfo<>(field);
    }

    public static <T> ArrayList<T> Where(ArrayList<T> list, Predicate<T> predicate)
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
        ArrayList<String> result = new ArrayList<>();

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
                if (name.startsWith(prefix) && name.endsWith(".class") && name.indexOf('$', 20) == -1)
                {
                    String className = entry.getName().replaceAll("/", "\\.");
                    String myClass = className.substring(0, className.lastIndexOf('.'));
                    result.add(myClass);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return result;
    }
}