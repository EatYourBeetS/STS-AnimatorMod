package eatyourbeets.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;
import java.util.function.BiConsumer;

public class ChooseAnyNumberFromPileAction extends AbstractGameAction
{
    private boolean useSource;

    private final String message;
    private final CardGroup sourceGroup;
    private final ArrayList<AbstractCard> selectedCards;
    private final BiConsumer<Object, ArrayList<AbstractCard>> onCompletion;
    private final Object state;

    public ChooseAnyNumberFromPileAction(int amount, CardGroup sourceGroup, BiConsumer<Object, ArrayList<AbstractCard>> onCompletion, Object state, String message)
    {
        this(amount, sourceGroup, onCompletion, state, message, false);
    }

    public ChooseAnyNumberFromPileAction(int amount, CardGroup sourceGroup, BiConsumer<Object, ArrayList<AbstractCard>> onCompletion, Object state, String message, boolean useSource)
    {
        this.useSource = useSource;
        this.message = message;
        this.selectedCards = new ArrayList<>();
        this.onCompletion = onCompletion;
        this.state = state;
        this.sourceGroup = sourceGroup;

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
            else
            {
                AbstractDungeon.gridSelectScreen.open(tmp, this.amount, true, message);

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
