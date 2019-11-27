package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.animator.TetDiscardAction;
import eatyourbeets.actions.animator.TetRecoverAction;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.cards.Synergies;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActionsHelper;

public class Tet extends AnimatorCard
{
    public static final String ID = Register(Tet.class.getSimpleName(), EYBCardBadge.Synergy);

    public Tet()
    {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.NONE);

        Initialize(0,0,2);

        SetSynergy(Synergies.NoGameNoLife);
    }

    @Override
    public void triggerOnEndOfTurnForPlayingCard()
    {
        super.triggerOnEndOfTurnForPlayingCard();

        if (upgraded)
        {
            SetRetain(true);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.AddToBottom(new TetDiscardAction(magicNumber));
        GameActionsHelper.AddToBottom(new TetRecoverAction(magicNumber));

        if (HasActiveSynergy() && EffectHistory.TryActivateSemiLimited(cardID))
        {
            GameActionsHelper.GainEnergy(1);
        }
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            SetRetain(true);
        }
    }
}