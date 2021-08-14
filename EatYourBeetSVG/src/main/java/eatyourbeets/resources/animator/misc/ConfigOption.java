package eatyourbeets.resources.animator.misc;

import basemod.ModPanel;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;

import java.io.IOException;

public abstract class ConfigOption<T>
{
    public String Key;
    public SpireConfig Config;

    protected T Value;

    public ConfigOption(SpireConfig config, String key)
    {
        Config = config;
        Key = key;
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
