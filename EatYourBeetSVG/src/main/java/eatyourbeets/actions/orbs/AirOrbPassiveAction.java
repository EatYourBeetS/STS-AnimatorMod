package eatyourbeets.actions.orbs;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.Settings;
import eatyourbeets.actions.EYBAction;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.SFX;
import eatyourbeets.effects.VFX;
import eatyourbeets.orbs.animator.Air;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

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
        GameEffects.Queue.Add(VFX.Whirlwind());
        GameEffects.Queue.Add(VFX.RazorWind(orb.hb, player.hb, MathUtils.random(1000.0F, 1200.0F), MathUtils.random(-20.0F, 20.0F)));
        int[] damage = DamageInfo.createDamageMatrix(amount, true, true);
        GameActions.Top.DealDamageToAll(damage, DamageInfo.DamageType.THORNS, AttackEffects.SLASH_HORIZONTAL)
        .SetPiercing(true, true)
        .SetVFX(true, false);

        Complete();
    }
}