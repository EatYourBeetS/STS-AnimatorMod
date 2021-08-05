package eatyourbeets.actions.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import eatyourbeets.actions.EYBActionWithCallback;
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

        GameActions.Top.StackPower(source, new StrengthPower(target, -amount));

        if (!target.hasPower(ArtifactPower.POWER_ID))
        {
            if (temporary)
            {
                GameActions.Top.StackPower(source, new GainStrengthPower(target, amount));
            }

            if (giveForceToPlayer)
            {
                GameActions.Top.GainForce(amount);
            }
            if (giveStrengthToSource)
            {
                GameActions.Top.StackPower(new StrengthPower(source, amount));
            }


            Complete(true);
        }
        else
        {
            Complete(false);
        }
    }
}
