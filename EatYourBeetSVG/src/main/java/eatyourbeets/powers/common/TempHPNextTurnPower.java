package eatyourbeets.powers.common;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import eatyourbeets.powers.CommonPower;
import eatyourbeets.utilities.GameActions;

public class TempHPNextTurnPower extends CommonPower
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
        GameActions.Bottom.GainTemporaryHP(amount);
        GameActions.Bottom.RemovePower(owner, owner, this);
    }
}