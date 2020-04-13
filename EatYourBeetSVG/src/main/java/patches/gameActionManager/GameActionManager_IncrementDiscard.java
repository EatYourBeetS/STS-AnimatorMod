package patches.gameActionManager;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.powers.CombatStats;

@SpirePatch(clz = GameActionManager.class, method = "incrementDiscard", paramtypez = {boolean.class})
public class GameActionManager_IncrementDiscard
{
    @SpirePostfixPatch
    public static void Postfix(boolean endOfTurn)
    {
        if (!AbstractDungeon.actionManager.turnHasEnded && !endOfTurn)
        {
            CombatStats.Instance.OnManualDiscard();
        }
    }
}