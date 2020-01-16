package patches;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.cardManip.CardGlowBorder;
import eatyourbeets.utilities.FieldInfo;
import eatyourbeets.utilities.JavaUtilities;

@SpirePatch(clz= CardGlowBorder.class, method = SpirePatch.CONSTRUCTOR, paramtypez = {AbstractCard.class, Color.class})
public class CardGlowBorderPatches
{
    private static final FieldInfo<Color> colorField = JavaUtilities.GetField("color", AbstractGameEffect.class);

    public static Color overrideColor;

    @SpirePostfixPatch
    public static void Method(CardGlowBorder __instance, AbstractCard card, Color letsHardcodeEverything)
    {
        if (overrideColor != null)
        {
            Color color = colorField.Get(__instance);
            if (color != null)
            {
                color.r = overrideColor.r;
                color.g = overrideColor.g;
                color.b = overrideColor.b;
            }
        }
    }
}