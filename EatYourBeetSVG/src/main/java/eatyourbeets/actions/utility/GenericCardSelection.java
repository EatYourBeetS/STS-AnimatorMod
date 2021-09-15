package eatyourbeets.actions.utility;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import eatyourbeets.actions.EYBActionWithCallback;
import eatyourbeets.interfaces.delegates.FuncT1;
import eatyourbeets.interfaces.delegates.FuncT2;
import eatyourbeets.utilities.CardSelection;
import eatyourbeets.utilities.GenericCondition;
import eatyourbeets.utilities.ListSelection;

import java.util.ArrayList;

public class GenericCardSelection extends EYBActionWithCallback<ArrayList<AbstractCard>>
{
    protected ArrayList<AbstractCard> result = new ArrayList<>();
    protected GenericCondition<AbstractCard> filter;
    protected ListSelection<AbstractCard> selection;
    protected AbstractCard card;
    protected CardGroup group;
    protected boolean forceSelect;

    protected GenericCardSelection(AbstractCard card, CardGroup group, int amount)
    {
        super(ActionType.CARD_MANIPULATION);

        this.card = card;
        this.group = group;

        Initialize(amount);
    }

    public GenericCardSelection(CardGroup group, int amount)
    {
        this(null, group, amount);
    }

    public GenericCardSelection(AbstractCard card)
    {
        this(card, null, 1);
    }

    public GenericCardSelection ForceSelect(boolean forceSelect)
    {
        this.forceSelect = forceSelect;

        return this;
    }

    public GenericCardSelection SetSelection(ListSelection<AbstractCard> selection)
    {
        this.selection = selection;

        return this;
    }

    public GenericCardSelection SetFilter(FuncT1<Boolean, AbstractCard> filter)
    {
        this.filter = GenericCondition.FromT1(filter);

        return this;
    }

    public <S> GenericCardSelection SetFilter(S state, FuncT2<Boolean, S, AbstractCard> filter)
    {
        this.filter = GenericCondition.FromT2(filter, state);

        return this;
    }

    @Override
    protected void FirstUpdate()
    {
        if (card != null)
        {
            if (forceSelect || CanSelect(card))
            {
                SelectCard(card);
            }

            Complete(result);
            return;
        }

        if (group == null)
        {
            Complete(null);
            return;
        }

        final ArrayList<AbstractCard> list = new ArrayList<>();
        for (AbstractCard card : group.group)
        {
            if (forceSelect || CanSelect(card))
            {
                list.add(card);
            }
        }

        if (selection == null)
        {
            selection = CardSelection.Default;
        }

        selection.ForEach(list, amount, this::SelectCard);
        Complete(result);
    }

    protected boolean CanSelect(AbstractCard card)
    {
        return filter == null || filter.Check(card);
    }

    protected void SelectCard(AbstractCard card)
    {
        result.add(card);
    }
}