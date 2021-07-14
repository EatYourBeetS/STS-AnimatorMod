package eatyourbeets.cards.animator.basic;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.utilities.GameActions;

public class Strike_GoblinSlayer extends Strike
{
    public static final String ID = Register(Strike_GoblinSlayer.class).ID;

    public Strike_GoblinSlayer()
    {
        super(ID, 1, CardTarget.ENEMY);

        Initialize(8, 0);
        SetUpgrade(3, 0);

        SetExhaust(true);
        SetSeries(CardSeries.GoblinSlayer);
        SetAffinity_Red(1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
    }
}