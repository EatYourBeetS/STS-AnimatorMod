package eatyourbeets.powers.unnamed;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.actions.common.IncreaseMaxHpAction;
import eatyourbeets.utilities.GameActionsHelper;

public class UnnamedDollPlayerPower extends UnnamedPower
{
    public static final String POWER_ID = CreateFullID(UnnamedDollPlayerPower.class.getSimpleName());

    private int maxHPGainedThisTurn;

    public UnnamedDollPlayerPower(AbstractCreature owner, int value)
    {
        super(owner, POWER_ID);

        this.amount = value;

        updateDescription();
    }

    @Override
    public void onInitialApplication()
    {
        super.onInitialApplication();

        maxHPGainedThisTurn = 0;
    }

    @Override
    public void updateDescription()
    {
        String[] desc = powerStrings.DESCRIPTIONS;

        description = desc[0] + amount + desc[1];
    }

    @Override
    public void atEndOfTurn(boolean isPlayer)
    {
        super.atEndOfTurn(isPlayer);

        GameActionsHelper.AddToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, this));
    }

    @Override
    public void onAfterCardPlayed(AbstractCard usedCard)
    {
        super.onAfterCardPlayed(usedCard);

        int amount = Math.min(20 - maxHPGainedThisTurn, this.amount);
        if (amount > 0)
        {
            GameActionsHelper.AddToBottom(new IncreaseMaxHpAction(owner, amount, true));

            maxHPGainedThisTurn += amount;

            this.flash();
        }
    }
}
