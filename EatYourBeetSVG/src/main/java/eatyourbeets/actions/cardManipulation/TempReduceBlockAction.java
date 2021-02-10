package eatyourbeets.actions.cardManipulation;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import eatyourbeets.actions.EYBActionWithCallback;
import eatyourbeets.cards.base.modifiers.BlockModifier;
import eatyourbeets.interfaces.subscribers.OnAfterCardPlayedSubscriber;
import eatyourbeets.powers.CombatStats;

public class TempReduceBlockAction extends EYBActionWithCallback<AbstractCard>
        implements OnAfterCardPlayedSubscriber
{
    protected String sourceName;
    protected boolean firstTimePerTurn = false;
    protected AbstractCard card;

    public TempReduceBlockAction(AbstractCard card, int amount, String name)
    {
        super(ActionType.CARD_MANIPULATION, Settings.ACTION_DUR_FASTER);

        this.card = card;
        this.sourceName = name;

        Initialize(amount);
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

            ReduceBlock(card);

            CombatStats.onAfterCardPlayed.Subscribe(this);
        }
        else
        {
            Complete(null);
        }
    }

    @Override
    protected void UpdateInternal(float deltaTime)
    {
        if (TickDuration(deltaTime))
        {
            Complete(card);
        }
    }

    @Override
    public void OnAfterCardPlayed(AbstractCard other)
    {
        if (card.uuid.equals(other.uuid))
        {
            BlockModifier.For(card).RemoveModifier(sourceName);
        }
    }

    private void ReduceBlock(AbstractCard card)
    {
        BlockModifier.For(card).SetModifier(sourceName, amount);
    }
}
