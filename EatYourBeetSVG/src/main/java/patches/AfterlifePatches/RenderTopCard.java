package patches.AfterlifePatches;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.LineFinder;
import com.evacipated.cardcrawl.modthespire.lib.Matcher;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertLocator;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.ui.common.ControllableCard;
import javassist.CtBehavior;

public class RenderTopCard {

    @SpirePatch(
            clz = AbstractCard.class,
            method = "renderEnergy"
    )
    public static class RenderAltColor
    {
        private static final Color RED = new Color(1.0f, 0.3f, 0.3f, 1.0f);

        @SpireInsertPatch(
                locator = Locator.class,
                localvars = { "costColor" }
        )
        public static void modifyColor(AbstractCard __instance, SpriteBatch sb, @ByRef Color[] costColor)
        {
            if (AbstractDungeon.player != null)
            {
                for (ControllableCard c : CombatStats.ControlPile.controllers) {
                    AbstractCard card = c.card;
                    if (__instance.equals(card)) {
                        if (!__instance.hasEnoughEnergy()) {
                            costColor[0] = RED;
                        }
                    }
                }
            }
        }

        private static class Locator extends SpireInsertLocator
        {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
            {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractCard.class, "transparency");

                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }
}