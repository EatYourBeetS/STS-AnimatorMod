package eatyourbeets.utilities;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.interfaces.delegates.ActionT1;
import eatyourbeets.interfaces.delegates.ActionT3;
import eatyourbeets.interfaces.delegates.FuncT1;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.util.*;
import java.util.function.Predicate;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

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

    public static <T> boolean All(Iterable<T> list, Predicate<T> predicate)
    {
        for (T t : list)
        {
            if (!predicate.test(t))
            {
                return false;
            }
        }

        return true;
    }

    public static <T> boolean Any(Iterable<T> list, Predicate<T> predicate)
    {
        for (T t : list)
        {
            if (predicate.test(t))
            {
                return true;
            }
        }

        return false;
    }

    public static String Capitalize(String text)
    {
        return text.length() <= 1 ? text.toUpperCase() : TipHelper.capitalize(text);
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
        final Map<K, List<V>> map = new HashMap<>();
        for (V v : list)
        {
            K k = getKey.Invoke(v);
            map.computeIfAbsent(k, key -> new ArrayList<>()).add(v);
        }

        return map;
    }

    public static <K, V, C> Map<K, C> Group(Iterable<V> list, FuncT1<K, V> getKey, ActionT3<K, V, C> add)
    {
        final Map<K, C> map = new HashMap<>();
        for (V v : list)
        {
            K k = getKey.Invoke(v);
            add.Invoke(k, v, map.get(k));
        }

        return map;
    }

    public static <T> ArrayList<T> Filter(Iterable<T> list, Predicate<T> predicate)
    {
        final ArrayList<T> res = new ArrayList<>();
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
        if (StringUtils.isEmpty(format))
        {
            return "";
        }
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
                        int index;
                        if (sb2.length() == 1)
                        {
                            index = Character.getNumericValue(sb2.toString().charAt(0));
                        }
                        else
                        {
                            index = ParseInt(sb2.toString(), -1);
                        }

                        if (index >= 0 && index < args.length)
                        {
                            sb1.append(args[index]);
                        }
                        else
                        {
                            LogError(JUtils.class, "Invalid format: " + format + "\n" + JoinStrings(", " , args));
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
            final Field field = type.getDeclaredField(fieldName);
            field.setAccessible(true);
            return new FieldInfo<>(field);
        }
        catch (NoSuchFieldException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static <T> MethodInfo.T0<T> GetMethod(String methodName, Class<?> type) throws RuntimeException
    {
        return new MethodInfo.T0<>(methodName, type);
    }

    public static <T, T1> MethodInfo.T1<T, T1> GetMethod(String methodName, Class<?> type, Class<T1> t1) throws RuntimeException
    {
        return new MethodInfo.T1<>(methodName, type, t1);
    }

    public static <T, T1, T2> MethodInfo.T2<T, T1, T2> GetMethod(String methodName, Class<?> type, Class<T1> t1, Class<T2> t2) throws RuntimeException
    {
        return new MethodInfo.T2<>(methodName, type, t1, t2);
    }

    public static boolean IsNotEmpty(List list)
    {
        return list != null && list.size() > 0;
    }

    public static boolean IsNullOrEmpty(List list)
    {
        return list == null || list.isEmpty();
    }

    public static boolean IsNullOrZero(Number number)
    {
        return number == null || number.intValue() == 0;
    }

    public static <T> Constructor<T> TryGetConstructor(Class<T> type, Class... paramTypes)
    {
        try
        {
            return paramTypes.length > 0 ? type.getDeclaredConstructor(paramTypes) : type.getConstructor();
        }
        catch (NoSuchMethodException e)
        {
            return null;
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

    public static String[] SplitString(String separator, String text)
    {
        return SplitString(separator, text, true);
    }

    public static String[] SplitString(String separator, String text, boolean removeEmptyEntries)
    {
        if (StringUtils.isEmpty(text))
        {
            return new String[0];
        }

        sb1.setLength(0);
        sb2.setLength(0);

        int s_index = 0;
        final ArrayList<String> result = new ArrayList<>();
        for (int i = 0; i < text.length(); i++)
        {
            char c = text.charAt(i);
            if (separator.charAt(s_index) != c)
            {
                if (s_index > 0)
                {
                    sb1.append(sb2.toString());
                    sb2.setLength(0);
                    s_index = 0;
                }
                sb1.append(c);
            }
            else if ((separator.length() - 1) > s_index)
            {
                s_index += 1;
                sb2.append(c);
            }
            else
            {
                if (!removeEmptyEntries)
                {
                    result.add(sb1.toString());
                    if (i == text.length() - 1)
                    {
                        result.add("");
                    }
                }
                else if (sb1.length() > 0)
                {
                    result.add(sb1.toString());
                }

                s_index = 0;
                sb1.setLength(0);
                sb2.setLength(0);
            }
        }

        if (sb1.length() > 0)
        {
            result.add(sb1.toString());
        }

        final String[] arr = new String[result.size()];
        return result.toArray(arr);
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
        final String[] words = SplitString(separator, text);
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