package eatyourbeets.powers.monsters;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class LightningCubePower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(LightningCubePower.class);

    public LightningCubePower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        this.priority = -99;

        Initialize(amount);
    }

    @Override
    public void atEndOfTurn(boolean isPlayer)
    {
        super.atEndOfTurn(isPlayer);

        final AbstractCreature target = owner.isPlayer ? GameUtilities.GetRandomEnemy(true) : player;
        GameActions.Bottom.DealDamage(owner, target, amount, DamageInfo.DamageType.THORNS, AttackEffects.LIGHTNING)
        .SetPiercing(true, true)
        .CanKill(!target.isPlayer);
    }
}
