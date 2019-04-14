package eatyourbeets.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;

import java.util.Date;
import java.time.Instant;

public class WaitRealtimeAction extends AbstractGameAction
{
    private Instant targetTime;

    public WaitRealtimeAction(float setDur)
    {
        this.setValues(null, null, 0);

        this.duration = setDur;
        this.actionType = ActionType.WAIT;
        this.targetTime = Date.from(Instant.now()).toInstant().plusMillis((long)(setDur * 1000));
    }

    public void update()
    {
        Instant now = Date.from(Instant.now()).toInstant();
        if (now.isAfter(targetTime))
        {
            this.isDone = true;
        }
    }
}
