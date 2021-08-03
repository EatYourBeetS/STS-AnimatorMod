package eatyourbeets.utilities;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.interfaces.delegates.ActionT1;
import eatyourbeets.interfaces.delegates.ActionT3;
import eatyourbeets.interfaces.delegates.FuncT1;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.util.*;
import java.util.function.Predicate;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.regex.Pattern;

public class JUtils
{
    public static final Random RNG = new Random();

    private static final StringBuilder sb1 = new StringBuilder();
    private static final StringBuilder sb2 = new StringBuilder();
    private static final ArrayList<String> classNames = new ArrayList<>();
    private static final WeightedList<AbstractOrb> orbs = new WeightedList<>();

    public static void Breakpoint()
    {
        // Place a permanent breakpoint here
        JUtils.LogInfo(JUtils.class, "Breakpoint Reached");
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

    public static <K, V> Map<K, List<V>> Group(Iterable<V> list, FuncT1<K, V> getKey)
    {
        Map<K, List<V>> map = new HashMap<>();
        for (V v : list)
        {
            K k = getKey.Invoke(v);
            map.computeIfAbsent(k, key -> new ArrayList<>()).add(v);
        }

        return map;
    }

    public static <K, V, C> Map<K, C> Group(Iterable<V> list, FuncT1<K, V> getKey, ActionT3<K, V, C> add)
    {
        Map<K, C> map = new HashMap<>();
        for (V v : list)
        {
            K k = getKey.Invoke(v);
            add.Invoke(k, v, map.get(k));
        }

        return map;
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

    public static <T> T Find(Iterable<T> list, FuncT1<Boolean, T> predicate)
    {
        for (T t : list)
        {
            if (predicate.Invoke(t))
            {
                return t;
            }
        }

        return null;
    }

    public static <T> void ForEach(Iterable<T> list, ActionT1<T> action)
    {
        for (T t : list)
        {
            action.Invoke(t);
        }
    }

    public static <T, N extends Comparable<N>> T FindMax(Iterable<T> list, FuncT1<N, T> getProperty)
    {
        N best = null;
        T result = null;
        for (T t : list)
        {
            N prop = getProperty.Invoke(t);
            if (best == null || prop.compareTo(best) > 0)
            {
                best = prop;
                result = t;
            }
        }

        return result;
    }

    public static <T, N extends Comparable<N>> T FindMin(Iterable<T> list, FuncT1<N, T> getProperty)
    {
        N best = null;
        T result = null;
        for (T t : list)
        {
            N prop = getProperty.Invoke(t);
            if (best == null || prop.compareTo(best) < 0)
            {
                best = prop;
                result = t;
            }
        }

        return result;
    }

    // Simple string Formatting in which integers inside curly braces are replaced by args[i].
    public static String Format(String format, Object... args)
    {
        if (args == null || args.length == 0)
        {
            return format;
        }

        sb1.setLength(0);
        sb2.setLength(0);
        int braces = 0;
        for (int i = 0; i < format.length(); i++)
        {
            Character c = format.charAt(i);
            if (c == '{')
            {
                sb2.setLength(0);
                int j = i + 1;
                while (j < format.length())
                {
                    final Character next = format.charAt(j);
                    if (Character.isDigit(next))
                    {
                        sb2.append(next);
                        j += 1;
                        continue;
                    }
                    else if (next == '}' && sb2.length() > 0)
                    {
                        if (sb2.length() == 1)
                        {
                            sb1.append(args[Character.getNumericValue(sb2.toString().charAt(0))]);
                        }
                        else
                        {
                            sb1.append(args[ParseInt(sb2.toString(), -1)]);
                        }
                        i = j;
                    }

                    break;
                }

                if (sb2.length() > 0)
                {
                    continue;
                }
            }

            sb1.append(c);
        }

        return sb1.toString();
    }

    public static ArrayList<String> GetClassNamesFromJarFile(String prefix)
    {
        if (classNames.size() == 0)
        {
            try
            {
                String path = JUtils.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
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

    public static Logger GetLogger(Object source)
    {
        if (source == null)
        {
            return LogManager.getLogger();
        }

        return LogManager.getLogger((source instanceof Class) ? ((Class)source).getName() : source.getClass().getName());
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

    public static int IncrementMapElement(Map map, Object key)
    {
        //noinspection unchecked
        return (int)map.compute(key, (k, v) -> v == null ? 1 : (int)v + 1);
    }

    public static int IncrementMapElement(Map map, Object key, int amount)
    {
        if (map.containsKey(key))
        {
            amount += (int)map.get(key);
        }

        //noinspection unchecked
        map.put(key, amount);
        return amount;
    }

    public static <T> String JoinStrings(String delimiter, Collection<T> values)
    {
        final StringJoiner sj = new StringJoiner(delimiter);
        for (T value : values)
        {
            sj.add(String.valueOf(value));
        }

        return sj.toString();
    }

    public static <T> String JoinStrings(String delimiter, T[] values)
    {
        final StringJoiner sj = new StringJoiner(delimiter);
        for (T value : values)
        {
            sj.add(String.valueOf(value));
        }

        return sj.toString();
    }

    public static String TitleCase(String text)
    {
        return ModifyString(text, w -> Character.toUpperCase(w.charAt(0)) + (w.length() > 1 ? w.substring(1) : ""));
    }

    public static String ModifyString(String text, FuncT1<String, String> modifyWord)
    {
        return ModifyString(text, " ", " ", modifyWord);
    }

    public static String ModifyString(String text, String separator, String delimiter, FuncT1<String, String> modifyWord)
    {
        final String[] words = text.split(Pattern.quote(separator));
        if (modifyWord != null)
        {
            for (int i = 0; i < words.length; i++)
            {
                words[i] = modifyWord.Invoke(words[i]);
            }
        }

        return JoinStrings(delimiter, words);
    }

    public static void LogError(Object source, Object message)
    {
        GetLogger(source).error(message);
    }

    public static void LogError(Object source, String format, Object... values)
    {
        GetLogger(source).error(Format(format, values));
    }

    public static void LogInfo(Object source, Object message)
    {
        GetLogger(source).info(message);
    }

    public static void LogInfo(Object source, String format, Object... values)
    {
        GetLogger(source).info(Format(format, values));
    }

    public static void LogWarning(Object source, Object message)
    {
        GetLogger(source).warn(message);
    }

    public static void LogWarning(Object source, String format, Object... values)
    {
        GetLogger(source).warn(Format(format, values));
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

    public static float Round(float value, int precision)
    {
        final float scale = (float) Math.pow(10, precision);
        return Math.round(value * scale) / scale;
    }

    public static <T> T SafeCast(Object o, Class<T> type)
    {
        return type.isInstance(o) ? (T)o : null;
    }

    public static float Lerp(float current, float target, float amount)
    {
        float lerp = (target - current) * amount;
        float result = current + lerp;
        return (current < target) ? (result > target ? target : result) : (result < target ? target : result);
    }

    public static void Lerp(Color current, Color target, float amount)
    {
        current.r = Lerp(current.r, target.r, amount);
        current.g = Lerp(current.g, target.g, amount);
        current.b = Lerp(current.b, target.b, amount);
        current.a = Lerp(current.a, target.a, amount);
    }

    public static boolean ShowDebugInfo()
    {
        return Settings.isDebug || Settings.isInfo;
    }

    public static <T> T Random(T[] items)
    {
        return items[RNG.nextInt(items.length)];
    }

    public static <T> T Random(ArrayList<T> items)
    {
        return items.get(RNG.nextInt(items.size()));
    }

    public static <T> T Random(Collection<T> items)
    {
        int size = items.size();
        if (size == 0)
        {
            return null;
        }

        int i = 0;
        int targetIndex = RNG.nextInt(size);
        for (T item : items)
        {
            if (i++ == targetIndex)
            {
                return item;
            }
        }

        throw new RuntimeException("items.size() was smaller than " + targetIndex + ".");
    }
}