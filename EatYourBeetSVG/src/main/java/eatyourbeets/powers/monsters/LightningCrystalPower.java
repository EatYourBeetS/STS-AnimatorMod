package eatyourbeets.powers.monsters;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.SFX;
import eatyourbeets.utilities.GameActions;

public class LightningCrystalPower extends AbstractCrystalPower
{
    public static final String POWER_ID = CreateFullID(LightningCrystalPower.class);

    public LightningCrystalPower(AbstractCreature owner, int amount)
    {
        super(POWER_ID, owner, amount);
    }

    @Override
    protected void Activate(AbstractCreature target)
    {
        int damage = amount;
        if (target.isPlayer && damage > target.currentHealth)
        {
            damage = target.currentHealth - 1;
        }

        if (damage > 0)
        {
            GameActions.Bottom.SFX(SFX.ORB_LIGHTNING_EVOKE);
            GameActions.Bottom.VFX(new LightningEffect(target.drawX, target.drawY));
            GameActions.Bottom.DealDamage(owner, target, damage, DamageInfo.DamageType.THORNS, AttackEffects.NONE)
            .SetPiercing(true, true);
        }
    }
}