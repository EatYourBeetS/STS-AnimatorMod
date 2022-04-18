package patches.cardGroup;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.random.Random;
import eatyourbeets.cards.base.CardRarityComparator;
import eatyourbeets.interfaces.listeners.OnRemovedFromDeckListener;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

import java.util.ArrayList;
import java.util.Comparator;

public class CardGroupPatches
{
    @SpirePatch(clz = CardGroup.class, method = "sortWithComparator", paramtypez = {Comparator.class, boolean.class})
    public static class CardGroupPatches_SortWithComparator
    {
        @SpirePrefixPatch
        public static void Prefix(CardGroup __instance, @ByRef Comparator<AbstractCard>[] comp, boolean ascending)
        {
            if (comp[0].getClass().getSimpleName().equals(CardRarityComparator.class.getSimpleName()))
            {
                comp[0] = new CardRarityComparator();
            }
        }
    }

    @SpirePatch(clz = CardGroup.class, method = "getPurgeableCards")
    public static class CardGroupPatches_GetPurgeableCards
    {
        @SpirePrefixPatch
        public static SpireReturn<CardGroup> Postfix(CardGroup __instance)
        {
            final CardGroup result = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            for (AbstractCard c : __instance.group)
            {
                if (GameUtilities.CanRemoveFromDeck(c))
                {
                    result.group.add(c);
                }
            }

            return SpireReturn.Return(result);
        }
    }

    @SpirePatch(clz = CardGroup.class, method = "removeCard", paramtypez = {AbstractCard.class})
    public static class CardGroupPatches_RemoveCard
    {
        public static void Postfix(CardGroup __instance, AbstractCard c)
        {
            if (__instance.type == CardGroup.CardGroupType.MASTER_DECK)
            {
                OnRemovedFromDeckListener card = JUtils.SafeCast(c, OnRemovedFromDeckListener.class);
                if (card != null)
                {
                    card.OnRemovedFromDeck();
                }
            }
        }
    }

    @SpirePatch(clz = CardGroup.class, method = "shuffle", paramtypez = {})
    public static class CardGroupPatches_Shuffle1
    {
        @SpirePostfixPatch
        public static void Postfix(CardGroup __instance)
        {
            _Shuffle(__instance, AbstractDungeon.shuffleRng);
        }
    }

    @SpirePatch(clz = CardGroup.class, method = "shuffle", paramtypez = {Random.class})
    public static class CardGroupPatches_Shuffle2
    {
        @SpirePostfixPatch
        public static void Postfix(CardGroup __instance, Random rng)
        {
            _Shuffle(__instance, rng);
        }
    }

    private static void _Shuffle(CardGroup group, Random rng)
    {
        final java.util.Random random = new java.util.Random(rng.randomLong());
        final ArrayList<AbstractCard> delayedCards = new ArrayList<>();
        final ArrayList<AbstractCard> cards = group.group;

        int i = 0;
        while (i < cards.size())
        {
            final AbstractCard c = cards.get(i);
            if (c.hasTag(GR.Enums.CardTags.DELAYED))
            {
                c.tags.remove(GR.Enums.CardTags.DELAYED);
                c.isInnate = false;
                delayedCards.add(c);
                cards.remove(i);
            }
            else
            {
                i++;
            }
        }

        if (cards.size() <= 1)
        {
            cards.addAll(0, delayedCards);
        }
        else for (AbstractCard c : delayedCards)
        {
            cards.add(random.nextInt(cards.size() / 2), c);
        }
    }
}
