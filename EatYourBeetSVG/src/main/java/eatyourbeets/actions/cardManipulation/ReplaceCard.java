package eatyourbeets.actions.cardManipulation;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.actions.EYBActionWithCallback;
import eatyourbeets.utilities.FieldInfo;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ReplaceCard extends EYBActionWithCallback<Map<AbstractCard, AbstractCard>>
{
    protected final static FieldInfo<AbstractCard> _targetCard = JUtils.GetField("targetCard", UseCardAction.class);
    protected final Map<AbstractCard, AbstractCard> newCards = new HashMap<>();
    protected boolean upgrade;
    protected UUID cardUUID;

    public ReplaceCard(UUID cardUUID, AbstractCard replacement)
    {
        super(ActionType.CARD_MANIPULATION);

        this.cardUUID = cardUUID;
        this.card = replacement;

        Initialize(1);
    }

    public ReplaceCard SetUpgrade(boolean upgrade)
    {
        this.upgrade = upgrade;

        return this;
    }

    @Override
    protected void FirstUpdate()
    {
        Replace(player.limbo.group);
        Replace(player.exhaustPile.group);
        Replace(player.discardPile.group);
        Replace(player.drawPile.group);
        Replace(player.hand.group);

        for (int i = 0; i < AbstractDungeon.actionManager.actions.size(); i++)
        {
            AbstractGameAction action = AbstractDungeon.actionManager.actions.get(i);
            if (action instanceof UseCardAction)
            {
                AbstractCard card = _targetCard.Get(action);
                if (newCards.containsKey(card) || cardUUID.equals(card.uuid))
                {
                    _targetCard.Set(action, Replace(card));
                }
            }
        }

        if (player.cardInUse != null && (newCards.containsKey(player.cardInUse) || cardUUID.equals(player.cardInUse.uuid)))
        {
            player.cardInUse = Replace(player.cardInUse);
        }

        Complete(newCards);
    }

    protected void Replace(ArrayList<AbstractCard> cards)
    {
        for (int i = 0; i < cards.size(); i++)
        {
            AbstractCard original = cards.get(i);
            if (cardUUID.equals(original.uuid))
            {
                cards.set(i, Replace(original));
            }
        }
    }

    protected AbstractCard Replace(AbstractCard original)
    {
        AbstractCard replacement;
        if (newCards.containsKey(original))
        {
            replacement = newCards.get(original);
        }
        else
        {
            replacement = card.makeStatEquivalentCopy();
            replacement.uuid = original.uuid;

            if (upgrade)
            {
                replacement.upgrade();
            }

            GameUtilities.CopyVisualProperties(replacement, original);
            newCards.put(original, replacement);
        }

        return replacement;
    }
}
