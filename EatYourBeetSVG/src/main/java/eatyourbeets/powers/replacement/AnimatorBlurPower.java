package eatyourbeets.powers.replacement;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.BlurPower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;

public class AnimatorBlurPower extends BlurPower implements CloneablePowerInterface
{
    public final int maxBlock;

    public AnimatorBlurPower(AbstractCreature owner, int amount)
    {
        super(owner, amount);

        this.maxBlock = (owner != null && owner.isPlayer) ? 30 : 999;
        this.ID = "Animator" + ID;
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

        GameActions.Bottom.RemovePower(owner, this);
    }

    @Override
    public void updateDescription()
    {
        super.updateDescription();

        if (maxBlock > 0 && maxBlock < 999)
        {
            this.description += " NL " + GR.Animator.Strings.Misc.MaxBlock(maxBlock);
        }
    }

    @Override
    public AbstractPower makeCopy()
    {
        return new AnimatorBlurPower(owner, amount);
    }
}
