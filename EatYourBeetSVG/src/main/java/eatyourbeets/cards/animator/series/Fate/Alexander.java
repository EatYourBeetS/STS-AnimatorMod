package eatyourbeets.cards.animator.series.Fate;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.stances.ForceStance;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Alexander extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Alexander.class)
            .SetAttack(1, CardRarity.COMMON, EYBAttackType.Normal, EYBCardTarget.ALL)
            .SetSeriesFromClassPackage();

    public Alexander()
    {
        super(DATA);

        Initialize(7, 0);
        SetUpgrade(2, 0);

        SetAffinity_Fire(1, 1, 1);
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

        if (ForceStance.IsActive())
        {
            GameActions.Bottom.Draw(1);
        }
        else {
            GameUtilities.MaintainPower(Affinity.Fire);
        }
    }
}