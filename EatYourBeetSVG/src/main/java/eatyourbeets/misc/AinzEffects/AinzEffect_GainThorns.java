package eatyourbeets.misc.AinzEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.powers.ThornsPower;
import eatyourbeets.GameActionsHelper;


public class AinzEffect_GainThorns extends AinzEffect
{
    public AinzEffect_GainThorns(int descriptionIndex)
    {
        super(descriptionIndex);
    }

    @Override
    protected void Setup(boolean upgraded)
    {
        ainz.baseMagicNumber = ainz.magicNumber = upgraded ? 4 : 3;
    }

    @Override
    public void EnqueueAction(AbstractPlayer p)
    {
        GameActionsHelper.ApplyPower(p, p, new ThornsPower(p, ainz.magicNumber), ainz.magicNumber);
    }
}