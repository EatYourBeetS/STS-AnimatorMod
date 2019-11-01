package eatyourbeets.misc.AinzEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.powers.FocusPower;
import eatyourbeets.powers.common.IntellectPower;
import eatyourbeets.utilities.GameActionsHelper;


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
        GameActionsHelper.ApplyPower(p, p, new IntellectPower(p, ainz.magicNumber), ainz.magicNumber);
    }
}