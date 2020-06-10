package eatyourbeets.actions.pileSelection;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.FrozenEye;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import eatyourbeets.actions.EYBActionWithCallback;
import eatyourbeets.ui.GridCardSelectScreenPatch;
import eatyourbeets.utilities.CardSelection;
import eatyourbeets.utilities.JUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public class SelectFromPile extends EYBActionWithCallback<ArrayList<AbstractCard>>
{
    protected final ArrayList<AbstractCard> selectedCards = new ArrayList<>();
    protected final CardGroup fakeHandGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
    protected final CardGroup[] groups;

    protected Predicate<AbstractCard> filter;
    protected CardSelection origin;
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
        this.message = JUtils.Format(format, args);

        return this;
    }

    public SelectFromPile SetOptions(boolean isRandom, boolean anyNumber)
    {
        this.anyNumber = anyNumber;

        if (isRandom)
        {
            this.origin = CardSelection.Random;
        }

        return this;
    }

    public SelectFromPile SetOptions(CardSelection origin, boolean anyNumber)
    {
        this.origin = origin;
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
                    AddCard(temp, card);
                }
            }

            if (temp.type == CardGroup.CardGroupType.HAND && origin == null)
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
                if (temp.type == CardGroup.CardGroupType.DRAW_PILE)
                {
                    if (!player.hasRelic(FrozenEye.ID) && !player.hasPower("animator:SatoriKomeijiPower")){
                        temp.sortAlphabetically(true);
                        temp.sortByRarityPlusStatusCardType(true);
                    } else {
                        Collections.reverse(temp.group); //The top of the deck is at the end of the arraylist
                    }
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

        if (origin != null)
        {
            List<AbstractCard> temp = new ArrayList<>(mergedGroup.group);

            boolean remove = origin.mode.IsRandom();
            int max = Math.min(temp.size(), amount);
            for (int i = 0; i < max; i++)
            {
                AbstractCard card = origin.GetCard(temp, i, remove);
                if (card != null)
                {
                    selectedCards.add(card);
                }
            }

            GridCardSelectScreenPatch.Clear();
            Complete(selectedCards);
        }
        else
        {
            if (anyNumber)
            {
                AbstractDungeon.gridSelectScreen.open(mergedGroup, amount, true, CreateMessage());
            }
            else
            {
                AbstractDungeon.gridSelectScreen.open(mergedGroup, amount, CreateMessage(), false, false, false, false);
            }
        }
    }

    protected void AddCard(CardGroup group, AbstractCard card)
    {
        group.group.add(card);

        if (!card.isSeen)
        {
            UnlockTracker.markCardAsSeen(card.cardID);
            card.isLocked = false;
            card.isSeen = true;
        }
    }

    @Override
    protected void UpdateInternal(float deltaTime)
    {
        if (AbstractDungeon.gridSelectScreen.selectedCards.size() != 0)
        {
            selectedCards.addAll(AbstractDungeon.gridSelectScreen.selectedCards);

            player.hand.group.addAll(fakeHandGroup.group);
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            GridCardSelectScreenPatch.Clear();
        }

        if (TickDuration(deltaTime))
        {
            Complete(selectedCards);
        }
    }
}
