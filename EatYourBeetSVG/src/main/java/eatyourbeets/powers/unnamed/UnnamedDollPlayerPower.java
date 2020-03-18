package eatyourbeets.powers.unnamed;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.powers.UnnamedPower;

public class UnnamedDollPlayerPower extends UnnamedPower
{
    public static final String POWER_ID = CreateFullID(UnnamedDollPlayerPower.class);

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

        RemovePower();
    }

    @Override
    public void onAfterCardPlayed(AbstractCard usedCard)
    {
        super.onAfterCardPlayed(usedCard);

        int amount = Math.min(20 - maxHPGainedThisTurn, this.amount);
        if (amount > 0)
        {
            //GameActions.Bottom.Add(new IncreaseMaxHpAction(owner, amount, true));

            maxHPGainedThisTurn += amount;

            this.flash();
        }
    }
}
