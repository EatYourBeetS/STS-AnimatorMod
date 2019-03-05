package eatyourbeets.actions;


import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.FrozenEye;
import eatyourbeets.AnimatorResources;

import java.util.ArrayList;

public class TetAction extends AnimatorAction
{
    private static final String[] TEXT = AnimatorResources.GetUIStrings(AnimatorResources.UIStringType.TetAction).TEXT;

    private final ArrayList<AbstractCard> drawPileCards = new ArrayList<>();
    private final ArrayList<AbstractCard> discardPileCards = new ArrayList<>();

    private String moveToDiscardMessage;
    private String shuffleIntoDrawPileMessage;
    private boolean selectedFromDrawPile;
    private boolean selectedFromDiscard;
    private boolean selectionCompleted;

    public TetAction(int num)
    {
        this.amount = num;
        this.actionType = AbstractGameAction.ActionType.DISCARD;
        this.duration = Settings.ACTION_DUR_FAST;

        moveToDiscardMessage = TEXT[0].replace("#", String.valueOf(num));
        shuffleIntoDrawPileMessage = TEXT[1].replace("#", String.valueOf(num));
    }

    public void update()
    {
        if (this.duration == Settings.ACTION_DUR_FAST)
        {
            AbstractPlayer p = AbstractDungeon.player;
            if (p.drawPile.isEmpty())
            {
                selectedFromDrawPile = true;
            }
            else if (p.drawPile.size() <= this.amount)
            {
                drawPileCards.addAll(p.drawPile.group);
                selectedFromDrawPile = true;
            }
            else
            {
                CardGroup tmp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

                if (p.hasRelic(FrozenEye.ID))
                {
                    for (AbstractCard c : p.drawPile.group)
                    {
                        tmp.addToBottom(c);
                    }
                }
                else
                {
                    for (AbstractCard c : p.drawPile.group)
                    {
                        tmp.addToRandomSpot(c);
                    }
                }

                AbstractDungeon.gridSelectScreen.open(tmp, this.amount, moveToDiscardMessage, false, false, false, false);
            }
        }
        else if (!selectedFromDrawPile && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty())
        {
            drawPileCards.addAll(AbstractDungeon.gridSelectScreen.selectedCards);
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            selectedFromDrawPile = true;
        }

        if (selectedFromDrawPile && !selectionCompleted)
        {
            if (!selectedFromDiscard)
            {
                if (AbstractDungeon.player.discardPile.isEmpty())
                {
                    selectionCompleted = true;
                }
                else if (AbstractDungeon.player.discardPile.size() <= this.amount)
                {
                    discardPileCards.addAll(AbstractDungeon.player.discardPile.group);
                    selectionCompleted = true;
                }
                else if (AbstractDungeon.player.discardPile.group.size() > this.amount)
                {
                    AbstractDungeon.gridSelectScreen.open(AbstractDungeon.player.discardPile, amount, shuffleIntoDrawPileMessage, false, false, false, false);
                }

                selectedFromDiscard = true;
            }

            if (!selectionCompleted && AbstractDungeon.gridSelectScreen.selectedCards.size() > 0)
            {
                discardPileCards.addAll(AbstractDungeon.gridSelectScreen.selectedCards);
                AbstractDungeon.gridSelectScreen.selectedCards.clear();
                selectionCompleted = true;
            }
        }

        if (selectionCompleted)
        {
            for (AbstractCard c : drawPileCards)
            {
                AbstractDungeon.player.drawPile.moveToDiscardPile(c);
                c.triggerOnManualDiscard();
                GameActionManager.incrementDiscard(false);
            }

            for (AbstractCard c : discardPileCards)
            {
                AbstractDungeon.player.discardPile.removeCard(c);
                AbstractDungeon.player.discardPile.moveToBottomOfDeck(c);
            }

            isDone = true;
        }

        tickDuration();
    }
}