package patches.cardCrawlGame;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import eatyourbeets.powers.PlayerStatistics;

@SpirePatch(clz = CardCrawlGame.class, method = "startOver")
public class CardCrawlGame_StartOver
{
    @SpirePrefixPatch
    public static void Prefix()
    {
        PlayerStatistics.OnStartOver();
    }
}