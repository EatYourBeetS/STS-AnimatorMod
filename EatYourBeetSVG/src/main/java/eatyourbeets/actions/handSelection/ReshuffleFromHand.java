package eatyourbeets.actions.handSelection;

import com.megacrit.cardcrawl.cards.AbstractCard;
import eatyourbeets.actions.special.RefreshHandLayout;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;

import java.util.ArrayList;

public class ReshuffleFromHand extends SelectFromHand
{
    protected boolean realtime = false;
    protected boolean showEffect = false;

    public ReshuffleFromHand(String sourceName, int amount, boolean isRandom)
    {
        super(ActionType.CARD_MANIPULATION, sourceName, amount, isRandom);
    }

    public ReshuffleFromHand ShowEffect(boolean showEffect, boolean realtime)
    {
        this.showEffect = showEffect;
        this.realtime = realtime;

        return this;
    }

    @Override
    protected void Complete(ArrayList<AbstractCard> result)
    {
        for (AbstractCard card : result)
        {
            GameActions.Top.Reshuffle(card, player.hand).ShowEffect(showEffect, realtime);
        }

        GameActions.Bottom.Add(new RefreshHandLayout());

        super.Complete(result);
    }

    @Override
    public String UpdateMessage()
    {
        return super.UpdateMessageInternal(GR.Common.Strings.HandSelection.MoveToDrawPile);
    }
}
