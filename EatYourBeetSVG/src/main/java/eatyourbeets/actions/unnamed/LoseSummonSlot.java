package eatyourbeets.actions.unnamed;

import eatyourbeets.actions.EYBActionWithCallback;
import eatyourbeets.monsters.PlayerMinions.UnnamedDoll;
import eatyourbeets.powers.CombatStats;

import java.util.ArrayList;

public class LoseSummonSlot extends EYBActionWithCallback<ArrayList<UnnamedDoll>>
{
    public LoseSummonSlot(int amount)
    {
        super(ActionType.SPECIAL, 0.2f);

        Initialize(amount);
    }

    @Override
    protected void FirstUpdate()
    {
        super.FirstUpdate();

        for (int i = 0; i < amount; i++)
        {
            CombatStats.Dolls.LoseSlot();
        }

        Complete();
    }
}
