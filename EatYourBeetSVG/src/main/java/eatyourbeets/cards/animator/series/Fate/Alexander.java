package eatyourbeets.cards.animator.series.Fate;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.stances.ForceStance;
import eatyourbeets.utilities.GameActions;

public class Alexander extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Alexander.class).SetAttack(1, CardRarity.COMMON, EYBAttackType.Normal, EYBCardTarget.ALL);

    public Alexander()
    {
        super(DATA);

        Initialize(6, 0);
        SetUpgrade(1, 0);
        SetScaling(0, 0, 1);

        SetMultiDamage(true);
        SetSeries(CardSeries.Fate);
        SetAffinity(2, 1, 0, 1, 0);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        GameActions.Bottom.PlayCopy(this, null);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.DealDamageToAll(this, AbstractGameAction.AttackEffect.SLASH_HEAVY);

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
            CombatStats.Affinities.Force.Retain();
        }
    }
}