package eatyourbeets.powers.common;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.FocusPower;
import eatyourbeets.powers.CommonPower;
import eatyourbeets.utilities.GameActions;

public class TemporaryFocusPower extends CommonPower
{
    public static final String POWER_ID = CreateFullID(TemporaryFocusPower.class);

    public TemporaryFocusPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        this.amount = amount;
        this.type = PowerType.BUFF;
        this.loadRegion("focus");
        enabled = false;

        updateDescription();
    }

    @Override
    public void onInitialApplication()
    {
        super.onInitialApplication();

        GameActions.Bottom.GainFocus(this.amount);
    }

    @Override
    public void atStartOfTurnPostDraw()
    {
        super.atStartOfTurnPostDraw();

        GameActions.Bottom.ReducePower(owner, owner, FocusPower.POWER_ID, this.amount);
        GameActions.Bottom.RemovePower(owner, owner, this);
    }

    @Override
    public void updateDescription()
    {
        description = FormatDescription(0, amount);
    }
}