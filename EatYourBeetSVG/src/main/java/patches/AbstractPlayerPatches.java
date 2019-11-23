package patches;

import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import eatyourbeets.cards.UnnamedCard;
import eatyourbeets.potions.FalseLifePotion;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.relics.UnnamedReign.UnnamedReignRelic;
import eatyourbeets.utilities.JavaUtilities;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

public class AbstractPlayerPatches
{
    @SpirePatch(clz= AbstractPlayer.class, method = "obtainPotion", paramtypez = {AbstractPotion.class})
    public static class AbstractPlayerPatch_ObtainPotion1
    {
        @SpireInsertPatch(rloc = 0, localvars = {"potionToObtain"})
        public static void Method(AbstractPlayer __instance, AbstractPotion param, @ByRef AbstractPotion[] potionToObtain)
        {
            if (UnnamedReignRelic.IsEquipped())
            {
                potionToObtain[0] = new FalseLifePotion();
            }
        }
    }

    @SpirePatch(clz= AbstractPlayer.class, method = "obtainPotion", paramtypez = {int.class, AbstractPotion.class})
    public static class AbstractPlayerPatch_ObtainPotion2
    {
        @SpireInsertPatch(rloc = 0, localvars = {"potionToObtain"})
        public static void Method(AbstractPlayer __instance, int slot, AbstractPotion param, @ByRef AbstractPotion[] potionToObtain)
        {
            if (UnnamedReignRelic.IsEquipped())
            {
                potionToObtain[0] = new FalseLifePotion();
            }
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "useCard")
    public static class AbstractPlayerPatches_useCard
    {
        public static ExprEditor Instrument()
        {
            return new ExprEditor()
            {
                public void edit(MethodCall m) throws CannotCompileException
                {
                    if (m.getClassName().equals(AbstractCard.class.getName()) && m.getMethodName().equals("use"))
                    {
                        m.replace("{ " +
                                "if (patches.AbstractPlayerPatches.AbstractPlayerPatches_useCard.CanUse($0)) " +
                                "{ " +
                                "$proceed($$); " +
                                "} " +
                                "patches.AbstractPlayerPatches.AbstractPlayerPatches_useCard.AfterUse($0); " +
                                "}");
                    }
                }
            };
        }

        public static boolean CanUse(AbstractCard c)
        {
            UnnamedCard card = JavaUtilities.SafeCast(c, UnnamedCard.class);

            return card == null || !card.isVoidbound() || card.enteredVoid;
        }

        public static void AfterUse(AbstractCard c)
        {
            PlayerStatistics.Void.UseMastery(c);
        }
    }
}