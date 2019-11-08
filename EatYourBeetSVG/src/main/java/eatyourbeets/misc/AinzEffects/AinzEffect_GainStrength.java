package eatyourbeets.misc.AinzEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import eatyourbeets.powers.common.ForcePower;
import eatyourbeets.utilities.GameActionsHelper;


public class AinzEffect_GainStrength extends AinzEffect
{
    public AinzEffect_GainStrength(int descriptionIndex)
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
        GameActionsHelper.ApplyPower(p, p, new ForcePower(p, ainz.magicNumber), ainz.magicNumber);
    }
}