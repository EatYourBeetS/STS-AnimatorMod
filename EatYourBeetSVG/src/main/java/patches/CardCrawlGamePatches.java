package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import eatyourbeets.dungeons.TheUnnamedReign;
import eatyourbeets.powers.PlayerStatistics;

public class CardCrawlGamePatches
{
    @SpirePatch(clz = CardCrawlGame.class, method = "loadPlayerSave", paramtypez = {AbstractPlayer.class})
    public static class CardCrawlGamePatches_loadPlayerSave
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
    public static class CardCrawlGamePatches_startOver
    {
        @SpirePrefixPatch
        public static void Prefix()
        {
            PlayerStatistics.OnStartOver();
        }
    }

    @SpirePatch(clz = CardCrawlGame.class, method = "getDungeon", paramtypez = {String.class, AbstractPlayer.class})
    public static class CardCrawlGamePatches_GetDungeon1
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
    public static class CardCrawlGamePatches_GetDungeon2
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
}