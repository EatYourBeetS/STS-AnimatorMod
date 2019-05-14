package eatyourbeets.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction.ActionType;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class RetainCardsOfTypeAction extends AbstractGameAction
{
    private final AbstractCard.CardType cardType;

    public RetainCardsOfTypeAction(AbstractCreature source, AbstractCard.CardType cardType)
    {
        this.setValues(AbstractDungeon.player, source, amount);
        this.actionType = ActionType.CARD_MANIPULATION;
        this.cardType = cardType;
    }

    public void update()
    {
        if (this.duration == 0.5F)
        {
            for (AbstractCard card : AbstractDungeon.player.hand.group)
            {
                if (card.type == cardType && !card.isEthereal)
                {
                    card.retain = true;
                    card.flash();
                }
            }
        }

        this.tickDuration();
    }
}
