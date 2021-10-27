package eatyourbeets.cards.animator.series.GATE;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;

public class ShunyaKengun extends AnimatorCard
{
    public static final EYBCardData DATA = Register(ShunyaKengun.class)
            .SetAttack(1, CardRarity.COMMON, EYBAttackType.Ranged)
            .SetSeriesFromClassPackage();

    public ShunyaKengun()
    {
        super(DATA);

        Initialize(8, 0, 3);
        SetUpgrade(1, 0, 2);

        SetAffinity_Earth();
        SetAffinity_Mind();
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.GUNSHOT).SetSoundPitch(0.9f, 1f);
        GameActions.Bottom.GainSupportDamage(magicNumber);
    }
}