package eatyourbeets.effects.utility;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.effects.EYBEffectWithCallback;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class CallbackEffect extends EYBEffectWithCallback<AbstractGameAction>
{
    private boolean updateIfScreenIsUp = true;
    private final AbstractGameAction action;

    public CallbackEffect(AbstractGameAction action)
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