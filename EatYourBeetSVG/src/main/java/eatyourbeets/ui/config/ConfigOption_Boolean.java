package eatyourbeets.ui.config;

import basemod.ModLabeledToggleButton;
import basemod.ModPanel;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;

public class ConfigOption_Boolean extends ConfigOption<Boolean>
{
    public boolean DefaultValue;

    public ConfigOption_Boolean(String key, boolean defaultValue)
    {
        super(key);

        DefaultValue = defaultValue;
    }

    @Override
    public Boolean Get()
    {
        return Get(DefaultValue);
    }

    @Override
    public Boolean Get(Boolean defaultValue)
    {
        if (Value == null)
        {
            if (Config.has(Key))
            {
                Value = Config.getBool(Key);
            }
            else
            {
                Value = defaultValue;
            }
        }

        return Value;
    }

    @Override
    public Boolean Set(Boolean value, boolean save)
    {
        Value = value;
        Config.setBool(Key, value);

        if (save)
        {
            Save();
        }

        return Value;
    }

    public Boolean Toggle(boolean save)
    {
        Value = !Value;
        Config.setBool(Key, Value);

        if (save)
        {
            Save();
        }

        return Value;
    }

    @Override
    public void AddToPanel(ModPanel panel, String label, float x, float y)
    {
        panel.addUIElement(new ModLabeledToggleButton(label, x, y, Settings.CREAM_COLOR.cpy(),
                FontHelper.charDescFont, Get(), panel, __ -> { }, c -> Set(c.enabled, true)));
    }
}
