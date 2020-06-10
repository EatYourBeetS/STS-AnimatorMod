package eatyourbeets.resources.animator;

import com.badlogic.gdx.utils.Base64Coder;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.cards.base.Synergy;
import eatyourbeets.interfaces.delegates.ActionT2;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.loadouts.*;
import eatyourbeets.resources.animator.misc.AnimatorLoadout;
import eatyourbeets.resources.animator.misc.AnimatorTrophies;
import eatyourbeets.utilities.JUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.regex.Pattern;

public class AnimatorPlayerData
{
    public final ArrayList<AnimatorLoadout> BaseLoadouts = new ArrayList<>();
    public final ArrayList<AnimatorLoadout> BetaLoadouts = new ArrayList<>();
    public final ArrayList<AnimatorTrophies> Trophies = new ArrayList<>();
    public AnimatorTrophies SpecialTrophies = new AnimatorTrophies(-1);
    public AnimatorLoadout SelectedLoadout = new _FakeLoadout();

    public void Initialize()
    {
        AddBaseLoadouts();
        AddBetaLoadouts();
        DeserializeTrophies(GR.Animator.Config.GetTrophyString());

        if (SelectedLoadout == null || SelectedLoadout.ID < 0)
        {
            SelectedLoadout = GetBaseLoadout(Synergies.Konosuba.ID);
        }

        if (SpecialTrophies == null || SpecialTrophies.ID != 0)
        {
            SpecialTrophies = new AnimatorTrophies(0);
        }
    }

    public List<AnimatorLoadout> GetEveryLoadout()
    {
        return GetEveryLoadout(new ArrayList<>(BaseLoadouts.size() + BetaLoadouts.size()));
    }

    public List<AnimatorLoadout> GetEveryLoadout(List<AnimatorLoadout> list)
    {
        list.clear();
        list.addAll(BaseLoadouts);
        list.addAll(BetaLoadouts);
        return list;
    }

    public AnimatorLoadout GetByName(String name)
    {
        for (AnimatorLoadout loadout : GetEveryLoadout())
        {
            if (loadout.Name.equals(name))
            {
                return loadout;
            }
        }

        return null;
    }

    public AnimatorLoadout GetLoadout(Synergy synergy)
    {
        if (synergy != null)
        {
            for (AnimatorLoadout loadout : GetEveryLoadout())
            {
                if (synergy.equals(loadout.Synergy))
                {
                    return loadout;
                }
            }
        }

        return null;
    }

    public AnimatorLoadout GetLoadout(int id, boolean isBeta)
    {
        return isBeta ? GetBetaLoadout(id) : GetBaseLoadout(id);
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

    public void RecordVictory(int ascensionLevel)
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

    public void RecordTrueVictory(int ascensionLevel)
    {
        if (ascensionLevel < 0) // Ascension reborn mod adds negative ascension levels
        {
            return;
        }

        if (GR.Common.Dungeon.IsUnnamedReign())
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
        JUtils.LogInfo(AnimatorPlayerData.class, "Saving Trophies");

        GR.Animator.Config.SetTrophyString(SerializeTrophies(), flush);
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

    private void AddBetaLoadouts()
    {
        BetaLoadouts.clear();
        //BetaLoadouts.add(new <YourLoadoutHere>);
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
                int loadoutID = JUtils.ParseInt(items[0], -1);
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
