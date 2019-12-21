package eatyourbeets.effects.utility;

import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import eatyourbeets.effects.EYBEffectWithCallback;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class CallbackEffect2 extends EYBEffectWithCallback<AbstractGameEffect>
{
    private final AbstractGameEffect effect;

    public CallbackEffect2(AbstractGameEffect effect)
    {
        super(1);

        this.effect = effect;
    }

    public CallbackEffect2(AbstractGameEffect effect, Consumer<AbstractGameEffect> onCompletion)
    {
        this(effect);

        AddCallback(onCompletion);
    }

    public CallbackEffect2(AbstractGameEffect effect, Object state, BiConsumer<Object, AbstractGameEffect> onCompletion)
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