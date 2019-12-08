package eatyourbeets.actions.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import eatyourbeets.actions.EYBActionWithCallback;
import eatyourbeets.utilities.GameActionsHelper2;

public class StealStrength extends EYBActionWithCallback<Boolean>
{
    private final boolean temporary;

    public StealStrength(AbstractCreature source, AbstractCreature target, int amount, boolean temporary)
    {
        super(ActionType.DEBUFF);

        this.temporary = temporary;

        Initialize(source, target, amount);
    }

    @Override
    protected void FirstUpdate()
    {
        super.FirstUpdate();

        GameActionsHelper2.SetOrderTop();
        GameActionsHelper2.ApplyPower(source, target, new StrengthPower(target, -amount), -amount);

        if (!target.hasPower(ArtifactPower.POWER_ID))
        {
            if (temporary)
            {
                GameActionsHelper2.ApplyPower(source, target, new GainStrengthPower(target, amount), amount);
                GameActionsHelper2.ApplyPower(source, source, new LoseStrengthPower(source, amount), amount);
            }

            GameActionsHelper2.StackPower(new StrengthPower(source, amount));

            Complete(true);
        }
        else
        {
            Complete(false);
        }

        GameActionsHelper2.ResetOrder();
    }
}
