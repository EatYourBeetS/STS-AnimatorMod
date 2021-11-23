package eatyourbeets.cards.animator.beta.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JUtils;

public class GirlDeMo extends AnimatorCard
{
    public static final EYBCardData DATA = Register(GirlDeMo.class).SetSkill(1, CardRarity.SPECIAL, EYBCardTarget.None).SetSeries(CardSeries.AngelBeats);

    public GirlDeMo()
    {
        super(DATA);

        Initialize(0, 0, 2);
        SetUpgrade(0, 0, 1);
        SetAffinity_Star(2);
        SetHarmonic(true);
        SetExhaust(true);
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
        GameActions.Bottom.Motivate(magicNumber);

        Affinity lowest = JUtils.FindMin(Affinity.Basic(), CombatStats.Affinities::GetPowerAmount);
        Affinity highest = JUtils.FindMax(Affinity.Basic(), CombatStats.Affinities::GetPowerAmount);

        if (lowest != null && highest != null) {
            GameActions.Bottom.StackAffinityPower(lowest, CombatStats.Affinities.GetPowerAmount(highest) - CombatStats.Affinities.GetPowerAmount(lowest), false);
        }
    }
}