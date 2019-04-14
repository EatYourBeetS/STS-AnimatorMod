package eatyourbeets.characters;

import com.badlogic.gdx.utils.Base64Coder;
import com.megacrit.cardcrawl.helpers.Prefs;
import com.megacrit.cardcrawl.helpers.SaveHelper;
import eatyourbeets.Utilities;
import patches.AbstractEnums;

import java.util.ArrayList;
import java.util.StringJoiner;
import java.util.regex.Pattern;

public class AnimatorMetrics
{
    private static final String TROPHY_DATA_KEY = "TDAL";
    private static Prefs prefs;

    public static final ArrayList<AnimatorTrophies> trophiesData = new ArrayList<>();
    public static int lastLoadout = 0;

    public static void SaveTrophies(boolean flush)
    {
        Utilities.Logger.info("Saving Trophies");

        lastLoadout = AnimatorCharacterSelect.GetSelectedLoadout(false).ID;

        StringJoiner sj = new StringJoiner("|");
        sj.add(String.valueOf(lastLoadout));
        for (AnimatorTrophies t : trophiesData)
        {
            sj.add(t.Serialize());
        }

        prefs.putString(TROPHY_DATA_KEY, Base64Coder.encodeString(sj.toString()));
        if (flush)
        {
            prefs.flush();
        }
    }

    static
    {
        try
        {
            prefs = SaveHelper.getPrefs(AbstractEnums.Characters.THE_ANIMATOR.name());

            String data = prefs.getString(TROPHY_DATA_KEY);
            if (data != null && data.length() > 0)
            {
                trophiesData.clear();

                data = Base64Coder.decodeString(data);
                String[] items = data.split(Pattern.quote("|"));

                if (items.length > 0)
                {
                    lastLoadout = Integer.parseInt(items[0]);
                }

                for (int i = 1; i < items.length; i++)
                {
                    trophiesData.add(new AnimatorTrophies(items[i]));
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
