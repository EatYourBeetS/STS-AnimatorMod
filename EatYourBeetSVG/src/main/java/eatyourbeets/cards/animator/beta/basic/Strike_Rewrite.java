package eatyourbeets.cards.animator.beta.basic;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.basic.Strike;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class Strike_Rewrite extends Strike
{
    public static final String ID = Register(Strike_Rewrite.class).ID;

    public Strike_Rewrite()
    {
        super(ID, 1, CardTarget.ENEMY);

        Initialize(7, 0, 1);
        SetUpgrade(3, 0);

        SetSeries(CardSeries.Rewrite);
        SetAffinity_Red(1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
        CombatStats.Affinities.Force.Retain();
    }
}