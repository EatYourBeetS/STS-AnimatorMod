package eatyourbeets.relics.animator;

import eatyourbeets.powers.common.InspirationPower;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class MantleOfTheStrategist extends AnimatorRelic
{
    public static final String ID = CreateFullID(MantleOfTheStrategist.class);
    public static int INSPIRATION_AMOUNT = 5;

    public MantleOfTheStrategist()
    {
        super(ID, RelicTier.UNCOMMON, LandingSound.FLAT);
    }

    @Override
    public String getUpdatedDescription()
    {
        return FormatDescription(0, INSPIRATION_AMOUNT);
    }

    @Override
    protected void RefreshBattleEffect(boolean enabled)
    {
        super.RefreshBattleEffect(enabled);

        stopPulse();
    }

    @Override
    public void update()
    {
        super.update();

        if (GR.UI.Elapsed50())
        {
            if (GameUtilities.InBattle() && GameUtilities.IsPlayerTurn(false) && CheckInspiration())
            {
                if (!pulse)
                {
                    beginLongPulse();
                }
            }
            else
            {
                stopPulse();
            }
        }
    }

    @Override
    public void onPlayerEndTurn()
    {
        super.onPlayerEndTurn();

        if (CheckInspiration())
        {
            GameActions.Bottom.Retain(name, 1, false);
            stopPulse();
            flash();
        }
    }

    private boolean CheckInspiration()
    {
        return GameUtilities.GetPowerAmount(InspirationPower.POWER_ID) >= INSPIRATION_AMOUNT;
    }
}