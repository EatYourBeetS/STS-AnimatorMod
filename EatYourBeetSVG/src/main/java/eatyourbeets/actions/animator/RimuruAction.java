package eatyourbeets.actions.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import eatyourbeets.actions.EYBAction;
import eatyourbeets.cards.animator.series.TenseiSlime.Rimuru;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameUtilities;

public class RimuruAction extends EYBAction
{
    private final AbstractCard copy;
    private final AbstractCard newCopy;
    private final Rimuru rimuru;

    public RimuruAction(Rimuru rimuru, AbstractCard card)
    {
        super(ActionType.CARD_MANIPULATION);

        this.card = card;
        this.newCopy = card.makeStatEquivalentCopy();
        this.copy = rimuru.copy;
        this.rimuru = rimuru;

        Initialize(1);
    }

    @Override
    protected void FirstUpdate()
    {
        if (!transform(player.hand, card) && !transform(player.drawPile, card) && !transform(player.discardPile, card)
        && !transform(player.exhaustPile, card) && !transform(player.limbo, card))
        {
            // Rimuru has been purged or removed from the game, unsubscribe it
            CombatStats.onAfterCardPlayed.Unsubscribe(rimuru);
        }
        else
        {
            GameUtilities.RefreshHandLayout();
        }

        Complete();
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private boolean transform(CardGroup group, AbstractCard card)
    {
        int index = group.group.indexOf(rimuru.copy);
        if (index >= 0)
        {
            group.group.remove(index);
            group.group.add(index, newCopy);

            if (rimuru.upgraded || copy.retain)
            {
                GameUtilities.Retain(newCopy);
            }

            newCopy.tags.add(GR.Enums.CardTags.TEMPORARY);
            GameUtilities.ChangeCardName(newCopy, rimuru.originalName);
            GameUtilities.CopyVisualProperties(newCopy, copy);

            if (group.type == CardGroup.CardGroupType.HAND)
            {
                newCopy.applyPowers();
            }

            if (newCopy instanceof EYBCard)
            {
                ((EYBCard)newCopy).triggerWhenCreated(false);
            }

            rimuru.copy = newCopy;
            rimuru.copy.uuid = rimuru.uuid;

            return true;
        }

        return false;
    }
}