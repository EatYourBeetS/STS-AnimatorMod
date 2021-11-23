package eatyourbeets.actions.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import eatyourbeets.actions.EYBActionWithCallback;
import eatyourbeets.powers.common.ShacklesPower;
import eatyourbeets.utilities.GameActions;

public class ReduceStrength extends EYBActionWithCallback<Boolean>
{
    protected boolean temporary;
    protected boolean giveForceToPlayer;
    protected boolean giveStrengthToSource;

    public ReduceStrength(AbstractCreature source, AbstractCreature target, int amount, boolean temporary)
    {
        super(ActionType.DEBUFF);

        this.temporary = temporary;

        Initialize(source, target, amount);
    }

    public ReduceStrength SetForceGain(boolean giveForceToPlayer)
    {
        this.giveForceToPlayer = giveForceToPlayer;

        return this;
    }

    public ReduceStrength SetStrengthGain(boolean giveStrengthToSource)
    {
        this.giveStrengthToSource = giveStrengthToSource;

        return this;
    }

    @Override
    protected void FirstUpdate()
    {
        super.FirstUpdate();

        if (temporary)
        {
            GameActions.Top.StackPower(source, new ShacklesPower(target, amount));
        }
        else
        {
            GameActions.Top.StackPower(source, new StrengthPower(target, -amount));
        }

        if (!target.hasPower(ArtifactPower.POWER_ID))
        {
            if (giveForceToPlayer)
            {
                GameActions.Top.GainMight(amount);
            }
            if (giveStrengthToSource)
            {
                GameActions.Top.StackPower(new StrengthPower(source, amount));
            }

            Complete(true);
            return;
        }

        Complete(false);
    }
}
