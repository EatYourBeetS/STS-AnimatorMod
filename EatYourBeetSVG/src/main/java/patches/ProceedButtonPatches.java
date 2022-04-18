package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.TheBeyond;
import com.megacrit.cardcrawl.ui.buttons.ProceedButton;
import eatyourbeets.utilities.GameUtilities;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

public class ProceedButtonPatches
{
    @SpirePatch(clz = ProceedButton.class, method = "update")
    public static class ProceedButtonPatches_update
    {
        public static ExprEditor Instrument()
        {
            return new ExprEditor()
            {
                @Override
                public void edit(MethodCall m) throws CannotCompileException
                {
                    if (m.getMethodName().equals("equals"))
                    {
                        m.replace("$_ = patches.ProceedButtonPatches.ProceedButtonPatches_update.CheckCondition($$);");
                    }
                }
            };
        }

        public static boolean CheckCondition(Object requiredID)
        {
            if (TheBeyond.ID.equals(requiredID))
            {
                if (AbstractDungeon.bossList.size() >= 2 && GameUtilities.GetAscensionData(true).DoubleBosses(AbstractDungeon.actNum))
                {
                    return true;
                }
            }

            return AbstractDungeon.id.equals(requiredID);
        }
    }
}
