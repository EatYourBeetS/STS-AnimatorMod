package eatyourbeets.ui.config;

import basemod.ModPanel;
import com.badlogic.gdx.math.Vector2;
import eatyourbeets.utilities.JUtils;

public class ConfigOption_Vector2 extends ConfigOption<Vector2>
{
    public Vector2 DefaultValue;

    public ConfigOption_Vector2(String key, Vector2 defaultValue)
    {
        super(key);

        DefaultValue = defaultValue;
    }

    @Override
    public Vector2 Get()
    {
        return Get(DefaultValue);
    }

    @Override
    public Vector2 Get(Vector2 defaultValue)
    {
        if (Value == null)
        {
            if (defaultValue == null)
            {
                defaultValue = new Vector2();
            }

            Value = defaultValue;

            if (Config.has(Key))
            {
                final String[] data = JUtils.SplitString("|", Config.getString(Key));
                if (data.length > 0)
                {
                    Value = new Vector2(JUtils.ParseFloat(data[0], defaultValue.x), JUtils.ParseFloat(data[1], defaultValue.y));
                }
            }
        }

        return Value;
    }

    @Override
    public Vector2 Set(Vector2 value, boolean save)
    {
        Value = value;
        Config.setString(Key, value.x + "|" + value.y);

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
