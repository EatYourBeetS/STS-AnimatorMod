package eatyourbeets.actions.pileSelection;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.cardManipulation.PlayCard;
import eatyourbeets.interfaces.delegates.ActionT1;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

import java.util.ArrayList;

public class PlayFromPile extends SelectFromPile
{
    protected ActionT1<PlayCard> onPlayCard;
    protected AbstractMonster target;
    protected boolean spendEnergy;
    protected boolean autoPlay;
    protected boolean exhaust;
    protected boolean purge;

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

    public PlayFromPile SpendEnergy(boolean spendEnergy, boolean autoPlay)
    {
        this.spendEnergy = spendEnergy;
        this.autoPlay = autoPlay;

        return this;
    }
    public PlayFromPile SetExhaust(boolean exhaust)
    {
        if (this.exhaust = exhaust)
        {
            this.purge = false;
        }

        return this;
    }

    public PlayFromPile SetPurge(boolean purge)
    {
        if (this.purge = purge)
        {
            this.exhaust = false;
        }

        return this;
    }

    @Override
    protected void Complete(ArrayList<AbstractCard> result)
    {
        for (AbstractCard card : result)
        {
            PlayCard action = GameActions.Top.PlayCard(card, target)
            .SpendEnergy(spendEnergy, autoPlay)
            .SetExhaust(exhaust)
            .SetPurge(purge)
            .SetSourcePile(GameUtilities.FindCardGroup(card, false));

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
        return super.UpdateMessageInternal(GR.Common.Strings.GridSelection.ChooseAndPlay);
    }

    @Override
    protected void AddCard(CardGroup group, AbstractCard card)
    {
        super.AddCard(group, card);

        card.calculateCardDamage(target);
    }
}
