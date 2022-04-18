package eatyourbeets.powers.deprecated;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;

public class MarkOfPoisonPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(MarkOfPoisonPower.class);

    public MarkOfPoisonPower(AbstractCreature owner, AbstractCreature source, int amount)
    {
        super(owner, source, POWER_ID);

        Initialize(amount, PowerType.DEBUFF, false);
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount)
    {
        if (info.type != DamageInfo.DamageType.THORNS && damageAmount > 0)
        {
            GameActions.Bottom.ApplyPoison(source, owner, amount);
        }

        return super.onAttacked(info, damageAmount);
    }

    @Override
    public void atEndOfTurn(boolean isPlayer)
    {
        GameActions.Bottom.RemovePower(owner, owner, this);

        super.atEndOfTurn(isPlayer);
    }
}
