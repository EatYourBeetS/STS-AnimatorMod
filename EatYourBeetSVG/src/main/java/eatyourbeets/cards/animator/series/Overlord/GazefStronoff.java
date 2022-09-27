package eatyourbeets.cards.animator.series.Overlord;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
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

        Initialize(0, 14, 3);

        SetAffinity_Red(1, 1, 0);
        SetAffinity_Light(1);

        SetAffinityRequirement(Affinity.Red, 1);

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
        GameActions.Bottom.StackPower(new DeEnergizedPower(p, 1)).ShowEffect(false, false);

        if (CheckSpecialCondition(true))
        {
            GameActions.Bottom.RemovePower(p, p, WeakPower.POWER_ID);
        }
        else
        {
            GameActions.Bottom.GainAffinity(Affinity.Red, 1, true);
        }
    }

    @Override
    public boolean CheckSpecialCondition(boolean tryUse)
    {
        return player.hasPower(WeakPower.POWER_ID) && super.CheckSpecialCondition(tryUse);
    }
}