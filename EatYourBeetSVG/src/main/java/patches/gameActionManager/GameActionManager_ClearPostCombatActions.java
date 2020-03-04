package patches.gameActionManager;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import eatyourbeets.actions.EYBAction;

import java.util.ArrayList;

@SpirePatch(clz = GameActionManager.class, method = "clearPostCombatActions")
public class GameActionManager_ClearPostCombatActions
{
    @SpirePrefixPatch
    public static SpireReturn Prefix(GameActionManager __instance)
    {
        ArrayList<AbstractGameAction> actions = __instance.actions;
        for (int i = actions.size() - 1; i >= 0; i--)
        {
            AbstractGameAction action = actions.get(i);
            if (action instanceof EYBAction)
            {
                if (((EYBAction) action).canCancel)
                {
                    actions.remove(i);
                }
            }
            else if (!(action instanceof HealAction
                    || action instanceof GainBlockAction
                    || action instanceof UseCardAction
                    || action.actionType == AbstractGameAction.ActionType.DAMAGE))
            {
                actions.remove(i);
            }
        }

        return SpireReturn.Return(null);
    }
}