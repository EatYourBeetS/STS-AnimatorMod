package eatyourbeets.effects.utility;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.effects.EYBEffectWithCallback;
import eatyourbeets.interfaces.delegates.ActionT0;
import eatyourbeets.interfaces.delegates.ActionT1;
import eatyourbeets.interfaces.delegates.ActionT2;

public class CallbackEffect extends EYBEffectWithCallback<AbstractGameAction>
{
    private boolean updateIfScreenIsUp = true;
    private final AbstractGameAction action;

    public CallbackEffect(AbstractGameAction action)
    {
        super(1);

        this.action = action;
    }

    public CallbackEffect(AbstractGameAction action, ActionT0 onCompletion)
    {
        this(action);

        AddCallback(onCompletion);
    }

    public CallbackEffect(AbstractGameAction action, ActionT1<AbstractGameAction> onCompletion)
    {
        this(action);

        AddCallback(onCompletion);
    }

    public <S> CallbackEffect(AbstractGameAction action, S state, ActionT2<S, AbstractGameAction> onCompletion)
    {
        this(action);

        AddCallback(state, onCompletion);
    }

    public CallbackEffect UpdateIfScreenIsUp(boolean value)
    {
        updateIfScreenIsUp = value;

        return this;
    }

    @Override
    public void update()
    {
        if (!action.isDone)
        {
            if (updateIfScreenIsUp || !AbstractDungeon.isScreenUp)
            {
                action.update();
            }
        }
        else
        {
            Complete(action);
        }
    }
}