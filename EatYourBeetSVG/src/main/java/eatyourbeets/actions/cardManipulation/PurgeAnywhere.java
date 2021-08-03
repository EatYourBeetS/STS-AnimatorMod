package eatyourbeets.actions.cardManipulation;

import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import eatyourbeets.actions.EYBActionWithCallback;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

import java.util.ArrayList;
import java.util.UUID;

public class PurgeAnywhere extends EYBActionWithCallback<Boolean>
{
    protected final UUID uuid;
    protected boolean showEffect;
    protected boolean purged;

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

    public PurgeAnywhere ShowEffect(boolean value)
    {
        this.showEffect = value;

        return this;
    }

    @Override
    protected void FirstUpdate()
    {
        super.FirstUpdate();

        final ArrayList<CardGroup> groups = new ArrayList<>();
        groups.add(player.hand);
        groups.add(player.limbo);
        groups.add(player.drawPile);
        groups.add(player.discardPile);
        groups.add(player.exhaustPile);

        if (card != null)
        {
            boolean queueEffect = showEffect;
            for (CardGroup group : groups)
            {
                if (group.contains(card))
                {
                    group.removeCard(card);
                    this.purged = true;

                    if (queueEffect)
                    {
                        PurgeEffect(group, card);
                        queueEffect = false;
                    }
                }
            }
        }

        if (uuid != null)
        {
            for (CardGroup group : groups)
            {
                RemoveAll(group);
            }
        }

        if (amount > 0)
        {
            GameActions.Bottom.Add(new PurgeAnywhere(card, uuid, amount - 1).ShowEffect(showEffect));
        }

        Complete(purged);
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
            this.purged = true;

            if (showEffect)
            {
                PurgeEffect(group, c);
            }
        }
    }

    private void PurgeEffect(CardGroup group, AbstractCard c)
    {
        final Vector2 pos = GameUtilities.TryGetPosition(group);
        if (pos != null)
        {
            GameEffects.List.Add(new PurgeCardEffect(c, pos.x, pos.y));
        }
        else
        {
            GameEffects.List.Add(new PurgeCardEffect(c));
        }
    }
}
