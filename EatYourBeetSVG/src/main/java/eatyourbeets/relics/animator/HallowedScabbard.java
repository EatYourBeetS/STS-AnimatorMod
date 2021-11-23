package eatyourbeets.relics.animator;

import com.megacrit.cardcrawl.powers.RegenPower;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

public class HallowedScabbard extends AnimatorRelic
{
    public static final String ID = CreateFullID(HallowedScabbard.class);
    public static final int DAMAGE_THRESHOLD = 12;
    public static final int REGENERATION = 4;
    public static final int BLESSING = 2;

    public HallowedScabbard()
    {
        super(ID, RelicTier.RARE, LandingSound.MAGICAL);
    }

    @Override
    public String getUpdatedDescription()
    {
        return JUtils.Format(DESCRIPTIONS[0], DAMAGE_THRESHOLD, REGENERATION, BLESSING);
    }

    @Override
    public void atBattleStart()
    {
        super.atBattleStart();

        SetEnabled(true);
        SetCounter(0);
    }

    @Override
    public void onVictory()
    {
        super.onVictory();

        SetEnabled(true);
        SetCounter(-1);
    }

    @Override
    public void onLoseHp(int damageAmount)
    {
        super.onLoseHp(damageAmount);

        if (GameUtilities.InBattle())
        {
            if (AddCounter(damageAmount) >= DAMAGE_THRESHOLD && IsEnabled())
            {
                GameActions.Bottom.GainSupercharge(BLESSING, false);
                GameActions.Bottom.StackPower(new RegenPower(player, REGENERATION));
                SetEnabled(false);
                flash();
            }
        }
    }
}