package eatyourbeets.resources.animator.misc;

import basemod.ModLabeledToggleButton;
import basemod.ModPanel;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;

public class ConfigOption_Boolean extends ConfigOption<Boolean>
{
    public boolean DefaultValue;

    public ConfigOption_Boolean(SpireConfig config, String key, boolean defaultValue)
    {
        super(config, key);

        DefaultValue = defaultValue;
    }

    @Override
    public Boolean Get()
    {
        if (Value == null)
        {
            if (Config.has(Key))
            {
                Value = Config.getBool(Key);
            }
            else
            {
                Value = DefaultValue;
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

    @Override
    public void AddToPanel(ModPanel panel, String label, float x, float y)
    {
        panel.addUIElement(new ModLabeledToggleButton(label, x, y, Settings.CREAM_COLOR.cpy(),
                FontHelper.charDescFont, Get(), panel, __ -> { }, c -> Set(c.enabled, true)));
    }
}
