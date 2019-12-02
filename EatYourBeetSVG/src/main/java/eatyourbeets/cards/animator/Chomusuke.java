package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.common.MoveSpecificCardAction;
import eatyourbeets.cards.AnimatorCard_UltraRare;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.cards.Synergies;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActionsHelper;

public class Chomusuke extends AnimatorCard_UltraRare
{
    public static final String ID = Register(Chomusuke.class.getSimpleName(), EYBCardBadge.Exhaust);

    public Chomusuke()
    {
        super(ID, 0, CardType.SKILL, CardTarget.NONE);

        Initialize(0, 0);

        SetSynergy(Synergies.Konosuba);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        if (EffectHistory.TryActivateSemiLimited(cardID))
        {
            AbstractPlayer p = AbstractDungeon.player;
            GameActionsHelper.AddToBottom(new MoveSpecificCardAction(this, p.hand, p.exhaustPile, true));
            GameActionsHelper.GainEnergy(2);
        }
    }

    @Override
    public void triggerOnEndOfTurnForPlayingCard()
    {
        super.triggerOnEndOfTurnForPlayingCard();

        SetRetain(true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {

    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            SetInnate(true);
        }
    }
}