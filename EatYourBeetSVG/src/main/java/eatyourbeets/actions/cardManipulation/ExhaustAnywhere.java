package eatyourbeets.actions.cardManipulation;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.actions.EYBAction;
import eatyourbeets.utilities.GameActionsHelper_Legacy;

public class ExhaustAnywhere extends EYBAction
{
    public ExhaustAnywhere(AbstractCard card)
    {
        super(ActionType.EXHAUST);

        this.card = card;
    }

    @Override
    protected void FirstUpdate()
    {
        AbstractPlayer p = AbstractDungeon.player;
        if (p.limbo.contains(card))
        {
            GameActionsHelper_Legacy.ExhaustCard(card, p.limbo);
        }
        else if (p.discardPile.contains(card))
        {
            GameActionsHelper_Legacy.ExhaustCard(card, p.discardPile);
        }
        else if (p.drawPile.contains(card))
        {
            GameActionsHelper_Legacy.ExhaustCard(card, p.drawPile);
        }
        else
        {
            GameActionsHelper_Legacy.ExhaustCard(card, p.hand);
        }

        Complete();
    }
}
