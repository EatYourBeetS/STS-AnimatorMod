package eatyourbeets.cards.animator.series.GATE;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.GameActions;

public class TukaLunaMarceau extends AnimatorCard
{
    public static final EYBCardData DATA = Register(TukaLunaMarceau.class)
            .SetSkill(0, CardRarity.COMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage();

    public TukaLunaMarceau()
    {
        super(DATA);

        Initialize(0, 3, 2);
        SetUpgrade(0, 2);

        SetAffinity_Green(1);

        SetAffinityRequirement(Affinity.Light, 2);
        SetAffinityRequirement(Affinity.Green, 2);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (p.currentBlock <= 0)
        {
            GameActions.Bottom.Draw(1);
        }

        GameActions.Bottom.GainBlock(block);

        if (TrySpendAffinity(Affinity.Light))
        {
            GameActions.Bottom.GainBlessing(magicNumber);
        }
        if (TrySpendAffinity(Affinity.Green))
        {
            GameActions.Bottom.GainAgility(magicNumber);
        }
    }
}