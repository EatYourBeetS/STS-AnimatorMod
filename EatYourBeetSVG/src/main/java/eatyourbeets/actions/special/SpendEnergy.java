package eatyourbeets.actions.special;

import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import eatyourbeets.actions.EYBActionWithCallback;

public class SpendEnergy extends EYBActionWithCallback<Integer>
{
    protected boolean canSpendLess;

    public SpendEnergy(int amount, boolean canSpendLess)
    {
        super(ActionType.ENERGY);

        this.canSpendLess = canSpendLess;

        Initialize(amount);
    }

    @Override
    protected void FirstUpdate()
    {
        int energy = EnergyPanel.getCurrentEnergy();
        if (energy >= amount || canSpendLess)
        {
            player.loseEnergy(amount);
            Complete(amount);
        }
        else
        {
            Complete();
        }
    }
}
