package eatyourbeets.misc.AinzEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import eatyourbeets.powers.animator.AinzPower;
import eatyourbeets.utilities.GameActions;

public class AinzEffect_GainIntangibleLosePower extends AinzEffect
{
    public AinzEffect_GainIntangibleLosePower(int descriptionIndex)
    {
        super(descriptionIndex);
    }

    @Override
    protected void Setup(boolean upgraded)
    {
        ainz.baseMagicNumber = ainz.magicNumber = 1;
    }

    @Override
    public void EnqueueAction(AbstractPlayer p)
    {
        GameActions.Bottom.StackPower(new IntangiblePlayerPower(p, ainz.magicNumber));
        GameActions.Bottom.ReducePower(p, AinzPower.POWER_ID, 1);
    }
}