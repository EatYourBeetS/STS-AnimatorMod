package pinacolada.patches.abstractCard;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import javassist.CtBehavior;
import pinacolada.cards.base.modifiers.BlockModifiers;
import pinacolada.cards.base.modifiers.CostModifiers;
import pinacolada.cards.base.modifiers.DamageModifiers;
import pinacolada.powers.PCLCombatStats;

public class AbstractCardPatches
{
    @SpirePatch(clz = AbstractCard.class, method = "<class>")
    public static class AbstractCard_Fields
    {
        public static final SpireField<CostModifiers> costModifiers = new SpireField<>(() -> null);
        public static final SpireField<DamageModifiers> damageModifiers = new SpireField<>(() -> null);
        public static final SpireField<BlockModifiers> blockModifiers = new SpireField<>(() -> null);
    }

    @SpirePatch(clz = AbstractCard.class, method = "canUse", paramtypez = {AbstractPlayer.class, AbstractMonster.class})
    public static class AbstractCard_CanUse
    {
        @SpirePostfixPatch
        public static boolean Method(boolean __result, AbstractCard __instance, AbstractPlayer p, AbstractMonster m)
        {
            return PCLCombatStats.OnTryUsingCard(__instance, p, m, __result);
        }
    }

    @SpirePatch(clz = AbstractCard.class, method = "makeStatEquivalentCopy")
    public static class AbstractCard_MakeStatEquivalentCopy
    {
        @SpireInsertPatch(localvars = {"card"}, locator = AbstractCardPatches.AbstractCard_MakeStatEquivalentCopy.Locator.class)
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
        @SpireInsertPatch(localvars = {"card"}, locator = AbstractCardPatches.AbstractCard_MakeSameInstanceOf.Locator.class)
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