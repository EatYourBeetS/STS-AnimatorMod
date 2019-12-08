package eatyourbeets.misc.AinzEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import eatyourbeets.utilities.GameActionsHelper2;

public class AinzEffect_GainTemporaryHP extends AinzEffect
{
    public AinzEffect_GainTemporaryHP(int descriptionIndex)
    {
        super(descriptionIndex);
    }

    @Override
    protected void Setup(boolean upgraded)
    {
        ainz.baseMagicNumber = ainz.magicNumber = upgraded ? 9 : 6;
    }

    @Override
    public void EnqueueAction(AbstractPlayer p)
    {
        GameActionsHelper2.GainTemporaryHP(ainz.magicNumber);
    }
}