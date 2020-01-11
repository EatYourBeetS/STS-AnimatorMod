package eatyourbeets.resources.animator;

import com.badlogic.gdx.utils.Base64Coder;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Prefs;
import com.megacrit.cardcrawl.helpers.SaveHelper;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.loadouts.*;
import eatyourbeets.resources.animator.metrics.AnimatorLoadout;
import eatyourbeets.resources.animator.metrics.AnimatorTrophies;
import eatyourbeets.utilities.JavaUtilities;

import java.util.ArrayList;
import java.util.StringJoiner;
import java.util.regex.Pattern;

public class AnimatorMetrics
{
    private static final String TROPHY_DATA_KEY = "TDAL";

    public final ArrayList<AnimatorLoadout> BaseLoadouts = new ArrayList<>();    // Contains starting series
    public final ArrayList<AnimatorLoadout> CurrentLoadouts = new ArrayList<>(); // Contains series available for the current run
    public final ArrayList<AnimatorTrophies> Trophies = new ArrayList<>();
    public AnimatorTrophies SpecialTrophies = new AnimatorTrophies(-1);
    public AnimatorLoadout SelectedLoadout = new _FakeLoadout();

    public void SaveTrophies(boolean flush)
    {
        JavaUtilities.GetLogger(AnimatorMetrics.class).info("Saving Trophies");

        String serialized = SerializeTrophies();

        GR.Animator.GetConfig().setString(TROPHY_DATA_KEY, serialized);

        if (flush)
        {
            GR.Animator.SaveConfig();
        }

        Prefs prefs = null;

        if (AbstractDungeon.player != null)
        {
            prefs = AbstractDungeon.player.getPrefs();
        }

        if (prefs == null)
        {
            prefs = SaveHelper.getPrefs(GR.Animator.PlayerClass.name());
        }

        if (prefs != null)
        {
            prefs.putString(TROPHY_DATA_KEY, serialized);

            if (flush)
            {
                prefs.flush();
            }
        }
    }

    public AnimatorLoadout GetBaseLoadout(int id)
    {
        for (AnimatorLoadout loadout : BaseLoadouts)
        {
            if (loadout.ID == id)
            {
                return loadout;
            }
        }

        return null;
    }

    public AnimatorTrophies GetTrophies(int id)
    {
        for (AnimatorTrophies trophies : Trophies)
        {
            if (trophies.ID == id)
            {
                return trophies;
            }
        }

        return null;
    }

    public void Initialize()
    {
        String data = GR.Animator.GetConfig().getString(TROPHY_DATA_KEY);
        if (data == null)
        {
            Prefs prefs = SaveHelper.getPrefs(GR.Animator.PlayerClass.name());
            data = prefs.getString(TROPHY_DATA_KEY);

            GR.Animator.GetConfig().setString(TROPHY_DATA_KEY, data);
            GR.Animator.SaveConfig();
        }

        AddLoadouts();
        DeserializeTrophies(data);

        if (SelectedLoadout == null || SelectedLoadout.ID < 0)
        {
            SelectedLoadout = GetBaseLoadout(Synergies.Konosuba.ID);
        }

        if (SpecialTrophies == null || SpecialTrophies.ID != 0)
        {
            SpecialTrophies = new AnimatorTrophies(0);
        }
    }

    public void OnVictory(int ascensionLevel)
    {
        if (ascensionLevel < 0) // Ascension reborn mod adds negative ascension levels
        {
            return;
        }

//        for (AnimatorLoadout loadout : BaseLoadouts)
//        {
//            if (!(loadout instanceof Random))
//            {
//                loadout.OnVictory(GetSelectedLoadout(false), ascensionLevel);
//            }
//        }
//
//        AnimatorMetrics.SaveTrophies(true);
    }

    public void OnTrueVictory(int ascensionLevel)
    {
        if (ascensionLevel < 0) // Ascension reborn mod adds negative ascension levels
        {
            return;
        }

//        if (PlayerStatistics.SaveData.EnteredUnnamedReign)
//        {
//            if (AnimatorCustomLoadout.specialTrophies.Trophy1 < 0)
//            {
//                AnimatorCustomLoadout.specialTrophies.Trophy1 = 0;
//            }
//
//            AnimatorCustomLoadout.specialTrophies.Trophy1 += 1 + Math.floorDiv(ascensionLevel, 4);
//        }
//
//        AnimatorMetrics.SaveTrophies(true);
    }

    private void AddLoadouts()
    {
        BaseLoadouts.clear();

        AddLoadout(new Konosuba(), 0);
        AddLoadout(new Gate(), 1);
        AddLoadout(new Elsword(), 1);
        AddLoadout(new NoGameNoLife(), 1);
        AddLoadout(new OwariNoSeraph(), 2);
        AddLoadout(new GoblinSlayer(), 2);
        AddLoadout(new Katanagatari(), 2);
        AddLoadout(new FullmetalAlchemist(), 2);
        AddLoadout(new Fate(), 3);
        AddLoadout(new Overlord(), 3);
        AddLoadout(new Chaika(), 3);
        AddLoadout(new TenSura(), 3);
        AddLoadout(new OnePunchMan(), 4);
        AddLoadout(new Kancolle(), 4);
        AddLoadout(new AccelWorld(), 4);
    }

    private void AddLoadout(AnimatorLoadout loadout, int unlockLevel)
    {
        BaseLoadouts.add(loadout);
        loadout.UnlockLevel = unlockLevel;
    }

    // SelectedLoadout|Series_1,Trophy1,Trophy2,Trophy3|Series_2,Trophy1,Trophy2,Trophy3|...
    private String SerializeTrophies()
    {
        StringJoiner sj = new StringJoiner("|");

        sj.add(String.valueOf(SelectedLoadout.ID));
        sj.add(SpecialTrophies.Serialize());

        for (AnimatorTrophies t : Trophies)
        {
            sj.add(t.Serialize());
        }

        return Base64Coder.encodeString(sj.toString());
    }

    private void DeserializeTrophies(String data)
    {
        Trophies.clear();

        if (data != null && data.length() > 0)
        {
            String decoded = Base64Coder.decodeString(data);
            String[] items = decoded.split(Pattern.quote("|"));

            if (items.length > 0)
            {
                int loadoutID = JavaUtilities.ParseInt(items[0], -1);
                if (loadoutID > 0)
                {
                    SelectedLoadout = GetBaseLoadout(loadoutID);
                }

                for (int i = 1; i < items.length; i++)
                {
                    AnimatorTrophies trophies = new AnimatorTrophies();

                    trophies.Deserialize(items[i]);

                    if (trophies.ID == 0)
                    {
                        SpecialTrophies = trophies;
                    }
                    else if (trophies.ID > 0)
                    {
                        Trophies.add(trophies);
                    }
                }
            }
        }
    }
}
