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

    private final ArrayList<AbstractCard> drawPileSelectedCards = new ArrayList<>();
    private final CardGroup cardGroup;
    private final AbstractCard card;

    public TransformIntoSpecificCardAction(AbstractCard card, CardGroup cardGroup, int amount)
    {
        this.amount = amount;
        this.card = card;
        this.cardGroup = cardGroup;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    public void update()
    {
        if (this.duration == Settings.ACTION_DUR_FAST)
        {
            logger.info("Amount:" + amount);
            if (amount == 0)
            {
                this.isDone = true;
                return;
            }

            SelectFromDrawPile(true);
        }

        if (SelectFromDrawPile(false))
        {
            for (AbstractCard c : drawPileSelectedCards)
            {
                AbstractCard replacement = card.makeCopy();
                if (card.upgraded)
                {
                    replacement.upgrade();
                }

                int index = cardGroup.group.indexOf(c);
                cardGroup.group.remove(c);
                cardGroup.group.add(index, replacement);
            }

            isDone = true;
        }

        tickDuration();
    }

    private boolean SelectFromDrawPile(boolean initialize)
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
        else if (drawPileSelectedCards.isEmpty() && AbstractDungeon.gridSelectScreen.selectedCards.size() > 0)
        {
            drawPileSelectedCards.addAll(AbstractDungeon.gridSelectScreen.selectedCards);
            AbstractDungeon.gridSelectScreen.selectedCards.clear();

            return true;
        }

        return false;
    }
}