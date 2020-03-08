package patches.abstractDungeon;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.random.Random;
import eatyourbeets.events.base.EYBEvent;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameUtilities;

@SpirePatch(clz = AbstractDungeon.class, method = "getEvent", paramtypez = Random.class)
public class AbstractDungeon_GetEvent
{
    @SpirePrefixPatch
    public static SpireReturn<AbstractEvent> Prefix(Random rng)
    {
        AbstractEvent event = EYBEvent.GenerateSpecialEvent(CardCrawlGame.dungeon, rng, GameUtilities.IsPlayerClass(GR.Animator.PlayerClass));
        if (event != null)
        {
            return SpireReturn.Return(event);
        }

        return SpireReturn.Continue();
    }
}