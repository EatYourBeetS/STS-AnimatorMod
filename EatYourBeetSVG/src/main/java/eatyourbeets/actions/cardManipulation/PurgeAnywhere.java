package eatyourbeets.actions.cardManipulation;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import eatyourbeets.actions.EYBAction;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;

import java.util.ArrayList;
import java.util.UUID;

public class PurgeAnywhere extends EYBAction
{
    protected final UUID uuid;

    public PurgeAnywhere(AbstractCard card)
    {
        this(card, null, 3);
    }

    public PurgeAnywhere(UUID uuid)
    {
        this(null, uuid, 3);
    }

    public PurgeAnywhere(AbstractCard card, UUID uuid, int repeat)
    {
        super(ActionType.CARD_MANIPULATION);

        this.uuid = uuid;
        this.card = card;
        if (this.card != null)
        {
            this.card.tags.add(GR.Enums.CardTags.PURGING);
        }

        Initialize(repeat);
    }

    public void update()
    {
        if (card != null)
        {
            player.hand.removeCard(card);
            player.limbo.removeCard(card);
            player.drawPile.removeCard(card);
            player.discardPile.removeCard(card);
            player.exhaustPile.removeCard(card);

            PlayerStatistics.Void.removeCard(card);
        }

        if (uuid != null)
        {
            RemoveAll(player.hand);
            RemoveAll(player.limbo);
            RemoveAll(player.drawPile);
            RemoveAll(player.discardPile);
            RemoveAll(player.exhaustPile);

            RemoveAll(PlayerStatistics.Void);
        }

        if (amount > 0)
        {
            GameActions.Bottom.Add(new PurgeAnywhere(card, uuid, amount - 1));
        }

        Complete();
    }

    private void RemoveAll(CardGroup group)
    {
        ArrayList<AbstractCard> toRemove = new ArrayList<>();
        for (AbstractCard c : group.group)
        {
            if (c.uuid.equals(uuid))
            {
                toRemove.add(c);
            }
        }

        for (AbstractCard c : toRemove)
        {
            group.removeCard(c);
        }
    }
}
