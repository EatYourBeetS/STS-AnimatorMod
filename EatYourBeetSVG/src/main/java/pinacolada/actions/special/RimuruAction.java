package pinacolada.actions.special;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import eatyourbeets.actions.EYBAction;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.pcl.series.TenseiSlime.Rimuru;
import pinacolada.powers.PCLCombatStats;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLGameUtilities;

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
            PCLCombatStats.onAfterCardPlayed.Unsubscribe(rimuru);
        }
        else
        {
            PCLGameUtilities.RefreshHandLayout();
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
                PCLGameUtilities.Retain(newCopy);
            }

            PCLGameUtilities.ModifyCardTag(newCopy, GR.Enums.CardTags.VOLATILE, true);
            PCLGameUtilities.ModifyAffinityLevel(card, PCLAffinity.Star, 1, false);
            PCLGameUtilities.ChangeCardName(newCopy, rimuru.originalName);
            PCLGameUtilities.CopyVisualProperties(newCopy, copy);

            if (group.type == CardGroup.CardGroupType.HAND)
            {
                newCopy.applyPowers();
            }

            if (newCopy instanceof PCLCard)
            {
                ((PCLCard)newCopy).triggerWhenCreated(false);
            }

            rimuru.copy = newCopy;

            return true;
        }

        return false;
    }
}