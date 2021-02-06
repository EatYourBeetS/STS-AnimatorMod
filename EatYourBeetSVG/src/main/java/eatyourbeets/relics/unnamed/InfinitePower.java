package eatyourbeets.relics.unnamed;

import eatyourbeets.relics.UnnamedRelic;
import eatyourbeets.utilities.JUtils;

public class InfinitePower extends UnnamedRelic
{
    public static final String ID = CreateFullID(InfinitePower.class);

    private static final int TEMP_HP_AMOUNT = 6;
    private static final int HEAL_AMOUNT = 2;

    public InfinitePower()
    {
        super(ID, RelicTier.STARTER, LandingSound.MAGICAL);
    }

    @Override
    public String getUpdatedDescription()
    {
        return JUtils.Format(DESCRIPTIONS[0], TEMP_HP_AMOUNT, HEAL_AMOUNT);
    }
}