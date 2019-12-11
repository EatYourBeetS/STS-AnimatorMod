package eatyourbeets.cards.animator.series.Elsword;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Raven extends AnimatorCard
{
    public static final String ID = Register(Raven.class.getSimpleName(), EYBCardBadge.Special);

    public Raven()
    {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);

        Initialize(5, 0, 1);

        SetSynergy(Synergies.Elsword);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);

        if (GameUtilities.IsAttacking(m.intent))
        {
            GameActions.Bottom.ApplyWeak(p, m, 1);
        }
        else
        {
            GameActions.Bottom.ApplyVulnerable(p, m, 1);
        }

        if (p.drawPile.size() > 0)
        {
            AbstractCard selected = null;
            int minDamage = Integer.MAX_VALUE;
            for (AbstractCard c : p.drawPile.getAttacks().group)
            {
                if (c.baseDamage < minDamage)
                {
                    minDamage = c.baseDamage;
                    selected = c;
                }
            }

            if (selected != null)
            {
                GameActions.Top.MoveCard(selected, p.hand, p.drawPile, true);
            }
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