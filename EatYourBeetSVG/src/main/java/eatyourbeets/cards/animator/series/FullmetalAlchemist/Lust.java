package eatyourbeets.cards.animator.series.FullmetalAlchemist;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.TempHPAttribute;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Lust extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Lust.class)
            .SetAttack(1, CardRarity.COMMON, EYBAttackType.Piercing, EYBCardTarget.Normal)
            .SetSeriesFromClassPackage();

    public Lust()
    {
        super(DATA);

        Initialize(8, 0, 8);
        SetUpgrade(2,0,2);

        SetAffinity_Fire();
        SetAffinity_Mind();
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return (GameUtilities.HasFullHand()) ? TempHPAttribute.Instance.SetCard(this, true) : null;
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.CLAW);

        if (GameUtilities.HasFullHand())
        {
            GameActions.Bottom.GainTemporaryHP(magicNumber);
        }
    }
}