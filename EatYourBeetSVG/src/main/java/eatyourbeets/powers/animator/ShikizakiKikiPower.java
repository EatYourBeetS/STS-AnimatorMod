package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.PlayerStatistics;

public class ShikizakiKikiPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(ShikizakiKikiPower.class.getSimpleName());

    public ShikizakiKikiPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        this.amount = amount;

        updateDescription();
    }

    public float atDamageGive(float damage, DamageInfo.DamageType type)
    {
        return type == DamageInfo.DamageType.NORMAL ? damage + (float) (PlayerStatistics.GetStrength(owner) * (this.amount - 1)) : damage;
    }
}
