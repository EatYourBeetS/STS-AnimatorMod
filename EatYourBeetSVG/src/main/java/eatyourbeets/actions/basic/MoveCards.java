package eatyourbeets.actions.basic;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import eatyourbeets.actions.EYBActionWithCallback;
import eatyourbeets.interfaces.delegates.FuncT1;
import eatyourbeets.interfaces.delegates.FuncT2;
import eatyourbeets.utilities.CardSelection;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GenericCondition;

import java.util.ArrayList;

public class MoveCards extends EYBActionWithCallback<ArrayList<AbstractCard>>
{
    protected ArrayList<AbstractCard> selectedCards = new ArrayList<>();
    protected GenericCondition<AbstractCard> filter;
    protected CardSelection destination;
    protected CardSelection origin;
    protected CardGroup targetPile;
    protected CardGroup sourcePile;
    protected boolean showEffect;
    protected boolean realtime;
    protected float effectDuration;

    public MoveCards(CardGroup targetPile, CardGroup sourcePile)
    {
        this(targetPile, sourcePile, -1);
    }

    public MoveCards(CardGroup targetPile, CardGroup sourcePile, int amount)
    {
        super(ActionType.CARD_MANIPULATION);

        this.destination = null;
        this.origin = CardSelection.Top;
        this.targetPile = targetPile;
        this.sourcePile = sourcePile;

        Initialize(amount);
    }

    public MoveCards ShowEffect(boolean showEffect, boolean isRealtime)
    {
        float duration = showEffect ? Settings.ACTION_DUR_MED : Settings.ACTION_DUR_FAST;

        if (Settings.FAST_MODE)
        {
            duration *= 0.7f;
        }

        return ShowEffect(showEffect, isRealtime, duration);
    }

    public MoveCards ShowEffect(boolean showEffect, boolean isRealtime, float effectDuration)
    {
        this.showEffect = showEffect;
        this.realtime = isRealtime;
        this.effectDuration = effectDuration;

        return this;
    }

    public MoveCards SetOrigin(CardSelection origin)
    {
        this.origin = (origin != null ? origin : CardSelection.Top);

        return this;
    }

    public MoveCards SetDestination(CardSelection destination)
    {
        this.destination = destination;

        return this;
    }

    public MoveCards SetFilter(FuncT1<Boolean, AbstractCard> filter)
    {
        this.filter = GenericCondition.FromT1(filter);

        return this;
    }

    public <S> MoveCards SetFilter(S state, FuncT2<Boolean, S, AbstractCard> filter)
    {
        this.filter = GenericCondition.FromT2(filter, state);

        return this;
    }

    @Override
    protected void FirstUpdate()
    {
        ArrayList<AbstractCard> temp = new ArrayList<>();
        for (AbstractCard card : sourcePile.group)
        {
            if (filter == null || filter.Check(card))
            {
                temp.add(card);
            }
        }

        int max = amount;
        if (amount == -1 || temp.size() < amount)
        {
            max = temp.size();
        }

        boolean remove = origin.mode.IsRandom();
        for (int i = 0; i < max; i++)
        {
            AbstractCard card = origin.GetCard(temp, i, remove);
            if (card != null)
            {
                MoveCard(card);
            }
        }

        Complete(selectedCards);
    }

    private void MoveCard(AbstractCard card)
    {
        selectedCards.add(card);
        GameActions.Top.MoveCard(card, sourcePile, targetPile)
        .ShowEffect(showEffect, realtime, effectDuration)
        .SetDestination(destination);
    }
}
