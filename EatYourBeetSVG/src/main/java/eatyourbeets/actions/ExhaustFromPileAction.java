package eatyourbeets.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction.ActionType;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;

public class ExhaustFromPileAction extends AbstractGameAction
{
    private static final UIStrings uiStrings;
    public static final String[] TEXT;
    private AbstractPlayer p;
    private boolean random;
    private boolean canCancel;
    private final CardGroup sourceGroup;

    public ExhaustFromPileAction(int amount, boolean random, CardGroup sourceGroup)
    {
        this(amount, random, sourceGroup, false);
    }

    public ExhaustFromPileAction(int amount, boolean random, CardGroup sourceGroup, boolean canCancel)
    {
        this.canCancel = canCancel;
        this.sourceGroup = sourceGroup;
        this.random = random;
        this.p = AbstractDungeon.player;
        this.setValues(this.p, AbstractDungeon.player, amount);
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_MED;
    }

    public void update()
    {
        AbstractCard card;
        if (this.duration == Settings.ACTION_DUR_MED)
        {
            CardGroup cards = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

            cards.group.addAll(sourceGroup.group);

            if (cards.size() == 0)
            {
                this.isDone = true;
            }
            else if (cards.size() <= this.amount)
            {
                for (int i = 0; i < cards.size(); ++i)
                {
                    card = cards.getNCardFromTop(i);
                    sourceGroup.moveToExhaustPile(card);
                }

                this.isDone = true;
            }
            else
            {
                if (this.random)
                {
                    for (int i = 0; i < this.amount; i++)
                    {
                        card = cards.getRandomCard(true);
                        cards.removeCard(card);

                        sourceGroup.moveToExhaustPile(card);
                    }

                    this.p.hand.applyPowers();
                }
                else if (this.amount == 1)
                {
                    AbstractDungeon.gridSelectScreen.open(cards, this.amount, TEXT[0], false, false, canCancel, false);
                }
                else
                {
                    AbstractDungeon.gridSelectScreen.open(cards, this.amount, TEXT[1], false, false, canCancel, false);
                }

                this.tickDuration();
            }
        }
        else
        {
            if (AbstractDungeon.gridSelectScreen.selectedCards.size() != 0)
            {

                for (AbstractCard abstractCard : AbstractDungeon.gridSelectScreen.selectedCards)
                {
                    card = abstractCard;
                    card.unhover();
                    sourceGroup.moveToExhaustPile(card);

                    this.p.hand.refreshHandLayout();
                    this.p.hand.applyPowers();
                }

                AbstractDungeon.gridSelectScreen.selectedCards.clear();
                this.p.hand.refreshHandLayout();
            }

            this.tickDuration();
        }
    }

    static
    {
        uiStrings = CardCrawlGame.languagePack.getUIString("ExhaustAction");
        TEXT = uiStrings.TEXT;
    }
}
