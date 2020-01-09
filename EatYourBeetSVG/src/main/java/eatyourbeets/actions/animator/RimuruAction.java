package eatyourbeets.actions.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import eatyourbeets.actions.EYBAction;
import eatyourbeets.cards.animator.series.TenseiSlime.Rimuru;
import eatyourbeets.powers.PlayerStatistics;
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
        if (!transform(player.hand, card) && !transform(player.drawPile, card) && !transform(player.discardPile, card) &&
            !transform(player.exhaustPile, card) && !transform(player.limbo, card))
        {
            // Rimuru has been purged or removed from the game, unsubscribe it
            PlayerStatistics.onAfterCardPlayed.Unsubscribe(rimuru);

            newCopy.name = newCopy.originalName;
            if (rimuru.timesUpgraded > 0)
            {
                newCopy.name += "+" + rimuru.timesUpgraded;
            }
            else if (rimuru.upgraded)
            {
                newCopy.name += "+";
            }
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
                newCopy.retain = true;
            }

            newCopy.tags.add(GR.Enums.CardTags.TEMPORARY);
            newCopy.name = rimuru.name;

            if (group.type == CardGroup.CardGroupType.HAND)
            {
                newCopy.current_x = copy.current_x;
                newCopy.current_y = copy.current_y;
                newCopy.target_x  = copy.target_x;
                newCopy.target_y  = copy.target_y;
                newCopy.drawScale = copy.drawScale;
                newCopy.angle     = copy.angle;
                newCopy.applyPowers();
            }

            rimuru.copy = newCopy;

            return true;
        }

        return false;
    }
}