package eatyourbeets.powers.deprecated;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;

public class MarkOfPoisonPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(MarkOfPoisonPower.class.getSimpleName());

    private final AbstractCreature source;

    public MarkOfPoisonPower(AbstractCreature owner, AbstractCreature source, int stacks)
    {
        super(owner, POWER_ID);
        this.source = source;
        this.amount = stacks;
        this.type = PowerType.DEBUFF;
        updateDescription();
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
