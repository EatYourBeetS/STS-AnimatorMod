package eatyourbeets.cards.animator.series.OnePunchMan;

import eatyourbeets.effects.AttackEffects;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.powers.common.DelayedDamagePower;
import eatyourbeets.utilities.GameActions;

public class Genos extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Genos.class)
            .SetAttack(1, CardRarity.COMMON, EYBAttackType.Ranged)
            .SetSeriesFromClassPackage();

    public Genos()
    {
        super(DATA);

        Initialize(14, 0, 3, 4);
        SetUpgrade(4, 0, 0, 0);

        SetAffinity_Red(1);
        SetAffinity_Light(1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.FIRE);
        GameActions.Bottom.StackPower(new DelayedDamagePower(p, secondaryValue));

        if (isSynergizing)
        {
            GameActions.Bottom.ApplyBurning(p, m, magicNumber);
        }
    }
}