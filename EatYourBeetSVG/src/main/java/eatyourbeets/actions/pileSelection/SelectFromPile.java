package eatyourbeets.actions.pileSelection;

import com.evacipated.cardcrawl.mod.stslib.patches.CenterGridCardSelectScreen;
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
import eatyourbeets.utilities.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SelectFromPile extends EYBActionWithCallback<ArrayList<AbstractCard>>
{
    protected final ArrayList<AbstractCard> selectedCards = new ArrayList<>();
    protected final CardGroup fakeHandGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
    protected final CardGroup[] groups;

    protected GenericCondition<AbstractCard> filter;
    protected ListSelection<AbstractCard> origin;
    protected boolean hideTopPanel;
    protected boolean canPlayerCancel;
    protected boolean canPickLower;
    protected boolean anyNumber;
    protected boolean selected;

    public SelectFromPile(String sourceName, int amount, CardGroup... groups)
    {
        this(ActionType.CARD_MANIPULATION, sourceName, amount, groups);
    }

    public SelectFromPile(ActionType type, String sourceName, int amount, CardGroup... groups)
    {
        super(type);

        this.groups = groups;
        this.canPlayerCancel = false;
        this.message = GR.Common.Strings.GridSelection.ChooseCards_F1;

        Initialize(amount, sourceName);
    }

    public SelectFromPile HideTopPanel(boolean hideTopPanel)
    {
        this.hideTopPanel = hideTopPanel;

        return this;
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
        return SetOptions(isRandom, anyNumber, true);
    }

    public SelectFromPile SetOptions(boolean isRandom, boolean anyNumber, boolean canPickLower)
    {
        if (isRandom)
        {
            this.origin = CardSelection.Random;
        }

        this.anyNumber = anyNumber;
        this.canPickLower = canPickLower;

        return this;
    }


    public SelectFromPile SetOptions(ListSelection<AbstractCard> origin, boolean anyNumber)
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
        if (hideTopPanel)
        {
            GameUtilities.SetTopPanelVisible(false);
        }

        GridCardSelectScreenPatch.Clear();

        for (CardGroup group : groups)
        {
            final CardGroup temp = new CardGroup(group.type);
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
                if (temp.type == CardGroup.CardGroupType.DRAW_PILE && origin == null)
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

        final CardGroup mergedGroup = GridCardSelectScreenPatch.GetCardGroup();
        if (mergedGroup.isEmpty())
        {
            player.hand.group.addAll(fakeHandGroup.group);
            GridCardSelectScreenPatch.Clear();
            Complete();
            return;
        }

        if (origin != null)
        {
            final List<AbstractCard> temp = new ArrayList<>(mergedGroup.group);
            final boolean remove = origin.mode.IsRandom();
            final int max = Math.min(temp.size(), amount);
            for (int i = 0; i < max; i++)
            {
                final AbstractCard card = origin.Get(temp, i, remove);
                if (card != null)
                {
                    selectedCards.add(card);
                }
            }

            selected = true;
            GridCardSelectScreenPatch.Clear();
            Complete(selectedCards);
        }
        else
        {
            if (anyNumber)
            {
                FIX_STS_LIB();
                AbstractDungeon.gridSelectScreen.open(mergedGroup, amount, true, UpdateMessage());
            }
            else
            {
                if (amount > mergedGroup.size())
                {
                    if (canPickLower)
                    {
                        if (amount > 1)
                        {
                            AbstractDungeon.gridSelectScreen.selectedCards.addAll(mergedGroup.group);
                            return;
                        }
                    }
                    else
                    {
                        return;
                    }
                }

                if (canPlayerCancel)
                {
                    // Setting canCancel to true does not ensure the cancel button will be shown...
                    AbstractDungeon.overlayMenu.cancelButton.show(GridCardSelectScreen.TEXT[1]);
                }

                FIX_STS_LIB();
                AbstractDungeon.gridSelectScreen.open(mergedGroup, Math.min(mergedGroup.size(), amount), UpdateMessage(), false, false, canPlayerCancel, false);
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
            selected = true;

            player.hand.group.addAll(fakeHandGroup.group);
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            GridCardSelectScreenPatch.Clear();
        }

        if (selected)
        {
            if (TickDuration(deltaTime))
            {
                Complete(selectedCards);
            }
            return;
        }

        if (AbstractDungeon.screen != AbstractDungeon.CurrentScreen.GRID) // cancelled
        {
            Complete();
        }
    }

    @Override
    protected void Complete()
    {
        if (hideTopPanel)
        {
            GameUtilities.SetTopPanelVisible(true);
        }

        super.Complete();
    }

    private static final FieldInfo<Boolean> _save_isJustForConfirming = JUtils.GetField("save_isJustForConfirming", CenterGridCardSelectScreen.class);
    private static void FIX_STS_LIB()
    {
        _save_isJustForConfirming.Set(null, false);
        CenterGridCardSelectScreen.centerGridSelect = false;
        AbstractDungeon.gridSelectScreen.isJustForConfirming = false;
    }
}
