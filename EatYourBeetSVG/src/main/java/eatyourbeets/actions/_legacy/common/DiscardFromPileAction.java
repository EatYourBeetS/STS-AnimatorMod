package eatyourbeets.actions._legacy.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;

public class DiscardFromPileAction extends AbstractGameAction
{
    private static final UIStrings uiStrings;
    public static final String[] TEXT;

    private final boolean anyNumber;
    private final CardGroup cardGroup;
    private float startingDuration;

    public DiscardFromPileAction(CardGroup cardGroup, int numCards, boolean anyNumber)
    {
        this.anyNumber = anyNumber;
        this.cardGroup = cardGroup;
        this.amount = numCards;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.startingDuration = Settings.ACTION_DUR_FAST;
        this.duration = this.startingDuration;
    }

    public void update()
    {
        if (this.duration == this.startingDuration)
        {
            if (cardGroup.isEmpty())
            {
                this.isDone = true;
                return;
            }

            AbstractDungeon.gridSelectScreen.open(cardGroup, this.amount, anyNumber, TEXT[0]);
        }
        else if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty())
        {
            for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards)
            {
                cardGroup.moveToDiscardPile(c);
                c.triggerOnManualDiscard();
                GameActionManager.incrementDiscard(false);
            }

            AbstractDungeon.gridSelectScreen.selectedCards.clear();
        }

        this.tickDuration();
    }

    static
    {
        uiStrings = CardCrawlGame.languagePack.getUIString("ReprogramAction");
        TEXT = uiStrings.TEXT;
    }
}
