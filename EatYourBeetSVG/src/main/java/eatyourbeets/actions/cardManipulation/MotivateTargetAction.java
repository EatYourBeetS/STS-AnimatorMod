package eatyourbeets.actions.cardManipulation;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import eatyourbeets.actions.EYBActionWithCallback;
import eatyourbeets.cards.base.modifiers.CostModifier;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class MotivateTargetAction extends EYBActionWithCallback<AbstractCard>
{
    protected String sourceName;
    protected AbstractCard card;

    public MotivateTargetAction(AbstractCard card, String name)
    {
        super(ActionType.CARD_MANIPULATION, Settings.ACTION_DUR_FASTER);

        this.card = card;
        this.sourceName = name;

        Initialize(1);
    }

    @Override
    protected void FirstUpdate()
    {
        if (card != null)
        {
            if (card.costForTurn > 0)
            {
                card.superFlash(Color.GOLD.cpy());
            }
            else
            {
                Complete(card);
            }

            GameActions.Bottom.ModifyAllInstances(card.uuid, c ->
            {
                CostModifier.For(c).AddModifier(sourceName, amount);
                CombatStats.onAfterCardPlayed.Subscribe(cardPlayed ->
                {
                    if (cardPlayed == c)
                    {
                        CostModifier.For(c).RemoveModifier(sourceName);
                    }
                });
            });
        }
        else
        {
            Complete(null);
        }
    }
}
