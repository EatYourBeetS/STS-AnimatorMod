package eatyourbeets.resources.animator;

import basemod.BaseMod;
import basemod.abstracts.CustomSavable;
import com.badlogic.gdx.utils.Base64Coder;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Prefs;
import com.megacrit.cardcrawl.helpers.SaveHelper;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.interfaces.csharp.ActionT2;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.loadouts.*;
import eatyourbeets.resources.animator.metrics.AnimatorLoadout;
import eatyourbeets.resources.animator.metrics.AnimatorTrophies;
import eatyourbeets.resources.common.CommonResources;
import eatyourbeets.utilities.JavaUtilities;

import java.util.ArrayList;
import java.util.StringJoiner;
import java.util.regex.Pattern;

public class AnimatorData implements CustomSavable<AnimatorData.SaveData>
{
    private static final String TROPHY_DATA_KEY = "TDAL";

    private SaveData saveData;

    public final ArrayList<AnimatorLoadout> BaseLoadouts = new ArrayList<>(); // Contains starting series
    public final ArrayList<AnimatorLoadout> BetaLoadouts = new ArrayList<>(); // Contains series which cannot be chosen from Character select
    public final ArrayList<AnimatorLoadout> Loadouts = new ArrayList<>();     // Contains series available for the current run
    public final ArrayList<AnimatorTrophies> Trophies = new ArrayList<>();
    public AnimatorTrophies SpecialTrophies = new AnimatorTrophies(-1);
    public AnimatorLoadout SelectedLoadout = new _FakeLoadout();

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

        AddBaseLoadouts();
        DeserializeTrophies(data);

        if (SelectedLoadout == null || SelectedLoadout.ID < 0)
        {
            SelectedLoadout = GetBaseLoadout(Synergies.Konosuba.ID);
        }

        if (SpecialTrophies == null || SpecialTrophies.ID != 0)
        {
            SpecialTrophies = new AnimatorTrophies(0);
        }

        BaseMod.addSaveField(CommonResources.SaveData.ID, this);
    }

    public void AddLoadout(AnimatorLoadout loadout)
    {
        if (!Loadouts.contains(loadout))
        {
            Loadouts.add(loadout);
        }
    }

    public boolean RemoveLoadout(AnimatorLoadout loadout)
    {
        return Loadouts.remove(loadout);
    }

    public ArrayList<AnimatorLoadout> GetAvailableLoadouts()
    {
        return Loadouts;
    }

    public AnimatorLoadout GetAvailableLoadout(int id)
    {
        for (AnimatorLoadout loadout : Loadouts)
        {
            if (loadout.ID == id)
            {
                return loadout;
            }
        }

        return null;
    }

    public AnimatorLoadout GetBetaLoadout(int id)
    {
        for (AnimatorLoadout loadout : BetaLoadouts)
        {
            if (loadout.ID == id)
            {
                return loadout;
            }
        }

        return null;
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

    public void OnVictory(int ascensionLevel)
    {
        if (ascensionLevel < 0) // Ascension reborn mod adds negative ascension levels
        {
            return;
        }

        for (AnimatorLoadout loadout : BaseLoadouts)
        {
            loadout.OnVictory(SelectedLoadout, ascensionLevel);
        }

        SaveTrophies(true);
    }

    public void OnTrueVictory(int ascensionLevel)
    {
        if (ascensionLevel < 0) // Ascension reborn mod adds negative ascension levels
        {
            return;
        }

        if (GR.Common.DungeonData.EnteredUnnamedReign)
        {
            if (SpecialTrophies.Trophy1 < 0)
            {
                SpecialTrophies.Trophy1 = 0;
            }

            SpecialTrophies.Trophy1 += 1 + Math.floorDiv(ascensionLevel, 4);
        }

        SaveTrophies(true);
    }

    public void SaveTrophies(boolean flush)
    {
        JavaUtilities.GetLogger(AnimatorData.class).info("Saving Trophies");

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

    private void AddBaseLoadouts()
    {
        BaseLoadouts.clear();

        final ActionT2<AnimatorLoadout, Integer> add = (loadout, unlockLevel) ->
        {
            BaseLoadouts.add(loadout);
            loadout.UnlockLevel = unlockLevel;
        };

        add.Invoke(new Konosuba(), 0);
        add.Invoke(new Gate(), 1);
        add.Invoke(new Elsword(), 1);
        add.Invoke(new NoGameNoLife(), 1);
        add.Invoke(new OwariNoSeraph(), 2);
        add.Invoke(new GoblinSlayer(), 2);
        add.Invoke(new Katanagatari(), 2);
        add.Invoke(new FullmetalAlchemist(), 2);
        add.Invoke(new Fate(), 3);
        add.Invoke(new Overlord(), 3);
        add.Invoke(new Chaika(), 3);
        add.Invoke(new TenSura(), 3);
        add.Invoke(new OnePunchMan(), 4);
        add.Invoke(new Kancolle(), 4);
        add.Invoke(new AccelWorld(), 4);
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

    @Override
    public SaveData onSave()
    {
        if (saveData == null)
        {
            saveData = new SaveData();
        }

        saveData.Validate();
        saveData.Import(Loadouts);

        return saveData;
    }

    @Override
    public void onLoad(SaveData saveData)
    {
        if (saveData == null)
        {
            saveData = new SaveData();
        }

        saveData.Validate();
        saveData.Export(Loadouts);

        this.saveData = saveData;
    }

    public static class SaveData
    {
        public static final String ID = GR.CreateID(AnimatorResources.ID, SaveData.class.getSimpleName());

        public ArrayList<Integer> baseLoadouts;
        public ArrayList<Integer> betaLoadouts;

        public void Import(ArrayList<AnimatorLoadout> loadouts)
        {
            baseLoadouts.clear();
            betaLoadouts.clear();

            for (AnimatorLoadout loadout : loadouts)
            {
                if (loadout.IsBeta)
                {
                    betaLoadouts.clear();
                }
                else
                {
                    baseLoadouts.add(loadout.ID);
                }
            }
        }

        public void Export(ArrayList<AnimatorLoadout> loadouts)
        {
            loadouts.clear();

            for (Integer id : baseLoadouts)
            {
                AnimatorLoadout loadout = GR.Animator.Database.GetBaseLoadout(id);
                if (loadout != null)
                {
                    loadouts.add(loadout);
                }
            }

            for (Integer id : betaLoadouts)
            {
                AnimatorLoadout loadout = GR.Animator.Database.GetBetaLoadout(id);
                if (loadout != null)
                {
                    loadouts.add(loadout);
                }
            }
        }

        public void Validate()
        {
            if (baseLoadouts == null)
            {
                baseLoadouts = new ArrayList<>();
            }
            if (betaLoadouts == null)
            {
                betaLoadouts = new ArrayList<>();
            }
        }
    }
}
