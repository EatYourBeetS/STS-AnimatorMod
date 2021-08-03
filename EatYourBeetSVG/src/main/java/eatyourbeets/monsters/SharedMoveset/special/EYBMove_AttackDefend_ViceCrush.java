package eatyourbeets.monsters.SharedMoveset.special;

import eatyourbeets.effects.AttackEffects;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.vfx.combat.ViceCrushEffect;
import eatyourbeets.monsters.SharedMoveset.EYBMove_AttackDefend;
import eatyourbeets.utilities.GameActions;

public class EYBMove_AttackDefend_ViceCrush extends EYBMove_AttackDefend
{
    public EYBMove_AttackDefend_ViceCrush(int damage, int block)
    {
        super(damage, block);
    }

    @Override
    public void QueueActions(AbstractCreature target)
    {
        GameActions.Bottom.GainBlock(owner, block.Calculate());

        damageInfo.base = damage.Calculate();
        damageInfo.applyPowers(owner, target);

        if (this.damageInfo.output < 30)
        {
            owner.useFastAttackAnimation();

            GameActions.Bottom.Add(new DamageAction(target, this.damageInfo, AttackEffects.BLUNT_LIGHT));
        }
        else
        {
            owner.useSlowAttackAnimation();

            GameActions.Bottom.VFX(new ViceCrushEffect(target.hb.cX, target.hb.cY), 0.5f);
            GameActions.Bottom.Add(new DamageAction(target, this.damageInfo, AttackEffects.BLUNT_HEAVY));
        }
    }
}