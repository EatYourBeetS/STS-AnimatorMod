package eatyourbeets.actions.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.actions.EYBActionWithCallback;
import eatyourbeets.powers.CombatStats;

public class ApplyAmountToOrbs extends EYBActionWithCallback<AbstractCard>
{
    private String orbType;

    public ApplyAmountToOrbs(String orbType, int amount)
    {
        super(ActionType.CARD_MANIPULATION);
        this.orbType = orbType;
        Initialize(amount);
    }

    @Override
    protected void FirstUpdate()
    {
        CombatStats.AddAmountIncreasedOnOrbs(orbType, amount);

        for (AbstractOrb orb : player.orbs)
        {
            if (orbType.equals(orb.ID))
            {
                orb.updateDescription();
            }
        }

        Complete();
    }
}
