package eatyourbeets.resources.animator;

import basemod.BaseMod;
import basemod.abstracts.CustomUnlock;
import basemod.abstracts.CustomUnlockBundle;
import com.badlogic.gdx.utils.Base64Coder;
import com.megacrit.cardcrawl.unlock.AbstractUnlock;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.interfaces.delegates.ActionT1;
import eatyourbeets.interfaces.delegates.ActionT2;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.loadouts.*;
import eatyourbeets.resources.animator.loadouts.beta.*;
import eatyourbeets.resources.animator.misc.AnimatorLoadout;
import eatyourbeets.resources.animator.misc.AnimatorRuntimeLoadout;
import eatyourbeets.resources.animator.misc.AnimatorTrophies;
import eatyourbeets.resources.animator.misc.CardSlot;
import eatyourbeets.utilities.JUtils;
import org.apache.commons.lang3.StringUtils;

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
        DeserializeCustomLoadouts(GR.Animator.Config.CustomLoadouts.Get());

        if (SelectedLoadout == null || SelectedLoadout.ID < 0)
        {
            SelectedLoadout = GetBaseLoadout(CardSeries.Konosuba.ID);
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

    public AnimatorLoadout GetLoadout(CardSeries series)
    {
        if (series != null)
        {
            for (AnimatorLoadout loadout : GetEveryLoadout())
            {
                if (series.equals(loadout.Series))
                {
                    return loadout;
                }
            }
        }

        return null;
    }

    public AnimatorLoadout GetLoadout(int id)
    {
        AnimatorLoadout loadout = GetBaseLoadout(id);
        if (loadout == null)
        {
            return GetBetaLoadout(id);
        }

        return loadout;
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

    public void SaveLoadouts(boolean flush)
    {
        JUtils.LogInfo(AnimatorPlayerData.class, "Saving Loadouts");

        GR.Animator.Config.CustomLoadouts.Set(SerializeCustomLoadouts(), flush);
    }

    private void AddBaseLoadouts()
    {
        BaseLoadouts.clear();

        final ActionT2<AnimatorLoadout, Integer> add = (loadout, unlockLevel) ->
        {
            BaseLoadouts.add(loadout);
            loadout.UnlockLevel = unlockLevel;
            loadout.InitializeSlots();
        };

        add.Invoke(new Loadout_Konosuba(), 0);
        add.Invoke(new Loadout_GATE(), 1);
        add.Invoke(new Loadout_Elsword(), 2);
        add.Invoke(new Loadout_Katanagatari(), 2);
        add.Invoke(new Loadout_GoblinSlayer(), 3);
        add.Invoke(new Loadout_NoGameNoLife(), 3);
        add.Invoke(new Loadout_OwariNoSeraph(), 3);
        add.Invoke(new Loadout_FullmetalAlchemist(), 4);
        add.Invoke(new Loadout_Overlord(), 4);
        add.Invoke(new Loadout_Fate(), 5);
        add.Invoke(new Loadout_HitsugiNoChaika(), 5);
        add.Invoke(new Loadout_OnePunchMan(), 6);
        add.Invoke(new Loadout_TenseiSlime(), 6);
        add.Invoke(new Loadout_MadokaMagica(), 7);
        add.Invoke(new Loadout_LogHorizon(), 7);

        for (AnimatorLoadout loadout : BaseLoadouts)
        {
            if (loadout.UnlockLevel == 0)
            {
                continue;
            }

            final String cardID = loadout.GetSymbolicCard().ID;
            CustomUnlockBundle bundle = BaseMod.getUnlockBundleFor(GR.Animator.PlayerClass, loadout.UnlockLevel - 1);

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

            BaseMod.addUnlockBundle(bundle, GR.Animator.PlayerClass, loadout.UnlockLevel - 1);
        }
    }

    private void AddBetaLoadouts()
    {
        final ActionT1<AnimatorLoadout> add = (loadout) ->
        {
            BetaLoadouts.add(loadout);
            loadout.InitializeSlots();
        };

        BetaLoadouts.clear();
        add.Invoke(new Loadout_DateALive());
        add.Invoke(new Loadout_Rewrite());
        add.Invoke(new Loadout_AngelBeats());
        add.Invoke(new Loadout_TouhouProject());
        add.Invoke(new Loadout_RozenMaiden());
        add.Invoke(new Loadout_Bleach());
        add.Invoke(new Loadout_GenshinImpact());
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

    //003 Strike@3;Defend@3;animator:Strike_Dark@1|004 Strike@4 ...
    private String SerializeCustomLoadouts()
    {
        StringJoiner sj = new StringJoiner("|");
        StringBuilder sb = new StringBuilder();

        int level = GR.Animator.GetUnlockLevel();
        for (AnimatorLoadout loadout : GetEveryLoadout())
        {
            if (loadout.UnlockLevel <= level)
            {
                sb.setLength(0);
                sb.append(StringUtils.leftPad(String.valueOf(loadout.ID), 3, '0'));
                sb.append(" ");
                for (CardSlot slot : loadout.Slots)
                {
                    if (slot.amount > 0)
                    {
                        sb.append(slot.GetData().ID);
                        sb.append("@");
                        sb.append(slot.amount);
                        sb.append(";");
                    }
                }
                sj.add(sb.toString());
            }
        }

        return Base64Coder.encodeString(sj.toString());
    }

    private void DeserializeCustomLoadouts(String data)
    {
        if (StringUtils.isEmpty(data))
        {
            return;
        }

        final String decoded = Base64Coder.decodeString(data);
        final String[] strings = decoded.split(Pattern.quote("|"));
        for (String s : strings)
        {
            final int id = JUtils.ParseInt(s.substring(0, 3), -1);
            final AnimatorLoadout loadout = GetLoadout(id);
            if (loadout == null)
            {
                JUtils.LogWarning(this, "Loadout not found, ID:" + id);
                continue;
            }

            int i = 0;
            for (String card : s.substring(4).split(Pattern.quote(";")))
            {
                final int index = card.indexOf("@");
                final int cardAmount = JUtils.ParseInt(card.substring(index + 1), 0);
                final String cardID = card.substring(0, index);
                final CardSlot slot = loadout.Slots.Get(i);
                for (CardSlot.Item item : slot.Cards)
                {
                    if (item.data.ID.equals(cardID))
                    {
                        slot.Select(item, cardAmount);
                        break;
                    }
                }

                i += 1;
            }

            if (!loadout.Validate().IsValid)
            {
                loadout.LoadStartingDeck();
            }
            else
            {
                loadout.Slots.Ready = true;
            }
        }
    }
}
