package eatyourbeets.actions.special;

import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import eatyourbeets.actions.EYBActionWithCallback;
import eatyourbeets.utilities.GameEffects;

public class PlayVFX extends EYBActionWithCallback<AbstractGameEffect>
{
    private AbstractGameEffect effect;
    private boolean isTopLevelEffect;
    private boolean wait;

    public PlayVFX(AbstractGameEffect effect, float duration)
    {
        super(ActionType.WAIT, duration);

        this.wait = false;
        this.effect = effect;
    }

    @Override
    protected void FirstUpdate()
    {
        if (this.isTopLevelEffect)
        {
            GameEffects.TopLevelList.Add(effect);
        }
        else
        {
            GameEffects.List.Add(effect);
        }
    }

    @Override
    protected void UpdateInternal(float deltaTime)
    {
        if (wait)
        {
            isDone = effect.isDone;
        }
        else
        {
            TickDuration(deltaTime);
        }

        if (isDone)
        {
            Complete(effect);
        }
    }
}
