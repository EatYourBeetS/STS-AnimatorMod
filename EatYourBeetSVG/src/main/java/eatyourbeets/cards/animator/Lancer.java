package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

public class Lancer extends AnimatorCard
{
    public static final String ID = Register(Lancer.class.getSimpleName(), EYBCardBadge.Special);

    public Lancer()
    {
        super(ID, 1, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);

        Initialize(7,0);

        SetSynergy(Synergies.Fate);
    }

    @Override
    public float calculateModifiedCardDamage(AbstractPlayer player, AbstractMonster mo, float tmp)
    {
        if (mo != null && mo.currentHealth <= mo.maxHealth / 2)
        {
            return super.calculateModifiedCardDamage(player, mo, tmp) * 2;
        }
        else
        {
            return super.calculateModifiedCardDamage(player, mo, tmp);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        AbstractGameAction.AttackEffect attackEffect;
        if (m.currentHealth <= (m.maxHealth / 2))
        {
            attackEffect = AbstractGameAction.AttackEffect.SLASH_HEAVY;
        }
        else
        {
            attackEffect = AbstractGameAction.AttackEffect.SLASH_VERTICAL;
        }

        GameActionsHelper.DamageTargetPiercing(p, m, this, attackEffect);

        if (HasActiveSynergy())
        {
            GameActionsHelper.GainEnergy(1);
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