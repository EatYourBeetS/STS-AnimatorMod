package eatyourbeets.cards.animator.series.NoGameNoLife;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Miko extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Miko.class)
            .SetSkill(2, CardRarity.COMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage();

    public Miko()
    {
        super(DATA);

        Initialize(0, 8, 2, 2);
        SetUpgrade(0, 2, 1, 0);

        SetAffinity_Light(1);
        SetAffinity_Red(1);

        SetMultiDamage(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.Scry(magicNumber);

        if (CheckSpecialCondition(true))
        {
            GameActions.Bottom.GainBlessing(secondaryValue);
        }
        else
        {
            GameActions.Bottom.RetainPower(Affinity.Light);
        }
    }

    @Override
    public boolean CheckSpecialCondition(boolean tryUse)
    {
        return GameUtilities.GetPowerAmount(Affinity.Light) < 6;
    }
}