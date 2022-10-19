package eatyourbeets.utilities;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.interfaces.delegates.ActionT1;
import eatyourbeets.interfaces.delegates.ActionT3;
import eatyourbeets.interfaces.delegates.FuncT1;
import eatyourbeets.resources.GR;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.function.Predicate;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public class JUtils
{
    public static final Random RNG = new Random();

    private static final Gson gson = new Gson();
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

    @SafeVarargs
    public static <T> ArrayList<T> CreateList(T... items)
    {
        final ArrayList<T> list = new ArrayList<>(items.length);
        for (int i = 0; i < items.length; i++)
        {
            list.add(i, items[i]);
        }

        return list;
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

    public static BitmapFont GetFontOrDefault(String text, BitmapFont font, BitmapFont fallback)
    {
        if (Settings.language == Settings.GameLanguage.ENG || GR.IsTranslationSupported(Settings.language) || IsNullOrEmpty(text))
        {
            return font;
        }

        for (int i = 0; i < text.length(); i++)
        {
            if (text.charAt(i) >= 1024)
            {
                return fallback;
            }
        }

        return font;
    }

    public static <T, S> ArrayList<S> Select(Iterable<T> list, FuncT1<S, T> select)
    {
        final ArrayList<S> res = new ArrayList<>();
        for (T t : list)
        {
            res.add(select.Invoke(t));
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
                final String path = JUtils.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
                final JarInputStream jarFile = new JarInputStream(new FileInputStream(path));

                while (true)
                {
                    final JarEntry entry = jarFile.getNextJarEntry();
                    if (entry == null)
                    {
                        break;
                    }

                    final String name = entry.getName();
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

        final ArrayList<String> result = new ArrayList<>();
        for (String entry : classNames)
        {
            if (entry.startsWith(prefix))
            {
                result.add(entry.substring(0, entry.lastIndexOf('.')));
            }
        }

        return result;
    }

    public static FieldInfo[] GetFields(Class<?> type) throws RuntimeException
    {
        try
        {
            final Field[] fields = type.getDeclaredFields();
            final FieldInfo[] result = new FieldInfo[fields.length];
            for (int i = 0; i < fields.length; i++)
            {
                fields[i].setAccessible(true);
                result[i] = new FieldInfo(fields[i]);
            }

            return result;
        }
        catch (SecurityException e)
        {
            throw new RuntimeException(e);
        }
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

    public static boolean IsNullOrEmpty(String text)
    {
        return text == null || text.isEmpty();
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

    public static <T> T CallDefaultConstructor(Class<T> type)
    {
        final Constructor<T> ctor = TryGetConstructor(type);
        if (ctor != null)
        {
            try
            {
                ctor.setAccessible(true);
                return ctor.newInstance();
            }
            catch (InstantiationException | IllegalAccessException | InvocationTargetException e)
            {
                e.printStackTrace();
            }
        }

        return null;
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

    public static String LowerCase(Object text)
    {
        return text == null ? "" : text.toString().toLowerCase(Locale.ROOT);
    }

    public static String UpperCase(Object text)
    {
        return text == null ? "" : text.toString().toUpperCase(Locale.ROOT);
    }

    public static String Capitalize(String text)
    {
        return ModifyString(text, w -> UpperCase(w.charAt(0)) + (w.length() > 1 ? LowerCase(w.substring(1)) : ""));
    }

    public static String TitleCase(String text)
    {
        return ModifyString(text, w -> UpperCase(w.charAt(0)) + (w.length() > 1 ? w.substring(1) : ""));
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

    public static Float ParseFloat(String value, Float defaultValue)
    {
        try
        {
            return Float.parseFloat(value);
        }
        catch (NumberFormatException | NullPointerException ex)
        {
            return defaultValue;
        }
    }

    public static Integer ParseInt(String value, Integer defaultValue)
    {
        try
        {
            return Integer.parseInt(value);
        }
        catch (NumberFormatException | NullPointerException ex)
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

    public static <T> T Deserialize(String json, Class<T> type)
    {
        return gson.fromJson(json, type);
    }

    public static String Serialize(Object object)
    {
        return gson.toJson(object);
    }
}