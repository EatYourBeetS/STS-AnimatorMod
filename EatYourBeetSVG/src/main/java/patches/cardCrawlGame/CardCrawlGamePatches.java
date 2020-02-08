package patches.cardCrawlGame;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.DrawMaster;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import eatyourbeets.dungeons.TheUnnamedReign;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.resources.GR;
import javassist.CannotCompileException;
import javassist.CtBehavior;

public class CardCrawlGamePatches
{
    @SpirePatch(clz = CardCrawlGame.class, method = "render")
    public static class CardCrawlGame_Render
    {
        @SpireInsertPatch(locator = Locator.class, localvars = {"sb"})
        public static void Insert(CardCrawlGame __Instance, SpriteBatch sb)
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

    @SpirePatch(clz = CardCrawlGame.class, method = "update")
    public static class CardCrawlGame_Update
    {
        @SpireInsertPatch(locator = Locator.class)
        public static void Insert(Object __obj_instance)
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
        public static SpireReturn<AbstractDungeon> Prefix(CardCrawlGame instance, String key, AbstractPlayer p)
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
        public static SpireReturn<AbstractDungeon> Prefix(CardCrawlGame instance, String key, AbstractPlayer p, SaveFile saveFile)
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
            PlayerStatistics.LoadingPlayerSave = true;
        }

        @SpirePostfixPatch
        public static void Postfix(CardCrawlGame instance, AbstractPlayer p)
        {
            PlayerStatistics.LoadingPlayerSave = false;
        }
    }

    @SpirePatch(clz = CardCrawlGame.class, method = "startOver")
    public static class CardCrawlGame_StartOver
    {
        @SpirePrefixPatch
        public static void Prefix()
        {
            PlayerStatistics.OnStartOver();
        }
    }
}
