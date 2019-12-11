package eatyourbeets.cards.animator.basic;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

import java.util.ArrayList;

public class Strike_NoGameNoLife extends Strike
{
    public static final String ID = Register(Strike_NoGameNoLife.class.getSimpleName());

    public Strike_NoGameNoLife()
    {
        super(ID, 1, CardTarget.ENEMY);

        Initialize(6,0);

        SetSynergy(Synergies.NoGameNoLife);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.BLUNT_LIGHT);

        ArrayList<AbstractCard> cards = p.hand.group;

        // TODO: This could be an action
        for (int i = cards.size() - 1; i >= 0; i--)
        {
            AbstractCard card = cards.get(i);
            if (card != this && card.type != CardType.CURSE && card.type != CardType.STATUS && !card.retain)
            {
                card.retain = true;
                card.flash();
                return;
            }
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