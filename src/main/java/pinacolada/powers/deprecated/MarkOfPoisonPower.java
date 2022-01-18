package pinacolada.powers.deprecated;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import pinacolada.powers.PCLPower;
import pinacolada.utilities.PCLActions;

public class MarkOfPoisonPower extends PCLPower
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
            PCLActions.Bottom.ApplyPoison(source, owner, amount);
        }

        return super.onAttacked(info, damageAmount);
    }

    @Override
    public void atEndOfTurn(boolean isPlayer)
    {
        PCLActions.Bottom.RemovePower(owner, owner, this);

        super.atEndOfTurn(isPlayer);
    }
}
