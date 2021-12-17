package pinacolada.actions.handSelection;

import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import pinacolada.actions.basic.MoveCard;
import pinacolada.powers.PCLCombatStats;
import pinacolada.utilities.PCLActions;

import java.util.ArrayList;

public class DiscardFromHand extends SelectFromHand
{
    protected boolean isReload = false;
    protected boolean realtime = false;
    protected boolean showEffect = false;

    public DiscardFromHand(String sourceName, int amount, boolean isRandom)
    {
        super(ActionType.DISCARD, sourceName, amount, isRandom);
    }

    public DiscardFromHand ShowEffect(boolean showEffect, boolean realtime)
    {
        this.showEffect = showEffect;
        this.realtime = realtime;

        return this;
    }

    public DiscardFromHand SetIsReload(boolean isReload)
    {
        this.isReload = isReload;

        return this;
    }

    @Override
    protected void Complete(ArrayList<AbstractCard> result)
    {
        ArrayList<AbstractCard> modifiedResults = new ArrayList<>();
        for (AbstractCard card : result)
        {
            MoveCard action = new MoveCard(card, player.discardPile);
            if (showEffect)
            {
                PCLActions.Top.Add(action).ShowEffect(showEffect, realtime);
            }
            else
            {
                action.update(); // only once
            }

            modifiedResults.add(card);
            if (isReload) {
                ArrayList<AbstractCard> generatedCards = PCLCombatStats.OnReloadPreDiscard(card);
                modifiedResults.addAll(generatedCards);
            }
        }

        if (isReload) {
            PCLCombatStats.OnReloadPostDiscard(modifiedResults);
        }

        super.Complete(modifiedResults);
    }

    @Override
    public String UpdateMessage()
    {
        return super.UpdateMessageInternal(DiscardAction.TEXT[0]);
    }
}
