package eatyourbeets.misc.AinzEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.powers.DexterityPower;
import eatyourbeets.GameActionsHelper;


public class AinzEffect_GainDexterity extends AinzEffect
{
    public AinzEffect_GainDexterity(int descriptionIndex)
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
        GameActionsHelper.ApplyPower(p, p, new DexterityPower(p, ainz.magicNumber), ainz.magicNumber);
    }
}