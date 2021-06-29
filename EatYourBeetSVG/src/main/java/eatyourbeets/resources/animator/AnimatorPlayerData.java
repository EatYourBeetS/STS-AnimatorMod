package eatyourbeets.resources.animator;

import basemod.BaseMod;
import basemod.abstracts.CustomUnlock;
import basemod.abstracts.CustomUnlockBundle;
import com.badlogic.gdx.utils.Base64Coder;
import eatyourbeets.resources.animator.loadouts.beta.*;
import com.megacrit.cardcrawl.unlock.AbstractUnlock;
import eatyourbeets.cards.base.*;
import eatyourbeets.interfaces.delegates.ActionT2;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.loadouts.*;
import eatyourbeets.resources.animator.misc.AnimatorLoadout;
import eatyourbeets.resources.animator.misc.AnimatorRuntimeLoadout;
import eatyourbeets.resources.animator.misc.AnimatorTrophies;
import eatyourbeets.utilities.JUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.regex.Pattern;

public class AnimatorPlayerData
{
    public final int MaxUnlockLevel = 8;
    public final ArrayList<AnimatorLoadout> BaseLoadouts = new ArrayList<>();
    public final ArrayList<AnimatorLoadout> BetaLoadouts = new ArrayList<>();
    public final ArrayList<AnimatorTrophies> Trophies = new ArrayList<>();
    public AnimatorTrophies SpecialTrophies = new AnimatorTrophies(-1);
    public AnimatorLoadout SelectedLoadout = new _FakeLoadout();

    public void Initialize()
    {
        AddBaseLoadouts();
        AddBetaLoadouts();
        DeserializeTrophies(GR.Animator.Config.TrophyString());

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

        GR.Animator.Config.TrophyString(SerializeTrophies(), flush);
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
        add.Invoke(new Elsword(), 2);
        add.Invoke(new Katanagatari(), 2);
        add.Invoke(new GoblinSlayer(), 3);
        add.Invoke(new NoGameNoLife(), 3);
        add.Invoke(new OwariNoSeraph(), 3);
        add.Invoke(new FullmetalAlchemist(), 4);
        add.Invoke(new Overlord(), 4);
        add.Invoke(new Fate(), 5);
        add.Invoke(new Chaika(), 5);
        add.Invoke(new Kancolle(), 6);
        add.Invoke(new OnePunchMan(), 6);
        add.Invoke(new AccelWorld(), 7);
        add.Invoke(new TenSura(), 7);
        add.Invoke(new MadokaMagica(), 8);
        add.Invoke(new LogHorizon(), 8);

        for (AnimatorLoadout loadout : BaseLoadouts)
        {
            final String cardID = loadout.GetSymbolicCard().ID;
            CustomUnlockBundle bundle = BaseMod.getUnlockBundleFor(GR.Animator.PlayerClass, loadout.UnlockLevel);

            CustomUnlock unlock = new CustomUnlock(AbstractUnlock.UnlockType.MISC, cardID);
            unlock.type = AbstractUnlock.UnlockType.CARD;
            unlock.card = new AnimatorRuntimeLoadout(loadout).BuildCard();
            unlock.key = unlock.card.cardID = GR.Animator.CreateID("series:" + loadout.Name);

            if (bundle == null)
            {
                bundle = new CustomUnlockBundle(AbstractUnlock.UnlockType.MISC, "", "", "");
                bundle.getUnlocks().clear();
                bundle.getUnlockIDs().clear();
                bundle.getUnlockIDs().add(unlock.key);
                bundle.getUnlocks().add(unlock);
                bundle.unlockType = AbstractUnlock.UnlockType.CARD;
            }
            else
            {
                bundle.getUnlockIDs().add(unlock.key);
                bundle.getUnlocks().add(unlock);
            }

            BaseMod.addUnlockBundle(bundle, GR.Animator.PlayerClass, loadout.UnlockLevel);
        }
    }

    private void AddBetaLoadouts()
    {
        BetaLoadouts.clear();
        BetaLoadouts.add(new DateALive());
        BetaLoadouts.add(new Rewrite());
        BetaLoadouts.add(new AngelBeats());
        BetaLoadouts.add(new TouhouProject());
        BetaLoadouts.add(new RozenMaiden());
        BetaLoadouts.add(new Bleach());
        BetaLoadouts.add(new GenshinImpact());
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

                    if (SelectedLoadout == null)
                    {
                        SelectedLoadout = GetBetaLoadout(loadoutID);
                    }
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
