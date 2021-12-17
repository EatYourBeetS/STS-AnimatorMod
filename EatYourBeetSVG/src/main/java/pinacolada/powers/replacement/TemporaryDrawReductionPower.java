package pinacolada.powers.replacement;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DrawReductionPower;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLActions;

public class TemporaryDrawReductionPower extends DrawReductionPower implements CloneablePowerInterface
{
    private static int counter = 0;

    public TemporaryDrawReductionPower(AbstractCreature owner, int turns)
    {
        this(owner, turns, false);
    }

    public TemporaryDrawReductionPower(AbstractCreature owner, int turns, boolean unique)
    {
        super(owner, turns);

        this.ID = GR.PCL.CreateID(TemporaryDrawReductionPower.class.getSimpleName());

        if (unique)
        {
            this.ID += "_" + counter++;
        }
    }

    @Override
    public void atStartOfTurnPostDraw()
    {
        super.atStartOfTurnPostDraw();

        PCLActions.Bottom.ReducePower(this, 1);
    }

    @Override
    public AbstractPower makeCopy()
    {
        return new TemporaryDrawReductionPower(owner, amount, false);
    }
}
