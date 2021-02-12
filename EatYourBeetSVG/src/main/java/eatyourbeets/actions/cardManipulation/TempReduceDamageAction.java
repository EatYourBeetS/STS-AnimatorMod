package eatyourbeets.actions.cardManipulation;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import eatyourbeets.actions.EYBActionWithCallback;
import eatyourbeets.cards.base.modifiers.DamageModifiers;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class TempReduceDamageAction extends EYBActionWithCallback<AbstractCard>
{
    protected String sourceName;
    protected AbstractCard card;

    public TempReduceDamageAction(AbstractCard card, int amount, String name)
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
            if (card.damage <= 0)
            {
                Complete(card);
                return;
            }

            GameActions.Bottom.ModifyAllInstances(card.uuid, c ->
            {
                if (c.damage > 0)
                {
                    card.superFlash(Color.GOLD.cpy());

                    DamageModifiers.For(c).Add(sourceName, -amount);
                    CombatStats.onAfterCardPlayed.Subscribe(cardPlayed ->
                    {
                        if (cardPlayed == c)
                        {
                            DamageModifiers.For(c).Remove(sourceName);
                        }
                    });
                }
            }).SetDuration(0.01f, false);
        }
        else
        {
            Complete(null);
        }
    }
}
