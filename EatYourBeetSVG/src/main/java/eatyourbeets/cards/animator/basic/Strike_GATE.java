package eatyourbeets.cards.animator.basic;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DrawCardNextTurnPower;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

public class Strike_GATE extends Strike
{
    public static final String ID = Register(Strike_GATE.class);

    public Strike_GATE()
    {
        super(ID, 1, CardTarget.ENEMY);

        Initialize(5, 0);
        SetUpgrade(3, 0);

        SetSynergy(Synergies.Gate);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
        GameActions.Bottom.StackPower(new DrawCardNextTurnPower(p, 1));
    }
}