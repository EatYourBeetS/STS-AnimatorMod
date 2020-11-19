package eatyourbeets.cards.animator.series.GATE;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.stances.AgilityStance;
import eatyourbeets.stances.ForceStance;
import eatyourbeets.utilities.GameActions;

public class Kuribayashi extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Kuribayashi.class).SetAttack(2, CardRarity.COMMON, EYBAttackType.Ranged);

    public Kuribayashi()
    {
        super(DATA);

        Initialize(7, 0, 2, 3);
        SetUpgrade(4, 0, 0);
        SetScaling(0, 1, 1);

        SetSynergy(Synergies.Gate);
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
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.SFX("ATTACK_FIRE");
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.NONE);

        if (AgilityStance.IsActive())
        {
            GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
        }

        if (ForceStance.IsActive())
        {
            GameActions.Bottom.ReduceStrength(m, secondaryValue, true);
        }

        GameActions.Bottom.ApplyVulnerable(p, m, magicNumber);
    }
}