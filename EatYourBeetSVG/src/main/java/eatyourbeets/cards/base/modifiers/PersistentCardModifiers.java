package eatyourbeets.cards.base.modifiers;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.resources.common.CommonDungeonData;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PersistentCardModifiers
{
    protected static final String KEY = PersistentCardModifiers.class.getSimpleName();
    protected static final Map<AbstractCard, Modifiers> map = new HashMap<>();
    protected static class Modifiers
    {
        protected int damage;
        protected int block;

        protected Modifiers(int damage, int block)
        {
            this.damage = damage;
            this.block = block;
        }

        protected Modifiers(String data)
        {
            final String[] arr = JUtils.SplitString(":", data);
            if (arr.length >= 2)
            {
                damage = JUtils.ParseInt(arr[0], 0);
                block = JUtils.ParseInt(arr[1], 0);
            }
        }

        protected StringBuilder Save(StringBuilder sb)
        {
            return sb.append(damage).append(":").append(block);
        }
    }

    public static void OnLoad(CommonDungeonData data)
    {
        final String text = data.GetString(KEY, "");
        if (StringUtils.isEmpty(text))
        {
            return;
        }

        final String[] pairs = JUtils.SplitString(",", text);
        final ArrayList<AbstractCard> deck = AbstractDungeon.player.masterDeck.group;
        for (String pair : pairs)
        {
            final String[] arr = JUtils.SplitString("=", pair);
            final int index = JUtils.ParseInt(arr[0], -1);
            if (index >= 0)
            {
                Apply(deck.get(index), new Modifiers(arr[1]), true);
            }
        }
    }

    public static void OnSave(CommonDungeonData data)
    {
        final StringBuilder sb = new StringBuilder();
        final ArrayList<AbstractCard> deck = AbstractDungeon.player.masterDeck.group;
        for (AbstractCard key : map.keySet())
        {
            final int index = deck.indexOf(key);
            final Modifiers mods = map.get(key);
            if (index >= 0 && mods != null)
            {
                sb.append(index).append("=");
                mods.Save(sb).append(",");
            }
        }

        data.SetData(KEY, sb);
    }

    public static void Clear()
    {
        map.clear();
    }

    public static void Apply(AbstractCard masterDeckCard, int damage, int block, boolean applyToBattleInstances)
    {
        final Modifiers mods = new Modifiers(damage, block);

        Apply(masterDeckCard, mods, true);

        if (applyToBattleInstances)
        {
            for (AbstractCard c : GameUtilities.GetAllInBattleInstances(masterDeckCard.uuid))
            {
                Apply(c, mods, false);
            }
        }
    }

    public static void Apply(AbstractCard card, Modifiers mods, boolean isMasterDeck)
    {
        if (mods.block > 0 && card.baseBlock > 0)
        {
            GameUtilities.IncreaseBlock(card, mods.block, false);
        }
        if (mods.damage > 0 && card.baseDamage > 0)
        {
            GameUtilities.IncreaseDamage(card, mods.damage, false);
        }

        if (isMasterDeck)
        {
            final Modifiers currentMods = map.get(card);
            if (currentMods == null)
            {
                map.put(card, mods);
            }
            else
            {
                currentMods.damage += mods.damage;
                currentMods.block += mods.block;
            }
        }
    }
}
