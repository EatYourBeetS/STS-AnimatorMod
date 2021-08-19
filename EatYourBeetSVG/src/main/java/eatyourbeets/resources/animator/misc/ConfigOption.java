package eatyourbeets.resources.animator.misc;

import basemod.ModPanel;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;

import java.io.IOException;

public abstract class ConfigOption<T>
{
    public final String Key;

    protected SpireConfig Config;
    protected T Value;

    public ConfigOption(String key)
    {
        Key = key;
    }

    public void SetConfig(SpireConfig config)
    {
        Config = config;
        Value = null;
    }

    public abstract T Get();

    public abstract T Get(T defaultValue);

    public abstract T Set(T value, boolean save);

    public boolean Save()
    {
        try
        {
            Config.save();
            return true;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public abstract void AddToPanel(ModPanel panel, String label, float x, float y);
}
