package pinacolada.powers.replacement;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.BlurPower;
import eatyourbeets.powers.CombatStats;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLActions;

public class PCLBlurPower extends BlurPower implements CloneablePowerInterface
{
    public final int maxBlock;

    public PCLBlurPower(AbstractCreature owner, int amount)
    {
        super(owner, amount);

        this.maxBlock = (owner != null && owner.isPlayer) ? 40 : 999;
    }

    @Override
    public void atEndOfRound()
    {
        //super.atEndOfRound();
    }

    @Override
    public void atEndOfTurn(boolean isPlayer)
    {
        super.atEndOfTurn(isPlayer);

        CombatStats.BlockRetained += maxBlock;
    }

    @Override
    public void atStartOfTurn()
    {
        super.atStartOfTurn();

        PCLActions.Bottom.RemovePower(owner, this);
    }

    @Override
    public void updateDescription()
    {
        super.updateDescription();

        if (maxBlock > 0 && maxBlock < 999)
        {
            this.description += " NL " + GR.PCL.Strings.Misc.MaxBlock(maxBlock);
        }
    }

    @Override
    public AbstractPower makeCopy()
    {
        return new PCLBlurPower(owner, amount);
    }
}
