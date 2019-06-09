package patches;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.cardManip.CardGlowBorder;
import eatyourbeets.Utilities;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.powers.PlayerStatistics;

import java.lang.reflect.Field;

@SpirePatch(clz= CardGlowBorder.class, method = SpirePatch.CONSTRUCTOR, paramtypez = {AbstractCard.class})
public class CardGlowBorderPatch
{
    private static Field colorField;

    public static Color overrideColor;

    @SpirePostfixPatch
    public static void Method(CardGlowBorder __instance, AbstractCard card) throws IllegalAccessException, NoSuchFieldException
    {
        if (overrideColor != null)
        {
            Color color = GetColor(__instance);
            if (color != null)
            {
                color.r = overrideColor.r;
                color.g = overrideColor.g;
                color.b = overrideColor.b;
            }
        }
        else
        {
            AnimatorCard c = Utilities.SafeCast(card, AnimatorCard.class);
            if (c != null && c.HasActiveSynergy())
            {
                Color color = GetColor(__instance);
                if (color != null)
                {
                    color.r = Color.GOLD.r;
                    color.g = Color.GOLD.g;
                    color.b = Color.GOLD.b;
                }
            }
        }
    }

    private static Color GetColor(CardGlowBorder instance) throws NoSuchFieldException, IllegalAccessException
    {
        if (colorField == null)
        {
            colorField = AbstractGameEffect.class.getDeclaredField("color");
            colorField.setAccessible(true);
        }

        return (Color) colorField.get(instance);
    }
}