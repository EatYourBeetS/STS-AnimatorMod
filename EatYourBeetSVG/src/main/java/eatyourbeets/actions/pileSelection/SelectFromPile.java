package eatyourbeets.actions.pileSelection;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.FrozenEye;
import eatyourbeets.actions.EYBActionWithCallback;
import eatyourbeets.ui.screens.GridCardSelectScreenPatch;
import eatyourbeets.utilities.JavaUtilities;
import eatyourbeets.utilities.RandomizedList;

import java.util.ArrayList;
import java.util.function.Predicate;

public class SelectFromPile extends EYBActionWithCallback<ArrayList<AbstractCard>>
{
    protected final ArrayList<AbstractCard> selectedCards = new ArrayList<>();
    protected final CardGroup fakeHandGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
    protected final CardGroup[] groups;

    protected Predicate<AbstractCard> filter;
    protected boolean isRandom;
    protected boolean anyNumber;

    public SelectFromPile(String sourceName, int amount, CardGroup... groups)
    {
        this(ActionType.CARD_MANIPULATION, sourceName, amount, groups);
    }

    public SelectFromPile(ActionType type, String sourceName, int amount, CardGroup... groups)
    {
        super(type);

        this.groups = groups;

        Initialize(amount, sourceName);
    }

    public SelectFromPile SetMessage(String message)
    {
        this.message = message;

        return this;
    }

    public SelectFromPile SetMessage(String format, Object... args)
    {
        this.message = JavaUtilities.Format(format, args);

        return this;
    }

    public SelectFromPile SetOptions(boolean isRandom, boolean anyNumber)
    {
        this.isRandom = isRandom;
        this.anyNumber = anyNumber;

        return this;
    }

    public SelectFromPile SetFilter(Predicate<AbstractCard> filter)
    {
        this.filter = filter;

        return this;
    }

    @Override
    protected void FirstUpdate()
    {
        GridCardSelectScreenPatch.Clear();

        for (CardGroup group : groups)
        {
            CardGroup temp = new CardGroup(group.type);
            for (AbstractCard card : group.group)
            {
                if (filter == null || filter.test(card))
                {
                    temp.group.add(card);
                }
            }

            if (temp.type == CardGroup.CardGroupType.HAND && !isRandom)
            {
                fakeHandGroup.group.addAll(temp.group);

                for (AbstractCard c : temp.group)
                {
                    player.hand.removeCard(c);
                }

                GridCardSelectScreenPatch.AddGroup(fakeHandGroup);
            }
            else
            {
                if (temp.type == CardGroup.CardGroupType.DRAW_PILE && !player.hasRelic(FrozenEye.ID))
                {
                    temp.sortAlphabetically(true);
                    temp.sortByRarityPlusStatusCardType(true);
                }

                GridCardSelectScreenPatch.AddGroup(temp);
            }
        }

        CardGroup mergedGroup = GridCardSelectScreenPatch.GetCardGroup();
        if (mergedGroup.isEmpty())
        {
            player.hand.group.addAll(fakeHandGroup.group);
            GridCardSelectScreenPatch.Clear();
            Complete();
            return;
        }

        if (isRandom)
        {
            RandomizedList<AbstractCard> temp = new RandomizedList<>(mergedGroup.group);

            int max = Math.min(temp.Count(), amount);
            for (int i = 0; i < max; i++)
            {
                selectedCards.add(temp.Retrieve(AbstractDungeon.cardRandomRng));
            }

            GridCardSelectScreenPatch.Clear();
            Complete(selectedCards);
        }
        else
        {
            if (anyNumber)
            {
                AbstractDungeon.gridSelectScreen.open(mergedGroup, this.amount, true, CreateMessage());
            }
            else
            {
                AbstractDungeon.gridSelectScreen.open(mergedGroup, this.amount, CreateMessage(), false, false, false, false);
            }
        }
    }

    @Override
    protected void UpdateInternal()
    {
        if (AbstractDungeon.gridSelectScreen.selectedCards.size() != 0)
        {
            selectedCards.addAll(AbstractDungeon.gridSelectScreen.selectedCards);

            player.hand.group.addAll(fakeHandGroup.group);
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            GridCardSelectScreenPatch.Clear();
        }

        tickDuration();

        if (isDone)
        {
            Complete(selectedCards);
        }
    }
}
