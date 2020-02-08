package eatyourbeets.actions.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import eatyourbeets.actions.EYBActionWithCallback;
import eatyourbeets.powers.common.ForcePower;
import eatyourbeets.utilities.GameActions;

public class ReduceStrength extends EYBActionWithCallback<Boolean>
{
    protected boolean temporary;
    protected boolean giveForceToSource;

    public ReduceStrength(AbstractCreature source, AbstractCreature target, int amount, boolean temporary)
    {
        super(ActionType.DEBUFF);

        this.temporary = temporary;

        Initialize(source, target, amount);
    }

    public ReduceStrength SetForceGain(boolean giveForceToSource)
    {
        this.giveForceToSource = giveForceToSource;

        return this;
    }

    @Override
    protected void FirstUpdate()
    {
        super.FirstUpdate();

        GameActions.Top.ApplyPower(source, target, new StrengthPower(target, -amount), -amount);

        if (!target.hasPower(ArtifactPower.POWER_ID))
        {
            if (temporary)
            {
                GameActions.Top.ApplyPower(source, target, new GainStrengthPower(target, amount), amount);
            }

            if (giveForceToSource)
            {
                GameActions.Top.StackPower(new ForcePower(source, amount));
            }

            Complete(true);
        }
        else
        {
            Complete(false);
        }
    }
}
