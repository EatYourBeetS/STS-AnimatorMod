package eatyourbeets.cards.animator.basic;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

public class Strike_Elsword extends Strike
{
    public static final String ID = Register(Strike_Elsword.class.getSimpleName());

    public Strike_Elsword()
    {
        super(ID, 1, CardTarget.SELF_AND_ENEMY);

        Initialize(6,0, 1);

        SetSynergy(Synergies.Elsword);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
        GameActions.Bottom.ApplyBurning(p, m, magicNumber);
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