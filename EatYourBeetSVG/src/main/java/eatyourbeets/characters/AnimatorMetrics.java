package eatyourbeets.characters;

import com.badlogic.gdx.utils.Base64Coder;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Prefs;
import com.megacrit.cardcrawl.helpers.SaveHelper;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.JavaUtilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.StringJoiner;
import java.util.regex.Pattern;

public class AnimatorMetrics
{
    private static final String TROPHY_DATA_KEY = "TDAL";
    private static SpireConfig config = null;
    private static Prefs prefs = null;

    public static final ArrayList<AnimatorTrophies> trophiesData = new ArrayList<>();
    public static int lastLoadout = 0;

    public static void SaveTrophies(boolean flush)
    {
        JavaUtilities.Logger.info("Saving Trophies");

        lastLoadout = AnimatorCharacterSelect.GetSelectedLoadout(false).ID;

        StringJoiner sj = new StringJoiner("|");
        sj.add(String.valueOf(lastLoadout));
        for (AnimatorTrophies t : trophiesData)
        {
            sj.add(t.Serialize());
        }

        String toSave = Base64Coder.encodeString(sj.toString());

        config.setString(TROPHY_DATA_KEY, toSave);
        if (flush)
        {
            SaveConfig();
        }

        if (AbstractDungeon.player != null)
        {
            Prefs currentPrefs = AbstractDungeon.player.getPrefs();
            if (currentPrefs != null)
            {
                currentPrefs.putString(TROPHY_DATA_KEY, toSave);

                if (flush)
                {
                    currentPrefs.flush();
                }
            }
        }
        else if (prefs != null)
        {
            prefs.putString(TROPHY_DATA_KEY, toSave);

            if (flush)
            {
                prefs.flush();
            }
        }
    }

    public static void SaveConfig()
    {
        try
        {
            config.save();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void Initialize()
    {
        String data = null;
        try
        {
            config = new SpireConfig("TheAnimator", "TheAnimatorConfig");
            prefs = SaveHelper.getPrefs(GR.Enums.Characters.THE_ANIMATOR.name());

            data = config.getString(TROPHY_DATA_KEY);
            if (data == null)
            {
                data = prefs.getString(TROPHY_DATA_KEY);
                config.setString(TROPHY_DATA_KEY, data);
                config.save();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        SetupTrophies(data);
    }

    public static SpireConfig GetConfig()
    {
        return config;
    }

    private static void SetupTrophies(String data)
    {
        if (data != null && data.length() > 0)
        {
            trophiesData.clear();

            data = Base64Coder.decodeString(data);
            String[] items = data.split(Pattern.quote("|"));

            if (items.length > 0)
            {
                try
                {
                    lastLoadout = Integer.parseInt(items[0]);
                }
                catch (NumberFormatException e)
                {
                    lastLoadout = Synergies.Konosuba.ID;

                    JavaUtilities.Logger.warn("Could not Parse player prefs, " + e.getMessage());
                }

                AnimatorCharacterSelect.SetLoadout(lastLoadout);
            }

            for (int i = 1; i < items.length; i++)
            {
                trophiesData.add(new AnimatorTrophies(items[i]));
            }
        }
    }
}
