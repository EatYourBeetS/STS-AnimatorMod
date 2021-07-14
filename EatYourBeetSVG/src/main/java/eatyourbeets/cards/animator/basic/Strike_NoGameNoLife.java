package eatyourbeets.cards.animator.basic;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Strike_NoGameNoLife extends Strike
{
    public static final String ID = Register(Strike_NoGameNoLife.class).ID;

    public Strike_NoGameNoLife()
    {
        super(ID, 1, CardTarget.ENEMY);

        Initialize(6, 0);
        SetUpgrade(3, 0);

        SetSeries(CardSeries.NoGameNoLife);
        SetAffinity_Blue(1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.BLUNT_LIGHT);

        for (int i = 0; i < p.hand.size(); i++)
        {
            AbstractCard card = p.hand.getNCardFromTop(i);
            if (card != this && !GameUtilities.IsCurseOrStatus(card) && GameUtilities.Retain(card))
            {
                card.flash();
                return;
            }
        }
    }
}