package eatyourbeets.misc.AinzEffects;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.powers.animator.BurningPower;
import eatyourbeets.powers.PlayerStatistics;


public class AinzEffect_ApplyBurning extends AinzEffect
{
    public AinzEffect_ApplyBurning(int descriptionIndex)
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
        for (AbstractMonster m : PlayerStatistics.GetCurrentEnemies(true))
        {
            GameActionsHelper.ApplyPower(p, m, new BurningPower(m, p, ainz.magicNumber), ainz.magicNumber);
        }
    }
}