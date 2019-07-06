package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.animator.TetDiscardAction;
import eatyourbeets.actions.animator.TetRecoverAction;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.utilities.GameActionsHelper;

public class Tet extends AnimatorCard
{
    public static final String ID = CreateFullID(Tet.class.getSimpleName());

    public Tet()
    {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);

        Initialize(0,0,2);

        SetSynergy(Synergies.NoGameNoLife);
    }

    @Override
    public void triggerOnEndOfTurnForPlayingCard()
    {
        super.triggerOnEndOfTurnForPlayingCard();

        if (upgraded)
        {
            this.retain = true;
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.AddToBottom(new TetDiscardAction(magicNumber));
        GameActionsHelper.AddToBottom(new TetRecoverAction(magicNumber));
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            this.retain = true;
        }
    }
}