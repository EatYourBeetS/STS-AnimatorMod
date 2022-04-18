package eatyourbeets.powers.monsters;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.effects.AttackEffects;
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
        GameActions.Bottom.DealDamage(owner, target, amount, DamageInfo.DamageType.THORNS, AttackEffects.LIGHTNING)
        .SetPiercing(true, true)
        .CanKill(!target.isPlayer);
    }
}