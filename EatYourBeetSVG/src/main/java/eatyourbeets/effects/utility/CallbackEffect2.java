package eatyourbeets.effects.utility;

import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import eatyourbeets.effects.EYBEffectWithCallback;
import eatyourbeets.interfaces.delegates.ActionT0;
import eatyourbeets.interfaces.delegates.ActionT1;
import eatyourbeets.interfaces.delegates.ActionT2;

public class CallbackEffect2 extends EYBEffectWithCallback<AbstractGameEffect>
{
    private final AbstractGameEffect effect;

    public CallbackEffect2(AbstractGameEffect effect)
    {
        super(1);

        this.effect = effect;
    }

    public CallbackEffect2(AbstractGameEffect effect, ActionT0 onCompletion)
    {
        this(effect);

        AddCallback(onCompletion);
    }

    public CallbackEffect2(AbstractGameEffect effect, ActionT1<AbstractGameEffect> onCompletion)
    {
        this(effect);

        AddCallback(onCompletion);
    }

    public CallbackEffect2(AbstractGameEffect effect, Object state, ActionT2<Object, AbstractGameEffect> onCompletion)
    {
        this(effect);

        AddCallback(state, onCompletion);
    }

    @Override
    public void update()
    {
        if (!effect.isDone)
        {
            effect.update();
        }
        else
        {
            Complete(effect);
        }
    }
}