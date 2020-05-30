package patches.ControlPilePatches;

import com.evacipated.cardcrawl.modthespire.lib.LineFinder;
import com.evacipated.cardcrawl.modthespire.lib.Matcher;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertLocator;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.ui.common.ControllableCard;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

public class CardUsePatch {

    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "useCard"
    )
    public static class AlterCardUse
    {
        public static ExprEditor Instrument()
        {
            return new ExprEditor()
            {
                @Override
                public void edit(MethodCall m) throws CannotCompileException
                {
                    //stops base game card.use from being called
                    if (m.getMethodName().equals("use"))
                    {
                        m.replace("");
                    }
                }
            };
        }
    }

    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "useCard"
    )
    public static class CheckForControlPileCard
    {
        @SpireInsertPatch(
                locator = CardUsePatch.CheckForControlPileCard.CheckLocator.class
        )
        public static void checkCard(AbstractPlayer __instance, AbstractCard c, AbstractMonster monster, int energyOnUse)
        {
            boolean triggered = false;
            for (ControllableCard controllableCard : CombatStats.ControlPile.controllers) {
                if (controllableCard.card.equals(c)) {
                    if (controllableCard.alterPlay) {
                        controllableCard.Select();
                    } else {
                        c.use(__instance, monster);
                    }
                    triggered = true;
                }
            }
            if (!triggered) {
                c.use(__instance, monster);
            }
        }

        private static class CheckLocator extends SpireInsertLocator
        {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
            {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(GameActionManager.class, "addToBottom");

                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

}