package eatyourbeets.actions.cardManipulation;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.actions.EYBActionWithCallback;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.utilities.FieldInfo;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ReplaceCard extends EYBActionWithCallback<Map<AbstractCard, AbstractCard>>
{
    protected final static FieldInfo<AbstractCard> _targetCard = JUtils.GetField("targetCard", UseCardAction.class);
    protected final Map<AbstractCard, AbstractCard> newCards = new HashMap<>();
    protected boolean triggerEvents;
    protected boolean upgrade;
    protected UUID cardUUID;

    public ReplaceCard(UUID cardUUID, AbstractCard replacement)
    {
        super(ActionType.CARD_MANIPULATION);

        this.triggerEvents = true;
        this.cardUUID = cardUUID;
        this.card = replacement;

        Initialize(1);
    }

    public ReplaceCard TriggerEvents(boolean triggerEvents)
    {
        this.triggerEvents = triggerEvents;

        return this;
    }

    public ReplaceCard SetUpgrade(boolean upgrade)
    {
        this.upgrade = upgrade;

        return this;
    }

    @Override
    protected void FirstUpdate()
    {
        Replace(player.limbo);
        Replace(player.exhaustPile);
        Replace(player.discardPile);
        Replace(player.drawPile);
        Replace(player.hand);

        for (int i = 0; i < AbstractDungeon.actionManager.actions.size(); i++)
        {
            final AbstractGameAction action = AbstractDungeon.actionManager.actions.get(i);
            if (action instanceof UseCardAction)
            {
                final AbstractCard card = _targetCard.Get(action);
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

    protected void Replace(CardGroup group)
    {
        for (int i = 0; i < group.group.size(); i++)
        {
            final AbstractCard original = group.group.get(i);
            if (cardUUID.equals(original.uuid))
            {
                final AbstractCard replacement = Replace(original);
                group.group.set(i, replacement);

                if (group.type == CardGroup.CardGroupType.HAND)
                {
                    replacement.triggerWhenDrawn();
                }
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

            if (triggerEvents && replacement instanceof EYBCard)
            {
                ((EYBCard)replacement).triggerWhenCreated(false);
            }

            GameUtilities.CopyVisualProperties(replacement, original);
            newCards.put(original, replacement);
        }

        return replacement;
    }
}
