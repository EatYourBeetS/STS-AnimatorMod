package eatyourbeets.cards.animator.basic;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DrawCardNextTurnPower;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.utilities.GameActions;

public class Strike_GATE extends Strike
{
    public static final String ID = Register(Strike_GATE.class).ID;

    public Strike_GATE()
    {
        super(ID, 1, CardTarget.ENEMY);

        Initialize(5, 0);
        SetUpgrade(3, 0);

        SetSeries(CardSeries.GATE);
        SetAffinity_Green(1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
        GameActions.Bottom.StackPower(new DrawCardNextTurnPower(p, 1));
    }
}