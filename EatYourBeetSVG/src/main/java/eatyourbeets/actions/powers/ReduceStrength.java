package eatyourbeets.actions.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import eatyourbeets.actions.EYBActionWithCallback;
import eatyourbeets.utilities.GameActions;

public class ReduceStrength extends EYBActionWithCallback<Boolean>
{
    protected boolean temporary;
    protected boolean onlyReduceStrength;

    public ReduceStrength(AbstractCreature source, AbstractCreature target, int amount, boolean temporary)
    {
        super(ActionType.DEBUFF);

        this.temporary = temporary;

        Initialize(source, target, amount);
    }

    public ReduceStrength SetOptions(boolean giveStrengthToSource)
    {
        this.onlyReduceStrength = giveStrengthToSource;

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

            if (onlyReduceStrength)
            {
                if (temporary)
                {
                    GameActions.Top.ApplyPower(source, source, new LoseStrengthPower(source, amount), amount);
                }

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
