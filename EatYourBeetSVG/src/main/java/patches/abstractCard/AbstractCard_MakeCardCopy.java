package patches.abstractCard;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import eatyourbeets.cards.base.modifiers.BlockModifier;
import eatyourbeets.cards.base.modifiers.CostModifier;
import eatyourbeets.cards.base.modifiers.DamageModifier;
import javassist.CtBehavior;

public class AbstractCard_MakeCardCopy
{
    @SpirePatch(clz = AbstractCard.class, method = "makeStatEquivalentCopy")
    public static class AbstractCard_MakeStatEquivalentCopy
    {
        @SpireInsertPatch(localvars = {"card"}, locator = Locator.class)
        public static void Method(AbstractCard __instance, AbstractCard card)
        {
            DamageModifier.For(card).CopyFrom(DamageModifier.For(__instance), false);
            BlockModifier.For(card).CopyFrom(BlockModifier.For(__instance), false);
            CostModifier.For(card).CopyFrom(CostModifier.For(__instance), false);
        }

        private static class Locator extends SpireInsertLocator
        {
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
            {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractCard.class, "makeCopy");
                return new int[]{LineFinder.findInOrder(ctMethodToPatch, finalMatcher)[0] + 1};
            }
        }
    }

    @SpirePatch(clz = AbstractCard.class, method = "makeSameInstanceOf")
    public static class AbstractCard_MakeSameInstanceOf
    {
        @SpireInsertPatch(localvars = {"card"}, locator = Locator.class)
        public static void Method(AbstractCard __instance, AbstractCard card)
        {
            DamageModifier.For(card).CopyFrom(DamageModifier.For(__instance), true);
            BlockModifier.For(card).CopyFrom(BlockModifier.For(__instance), true);
            CostModifier.For(card).CopyFrom(CostModifier.For(__instance), true);
        }

        private static class Locator extends SpireInsertLocator
        {
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
            {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractCard.class, "makeStatEquivalentCopy");
                return new int[]{LineFinder.findInOrder(ctMethodToPatch, finalMatcher)[0] + 1};
            }
        }
    }
}