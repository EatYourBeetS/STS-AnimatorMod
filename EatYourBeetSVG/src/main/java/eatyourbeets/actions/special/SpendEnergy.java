package eatyourbeets.actions.special;

import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import eatyourbeets.actions.EYBActionWithCallback;

public class SpendEnergy extends EYBActionWithCallback<Integer>
{
    public SpendEnergy(int amount)
    {
        super(ActionType.SPECIAL);

        Initialize(amount);
    }

    @Override
    protected void FirstUpdate()
    {
        int energy = Math.min(amount, EnergyPanel.getCurrentEnergy());
        if (energy > 0)
        {
            player.loseEnergy(energy);
        }

        Complete(energy);
    }
}
