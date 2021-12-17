package pinacolada.actions.handSelection;

import com.megacrit.cardcrawl.actions.unique.GamblingChipAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import pinacolada.utilities.PCLActions;

import java.util.ArrayList;

public class CycleCards extends DiscardFromHand
{
    public boolean drawInstantly = false;

    public CycleCards(String sourceName, int amount, boolean isRandom)
    {
        super(sourceName, amount, isRandom);
    }

    public CycleCards DrawInstantly(boolean value)
    {
        drawInstantly = value;
        
        return this;
    }

    @Override
    protected void Complete(ArrayList<AbstractCard> result)
    {
        if (drawInstantly)
        {
            PCLActions.Top.Draw(result.size());
        }
        else
        {
            PCLActions.Bottom.Draw(result.size());
        }

        super.Complete(result);
    }

    @Override
    public String UpdateMessage()
    {
        return super.UpdateMessageInternal(GamblingChipAction.TEXT[1]);
    }
}
