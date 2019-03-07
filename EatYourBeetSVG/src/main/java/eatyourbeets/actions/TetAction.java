package eatyourbeets.actions;


import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.FrozenEye;
import eatyourbeets.AnimatorResources;

import java.util.ArrayList;

public class TetAction extends AnimatorAction
{
    private static final String[] TEXT = AnimatorResources.GetUIStrings(AnimatorResources.UIStringType.TetAction).TEXT;

    private final ArrayList<AbstractCard> drawPileSelectedCards = new ArrayList<>();
    private final ArrayList<AbstractCard> discardPileSelectedCards = new ArrayList<>();

    public TetAction(int num)
    {
        AbstractPlayer p = AbstractDungeon.player;
        this.amount = Math.min(num, p.drawPile.size());
        this.amount = Math.min(amount, p.discardPile.size());

        this.actionType = AbstractGameAction.ActionType.DISCARD;
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
            SelectFromDiscardPile(true);
        }

        if (SelectFromDiscardPile(false))
        {
            for (AbstractCard c : drawPileSelectedCards)
            {
                AbstractDungeon.player.drawPile.moveToDiscardPile(c);
                c.triggerOnManualDiscard();
                GameActionManager.incrementDiscard(false);
            }

            for (AbstractCard c : discardPileSelectedCards)
            {
                AbstractDungeon.player.discardPile.moveToDeck(c, true);
            }

            isDone = true;
        }

        tickDuration();
    }

    private boolean SelectFromDrawPile(boolean initialize)
    {
        if (initialize)
        {
            String message = TEXT[0].replace("#", String.valueOf(amount));
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

    private boolean SelectFromDiscardPile(boolean initialize)
    {
        if (initialize)
        {
            int replaceNumber = drawPileSelectedCards.size();

            String message = TEXT[1].replace("#", String.valueOf(replaceNumber));

            AbstractDungeon.gridSelectScreen.open(AbstractDungeon.player.discardPile, replaceNumber, message, false, false, false, false);
        }
        else if (discardPileSelectedCards.isEmpty() && AbstractDungeon.gridSelectScreen.selectedCards.size() > 0)
        {
            discardPileSelectedCards.addAll(AbstractDungeon.gridSelectScreen.selectedCards);
            AbstractDungeon.gridSelectScreen.selectedCards.clear();

            return true;
        }

        return false;
    }
}