package eatyourbeets.actions.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;
import eatyourbeets.actions.animator.AnimatorAction;
import eatyourbeets.utilities.GameActionsHelper;

public class SequentialAction extends AnimatorAction
{
    private final AbstractGameAction action;
    private final AbstractGameAction action2;

    public SequentialAction(AbstractGameAction action, AbstractGameAction action2)
    {
        this.action = action;
        this.action2 = action2;
        this.target = action.target;
        this.duration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.SPECIAL;
        this.isDone = false;
    }

    public void update()
    {
        if (updateAction())
        {
            GameActionsHelper.AddToTop(action2);

            this.isDone = true;
        }
    }

    private boolean updateAction()
    {
        if (!action.isDone)
        {
            action.update();
        }

        return action.isDone;
    }
}
