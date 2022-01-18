package pinacolada.actions.pileSelection;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import eatyourbeets.interfaces.delegates.ActionT1;
import pinacolada.actions.cardManipulation.PlayCard;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

import java.util.ArrayList;

public class PlayFromPile extends SelectFromPile
{
    protected ActionT1<PlayCard> onPlayCard;
    protected AbstractMonster target;

    public PlayFromPile(String sourceName, int amount, CardGroup... groups)
    {
        this(sourceName, null, amount, groups);
    }

    public PlayFromPile(String sourceName, AbstractMonster target, int amount, CardGroup... groups)
    {
        super(ActionType.CARD_MANIPULATION, sourceName, amount, groups);

        this.target = target;
    }

    public PlayFromPile OnPlayCard(ActionT1<PlayCard> onPlayCard)
    {
        this.onPlayCard = onPlayCard;

        return this;
    }

    @Override
    protected void Complete(ArrayList<AbstractCard> result)
    {
        for (AbstractCard card : result)
        {
            PlayCard action = PCLActions.Top.PlayCard(card, target)
            .SetSourcePile(PCLGameUtilities.FindCardGroup(card, false));

            if (onPlayCard != null)
            {
                onPlayCard.Invoke(action);
            }
        }

        super.Complete(result);
    }

    @Override
    protected void Complete()
    {
        super.Complete();

        for (CardGroup group : groups)
        {
            if (group.type != CardGroup.CardGroupType.HAND)
            {
                for (AbstractCard card : group.group)
                {
                    card.resetAttributes();
                }
            }
        }
    }

    @Override
    public String UpdateMessage()
    {
        return super.UpdateMessageInternal(CardRewardScreen.TEXT[0]);
    }

    @Override
    protected void AddCard(CardGroup group, AbstractCard card)
    {
        super.AddCard(group, card);

        card.calculateCardDamage(target);
    }
}
