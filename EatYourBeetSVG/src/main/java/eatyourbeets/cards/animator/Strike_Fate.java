package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.GameActionsHelper;

public class Strike_Fate extends Strike
{
    public static final String ID = Register(Strike_Fate.class.getSimpleName());

    public Strike_Fate()
    {
        super(ID, 1, CardTarget.ENEMY);

        Initialize(6, 0);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.DamageTarget(p, m, this.damage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.BLUNT_LIGHT);

        int cards = p.hand.size();
        if (p.hand.contains(this))
        {
            cards -= 1;
        }

        if (cards < 3)
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