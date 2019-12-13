package eatyourbeets.actions.monsters;

import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;

public class MoveMonsterAction extends AbstractGameAction
{
    private float startX;
    private float startY;
    private float targetX;
    private float targetY;

    public MoveMonsterAction(AbstractCreature target, float targetX, float targetY, float duration)
    {
        this.target = target;
        this.targetX = targetX;
        this.targetY = targetY;
        this.startX = target.drawX;
        this.startY = target.drawY;
        this.startDuration = this.duration = duration;
    }

    public void update()
    {
        target.drawX = Interpolation.linear.apply(startX, targetX, (startDuration - duration) / startDuration);
        target.drawY = Interpolation.linear.apply(startY, targetY, (startDuration - duration) / startDuration);

        tickDuration();

        if (this.isDone)
        {
            target.drawX = targetX;
            target.drawY = targetY;
        }
    }
}