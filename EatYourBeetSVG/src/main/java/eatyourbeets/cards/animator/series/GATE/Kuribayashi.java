package eatyourbeets.cards.animator.series.GATE;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AffinityType;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.stances.AgilityStance;
import eatyourbeets.utilities.GameActions;

public class Kuribayashi extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Kuribayashi.class)
            .SetAttack(2, CardRarity.COMMON, EYBAttackType.Ranged)
            .SetSeriesFromClassPackage();

    public Kuribayashi()
    {
        super(DATA);

        Initialize(7, 0, 2, 3);
        SetUpgrade(4, 0, 0);

        SetAffinity_Red(1, 1, 1);
        SetAffinity_Green(2, 0, 1);
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        if (AgilityStance.IsActive())
        {
            return super.GetDamageInfo().AddMultiplier(2);
        }
        else
        {
            return super.GetDamageInfo();
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.SFX("ATTACK_FIRE");
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.NONE);

        if (CheckTeamwork(AffinityType.Green, 3))
        {
            GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
        }

        GameActions.Bottom.ApplyVulnerable(p, m, magicNumber);

        if (CheckTeamwork(AffinityType.Red, 3))
        {
            GameActions.Bottom.ReduceStrength(m, secondaryValue, true);
        }
    }
}