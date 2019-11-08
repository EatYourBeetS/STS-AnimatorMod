package eatyourbeets.actions.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;

import java.util.Date;
import java.time.Instant;

public class WaitRealtimeAction extends AbstractGameAction
{
    private final float realtimeDuration;
    private Instant targetTime;

    public WaitRealtimeAction(float setDur)
    {
        this.setValues(null, null, 0);

        this.duration = setDur;
        this.realtimeDuration = setDur;
        this.actionType = ActionType.WAIT;
    }

    public void update()
    {
        if (targetTime == null)
        {
            this.targetTime = Date.from(Instant.now()).toInstant().plusMillis((long)(realtimeDuration * 1000));
        }
        else
        {
            Instant now = Date.from(Instant.now()).toInstant();
            if (now.isAfter(targetTime))
            {
                this.isDone = true;
            }
        }
    }
}
