package patches.actions;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.utility.ShowCardAndPoofAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.FieldInfo;
import eatyourbeets.utilities.JUtils;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.expr.ExprEditor;

public class UseCardActionPatches
{
    private static final FieldInfo<AbstractCard> _targetCard = JUtils.GetField("targetCard", UseCardAction.class);

    @SpirePatch(clz = UseCardAction.class, method = "update")
    public static class UseCardAction_Update1
    {
        public static ExprEditor Instrument()
        {
            return new ExprEditor()
            {
                public void edit(javassist.expr.FieldAccess m) throws CannotCompileException
                {
                    if (m.getClassName().equals(AbstractCard.class.getName()) && m.getFieldName().equals("purgeOnUse"))
                    {
                        m.replace("{ $_ = patches.actions.UseCardActionPatches.UseCardAction_Update1.Patch($0); }");
                    }
                }
            };
        }

        public static boolean Patch(AbstractCard card)
        {
            return card.purgeOnUse || card.tags.contains(EYBCard.PURGE);
        }
    }

    @SpirePatch(clz = UseCardAction.class, method = "update")
    public static class UseCardAction_Update2
    {
        @SpireInsertPatch(locator = Locator.class)
        public static void Insert(UseCardAction __instance)
        {
            CombatStats.OnCardPurged(_targetCard.Get(__instance));
        }

        private static class Locator extends SpireInsertLocator
        {
            public int[] Locate(CtBehavior ctBehavior) throws Exception
            {
                final Matcher matcher = new Matcher.NewExprMatcher(ShowCardAndPoofAction.class);
                return new int[] { LineFinder.findInOrder(ctBehavior, matcher)[0] + 1};
            }
        }
    }
}