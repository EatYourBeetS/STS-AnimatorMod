package patches.AfterlifePatches;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.LineFinder;
import com.evacipated.cardcrawl.modthespire.lib.Matcher;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertLocator;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.ui.panels.ExhaustPanel;
import javassist.CtBehavior;

import java.util.ArrayList;

public class RenderTopCard {
    //patch refresh hand layout to also refresh position of top card

    @SpirePatch(
            clz = ExhaustPanel.class,
            method = "render"
    )
    public static class Render
    {
        @SpirePostfixPatch
        public static void doTheRenderThing(ExhaustPanel __instance, SpriteBatch sb)
        {
            if (!AbstractDungeon.isScreenUp)
            {
                if (!AbstractDungeon.player.exhaustPile.isEmpty())
                {
                    ArrayList<AbstractCard> c = UpdateAndTrackTopCard.Fields.currentCard.get(AbstractDungeon.player.exhaustPile);
                    if (c != null) {
                        for (AbstractCard card : c) {
                            if (!card.equals(AbstractDungeon.player.hoveredCard)) {
                                card.render(sb);
                            }
                        }
                    }
                }
            }
        }
    }

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
                ArrayList<AbstractCard> c = UpdateAndTrackTopCard.Fields.currentCard.get(AbstractDungeon.player.exhaustPile);
                if (c != null) {
                    for (AbstractCard card : c) {
                        if (__instance.equals(card)) {
                            if (!__instance.hasEnoughEnergy()) {
                                costColor[0] = RED;
                            }
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