package eatyourbeets.powers;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.GameActionsHelper;

public class NextTurnDexterityPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(NextTurnDexterityPower.class.getSimpleName());

    public NextTurnDexterityPower(AbstractCreature owner, int value)
    {
        super(owner, POWER_ID);

        this.amount = value;

        updateDescription();
    }

    @Override
    public void updateDescription()
    {
        String[] desc = powerStrings.DESCRIPTIONS;

        description = desc[0] + amount + desc[1];
    }

    @Override
    public void atStartOfTurnPostDraw()
    {
        super.atStartOfTurnPostDraw();

        PlayerStatistics.ApplyTemporaryDexterity(owner, owner, amount);
        GameActionsHelper.AddToBottom(new RemoveSpecificPowerAction(owner, owner, this));
    }
}
