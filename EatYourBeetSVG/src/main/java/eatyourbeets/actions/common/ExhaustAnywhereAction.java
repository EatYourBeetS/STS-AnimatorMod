package eatyourbeets.actions.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.utilities.GameActionsHelper;

public class ExhaustAnywhereAction extends AbstractGameAction
{
    private final AbstractCard card;

    public ExhaustAnywhereAction(AbstractCard card)
    {
        this.card = card;
        this.actionType = ActionType.EXHAUST;
        this.duration = Settings.ACTION_DUR_MED;
    }

    public void update()
    {
        AbstractPlayer p = AbstractDungeon.player;
        if (p.limbo.contains(card))
        {
            GameActionsHelper.ExhaustCard(card, p.limbo);
        }
        else if (p.discardPile.contains(card))
        {
            GameActionsHelper.ExhaustCard(card, p.discardPile);
        }
        else if (p.drawPile.contains(card))
        {
            GameActionsHelper.ExhaustCard(card, p.drawPile);
        }
        else
        {
            GameActionsHelper.ExhaustCard(card, p.hand);
        }

        this.isDone = true;
    }
}
