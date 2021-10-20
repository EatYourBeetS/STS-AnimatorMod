package eatyourbeets.cards.animator.series.FullmetalAlchemist;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.SFX;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.TargetHelper;

public class Scar extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Scar.class)
            .SetAttack(2, CardRarity.RARE, EYBAttackType.Elemental)
            .SetSeriesFromClassPackage();

    public Scar()
    {
        super(DATA);

        Initialize(10, 0, 7);
        SetUpgrade(0, 0, 3);

        SetAffinity_Poison(2);

        SetAffinityRequirement(Affinity.Poison, 6);
    }

    @Override
    public void triggerOnExhaust() {
        super.triggerOnExhaust();

        GameActions.Bottom.ApplyPoison(TargetHelper.RandomEnemy(), magicNumber);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.NONE)
        .SetDamageEffect(__ ->
        {
            SFX.Play(SFX.ORB_DARK_EVOKE, 0.5f, 0.7f);
            return 0f;
        });
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (GameUtilities.HasFullHand())
        {
            GameActions.Bottom.SelectFromPile(name, 1, p.exhaustPile)
            .SetOptions(true, true)
            .SetFilter(card -> {
                if (upgraded)
                {
                    return !GameUtilities.IsHindrance(card);
                }

                return true;
            });
        }

        if (CheckAffinity(Affinity.Poison))
        {
            GameActions.Bottom.ApplyPoison(TargetHelper.RandomEnemy(), magicNumber);
        }
    }
}