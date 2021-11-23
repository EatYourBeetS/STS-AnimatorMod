package eatyourbeets.cards.animator.series.Fate;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.stances.MightStance;
import eatyourbeets.utilities.GameActions;

public class Alexander extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Alexander.class)
            .SetAttack(1, CardRarity.COMMON, EYBAttackType.Normal, EYBCardTarget.ALL)
            .SetSeriesFromClassPackage();

    public Alexander()
    {
        super(DATA);

        Initialize(7, 0, 2);
        SetUpgrade(2, 0);

        SetAffinity_Red(1, 1, 2);
        SetAffinity_Light(1);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        GameActions.Bottom.PlayCopy(this, null);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamageToAll(this, AttackEffects.SLASH_HEAVY);
        GameActions.Bottom.GainMight(magicNumber);
        if (MightStance.IsActive())
        {
            GameActions.Bottom.Draw(1);
        }
    }
}