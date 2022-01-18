package pinacolada.powers.common;

import com.megacrit.cardcrawl.core.AbstractCreature;
import pinacolada.actions.special.SupportDamageAction;
import pinacolada.powers.PCLPower;
import pinacolada.utilities.PCLActions;

public class SupportDamagePower extends PCLPower
{
    public static final String POWER_ID = CreateFullID(SupportDamagePower.class);

    public SupportDamagePower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        Initialize(amount);
    }

    @Override
    public void atEndOfTurn(boolean isPlayer)
    {
        super.atEndOfTurn(isPlayer);

        PCLActions.Bottom.Add(new SupportDamageAction(owner, amount));
    }
}