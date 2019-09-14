package eatyourbeets.misc.AinzEffects;

import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import eatyourbeets.powers.animator.AinzPower;
import eatyourbeets.utilities.GameActionsHelper;


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
        GameActionsHelper.ApplyPower(p, p, new IntangiblePlayerPower(p, ainz.magicNumber), ainz.magicNumber);
        GameActionsHelper.AddToBottom(new ReducePowerAction(p, p, AinzPower.POWER_ID, 1));
    }
}