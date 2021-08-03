package patches.cardGroup;

import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import eatyourbeets.cards.base.CardRarityComparator;
import eatyourbeets.interfaces.listeners.OnRemovedFromDeckListener;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

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
}
