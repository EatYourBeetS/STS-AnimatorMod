package eatyourbeets.actions.cardManipulation;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.actions.EYBAction;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.utilities.GameActions;
import patches.AbstractEnums;

import java.util.ArrayList;
import java.util.UUID;

public class PurgeAnywhere extends EYBAction
{
    protected UUID uuid;

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
            this.card.tags.add(AbstractEnums.CardTags.PURGING);
        }

        Initialize(repeat);
    }

    public void update()
    {
        AbstractPlayer p = AbstractDungeon.player;

        if (card != null)
        {
            p.hand.removeCard(card);
            p.limbo.removeCard(card);
            p.drawPile.removeCard(card);
            p.discardPile.removeCard(card);
            p.exhaustPile.removeCard(card);

            PlayerStatistics.Void.removeCard(card);
        }

        if (uuid != null)
        {
            RemoveAll(p.hand);
            RemoveAll(p.limbo);
            RemoveAll(p.drawPile);
            RemoveAll(p.discardPile);
            RemoveAll(p.exhaustPile);

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
