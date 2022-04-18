package patches.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.ui.FtueTip;
import eatyourbeets.utilities.RenderHelpers;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

public class FtueTipPatches
{
    @SpirePatch(clz= FtueTip.class, method="render", paramtypez = {SpriteBatch.class})
    public static class FtueTipPatches_Render
    {
        public static ExprEditor Instrument()
        {
            return new ExprEditor()
            {
                public void edit(MethodCall m) throws CannotCompileException
                {
                    if (m.getClassName().equals(FontHelper.class.getName()) && m.getMethodName().equals("renderSmartText"))
                    {
                        m.replace("{ patches.screens.FtueTipPatches.FtueTipPatches_Render.RenderSmartText($$); }");
                    }
                }
            };
        }

        public static void RenderSmartText(SpriteBatch sb, BitmapFont font, String msg, float x, float y, float lineWidth, float lineSpacing, Color baseColor)
        {
            if (msg.startsWith("~"))
            {
                RenderHelpers.WriteSmartText(sb, font, msg.substring(1), x, y, lineWidth, lineSpacing, baseColor);
            }
            else
            {
                FontHelper.renderSmartText(sb, font, msg, x, y, lineWidth, lineSpacing, baseColor);
            }
        }
    }
}