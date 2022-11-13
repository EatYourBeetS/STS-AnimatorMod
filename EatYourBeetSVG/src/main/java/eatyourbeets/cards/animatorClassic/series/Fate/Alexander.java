package eatyourbeets.cards.animatorClassic.series.Fate;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.stances.ForceStance;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Alexander extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(Alexander.class).SetSeriesFromClassPackage().SetAttack(1, CardRarity.COMMON, EYBAttackType.Normal, EYBCardTarget.ALL);

    public Alexander()
    {
        super(DATA);

        Initialize(6, 0);
        SetUpgrade(1, 0);
        SetScaling(0, 0, 1);

        SetMultiDamage(true);

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
            GameActions.Bottom.GainPlatedArmor(1);
        }

        if (upgraded)
        {
            GameActions.Bottom.GainForce(1);
        }
        else
        {
            GameUtilities.RetainPower(Affinity.Red);
        }
    }
}