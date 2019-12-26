package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.powers.BarricadePower;
import com.megacrit.cardcrawl.powers.BlurPower;
import eatyourbeets.powers.AnimatorPower;

public class GazelDwargonPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(GazelDwargonPower.class.getSimpleName());

    private boolean handlePlayerBlock = false;

    public GazelDwargonPower(AbstractPlayer owner, int amount)
    {
        super(owner, POWER_ID);

        this.amount = amount;

        updateDescription();
    }

    @Override
    public void atStartOfTurn()
    {
        super.atStartOfTurn();

        handlePlayerBlock = (!owner.hasPower(BarricadePower.POWER_ID) && !owner.hasPower(BlurPower.POWER_ID));

        if (handlePlayerBlock)
        {
            this.ID = BarricadePower.POWER_ID;
        }
    }

    @Override
    public void atStartOfTurnPostDraw()
    {
        super.atStartOfTurnPostDraw();

        if (handlePlayerBlock)
        {
            this.ID = POWER_ID;

            //owner.loseBlock(owner.currentBlock / 2, true);
            int temp = Math.max(0, owner.currentBlock - amount);
            if (temp > 0)
            {
                owner.loseBlock(temp, true);
            }
        }
    }
}
