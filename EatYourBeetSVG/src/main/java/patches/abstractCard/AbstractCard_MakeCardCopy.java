package patches.abstractCard;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import eatyourbeets.cards.base.modifiers.BlockModifiers;
import eatyourbeets.cards.base.modifiers.CostModifiers;
import eatyourbeets.cards.base.modifiers.DamageModifiers;
import javassist.CtBehavior;

public class AbstractCard_MakeCardCopy
{
    @SpirePatch(clz = AbstractCard.class, method = "makeStatEquivalentCopy")
    public static class AbstractCard_MakeStatEquivalentCopy
    {
        @SpireInsertPatch(localvars = {"card"}, locator = Locator.class)
        public static void Method(AbstractCard __instance, AbstractCard card)
        {
            DamageModifiers.For(card).CopyFrom(DamageModifiers.For(__instance), false);
            BlockModifiers.For(card).CopyFrom(BlockModifiers.For(__instance), false);
            CostModifiers.For(card).CopyFrom(CostModifiers.For(__instance), false);
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
            DamageModifiers.For(card).CopyFrom(DamageModifiers.For(__instance), true);
            BlockModifiers.For(card).CopyFrom(BlockModifiers.For(__instance), true);
            CostModifiers.For(card).CopyFrom(CostModifiers.For(__instance), true);
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