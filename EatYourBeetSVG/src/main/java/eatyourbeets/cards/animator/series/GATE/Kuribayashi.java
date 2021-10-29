package eatyourbeets.cards.animator.series.GATE;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.TargetHelper;

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

        SetAffinityRequirement(Affinity.Red, 3);
        SetAffinityRequirement(Affinity.Green, 2);
    }

    @Override
    public void OnDrag(AbstractMonster m)
    {
        super.OnDrag(m);

        if (m != null && TrySpendAffinity(Affinity.Red))
        {
            GameUtilities.GetIntent(m).AddStrength(-secondaryValue);
        }
    }

    @Override
    public void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        if (CheckAffinity(Affinity.Green)) {
            GameUtilities.IncreaseHitCount(this, 1, true);
        }
    }


    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.GUNSHOT).forEach(d -> d.SetSoundPitch(0.6f, 0.8f));
        TrySpendAffinity(Affinity.Green);

        GameActions.Bottom.ApplyVulnerable(p, m, magicNumber);

        if (TrySpendAffinity(Affinity.Red))
        {
            GameActions.Bottom.StackPower(TargetHelper.Normal(m), PowerHelper.Shackles,secondaryValue);
        }
    }
}