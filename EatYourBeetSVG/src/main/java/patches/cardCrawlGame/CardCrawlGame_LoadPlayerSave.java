package patches.cardCrawlGame;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import eatyourbeets.powers.PlayerStatistics;

@SpirePatch(clz = CardCrawlGame.class, method = "loadPlayerSave", paramtypez = {AbstractPlayer.class})
public class CardCrawlGame_LoadPlayerSave
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