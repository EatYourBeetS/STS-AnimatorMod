package eatyourbeets.misc.AinzEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;


public class AinzEffect_ApplyPoison extends AinzEffect
{
    public AinzEffect_ApplyPoison(int descriptionIndex)
    {
        super(descriptionIndex);
    }

    @Override
    protected void Setup(boolean upgraded)
    {
        ainz.baseMagicNumber = ainz.magicNumber = upgraded ? 8 : 6;
    }

    @Override
    public void EnqueueAction(AbstractPlayer p)
    {
        for (AbstractMonster m : GameUtilities.GetCurrentEnemies(true))
        {
            GameActions.Bottom.ApplyPoison(p, m, ainz.magicNumber);
        }
    }
}