package pinacolada.effects.utility;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.interfaces.delegates.ActionT0;
import eatyourbeets.interfaces.delegates.ActionT1;
import eatyourbeets.interfaces.delegates.ActionT2;
import pinacolada.effects.PCLEffectWithCallback;

public class CallbackEffect extends PCLEffectWithCallback<AbstractGameAction>
{
    protected float blackScreenAlpha = 0;
    protected boolean updateIfScreenIsUp = true;
    protected final AbstractGameAction action;

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

    public CallbackEffect ShowBlackScreen(float alpha)
    {
        blackScreenAlpha = alpha;

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

        if (blackScreenAlpha != 0)
        {
            AbstractDungeon.overlayMenu.showBlackScreen(action.isDone ? 0 : blackScreenAlpha);
        }
    }
}