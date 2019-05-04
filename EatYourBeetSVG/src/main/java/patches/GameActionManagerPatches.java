package patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.metrics.MetricData;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import eatyourbeets.cards.animator.Gilgamesh;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.relics.ExquisiteBloodVial;

import java.util.Iterator;

public class GameActionManagerPatches
{
    @SpirePatch(clz = GameActionManager.class, method = "incrementDiscard", paramtypez = {boolean.class})
    public static class GameActionManager_IncrementDiscard
    {
        @SpirePostfixPatch
        public static void Postfix(boolean endOfTurn)
        {
            if (!AbstractDungeon.actionManager.turnHasEnded && !endOfTurn)
            {
                PlayerStatistics.Instance.OnManualDiscard();
            }
        }
    }
}