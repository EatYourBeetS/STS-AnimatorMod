package eatyourbeets.cards.animator.series.GATE;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.stances.AgilityStance;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Kuribayashi extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Kuribayashi.class)
            .SetAttack(2, CardRarity.COMMON, EYBAttackType.Ranged)
            .SetSeriesFromClassPackage();

    public Kuribayashi()
    {
        super(DATA);

        Initialize(7, 0, 2, 3);
        SetUpgrade(3, 0, 0);

        SetAffinity_Red(1, 1, 1);
        SetAffinity_Green(2, 0, 1);

        SetAffinityRequirement(Affinity.Fire, 3);
        SetAffinityRequirement(Affinity.Air, 2);
    }

    @Override
    public void OnDrag(AbstractMonster m)
    {
        super.OnDrag(m);

        if (m != null && CheckAffinity(Affinity.Fire))
        {
            GameUtilities.GetIntent(m).AddStrength(-secondaryValue);
        }
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        return AgilityStance.IsActive() ? super.GetDamageInfo().AddMultiplier(2) : super.GetDamageInfo();
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.GUNSHOT).SetSoundPitch(0.6f, 0.8f);

        if (CheckAffinity(Affinity.Air))
        {
            GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_DIAGONAL);
        }

        GameActions.Bottom.ApplyVulnerable(p, m, magicNumber);

        if (CheckAffinity(Affinity.Fire))
        {
            GameActions.Bottom.ReduceStrength(m, secondaryValue, true);
        }
    }
}