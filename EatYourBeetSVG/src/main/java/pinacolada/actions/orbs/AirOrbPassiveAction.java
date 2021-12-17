package pinacolada.actions.orbs;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.Settings;
import eatyourbeets.actions.EYBAction;
import pinacolada.effects.AttackEffects;
import pinacolada.effects.SFX;
import pinacolada.effects.VFX;
import pinacolada.orbs.pcl.Air;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;

public class AirOrbPassiveAction extends EYBAction
{
    protected final Air orb;

    public AirOrbPassiveAction(Air orb, int damage)
    {
        super(ActionType.DAMAGE, Settings.ACTION_DUR_XFAST);

        this.orb = orb;

        Initialize(damage);
    }

    @Override
    protected void FirstUpdate()
    {
        SFX.Play(SFX.ATTACK_WHIRLWIND);
        PCLGameEffects.Queue.Add(VFX.Whirlwind());
        PCLGameEffects.Queue.Add(VFX.RazorWind(orb.hb, player.hb, MathUtils.random(1000.0F, 1200.0F), MathUtils.random(-20.0F, 20.0F)));
        int[] damage = DamageInfo.createDamageMatrix(amount, true, true);
        PCLActions.Top.DealDamageToAll(damage, DamageInfo.DamageType.THORNS, AttackEffects.NONE)
        .SetPiercing(true, true)
        .SetVFX(true, false);

        Complete();
    }
}