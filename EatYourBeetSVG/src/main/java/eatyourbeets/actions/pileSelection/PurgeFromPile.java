package eatyourbeets.actions.pileSelection;

import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import eatyourbeets.actions.basic.MoveCard;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;

import java.util.ArrayList;

public class PurgeFromPile extends SelectFromPile
{
    protected boolean realtime = false;
    protected boolean showEffect = false;
    protected Vector2 targetPosition;

    public PurgeFromPile(String sourceName, int amount, CardGroup... groups)
    {
        super(ActionType.SPECIAL, sourceName, amount, groups);
    }

    public PurgeFromPile ShowEffect(boolean showEffect, boolean isRealtime)
    {
        this.showEffect = showEffect;
        this.realtime = isRealtime;

        return this;
    }

    @Override
    protected void Complete(ArrayList<AbstractCard> result)
    {
        ArrayList<MoveCard> actions = new ArrayList<>();
        for (AbstractCard card : result)
        {
            MoveCard action = new MoveCard(card, MoveCard.PURGE);
            if (showEffect)
            {
                GameActions.Top.Add(action).ShowEffect(showEffect, realtime);
            }
            else
            {
                actions.add(action);
            }
        }

        super.Complete(result);

        for (MoveCard action : actions)
        {
            action.update(); // only once
        }
    }

    @Override
    public String UpdateMessage()
    {
        return super.UpdateMessageInternal(GR.Common.Strings.GridSelection.Purge(amount));
    }
}
