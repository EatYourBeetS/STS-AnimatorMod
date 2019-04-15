package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.actions.DrawSpecificCardAction;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

public class Raven extends AnimatorCard
{
    public static final String ID = CreateFullID(Raven.class.getSimpleName());

    public Raven()
    {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);

        Initialize(7,0, 1);

        SetSynergy(Synergies.Elsword);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.DamageTarget(p, m, this.damage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
        GameActionsHelper.ApplyPower(p, m, new WeakPower(m, this.magicNumber, false), this.magicNumber);

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
                GameActionsHelper.AddToTop(new DrawSpecificCardAction(selected));
            }
        }
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {          
            upgradeDamage(1);
            upgradeMagicNumber(1);
        }
    }
}