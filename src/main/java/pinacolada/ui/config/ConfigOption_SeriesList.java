package pinacolada.ui.config;

import basemod.ModPanel;
import eatyourbeets.ui.config.ConfigOption;
import pinacolada.cards.base.CardSeries;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLJUtils;

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
                defaultValue = PCLJUtils.Map(GR.PCL.Data.GetEveryLoadout(), loadout -> loadout.Series);
            }

            Value = defaultValue;

            if (Config.has(Key))
            {
                final String[] data = PCLJUtils.SplitString("|", Config.getString(Key));
                if (data.length > 0)
                {
                    Value = PCLJUtils.Map(Arrays.asList(data), d -> CardSeries.GetByID(Integer.parseInt(d)));
                }
            }
        }

        return Value;
    }

    @Override
    public ArrayList<CardSeries> Set(ArrayList<CardSeries> value, boolean save)
    {
        Value = value;
        Config.setString(Key, PCLJUtils.JoinStrings("|", PCLJUtils.Map(value, s -> s.ID)));

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
