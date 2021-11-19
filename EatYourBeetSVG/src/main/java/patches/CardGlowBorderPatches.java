package patches;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.cardManip.CardGlowBorder;
import eatyourbeets.utilities.FieldInfo;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;
import javassist.CtBehavior;

public class CardGlowBorderPatches
{
    protected static final FieldInfo<AbstractCard> _card = JUtils.GetField("card", CardGlowBorder.class);
    protected static final FieldInfo<TextureAtlas.AtlasRegion> _img = JUtils.GetField("img", CardGlowBorder.class);
    protected static final FieldInfo<Float> _scale = JUtils.GetField("scale", CardGlowBorder.class);
    protected static final FieldInfo<Color> _color = JUtils.GetField("color", AbstractGameEffect.class);

    public static Color overrideColor;

    @SpirePatch(clz = CardGlowBorder.class, method = SpirePatch.CONSTRUCTOR, paramtypez = {AbstractCard.class, Color.class})
    public static class CardGlowBorderPatches_ctor
    {
        @SpireInsertPatch(locator = Locator.class)
        public static SpireReturn<Void> InsertPre(CardGlowBorder __instance, AbstractCard card, Color letsHardcodeEverything)
        {
            if (!GameUtilities.InGame()) {
                OverrideColor(__instance);
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }

        @SpirePostfixPatch
        public static void Method(CardGlowBorder __instance, AbstractCard card, Color letsHardcodeEverything)
        {
            OverrideColor(__instance);
        }

        private static void OverrideColor(CardGlowBorder __instance)
        {
            Color color = _color.Get(__instance);
            if (color == null) {
                _color.Set(__instance, Color.GREEN.cpy());
                color = _color.Get(__instance);
            }
            if (overrideColor != null)
            {
                if (_color.Get(__instance) != null)
                {
                    color.r = overrideColor.r;
                    color.g = overrideColor.g;
                    color.b = overrideColor.b;
                }
            }
        }

        private static class Locator extends SpireInsertLocator
        {
            public int[] Locate(CtBehavior ctBehavior) throws Exception
            {
                Matcher matcher = new Matcher.FieldAccessMatcher(CardGlowBorder.class, "duration");
                return new int[]{ LineFinder.findInOrder(ctBehavior, matcher)[0] + 1 };
            }
        }
    }

    @SpirePatch(clz = CardGlowBorder.class, method = "update")
    public static class CardGlowBorderPatches_Update
    {
        @SpirePrefixPatch
        public static void Method(CardGlowBorder __instance)
        {
            AbstractCard card = _card.Get(__instance);
            if (card.transparency < 0.9f)
            {
                __instance.duration = 0f;
            }
        }
    }
}