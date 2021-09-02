package eatyourbeets.resources.animator;

import basemod.BaseMod;
import basemod.abstracts.CustomUnlock;
import basemod.abstracts.CustomUnlockBundle;
import com.badlogic.gdx.utils.Base64Coder;
import com.megacrit.cardcrawl.unlock.AbstractUnlock;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.interfaces.delegates.ActionT2;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.loadouts.*;
import eatyourbeets.resources.animator.misc.*;
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
        Reload();
    }

    public void Reload()
    {
        DeserializeTrophies(GR.Animator.Config.Trophies.Get());
        DeserializeCustomLoadouts(GR.Animator.Config.CustomLoadouts.Get());

        if (SelectedLoadout == null || SelectedLoadout.ID < 0)
        {
            SelectedLoadout = BaseLoadouts.get(0);
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
        final AnimatorLoadout loadout = GetBaseLoadout(id);
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

        GR.Animator.Config.Trophies.Set(SerializeTrophies(), flush);
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
            loadout.IsBeta = false;
            loadout.UnlockLevel = unlockLevel;
            loadout.AddStarterCards();
        };

        //add.Invoke(new Loadout_Konosuba(), 0);
        add.Invoke(new Loadout_GATE(), 1);
        add.Invoke(new Loadout_Elsword(), 2);
        //add.Invoke(new Loadout_Katanagatari(), 2);
        add.Invoke(new Loadout_GoblinSlayer(), 3);
        //add.Invoke(new Loadout_NoGameNoLife(), 3);
        //add.Invoke(new Loadout_OwariNoSeraph(), 3);
        add.Invoke(new Loadout_FullmetalAlchemist(), 4);
        add.Invoke(new Loadout_Overlord(), 4);
        add.Invoke(new Loadout_Fate(), 5);
        //add.Invoke(new Loadout_HitsugiNoChaika(), 5);
        //add.Invoke(new Loadout_OnePunchMan(), 6);
        add.Invoke(new Loadout_TenseiSlime(), 6);
        //add.Invoke(new Loadout_MadokaMagica(), 7);
        //add.Invoke(new Loadout_LogHorizon(), 7);

        for (AnimatorLoadout loadout : BaseLoadouts)
        {
            if (loadout.UnlockLevel <= 0)
            {
                continue;
            }

            final String cardID = loadout.GetSymbolicCard().ID;
            final CustomUnlock unlock = new CustomUnlock(AbstractUnlock.UnlockType.MISC, cardID);
            unlock.type = AbstractUnlock.UnlockType.CARD;
            unlock.card = new AnimatorRuntimeLoadout(loadout).BuildCard();
            unlock.key = unlock.card.cardID = GR.Animator.CreateID("series:" + loadout.Name);

            CustomUnlockBundle bundle = BaseMod.getUnlockBundleFor(GR.Animator.PlayerClass, loadout.UnlockLevel - 1);
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
        BetaLoadouts.clear();

        final ActionT2<AnimatorLoadout, Integer> add = (loadout, unlockLevel) ->
        {
            BetaLoadouts.add(loadout);
            loadout.IsBeta = true;
            loadout.UnlockLevel = unlockLevel;
            loadout.AddStarterCards();
        };

        add.Invoke(new Loadout_Konosuba(), 0);
        add.Invoke(new Loadout_Katanagatari(), 2);
        add.Invoke(new Loadout_NoGameNoLife(), 3);
        add.Invoke(new Loadout_OwariNoSeraph(), 3);
        add.Invoke(new Loadout_HitsugiNoChaika(), 5);
        add.Invoke(new Loadout_OnePunchMan(), 6);
        add.Invoke(new Loadout_MadokaMagica(), 7);
        add.Invoke(new Loadout_LogHorizon(), 7);
    }

    // SelectedLoadout|Series_1,Trophy1,Trophy2,Trophy3|Series_2,Trophy1,Trophy2,Trophy3|...
    private String SerializeTrophies()
    {
        final StringJoiner sj = new StringJoiner("|");

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
        SpecialTrophies = null;

        if (data != null && data.length() > 0)
        {
            final String decoded = Base64Coder.decodeString(data);
            final String[] items = JUtils.SplitString("|", decoded);

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
                    final AnimatorTrophies trophies = new AnimatorTrophies();

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

    //003 _Gold@60;_HP@99;Strike@3:0;Defend@3:1;animator:Strike_Dark@1:2|004 Strike@4:0 ...
    private String SerializeCustomLoadouts()
    {
        final StringJoiner sj = new StringJoiner("|");
        final StringBuilder sb = new StringBuilder();

        final int level = GR.Animator.GetUnlockLevel();
        for (AnimatorLoadout loadout : GetEveryLoadout())
        {
            if (loadout.UnlockLevel <= level)
            {
                for (AnimatorLoadoutData data : loadout.Presets)
                {
                    if (data == null)
                    {
                        continue;
                    }

                    sb.setLength(0);
                    sb.append(StringUtils.leftPad(String.valueOf(loadout.ID), 3, '0'))
                      .append(" ")
                      .append("_Preset@").append(data.Preset).append(":").append((data.Preset == loadout.Preset) ? 1 : 0).append(";")
                      .append("_Gold@").append(data.Gold).append(";")
                      .append("_HP@").append(data.HP).append(";");

                    for (AnimatorCardSlot slot : data)
                    {
                        if (slot.amount > 0)
                        {
                            sb.append(slot.GetData().ID).append("@")
                              .append(slot.amount).append(":")
                              .append(slot.GetSlotIndex()).append(";");
                        }
                    }

                    sj.add(sb.toString());
                }
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
        final String[] strings = JUtils.SplitString("|", decoded);
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
            final AnimatorLoadoutData loadoutData = new AnimatorLoadoutData(loadout);
            for (String item : s.substring(4).split(Pattern.quote(";")))
            {
                final int index = item.indexOf("@");
                final String[] amountAndIndex = item.substring(index + 1).split(Pattern.quote(":"));
                final int itemAmount = JUtils.ParseInt(amountAndIndex[0], 0);
                final int itemIndex = amountAndIndex.length > 1 ? JUtils.ParseInt(amountAndIndex[1], -1) : -1;
                final String itemID = item.substring(0, index);
                switch (itemID)
                {
                    case "_Preset":
                        if (itemIndex == 1)
                        {
                            loadout.Preset = loadoutData.Preset = itemAmount;
                        }
                        else
                        {
                            loadoutData.Preset = itemAmount;
                        }
                        break;

                    case "_Gold":
                        loadoutData.Gold = itemAmount;
                        break;

                    case "_HP":
                        loadoutData.HP = itemAmount;
                        break;

                    default:
                    {
                        if (itemIndex < 0 || itemIndex >= loadoutData.Size())
                        {
                            return;
                        }

                        final AnimatorCardSlot slot = loadoutData.GetCardSlot(itemIndex);
                        for (AnimatorCardSlot.Item c : slot.Cards)
                        {
                            if (c.data.ID.equals(itemID))
                            {
                                slot.Select(c, itemAmount);
                                break;
                            }
                        }
                        
                        break;
                    }
                }
            }

            if (loadoutData.Validate().IsValid)
            {
                loadout.Presets[loadoutData.Preset] = loadoutData;
            }
        }
    }
}
