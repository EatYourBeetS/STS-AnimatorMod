package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.cards.animator.ThrowingKnife;
import eatyourbeets.utilities.GameActionsHelper;

public class SonicPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(SonicPower.class.getSimpleName());

    public SonicPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        this.amount = amount;

        updateDescription();
    }

    @Override
    public void atStartOfTurn()
    {
        super.atStartOfTurn();

        for (int i = 0; i < amount; i++)
        {
            GameActionsHelper.MakeCardInHand(ThrowingKnife.GetRandomCard(), 1, false);
        }
    }
}
