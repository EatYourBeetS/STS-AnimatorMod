package eatyourbeets.actions;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import com.megacrit.cardcrawl.actions.AbstractGameAction;

public class WaitRealtimeAction extends AbstractGameAction
{
    public WaitRealtimeAction(float setDur)
    {
        this.setValues(null, null, 0);

        this.duration = setDur;

        this.actionType = ActionType.WAIT;
    }

    public void update()
    {
        this.tickDuration();
    }
}
