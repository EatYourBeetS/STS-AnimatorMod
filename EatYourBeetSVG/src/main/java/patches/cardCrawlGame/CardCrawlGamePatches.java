package patches.cardCrawlGame;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.DrawMaster;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import eatyourbeets.dungeons.TheUnnamedReign;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.resources.GR;
import javassist.CannotCompileException;
import javassist.CtBehavior;

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

    @SpirePatch(clz = CardCrawlGame.class, method = "getDungeon", paramtypez = {String.class, AbstractPlayer.class})
    public static class CardCrawlGame_GetDungeon
    {
        @SpirePrefixPatch
        public static SpireReturn<AbstractDungeon> Prefix(CardCrawlGame __instance, String key, AbstractPlayer p)
        {
            if (key.equals(TheUnnamedReign.ID))
            {
                return SpireReturn.Return(new TheUnnamedReign(p, AbstractDungeon.specialOneTimeEventList));
            }
            else
            {
                return SpireReturn.Continue();
            }
        }
    }

    @SpirePatch(clz = CardCrawlGame.class, method = "getDungeon", paramtypez = {String.class, AbstractPlayer.class, SaveFile.class})
    public static class CardCrawlGame_GetDungeon2
    {
        @SpirePrefixPatch
        public static SpireReturn<AbstractDungeon> Prefix(CardCrawlGame __instance, String key, AbstractPlayer p, SaveFile saveFile)
        {
            if (key.equals(TheUnnamedReign.ID))
            {
                return SpireReturn.Return(new TheUnnamedReign(p, saveFile));
            }
            else
            {
                return SpireReturn.Continue();
            }
        }
    }

    @SpirePatch(clz = CardCrawlGame.class, method = "loadPlayerSave", paramtypez = {AbstractPlayer.class})
    public static class CardCrawlGame_LoadPlayerSave
    {
        @SpirePrefixPatch
        public static void Prefix(CardCrawlGame instance, AbstractPlayer p)
        {
            CombatStats.LoadingPlayerSave = true;
        }

        @SpirePostfixPatch
        public static void Postfix(CardCrawlGame instance, AbstractPlayer p)
        {
            CombatStats.LoadingPlayerSave = false;
        }
    }

    @SpirePatch(clz = CardCrawlGame.class, method = "startOver")
    public static class CardCrawlGame_StartOver
    {
        @SpirePrefixPatch
        public static void Prefix()
        {
            CombatStats.OnStartOver();
        }
    }
}
