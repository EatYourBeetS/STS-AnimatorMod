package eatyourbeets.ui.config;

import basemod.ModPanel;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.JUtils;

import java.util.ArrayList;
import java.util.Arrays;

public class ConfigOption_SeriesList extends ConfigOption<ArrayList<CardSeries>>
{
    public ArrayList<CardSeries> DefaultValue;

    public ConfigOption_SeriesList(String key, ArrayList<CardSeries> defaultValue)
    {
        super(key);

        DefaultValue = defaultValue;
    }

    @Override
    public ArrayList<CardSeries> Get()
    {
        return Get(DefaultValue);
    }

    @Override
    public ArrayList<CardSeries> Get(ArrayList<CardSeries> defaultValue)
    {
        if (Value == null)
        {
            if (defaultValue == null)
            {
                defaultValue = JUtils.Map(GR.Animator.Data.GetEveryLoadout(), loadout -> loadout.Series);
            }

            Value = defaultValue;

            if (Config.has(Key))
            {
                final String[] data = JUtils.SplitString("|", Config.getString(Key));
                if (data.length > 0)
                {
                    Value = JUtils.Map(Arrays.asList(data),d -> CardSeries.GetByID(Integer.parseInt(d)));
                }
            }
        }

        return Value;
    }

    @Override
    public ArrayList<CardSeries> Set(ArrayList<CardSeries> value, boolean save)
    {
        Value = value;
        Config.setString(Key, JUtils.JoinStrings("|",JUtils.Map(value, s -> s.ID)));

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
