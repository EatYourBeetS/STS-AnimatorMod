package eatyourbeets.actions.animator;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.SmallLaserEffect;
import eatyourbeets.actions.EYBAction;
import eatyourbeets.powers.animator.EvePower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class EveDamageAction extends EYBAction
{
    private final EvePower power;

    public EveDamageAction(AbstractCreature owner, int amount)
    {
        super(ActionType.DAMAGE);

        this.power = null;

        Initialize(owner, null, amount);
    }

    public EveDamageAction(EvePower power)
    {
        super(ActionType.DAMAGE);

        this.power = power;

        Initialize(power.owner, null, amount);
    }

    @Override
    protected void FirstUpdate()
    {
        AbstractMonster target = GameUtilities.GetRandomEnemy(true);
        if (target != null)
        {
            GameActions.Bottom.SFX("ATTACK_MAGIC_BEAM_SHORT");
            GameActions.Bottom.VFX(new BorderFlashEffect(Color.SKY));

            if (Settings.FAST_MODE)
            {
                GameActions.Bottom.VFX(new SmallLaserEffect(target.hb.cX, target.hb.cY, source.hb.cX, source.hb.cY), 0.1F);
            }
            else
            {
                GameActions.Bottom.VFX(new SmallLaserEffect(target.hb.cX, target.hb.cY, source.hb.cX, source.hb.cY), 0.3F);
            }

            GameActions.Bottom.DealDamage(source, target, amount, DamageInfo.DamageType.THORNS, AttackEffect.NONE);

            if (power != null)
            {
                power.flash();
                power.amount += EvePower.GROWTH_AMOUNT;
                power.updateDescription();
            }
        }

        Complete();
    }
}