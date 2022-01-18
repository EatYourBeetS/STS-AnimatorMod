package pinacolada.actions.cardManipulation;

import com.megacrit.cardcrawl.cards.AbstractCard;
import eatyourbeets.actions.EYBActionWithCallback;
import eatyourbeets.interfaces.delegates.ActionT1;
import eatyourbeets.interfaces.delegates.ActionT2;
import pinacolada.utilities.PCLGameUtilities;

public class ModifyAllCopies extends EYBActionWithCallback<AbstractCard>
{
    protected final String cardID;
    protected boolean includeMasterDeck;

    public ModifyAllCopies(String cardID, ActionT1<AbstractCard> onCompletion)
    {
        this(cardID);

        AddCallback(onCompletion);
    }

    public <S> ModifyAllCopies(String cardID, S state, ActionT2<S, AbstractCard> onCompletion)
    {
        this(cardID);

        AddCallback(state, onCompletion);
    }

    public ModifyAllCopies(String cardID)
    {
        super(ActionType.CARD_MANIPULATION);

        this.cardID = cardID;
 
        Initialize(1);
    }

    public ModifyAllCopies IncludeMasterDeck(boolean includeMasterDeck)
    {
        this.includeMasterDeck = includeMasterDeck;

        return this;
    }

    @Override
    protected void FirstUpdate()
    {
        if (includeMasterDeck)
        {
            for (AbstractCard card : PCLGameUtilities.GetAllCopies(cardID))
            {
                Complete(card);
            }
        }
        else
        {
            for (AbstractCard card : PCLGameUtilities.GetAllInBattleCopies(cardID))
            {
                Complete(card);
            }
        }
    }
}
