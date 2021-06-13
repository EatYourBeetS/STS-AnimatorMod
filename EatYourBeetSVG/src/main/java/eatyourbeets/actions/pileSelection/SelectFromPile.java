package eatyourbeets.actions.pileSelection;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.FrozenEye;
import com.megacrit.cardcrawl.screens.select.GridCardSelectScreen;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import eatyourbeets.actions.EYBActionWithCallback;
import eatyourbeets.interfaces.delegates.FuncT1;
import eatyourbeets.interfaces.delegates.FuncT2;
import eatyourbeets.resources.GR;
import eatyourbeets.ui.GridCardSelectScreenPatch;
import eatyourbeets.utilities.CardSelection;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.GenericCondition;
import eatyourbeets.utilities.JUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SelectFromPile extends EYBActionWithCallback<ArrayList<AbstractCard>>
{
    protected final ArrayList<AbstractCard> selectedCards = new ArrayList<>();
    protected final CardGroup fakeHandGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
    protected final CardGroup[] groups;

    protected GenericCondition<AbstractCard> filter;
    protected CardSelection origin;
    protected boolean canPlayerCancel;
    protected boolean anyNumber;

    public SelectFromPile(String sourceName, int amount, CardGroup... groups)
    {
        this(ActionType.CARD_MANIPULATION, sourceName, amount, groups);
    }

    public SelectFromPile(ActionType type, String sourceName, int amount, CardGroup... groups)
    {
        super(type);

        this.groups = groups;
        this.canPlayerCancel = false;
        this.message = GR.Common.Strings.GridSelection.ChooseCards;

        Initialize(amount, sourceName);
    }

    public SelectFromPile CancellableFromPlayer(boolean value)
    {
        this.canPlayerCancel = value;

        return this;
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

    public SelectFromPile SetFilter(FuncT1<Boolean, AbstractCard> filter)
    {
        this.filter = GenericCondition.FromT1(filter);

        return this;
    }

    public <S> SelectFromPile SetFilter(S state, FuncT2<Boolean, S, AbstractCard> filter)
    {
        this.filter = GenericCondition.FromT2(filter, state);

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
                if (filter == null || filter.Check(card))
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
                    if (GameUtilities.HasRelicEffect(FrozenEye.ID))
                    {
                        Collections.reverse(temp.group);
                    }
                    else
                    {
                        temp.sortAlphabetically(true);
                        temp.sortByRarityPlusStatusCardType(true);
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
                if (canPlayerCancel)
                {
                    // Setting canCancel to true does not ensure the cancel button will be shown...
                    AbstractDungeon.overlayMenu.cancelButton.show(GridCardSelectScreen.TEXT[1]);
                }
                else if (amount > 1 && amount > mergedGroup.size())
                {
                    AbstractDungeon.gridSelectScreen.selectedCards.addAll(mergedGroup.group);
                    return;
                }

                AbstractDungeon.gridSelectScreen.open(mergedGroup, Math.min(mergedGroup.size(), amount), CreateMessage(), false, false, canPlayerCancel, false);
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
