package eatyourbeets.actions;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.FrozenEye;
import eatyourbeets.AnimatorResources;

import java.util.ArrayList;

public class TransformIntoSpecificCardAction extends AnimatorAction
{
    private static final String[] TEXT = AnimatorResources.GetUIStrings(AnimatorResources.UIStringType.Actions).TEXT;

    private final ArrayList<AbstractCard> selectedCards = new ArrayList<>();
    private final CardGroup destination;
    private final CardGroup source;
    private final AbstractCard card;

    public TransformIntoSpecificCardAction(AbstractCard card, CardGroup source, CardGroup destination, int amount)
    {
        this.amount = amount;
        this.card = card;
        this.destination = destination;
        this.source = source;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    public void update()
    {
        if (this.duration == Settings.ACTION_DUR_FAST)
        {
            if (amount == 0)
            {
                this.isDone = true;
                return;
            }

            SelectCard(true);
        }

        if (SelectCard(false))
        {
            for (AbstractCard c : selectedCards)
            {
                AbstractCard replacement = card.makeCopy();
                if (card.upgraded)
                {
                    replacement.upgrade();
                }

                int index = destination.group.indexOf(c);
                destination.group.remove(c);
                destination.group.add(index, replacement);
            }

            isDone = true;
        }

        tickDuration();
    }

    private boolean SelectCard(boolean initialize)
    {
        if (source.type == CardGroup.CardGroupType.HAND)
        {
            return SelectFromHand(initialize);
        }
        else
        {
            return SelectFromGrid(initialize);
        }
    }

    private boolean SelectFromHand(boolean initialize)
    {
        if (initialize)
        {
            String message = TEXT[5] + card.name;

            AbstractDungeon.handCardSelectScreen.open(message, this.amount, true);
        }
        else if (selectedCards.isEmpty() && AbstractDungeon.handCardSelectScreen.selectedCards.size() > 0)
        {
            selectedCards.addAll(AbstractDungeon.handCardSelectScreen.selectedCards.group);
            AbstractDungeon.handCardSelectScreen.selectedCards.clear();
            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;

            return true;
        }

        return false;
    }

    private boolean SelectFromGrid(boolean initialize)
    {
        if (initialize)
        {
            String message = TEXT[5] + card.name;
            AbstractPlayer p = AbstractDungeon.player;
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

            AbstractDungeon.gridSelectScreen.open(tmp, this.amount, true, message);
        }
        else if (selectedCards.isEmpty() && AbstractDungeon.gridSelectScreen.selectedCards.size() > 0)
        {
            selectedCards.addAll(AbstractDungeon.gridSelectScreen.selectedCards);
            AbstractDungeon.gridSelectScreen.selectedCards.clear();

            return true;
        }

        return false;
    }
}