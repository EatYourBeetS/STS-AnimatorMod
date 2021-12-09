package eatyourbeets.cards.animator.series.Katanagatari;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.replacement.TemporaryDrawReductionPower;
import eatyourbeets.utilities.GameActions;

public class Azekura extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Azekura.class)
            .SetSkill(2, CardRarity.COMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage();

    public Azekura()
    {
        super(DATA);

        Initialize(0, 9, 2, 1);
        SetUpgrade(0, 0, 1);

        SetAffinity_Red(1, 0, 1);
        SetAffinity_Green(1);

        SetAffinityRequirement(Affinity.Red, 4);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.GainPlatedArmor(magicNumber);

        if (TrySpendAffinity(Affinity.Red))
        {
            GameActions.Bottom.GainThorns(secondaryValue);
        }
        else {
            GameActions.Bottom.StackPower(new TemporaryDrawReductionPower(p, 1));
        }
    }
}