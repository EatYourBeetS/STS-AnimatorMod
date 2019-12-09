package eatyourbeets.misc.AinzEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import eatyourbeets.utilities.GameActions;


public class AinzEffect_GainIntellect extends AinzEffect
{
    public AinzEffect_GainIntellect(int descriptionIndex)
    {
        super(descriptionIndex);
    }

    @Override
    protected void Setup(boolean upgraded)
    {
        ainz.baseMagicNumber = ainz.magicNumber = upgraded ? 3 : 2;
    }

    @Override
    public void EnqueueAction(AbstractPlayer p)
    {
        GameActions.Bottom.GainIntellect(ainz.magicNumber);
    }
}