package eatyourbeets.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;

import java.util.function.BiConsumer;

public class CallbackAction extends AnimatorAction
{
    private final AbstractGameAction action;
    private final BiConsumer<Object, AbstractGameAction> onCompletion;
    private final Object state;

    public CallbackAction(AbstractGameAction action, BiConsumer<Object, AbstractGameAction> onCompletion, Object state)
    {
        this.action = action;
        this.onCompletion = onCompletion;
        this.state = state;
        this.target = action.target;
        this.duration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.SPECIAL;
        this.isDone = false;
    }

    public void update()
    {
        if (updateAction())
        {
            onCompletion.accept(state, action);

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
