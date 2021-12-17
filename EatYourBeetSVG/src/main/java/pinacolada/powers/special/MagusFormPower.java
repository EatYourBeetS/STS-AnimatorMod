package pinacolada.powers.special;

import com.megacrit.cardcrawl.core.AbstractCreature;
import pinacolada.powers.PCLPower;
import pinacolada.utilities.PCLActions;

public class MagusFormPower extends PCLPower
{
    public static final String POWER_ID = CreateFullID(MagusFormPower.class);

    public int secondaryAmount = -1;

    public MagusFormPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        Initialize(amount);
    }

    public void atStartOfTurn()
    {
        super.atStartOfTurn();
        PCLActions.Bottom.GainFocus(amount);
        PCLActions.Bottom.GainStrength(secondaryAmount);
        PCLActions.Bottom.GainDexterity(secondaryAmount);
    }

    @Override
    public void updateDescription()
    {
        this.description = FormatDescription(0, amount, secondaryAmount);
    }
}
