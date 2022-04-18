package eatyourbeets.actions.pileSelection;

import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import eatyourbeets.powers.CombatStats;
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
        for (AbstractCard card : result)
        {
            GameActions.Top.MoveCard(card, CombatStats.PurgedCards).ShowEffect(showEffect, realtime);
        }

        super.Complete(result);
    }

    @Override
    public String UpdateMessage()
    {
        return super.UpdateMessageInternal(GR.Common.Strings.GridSelection.Purge(amount));
    }
}
