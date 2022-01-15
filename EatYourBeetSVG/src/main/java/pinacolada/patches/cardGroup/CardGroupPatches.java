package pinacolada.patches.cardGroup;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.random.Random;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLGameUtilities;

import java.util.ArrayList;

public class CardGroupPatches
{
    @SpirePatch(clz = CardGroup.class, method = "getPurgeableCards")
    public static class CardGroupPatches_GetPurgeableCards
    {
        @SpirePrefixPatch
        public static SpireReturn<CardGroup> Postfix(CardGroup __instance)
        {
            final CardGroup result = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            for (AbstractCard c : __instance.group)
            {
                if (PCLGameUtilities.CanRemoveFromDeck(c))
                {
                    result.group.add(c);
                }
            }

            return SpireReturn.Return(result);
        }
    }

    @SpirePatch(clz = CardGroup.class, method = "shuffle", paramtypez = {})
    public static class CardGroupPatches_Shuffle1
    {
        @SpirePostfixPatch
        public static void Postfix(CardGroup __instance)
        {
            _Shuffle(__instance);
        }
    }

    @SpirePatch(clz = CardGroup.class, method = "shuffle", paramtypez = {Random.class})
    public static class CardGroupPatches_Shuffle2
    {
        @SpirePostfixPatch
        public static void Postfix(CardGroup __instance, Random rng)
        {
            _Shuffle(__instance);
        }
    }

    private static void _Shuffle(CardGroup group)
    {
        final ArrayList<AbstractCard> cards = group.group;
        int delayedIndex = 0;
        int innateIndex = cards.size() - 1;
        for (int i = 0; i < cards.size(); i++)
        {
            final AbstractCard c = cards.get(i);
            if (c.hasTag(GR.Enums.CardTags.DELAYED))
            {
                if (i != delayedIndex)
                {
                    final AbstractCard temp = cards.get(delayedIndex);
                    cards.set(delayedIndex, c);
                    cards.set(i, temp);
                }

                c.isInnate = false;
                delayedIndex += 1;
            }
        }

        for (int i = cards.size() - 1; i >= 0; i--)
        {
            final AbstractCard c = cards.get(i);
            if (c.hasTag(GR.Enums.CardTags.PCL_INNATE)) {
                if (i != innateIndex) {
                    final AbstractCard temp = cards.get(innateIndex);
                    cards.set(innateIndex, c);
                    cards.set(i, temp);
                }
                innateIndex -= 1;
            }
        }
    }

    @SpirePatch(clz = CardGroup.class, method = "getBottomCard", paramtypez = {})
    public static class CardGroupPatches_GetBottomCard
    {
        @SpirePostfixPatch
        public static SpireReturn<AbstractCard> Prefix(CardGroup __instance)
        {
            if (__instance.group.size() == 0) {
                return SpireReturn.Return(null);
            }
            else {
                return SpireReturn.Continue();
            }
        }
    }

    @SpirePatch(clz = CardGroup.class, method = "getTopCard", paramtypez = {})
    public static class CardGroupPatches_GetTopCard
    {
        @SpirePostfixPatch
        public static SpireReturn<AbstractCard> Prefix(CardGroup __instance)
        {
            if (__instance.group.size() == 0) {
                return SpireReturn.Return(null);
            }
            else {
                return SpireReturn.Continue();
            }
        }
    }

    // These random methods crash if the group is empty

    @SpirePatch(clz = CardGroup.class, method = "getRandomCard", paramtypez = {Random.class})
    public static class CardGroupPatches_GetRandomCard1
    {
        @SpirePostfixPatch
        public static SpireReturn<AbstractCard> Prefix(CardGroup __instance, Random rng)
        {
            if (__instance.group.size() == 0) {
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = CardGroup.class, method = "getRandomCard", paramtypez = {boolean.class})
    public static class CardGroupPatches_GetRandomCard2
    {
        @SpirePostfixPatch
        public static SpireReturn<AbstractCard> Prefix(CardGroup __instance, boolean useRng)
        {
            if (__instance.group.size() == 0) {
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }
    }
}
