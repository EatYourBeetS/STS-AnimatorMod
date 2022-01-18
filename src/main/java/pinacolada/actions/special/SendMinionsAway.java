package pinacolada.actions.special;

import com.megacrit.cardcrawl.actions.common.EscapeAction;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.MinionPower;
import eatyourbeets.actions.EYBActionWithCallback;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

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
        for (AbstractMonster m : PCLGameUtilities.GetEnemies(true))
        {
            if (m.hasPower(MinionPower.POWER_ID))
            {
                PCLActions.Bottom.Add(new EscapeAction(m));
                minions.add(m);
            }
        }

        Complete(minions);
    }
}
