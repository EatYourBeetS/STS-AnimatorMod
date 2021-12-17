package pinacolada.actions.basic;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.powers.NoDrawPower;
import eatyourbeets.actions.EYBActionWithCallback;
import eatyourbeets.interfaces.delegates.FuncT1;
import eatyourbeets.interfaces.delegates.FuncT2;
import eatyourbeets.utilities.GenericCondition;
import pinacolada.utilities.PCLActions;

import java.util.ArrayList;

public class DrawCards extends EYBActionWithCallback<ArrayList<AbstractCard>>
{
    protected final ArrayList<AbstractCard> cards = new ArrayList<>();
    protected GenericCondition<AbstractCard> filter;
    protected boolean canDrawUnfiltered;
    protected boolean shuffleIfEmpty = true;

    protected DrawCards(DrawCards other, int amount)
    {
        this(amount);

        shuffleIfEmpty = other.shuffleIfEmpty;
        filter = other.filter;
        canDrawUnfiltered = other.canDrawUnfiltered;
        callbacks.addAll(other.callbacks);
        cards.addAll(other.cards);
    }

    public DrawCards(int amount)
    {
        super(ActionType.DRAW);

        Initialize(amount);
    }

    public DrawCards SetFilter(FuncT1<Boolean, AbstractCard> filter, boolean canDrawUnfiltered)
    {
        this.filter = GenericCondition.FromT1(filter);
        this.canDrawUnfiltered = canDrawUnfiltered;

        return this;
    }

    public <S> DrawCards SetFilter(S state, FuncT2<Boolean, S, AbstractCard> filter, boolean canDrawUnfiltered)
    {
        this.filter = GenericCondition.FromT2(filter, state);
        this.canDrawUnfiltered = canDrawUnfiltered;

        return this;
    }

    public DrawCards ShuffleIfEmpty(boolean shuffleIfEmpty)
    {
        this.shuffleIfEmpty = shuffleIfEmpty;

        return this;
    }

    @Override
    protected void FirstUpdate()
    {
        if (player.hasPower(NoDrawPower.POWER_ID))
        {
            player.getPower(NoDrawPower.POWER_ID).flash();
            Complete(cards);
            return;
        }

        if (amount == 0)
        {
            Complete(cards);
            return;
        }

        if (player.drawPile.isEmpty())
        {
            if (shuffleIfEmpty && !player.discardPile.isEmpty())
            {
                PCLActions.Top.Sequential(
                    new EmptyDeckShuffleAction(),
                    new DrawCards(this, amount)
                );

                Complete(); // Do not trigger callback
            }
            else
            {
                Complete(cards);
            }
        }
        else if (player.hand.size() >= BaseMod.MAX_HAND_SIZE)
        {
            player.createHandIsFullDialog();
            Complete(cards);
        }
        else
        {
            if (filter != null)
            {
                AbstractCard filtered = null;
                for (AbstractCard card : player.drawPile.group)
                {
                    if (filter.Check(card))
                    {
                        filtered = card;
                        break;
                    }
                }

                if (filtered != null)
                {
                    player.drawPile.removeCard(filtered);
                    player.drawPile.addToTop(filtered);
                }
                else if (!canDrawUnfiltered)
                {
                    Complete(cards);
                    return;
                }
            }

            cards.add(player.drawPile.getTopCard());

            PCLActions.Top.Sequential(
                new DrawCardAction(source, 1, false),
                new DrawCards(this, amount - 1)
            );

            Complete(); // Do not trigger callback
        }
    }
}
