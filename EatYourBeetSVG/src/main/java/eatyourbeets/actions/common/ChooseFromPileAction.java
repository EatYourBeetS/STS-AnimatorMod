package eatyourbeets.actions.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;
import java.util.function.BiConsumer;

public class ChooseFromPileAction extends AbstractGameAction
{
    private final boolean useSource;

    private final boolean random;
    private final String message;
    private final CardGroup sourceGroup;
    private final ArrayList<AbstractCard> selectedCards;
    private final BiConsumer<Object, ArrayList<AbstractCard>> onCompletion;
    private final Object state;

    public ChooseFromPileAction(int amount, boolean random, CardGroup sourceGroup, BiConsumer<Object, ArrayList<AbstractCard>> onCompletion, Object state, String message)
    {
        this(amount, random, sourceGroup, onCompletion, state, message, false);
    }

    public ChooseFromPileAction(int amount, boolean random, CardGroup sourceGroup, BiConsumer<Object, ArrayList<AbstractCard>> onCompletion, Object state, String message, boolean useSource)
    {
        this.useSource = useSource;
        this.message = message;
        this.selectedCards = new ArrayList<>();
        this.onCompletion = onCompletion;
        this.state = state;
        this.sourceGroup = sourceGroup;
        this.random = random;

        this.setValues(AbstractDungeon.player, AbstractDungeon.player, amount);
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_MED;
    }

    public void update()
    {
        if (this.duration == Settings.ACTION_DUR_MED)
        {
            CardGroup tmp;
            if (useSource)
            {
                tmp = sourceGroup;
            }
            else
            {
                tmp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
                for (AbstractCard c : sourceGroup.group)
                {
                    tmp.addToRandomSpot(c);
                }
            }

            if (tmp.size() == 0)
            {
                onCompletion.accept(state, selectedCards);
                this.isDone = true;
            }
            else if (tmp.size() <= this.amount)
            {
                for (int i = 0; i < tmp.size(); ++i)
                {
                    selectedCards.add(tmp.getNCardFromTop(i));
                }

                onCompletion.accept(state, selectedCards);
                this.isDone = true;
            }
            else
            {
                if (this.random)
                {
                    for (int i = 0; i < this.amount; i++)
                    {
                        AbstractCard card = tmp.getRandomCard(true);

                        tmp.removeCard(card);

                        selectedCards.add(card);
                    }
                }
                else if (this.amount == 1)
                {
                    AbstractDungeon.gridSelectScreen.open(tmp, this.amount, message, false);
                }

                this.tickDuration();
            }
        }
        else
        {
            if (AbstractDungeon.gridSelectScreen.selectedCards.size() != 0)
            {
                selectedCards.addAll(AbstractDungeon.gridSelectScreen.selectedCards);

                AbstractDungeon.gridSelectScreen.selectedCards.clear();

                onCompletion.accept(state, selectedCards);
            }

            this.tickDuration();
        }
    }
}
