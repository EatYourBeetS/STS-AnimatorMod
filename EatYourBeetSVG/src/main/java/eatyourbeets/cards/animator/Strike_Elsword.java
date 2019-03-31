package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.GameActionsHelper;

public class Strike_Elsword extends Strike
{
    public static final String ID = CreateFullID(Strike_Elsword.class.getSimpleName());

    public Strike_Elsword()
    {
        super(ID, 1, CardTarget.ALL_ENEMY);

        Initialize(4,0);

        this.isMultiDamage = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.DamageAllEnemies(p, this.multiDamage, this.damageType, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
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