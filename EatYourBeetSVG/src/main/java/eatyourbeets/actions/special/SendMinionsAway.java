package eatyourbeets.actions.special;

import com.megacrit.cardcrawl.actions.common.EscapeAction;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.MinionPower;
import eatyourbeets.actions.EYBActionWithCallback;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

import java.util.ArrayList;
import java.util.List;

public class SendMinionsAway extends EYBActionWithCallback<List<AbstractMonster>>
{
    protected final ArrayList<AbstractMonster> minions = new ArrayList<>();

    public SendMinionsAway()
    {
        super(ActionType.TEXT);

        Initialize(1);
    }

    @Override
    protected void FirstUpdate()
    {
        for (AbstractMonster m : GameUtilities.GetAllEnemies(true))
        {
            if (m.hasPower(MinionPower.POWER_ID))
            {
                GameActions.Bottom.Add(new EscapeAction(m));
                minions.add(m);
            }
        }

        Complete(minions);
    }
}
