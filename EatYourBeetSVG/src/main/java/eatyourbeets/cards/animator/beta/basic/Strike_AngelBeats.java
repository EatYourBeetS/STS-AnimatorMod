package eatyourbeets.cards.animator.beta.basic;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.basic.Strike;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.utilities.GameActions;

public class Strike_AngelBeats extends Strike
{
    public static final String ID = Register(Strike_AngelBeats.class).ID;

    public Strike_AngelBeats()
    {
        super(ID, 1, CardTarget.ENEMY);

        Initialize(7, 0);
        SetUpgrade(3, 0);

        SetAttackType(EYBAttackType.Ranged);
        SetSeries(CardSeries.AngelBeats);
        SetAffinity_Green(1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
    }
}
