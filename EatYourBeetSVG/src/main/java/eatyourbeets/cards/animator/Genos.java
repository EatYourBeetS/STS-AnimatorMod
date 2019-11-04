package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ConservePower;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.powers.common.SelfDamagePower;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.animator.BurningPower;

public class Genos extends AnimatorCard
{
    public static final String ID = Register(Genos.class.getSimpleName(), EYBCardBadge.Synergy);

    public Genos()
    {
        super(ID, 0, CardType.ATTACK, CardRarity.COMMON, CardTarget.SELF_AND_ENEMY);

        Initialize(6, 0, 2, 4);

        SetSynergy(Synergies.OnePunchMan);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.DamageTarget(p, m, this, AbstractGameAction.AttackEffect.FIRE);
        GameActionsHelper.ApplyPower(p, m, new BurningPower(m, p, this.magicNumber), this.magicNumber);
        GameActionsHelper.ApplyPower(p, p, new SelfDamagePower(p, secondaryValue), secondaryValue);

        if (HasActiveSynergy())
        {
            GameActionsHelper.ApplyPower(p, p, new ConservePower(p, 1), 1);
        }
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeDamage(3);
        }
    }
}