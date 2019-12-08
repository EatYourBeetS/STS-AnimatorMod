package eatyourbeets.actions.utility;

import eatyourbeets.actions.EYBAction;

import java.time.Instant;

public class WaitRealtimeAction extends EYBAction
{
    private final float realtimeDuration;
    private Instant targetTime;

    public WaitRealtimeAction(float duration)
    {
        super(ActionType.WAIT, duration);

        Initialize(1);

        this.realtimeDuration = duration;
    }

    @Override
    public void update()
    {
        if (targetTime == null)
        {
            this.targetTime = Instant.now().plusMillis((long)(realtimeDuration * 1000));
        }
        else if (Instant.now().isAfter(targetTime))
        {
            this.isDone = true;
        }
    }
}
