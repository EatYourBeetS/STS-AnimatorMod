package eatyourbeets.effects.utility;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import eatyourbeets.effects.EYBEffectWithCallback;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class CallbackEffect extends EYBEffectWithCallback<AbstractGameAction>
{
    private final AbstractGameAction action;

    protected CallbackEffect(AbstractGameAction action)
    {
        super(1);

        this.action = action;
    }

    public CallbackEffect(AbstractGameAction action, Consumer<AbstractGameAction> onCompletion)
    {
        this(action);

        AddCallback(onCompletion);
    }

    public CallbackEffect(AbstractGameAction action, Object state, BiConsumer<Object, AbstractGameAction> onCompletion)
    {
        this(action);

        AddCallback(state, onCompletion);
    }

    @Override
    public void update()
    {
        if (!action.isDone)
        {
            action.update();
        }
        else
        {
            Complete(action);
        }
    }
}