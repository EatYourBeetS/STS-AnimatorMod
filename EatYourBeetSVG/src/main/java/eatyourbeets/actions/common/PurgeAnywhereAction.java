package eatyourbeets.actions.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.utilities.GameActionsHelper;
import patches.AbstractEnums;

public class PurgeAnywhereAction extends AbstractGameAction
{
    private final int repeat;
    private final AbstractCard card;

    public PurgeAnywhereAction(AbstractCard card)
    {
        this(card, 3);
    }

    public PurgeAnywhereAction(AbstractCard card, int repeat)
    {
        this.card = card;
        this.card.tags.add(AbstractEnums.CardTags.PURGE);
        this.actionType = ActionType.EXHAUST;
        this.duration = Settings.ACTION_DUR_MED;
        this.repeat = repeat;
    }

    public void update()
    {
        AbstractPlayer p = AbstractDungeon.player;

        p.hand.removeCard(card);
        p.limbo.removeCard(card);
        p.drawPile.removeCard(card);
        p.discardPile.removeCard(card);
        p.exhaustPile.removeCard(card);

        this.isDone = true;

        if (repeat > 0)
        {
            GameActionsHelper.AddToBottom(new PurgeAnywhereAction(card, repeat - 1));
        }
    }
}
