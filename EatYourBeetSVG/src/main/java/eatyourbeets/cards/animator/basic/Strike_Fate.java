package eatyourbeets.cards.animator.basic;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.utilities.GameActions;

public class Strike_Fate extends Strike
{
    public static final String ID = Register(Strike_Fate.class).ID;

    public Strike_Fate()
    {
        super(ID, 1, CardTarget.ENEMY);

        Initialize(6, 0);
        SetUpgrade(3, 0);

        SetSeries(CardSeries.Fate);
        SetAffinity_Green(1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.BLUNT_LIGHT);

        int cards = p.hand.size();
        if (p.hand.contains(this))
        {
            cards -= 1;
        }

        if (cards < 3)
        {
            GameActions.Bottom.GainEnergy(1);
        }
    }
}