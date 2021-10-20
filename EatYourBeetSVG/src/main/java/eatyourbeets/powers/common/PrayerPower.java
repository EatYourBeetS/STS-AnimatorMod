package eatyourbeets.powers.common;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import eatyourbeets.cards.animator.special.Miracle;
import eatyourbeets.powers.CommonPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class PrayerPower extends CommonPower
{
    public static final String POWER_ID = CreateFullID(PrayerPower.class);

    public PrayerPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

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
            GameActions.Bottom.MakeCardInDrawPile(new Miracle()).AddCallback(GameUtilities::GiveHaste).SetDuration(Settings.ACTION_DUR_XFAST, true);
            GameActions.Bottom.MakeCardInDrawPile(new Miracle()).AddCallback(GameUtilities::GiveHaste).SetDuration(Settings.ACTION_DUR_XFAST, true);
        }

        super.onAmountChanged(previousAmount, difference);
    }
}
