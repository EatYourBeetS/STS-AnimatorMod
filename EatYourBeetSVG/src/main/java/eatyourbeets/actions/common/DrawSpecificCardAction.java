package eatyourbeets.actions.common;

import basemod.BaseMod;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.actions.animator.AnimatorAction;

import java.util.function.Predicate;

public class DrawSpecificCardAction extends AnimatorAction
{
    private final Predicate<AbstractCard> predicate;
    private final AbstractPlayer player;
    private AbstractCard card;

    public DrawSpecificCardAction(AbstractCard card)
    {
        this.card = card;
        this.player = AbstractDungeon.player;
        this.predicate = null;

        this.setValues(this.player, AbstractDungeon.player);
        this.actionType = ActionType.CARD_MANIPULATION;
    }

    public DrawSpecificCardAction(Predicate<AbstractCard> predicate)
    {
        this.predicate = predicate;
        this.player = AbstractDungeon.player;
        this.card = null;

        this.setValues(this.player, AbstractDungeon.player);
        this.actionType = ActionType.CARD_MANIPULATION;
    }

    public void update()
    {
        if (this.player.hand.size() >= BaseMod.MAX_HAND_SIZE)
        {
            this.player.createHandIsFullDialog();
        }
        else
        {
            if (this.card == null)
            {
                for (AbstractCard c : this.player.drawPile.group)
                {
                    if (predicate.test(c))
                    {
                        this.card = c;
                    }
                }
            }

            if (this.player.drawPile.contains(card))
            {
                this.player.drawPile.removeCard(card);
                this.player.drawPile.addToTop(card);
                this.player.draw(1);
            }
        }

        this.isDone = true;
    }
}
