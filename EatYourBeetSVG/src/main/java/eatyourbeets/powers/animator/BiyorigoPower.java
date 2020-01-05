package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;

public class BiyorigoPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(BiyorigoPower.class.getSimpleName());

    public BiyorigoPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        this.amount = 1;

        updateDescription();
    }

    @Override
    public void updateDescription()
    {
        this.description =
                powerStrings.DESCRIPTIONS[0] + (amount) +
                powerStrings.DESCRIPTIONS[1] + (amount * 2) +
                powerStrings.DESCRIPTIONS[2];
    }

    @Override
    public void atEndOfTurn(boolean isPlayer)
    {
        super.atEndOfTurn(isPlayer);

        int energy = Math.min(amount, EnergyPanel.getCurrentEnergy());
        if (energy > 0)
        {
            EnergyPanel.useEnergy(energy);
            GameActions.Bottom.GainForce(energy * 2);
            flash();
        }
    }
}
