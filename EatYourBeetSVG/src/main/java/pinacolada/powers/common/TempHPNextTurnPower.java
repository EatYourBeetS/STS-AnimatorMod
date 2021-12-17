package pinacolada.powers.common;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import pinacolada.powers.PCLPower;
import pinacolada.utilities.PCLActions;

public class TempHPNextTurnPower extends PCLPower
{
    public static final String POWER_ID = CreateFullID(TempHPNextTurnPower.class);

    public TempHPNextTurnPower(AbstractPlayer owner, int amount)
    {
        super(owner, POWER_ID);

        this.amount = amount;

        updateDescription();
    }

    @Override
    public void updateDescription()
    {
        description = FormatDescription(0, amount);
    }

    public void atStartOfTurn()
    {
        flash();
        PCLActions.Bottom.GainTemporaryHP(amount);
        PCLActions.Bottom.RemovePower(owner, owner, this);
    }
}