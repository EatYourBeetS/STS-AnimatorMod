package pinacolada.patches.cardCrawlGame;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.DrawMaster;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import pinacolada.powers.PCLCombatStats;
import pinacolada.resources.GR;

public class CardCrawlGamePatches
{
    @SpirePatch(clz = AbstractPlayer.class, method = "renderPlayerBattleUi", paramtypez = {SpriteBatch.class})
    public static class AbstractPlayer_PreRender
    {
        @SpirePrefixPatch
        public static void Insert(AbstractPlayer __instance, SpriteBatch sb)
        {
            GR.UI.PreRender(sb);
        }
    }

    @SpirePatch(clz = CardCrawlGame.class, method = "render")
    public static class CardCrawlGame_Render
    {
        @SpireInsertPatch(locator = Locator.class, localvars = {"sb"})
        public static void Insert(CardCrawlGame __instance, SpriteBatch sb)
        {
            GR.UI.Render(sb);
        }

        private static class Locator extends SpireInsertLocator
        {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException
            {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(DrawMaster.class, "draw");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch(clz = CardCrawlGame.class, method = "render")
    public static class CardCrawlGame_PostRender
    {
        @SpireInsertPatch(locator = Locator.class, localvars = {"sb"})
        public static void Insert(CardCrawlGame __instance, SpriteBatch sb)
        {
            //TODO make a generic overlay property for UI
            GR.UI.CardFilters.TryRender(sb);
            GR.UI.PostRender(sb);
        }

        private static class Locator extends SpireInsertLocator
        {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException
            {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(TipHelper.class, "render");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch(clz = CardCrawlGame.class, method = "update")
    public static class CardCrawlGame_Update
    {
        @SpirePrefixPatch
        public static void Prefix(CardCrawlGame __instance)
        {
            GR.UI.PreUpdate();
        }

        @SpirePostfixPatch
        public static void Postfix(CardCrawlGame __instance)
        {
            GR.UI.PostUpdate();
        }

        @SpireInsertPatch(locator = Locator.class)
        public static void Insert(CardCrawlGame __instance)
        {
            GR.UI.Update();
        }

        private static class Locator extends SpireInsertLocator
        {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException
            {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(InputHelper.class, "updateFirst");
                int[] res = LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
                res[0] += 1;
                return res;
            }
        }
    }

    @SpirePatch(clz = CardCrawlGame.class, method = "startOver")
    public static class CardCrawlGame_StartOver
    {
        @SpirePrefixPatch
        public static void Prefix()
        {
            PCLCombatStats.OnStartOver();
        }
    }
}
