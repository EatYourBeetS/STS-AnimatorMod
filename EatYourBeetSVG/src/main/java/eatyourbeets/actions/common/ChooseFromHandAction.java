package eatyourbeets.actions.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.utilities.RandomizedList;

import java.util.ArrayList;
import java.util.function.BiConsumer;

public class ChooseFromHandAction extends AbstractGameAction
{
    private final AbstractPlayer p;
    private final boolean random;
    private final boolean canCancel;
    private final boolean anyNumber;
    private final String message;
    private final ArrayList<AbstractCard> selectedCards;
    private final BiConsumer<Object, ArrayList<AbstractCard>> onCompletion;
    private final Object state;

    public ChooseFromHandAction(int amount, boolean random, BiConsumer<Object, ArrayList<AbstractCard>> onCompletion, Object state, String message)
    {
        this(amount, random, onCompletion, state, message, false, false);
    }

    public ChooseFromHandAction(int amount, boolean random, BiConsumer<Object, ArrayList<AbstractCard>> onCompletion, Object state, String message, boolean canCancel, boolean anyNumber)
    {
        this.canCancel = canCancel;
        this.anyNumber = anyNumber;
        this.message = message;
        this.selectedCards = new ArrayList<>();
        this.onCompletion = onCompletion;
        this.state = state;
        this.random = random;
        this.p = AbstractDungeon.player;

        this.setValues(p, p, amount);
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_MED;
    }

    public void update()
    {
        if (this.duration == Settings.ACTION_DUR_MED)
        {
            if (this.p.hand.group.size() > 0)
            {
                if (this.random)
                {
                    RandomizedList<AbstractCard> cards = new RandomizedList<>(p.hand.group);
                    for (int i = 0; i < amount; i++)
                    {
                        selectedCards.add(cards.Retrieve(AbstractDungeon.cardRandomRng));
                    }

                    onCompletion.accept(state, selectedCards);

                    this.isDone = true;
                }
                else
                {
                    AbstractDungeon.handCardSelectScreen.open(message, amount, anyNumber, canCancel);
                    this.tickDuration();
                }
            }
            else
            {
                this.isDone = true;
            }
        }
        else
        {
            if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved)
            {
                for (AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group)
                {
                    selectedCards.add(c);
                    p.hand.addToTop(c);
                }

                AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
                AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();

                onCompletion.accept(state, selectedCards);

                this.isDone = true;
            }

            this.tickDuration();
        }
    }
}
