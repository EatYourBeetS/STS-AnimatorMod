package eatyourbeets.cards.animator.series.GATE;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.stances.AgilityStance;
import eatyourbeets.utilities.GameActions;

public class Kuribayashi extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Kuribayashi.class)
            .SetAttack(2, CardRarity.COMMON, EYBAttackType.Ranged)
            .SetSeriesFromClassPackage();

    public Kuribayashi()
    {
        super(DATA);

        Initialize(7, 0, 2, 3);
        SetUpgrade(4, 0, 0);

        SetAffinity_Red(1, 1, 1);
        SetAffinity_Green(2, 0, 1);

        SetAffinityRequirement(Affinity.Red, 2);
        SetAffinityRequirement(Affinity.Green, 2);
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        return AgilityStance.IsActive() ? super.GetDamageInfo().AddMultiplier(2) : super.GetDamageInfo();
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.GUNSHOT).SetSoundPitch(0.6f, 0.8f);

        if (CheckAffinity(Affinity.Green))
        {
            GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_DIAGONAL);
        }

        GameActions.Bottom.ApplyVulnerable(p, m, magicNumber);

        if (CheckAffinity(Affinity.Red))
        {
            GameActions.Bottom.ReduceStrength(m, secondaryValue, true);
        }
    }
}