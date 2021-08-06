package eatyourbeets.actions.orbs;

import com.megacrit.cardcrawl.helpers.Hitbox;
import eatyourbeets.actions.EYBAction;
import eatyourbeets.effects.SFX;
import eatyourbeets.effects.VFX;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

public class WaterOrbEvokeAction extends EYBAction
{
    protected final float x;
    protected final float y;

    public WaterOrbEvokeAction(Hitbox hb, int tempHP)
    {
        super(ActionType.DEBUFF);
        this.x = hb.cX;
        this.y = hb.cY;

        Initialize(tempHP);
    }

    @Override
    protected void FirstUpdate()
    {
        GameEffects.Queue.Add(VFX.Water(x,y));
        GameEffects.Queue.Add(VFX.Water(x,y));
        GameEffects.Queue.Add(VFX.Water(x,y));
        SFX.Play(SFX.ANIMATOR_ORB_WATER_EVOKE, 0.9f, 1.1f);
        GameActions.Bottom.GainTemporaryHP(amount);
        Complete();
    }
}