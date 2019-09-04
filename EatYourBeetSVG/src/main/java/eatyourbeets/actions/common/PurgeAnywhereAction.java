package eatyourbeets.actions.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.utilities.GameActionsHelper;
import patches.AbstractEnums;

import java.util.UUID;

public class PurgeAnywhereAction extends AbstractGameAction
{
    private final int repeat;
    private final AbstractCard card;
    private final UUID uuid;

    public PurgeAnywhereAction(AbstractCard card)
    {
        this(card, null, 3);
    }

    public PurgeAnywhereAction(UUID uuid)
    {
        this(null, uuid, 3);
    }

    public PurgeAnywhereAction(AbstractCard card, UUID uuid, int repeat)
    {
        this.card = card;
        this.uuid = uuid;
        this.card.tags.add(AbstractEnums.CardTags.PURGING);
        this.actionType = ActionType.EXHAUST;
        this.duration = Settings.ACTION_DUR_MED;
        this.repeat = repeat;
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
        }

        if (uuid != null)
        {
            RemoveAll(p.hand);
            RemoveAll(p.limbo);
            RemoveAll(p.drawPile);
            RemoveAll(p.discardPile);
            RemoveAll(p.exhaustPile);
        }

        this.isDone = true;

        if (repeat > 0)
        {
            GameActionsHelper.AddToBottom(new PurgeAnywhereAction(card, uuid, repeat - 1));
        }
    }

    private void RemoveAll(CardGroup group)
    {
        group.group.removeIf((c -> c.uuid.equals(uuid)));
    }
}
