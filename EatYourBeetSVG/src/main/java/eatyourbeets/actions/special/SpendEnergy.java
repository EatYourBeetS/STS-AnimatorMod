package eatyourbeets.actions.special;

import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import eatyourbeets.actions.EYBActionWithCallbackAdvanced;

public class SpendEnergy extends EYBActionWithCallbackAdvanced<Integer>
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
            energy = Math.min(energy, amount);
            if (Check(energy))
            {
                player.loseEnergy(energy);
                Complete(energy);
                return;
            }
        }

        Complete();
    }
}
