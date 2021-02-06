package eatyourbeets.cards.animator.basic;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Defend_NoGameNoLife extends Defend
{
    public static final String ID = Register(Defend_NoGameNoLife.class).ID;

    public Defend_NoGameNoLife()
    {
        super(ID, 1, CardTarget.SELF);

        Initialize(0, 5);
        SetUpgrade(0, 3);

        SetSynergy(Synergies.NoGameNoLife);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainBlock(block);

        for (int i = 0; i < p.hand.size(); i++)
        {
            AbstractCard card = p.hand.group.get(i);
            if (card != this && !GameUtilities.IsCurseOrStatus(card) && GameUtilities.Retain(card))
            {
                card.flash();
                return;
            }
        }
    }
}