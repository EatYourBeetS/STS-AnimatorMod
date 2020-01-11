package patches.cardCrawlGame;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.dungeons.TheUnnamedReign;

@SpirePatch(clz = CardCrawlGame.class, method = "getDungeon", paramtypez = {String.class, AbstractPlayer.class})
public class CardCrawlGame_GetDungeon
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