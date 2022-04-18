package eatyourbeets.ui.config;

import basemod.ModPanel;

public class ConfigOption_Integer extends ConfigOption<Integer>
{
    public Integer DefaultValue;

    public ConfigOption_Integer(String key, Integer defaultValue)
    {
        super(key);

        DefaultValue = defaultValue;
    }

    @Override
    public Integer Get()
    {
        return Get(DefaultValue);
    }

    @Override
    public Integer Get(Integer defaultValue)
    {
        if (Value == null)
        {
            if (Config.has(Key))
            {
                Value = Config.getInt(Key);
            }
            else
            {
                Value = defaultValue;
            }
        }

        return Value;
    }

    @Override
    public Integer Set(Integer value, boolean save)
    {
        Value = value;
        Config.setInt(Key, value);

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
