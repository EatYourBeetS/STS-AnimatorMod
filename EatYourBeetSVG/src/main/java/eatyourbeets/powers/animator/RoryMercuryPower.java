package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.powers.AnimatorPower;

public class RoryMercuryPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(RoryMercuryPower.class.getSimpleName());

    public RoryMercuryPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        this.amount = amount;
        updateDescription();
    }

    @Override
    public void atEndOfTurn(boolean isPlayer)
    {
        RemovePower();

        super.atEndOfTurn(isPlayer);
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type)
    {
        if (type == DamageInfo.DamageType.NORMAL)
        {
            return Math.round(damage * (1 + amount / 100f));
        }
        else
        {
            return damage;
        }
    }
}
