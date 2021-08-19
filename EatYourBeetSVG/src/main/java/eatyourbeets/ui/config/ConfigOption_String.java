package eatyourbeets.ui.config;

import basemod.ModPanel;

public class ConfigOption_String extends ConfigOption<String>
{
    public String DefaultValue;

    public ConfigOption_String(String key, String defaultValue)
    {
        super(key);

        DefaultValue = defaultValue;
    }

    @Override
    public String Get()
    {
        return Get(DefaultValue);
    }

    @Override
    public String Get(String defaultValue)
    {
        if (Value == null)
        {
            if (Config.has(Key))
            {
                Value = Config.getString(Key);
            }
            else
            {
                Value = defaultValue;
            }
        }

        return Value;
    }

    @Override
    public String Set(String value, boolean save)
    {
        Value = value;
        Config.setString(Key, value);

        if (save)
        {
            Save();
        }

        return Value;
    }

    @Override
    public void AddToPanel(ModPanel panel, String label, float x, float y)
    {
        throw new RuntimeException("Not implemented");
    }
}
