package eatyourbeets.actions.special;

import com.megacrit.cardcrawl.cards.AbstractCard;
import eatyourbeets.actions.EYBActionWithCallback;
import eatyourbeets.utilities.GameUtilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ReplaceCard extends EYBActionWithCallback<Map<AbstractCard, AbstractCard>>
{
    protected Map<AbstractCard, AbstractCard> newCards = new HashMap<>();
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

        if (newCards.containsKey(player.cardInUse))
        {
            player.cardInUse = newCards.get(player.cardInUse);
        }

        Complete(newCards);
    }

    protected void Replace(ArrayList<AbstractCard> cards)
    {
        for (int i = 0; i < cards.size(); i++)
        {
            if (cardUUID.equals(cards.get(i).uuid))
            {
                AbstractCard original = cards.get(i);

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

                cards.set(i, replacement);
            }
        }
    }
}
