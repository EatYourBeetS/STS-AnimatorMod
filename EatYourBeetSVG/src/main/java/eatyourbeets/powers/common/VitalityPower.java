package eatyourbeets.powers.common;

import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.cards.animator.special.Miracle;
import eatyourbeets.powers.CommonPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class VitalityPower extends CommonPower
{
    public static final String POWER_ID = CreateFullID(VitalityPower.class);

    public VitalityPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        this.maxAmount = 12;

        Initialize(amount);
    }

    @Override
    public void updateDescription()
    {
        this.description = FormatDescription(0);
        if (amount > 0)
        {
            this.type = PowerType.BUFF;
        }
        else {
            this.type = PowerType.DEBUFF;
        }
    }

    @Override
    protected void onAmountChanged(int previousAmount, int difference)
    {
        if (difference > 0)
        {
            GameActions.Bottom.MakeCardInDrawPile(new Miracle()).AddCallback(GameUtilities::GiveHaste);
            GameActions.Bottom.MakeCardInDrawPile(new Miracle()).AddCallback(GameUtilities::GiveHaste);
        }

        super.onAmountChanged(previousAmount, difference);
    }
}
