package eatyourbeets.powers.animatorClassic;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import eatyourbeets.powers.AnimatorClassicPower;
import eatyourbeets.utilities.GameActions;

public class BiyorigoPower extends AnimatorClassicPower
{
    public static final String POWER_ID = CreateFullID(BiyorigoPower.class);

    public BiyorigoPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        this.amount = amount;

        updateDescription();
    }

    @Override
    public void updateDescription()
    {
        this.description = FormatDescription(0, amount, amount*2);
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
            GameActions.Bottom.GainAgility(energy * 2);
            flash();
        }
    }
}
