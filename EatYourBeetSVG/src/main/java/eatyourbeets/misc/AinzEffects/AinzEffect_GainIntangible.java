package eatyourbeets.misc.AinzEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import eatyourbeets.utilities.GameActionsHelper;


public class AinzEffect_GainIntangible extends AinzEffect
{
    public AinzEffect_GainIntangible(int descriptionIndex)
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
    }
}