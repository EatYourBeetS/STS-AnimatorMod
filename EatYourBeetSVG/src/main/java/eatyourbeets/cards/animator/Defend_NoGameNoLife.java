package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.GameActionsHelper;

import java.util.ArrayList;

public class Defend_NoGameNoLife extends Defend
{
    public static final String ID = CreateFullID(Defend_NoGameNoLife.class.getSimpleName());

    public Defend_NoGameNoLife()
    {
        super(ID, 1, CardTarget.SELF);

        Initialize(0, 5);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.GainBlock(p, this.block);

        ArrayList<AbstractCard> cards = p.hand.group;

        for (int i = 0; i < cards.size(); i++)
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
            upgradeBlock(3);
        }
    }
}