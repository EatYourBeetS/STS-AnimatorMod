package eatyourbeets.actions.monsters;

import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.actions.EYBAction;

public class MoveMonsterAction extends EYBAction
{
    private float startX;
    private float startY;
    private float targetX;
    private float targetY;

    public MoveMonsterAction(AbstractCreature target, float targetX, float targetY, float duration)
    {
        super(ActionType.SPECIAL, duration);

        this.targetX = targetX;
        this.targetY = targetY;
        this.startX = target.drawX;
        this.startY = target.drawY;

        Initialize(null, target, 1);
    }

    @Override
    protected void UpdateInternal(float deltaTime)
    {
        target.drawX = Interpolation.linear.apply(startX, targetX, (startDuration - duration) / startDuration);
        target.drawY = Interpolation.linear.apply(startY, targetY, (startDuration - duration) / startDuration);

        if (TickDuration(deltaTime))
        {
            target.drawX = targetX;
            target.drawY = targetY;
        }
    }
}