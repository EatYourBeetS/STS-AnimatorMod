package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.utilities.GameActions;

public class Alexander extends AnimatorCard
{
    public static final String ID = Register(Alexander.class.getSimpleName(), EYBCardBadge.Exhaust);

    public Alexander()
    {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ALL_ENEMY);

        Initialize(7,0,8);

        SetMultiDamage(true);
        SetSynergy(Synergies.Fate);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        GameActions.Bottom.DealDamageToAll(DamageInfo.createDamageMatrix(this.magicNumber, false),
        damageTypeForTurn, AbstractGameAction.AttackEffect.SLASH_HEAVY).SetOptions(true,false);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActions.Bottom.DealDamageToAll(this, AbstractGameAction.AttackEffect.SLASH_HEAVY);
        GameActions.Bottom.GainForce(1);
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