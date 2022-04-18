package eatyourbeets.actions.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import eatyourbeets.actions.EYBAction;
import eatyourbeets.cards.animator.series.TenseiSlime.Rimuru;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameUtilities;

import java.util.UUID;

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
        if (!TryReplace(player.hand) && !TryReplace(player.drawPile) && !TryReplace(player.discardPile)
        && !TryReplace(player.exhaustPile) && !TryReplace(player.limbo) && !TryReplace(CombatStats.PurgedCards))
        {
            CombatStats.onAfterCardPlayed.Unsubscribe(rimuru);
        }
        else
        {
            GameUtilities.RefreshHandLayout();
        }

        Complete();
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private boolean TryReplace(CardGroup group)
    {
        final UUID copyUUID = rimuru.copy.uuid;
        for (int i = 0; i < group.size(); i++)
        {
            if (group.group.get(i).uuid.equals(copyUUID))
            {
                group.group.set(i, newCopy);

                GameUtilities.SetCardTag(newCopy, EYBCard.VOLATILE, true);
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
        }

        return false;
    }
}