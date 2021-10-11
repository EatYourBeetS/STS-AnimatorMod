package eatyourbeets.cards.animator.series.GATE;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class TukaLunaMarceau extends AnimatorCard
{
    public static final EYBCardData DATA = Register(TukaLunaMarceau.class)
            .SetSkill(0, CardRarity.COMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage();

    public TukaLunaMarceau()
    {
        super(DATA);

        Initialize(0, 3);
        SetUpgrade(0, 2);

        SetAffinity_Air(1);

        SetAffinityRequirement(Affinity.Light, 3);
        SetAffinityRequirement(Affinity.Air, 3);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (p.currentBlock <= 0)
        {
            GameActions.Bottom.Draw(1);
        }

        GameActions.Bottom.GainBlock(block);

        if (CheckAffinity(Affinity.Light))
        {
            GameUtilities.MaintainPower(Affinity.Light);
        }
        if (CheckAffinity(Affinity.Air))
        {
            GameUtilities.MaintainPower(Affinity.Air);
        }
    }
}