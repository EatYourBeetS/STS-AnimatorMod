package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.screens.DrawPileViewScreen;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

public class SatoriFrozenEyePatch
{
    @SpirePatch(clz = DrawPileViewScreen.class, method = "open")
    public static class FrozenEyePls
    {
        public static ExprEditor Instrument()
        {
            return new ExprEditor()
            {
                @Override
                public void edit(MethodCall m) throws CannotCompileException
                {
                    if (m.getMethodName().equals("hasRelic"))
                    {
                        m.replace("$_ = $proceed($$) || " + AbstractDungeon.class.getName() + ".player.hasPower(\"animator:SatoriKomeijiPower\");");
                    }
                }
            };
        }
    }

}