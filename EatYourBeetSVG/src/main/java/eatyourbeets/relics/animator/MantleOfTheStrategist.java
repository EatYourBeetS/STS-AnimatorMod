package eatyourbeets.relics.animator;

import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.utilities.GameActions;

public class MantleOfTheStrategist extends AnimatorRelic
{
    public static final String ID = CreateFullID(MantleOfTheStrategist.class);
    public static int INSPIRATION_AMOUNT = 2;

    public MantleOfTheStrategist()
    {
        super(ID, RelicTier.RARE, LandingSound.FLAT);
    }

    @Override
    public String getUpdatedDescription()
    {
        return FormatDescription(0, INSPIRATION_AMOUNT);
    }

    @Override
    protected void ActivateBattleEffect()
    {
        GameActions.Bottom.GainInspiration(INSPIRATION_AMOUNT);
        this.flash();
    }
}