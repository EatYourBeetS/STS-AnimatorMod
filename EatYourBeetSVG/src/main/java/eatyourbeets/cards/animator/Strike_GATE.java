package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DrawCardNextTurnPower;
import eatyourbeets.utilities.GameActionsHelper;

public class Strike_GATE extends Strike
{
    public static final String ID = CreateFullID(Strike_GATE.class.getSimpleName());

    public Strike_GATE()
    {
        super(ID, 1, CardTarget.ENEMY);

        Initialize(5,0);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.DamageTarget(p, m, this.damage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
        GameActionsHelper.ApplyPower(p, p, new DrawCardNextTurnPower(p, 1), 1);
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