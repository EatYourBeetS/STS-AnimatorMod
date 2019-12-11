package eatyourbeets.cards.animator.series.NoGameNoLife;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.animator.TetDiscardAction;
import eatyourbeets.actions.animator.TetRecoverAction;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActions;

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
        // TODO: Simplify This
        GameActions.Bottom.Add(new TetDiscardAction(magicNumber));
        GameActions.Bottom.Add(new TetRecoverAction(magicNumber));

        if (HasActiveSynergy() && EffectHistory.TryActivateSemiLimited(cardID))
        {
            GameActions.Bottom.GainEnergy(1);
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