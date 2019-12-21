package eatyourbeets.actions.utility;

import eatyourbeets.actions.EYBAction;

public class WaitRealtimeAction extends EYBAction
{
    public WaitRealtimeAction(float duration)
    {
        super(ActionType.WAIT, duration);

        this.isRealtime = true;

        Initialize(1);
    }
}