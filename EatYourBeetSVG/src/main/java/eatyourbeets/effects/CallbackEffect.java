package eatyourbeets.effects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

import java.util.function.BiConsumer;

public class CallbackEffect extends AbstractGameEffect
{
    private final AbstractGameAction action;
    private final BiConsumer<Object, AbstractGameAction> onCompletion;
    private final Object state;

    public CallbackEffect(AbstractGameAction action)
    {
        this(action, null, null);
    }

    public CallbackEffect(AbstractGameAction action, Object state, BiConsumer<Object, AbstractGameAction> onCompletion)
    {
        this.action = action;
        this.onCompletion = onCompletion;
        this.state = state;
        this.duration = 1f;
    }

    public void update()
    {
        if (!action.isDone)
        {
            action.update();
        }
        else
        {
            this.isDone = true;
            if (onCompletion != null)
            {
                onCompletion.accept(state, action);
            }
        }
    }

    public void render(SpriteBatch sb)
    {

    }

    public void dispose()
    {

    }
}