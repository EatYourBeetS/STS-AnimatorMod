package eatyourbeets.cards.animator.series.Overlord;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.common.DeEnergizedPower;
import eatyourbeets.utilities.GameActions;

public class GazefStronoff extends AnimatorCard
{
    public static final EYBCardData DATA = Register(GazefStronoff.class)
            .SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetMaxCopies(2)
            .SetSeriesFromClassPackage();

    public GazefStronoff()
    {
        super(DATA);

        Initialize(0, 14, 2);

        SetAffinity_Red(2);
        SetAffinity_Light(1);

        SetAffinityRequirement(Affinity.Red, 5);

        SetEthereal(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetEthereal(false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.GainTemporaryStats(magicNumber, 0, 0);

        if (!CheckAffinity(Affinity.Red))
        {
            GameActions.Bottom.StackPower(new DeEnergizedPower(p, 1)).ShowEffect(false, false);
        }
    }
}