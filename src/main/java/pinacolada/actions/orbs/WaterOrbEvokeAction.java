package pinacolada.actions.orbs;

import com.megacrit.cardcrawl.helpers.Hitbox;
import eatyourbeets.actions.EYBAction;
import pinacolada.effects.SFX;
import pinacolada.effects.VFX;
import pinacolada.powers.common.BurningPower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;

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
        PCLGameEffects.Queue.Add(VFX.Water(x,y));
        PCLGameEffects.Queue.Add(VFX.Water(x,y));
        PCLGameEffects.Queue.Add(VFX.Water(x,y));
        SFX.Play(SFX.PCL_ORB_WATER_EVOKE, 0.9f, 1.1f);
        PCLActions.Bottom.GainTemporaryHP(amount);
        PCLActions.Bottom.RemovePower(player,player, BurningPower.POWER_ID);
        Complete();
    }
}