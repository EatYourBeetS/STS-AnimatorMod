package eatyourbeets.misc.AinzEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.powers.ThornsPower;
import eatyourbeets.utilities.GameActionsHelper2;


public class AinzEffect_GainThorns extends AinzEffect
{
    public AinzEffect_GainThorns(int descriptionIndex)
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
        GameActionsHelper2.StackPower(new ThornsPower(p, ainz.magicNumber));
    }
}