package eatyourbeets.powers.common;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.powers.CommonPower;
import eatyourbeets.utilities.GameActions;

public class AftershockPower extends CommonPower
{
    public static final String POWER_ID = CreateFullID(AftershockPower.class);

    public AftershockPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        Initialize(amount);
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target)
    {
        super.onAttack(info, damageAmount, target);

        if (target != null && target.isPlayer != owner.isPlayer)
        {
            GameActions.Bottom.DealDamageAtEndOfTurn(owner, target, amount);
        }
    }
}