package eatyourbeets.cards.animator.series.Katanagatari;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Konayuki extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Konayuki.class)
            .SetSkill(2, CardRarity.COMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage();

    public Konayuki()
    {
        super(DATA);

        Initialize(0, 6, 2, 3);
        SetUpgrade(0, 2, 1, 0);

        SetAffinity_Red(2, 0, 2);
    }

    @Override
    protected float GetInitialBlock()
    {
        int bonus = 0;
        for (AbstractCard c : player.hand.group)
        {
            if (c.uuid != uuid && GameUtilities.IsHighCost(c))
            {
                bonus += secondaryValue;
            }
        }

        return super.GetInitialBlock() + bonus;
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.RaiseFireLevel(magicNumber);

        if (CheckSpecialCondition(true))
        {
            GameActions.Bottom.GainEnergy(1);
            this.exhaustOnUseOnce = true;
        }
    }

    @Override
    public boolean CheckSpecialCondition(boolean tryUse)
    {
        return GameUtilities.GetPowerAmount(Affinity.Fire) > 6;
    }
}