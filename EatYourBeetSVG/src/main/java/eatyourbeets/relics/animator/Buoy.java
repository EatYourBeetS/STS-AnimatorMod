package eatyourbeets.relics.animator;

import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JavaUtilities;

public class Buoy extends AnimatorRelic
{
    public static final String ID = CreateFullID(Buoy.class);
    public static final int HP_THRESHOLD = 30;
    public static final int BLOCK_AMOUNT = 4;

    public Buoy()
    {
        super(ID, RelicTier.COMMON, LandingSound.FLAT);
    }

    @Override
    public String getUpdatedDescription()
    {
        return JavaUtilities.Format(DESCRIPTIONS[0], HP_THRESHOLD, BLOCK_AMOUNT);
    }

    @Override
    public void onEquip()
    {
        super.onEquip();

        SetCounter(-1);
    }

    @Override
    public void atBattleStart()
    {
        super.atBattleStart();

        SetCounter(-1);
    }

    @Override
    public void onVictory()
    {
        super.onVictory();

        stopPulse();
    }

    @Override
    public void atTurnStartPostDraw()
    {
        super.atTurnStartPostDraw();

        UpdateThreshold();
    }

    @Override
    public void onLoseHp(int damageAmount)
    {
        super.onLoseHp(damageAmount);

        UpdateThreshold();
    }

    @Override
    public int onPlayerHeal(int healAmount)
    {
        UpdateThreshold();

        return super.onPlayerHeal(healAmount);
    }

    @Override
    public void onPlayerEndTurn()
    {
        super.onPlayerEndTurn();

        if (player.currentHealth < counter)
        {
            GameActions.Bottom.GainBlock(BLOCK_AMOUNT);
            flash();
        }
    }

    private void UpdateThreshold()
    {
        GameActions.Bottom.Callback(() ->
        {
            if (SetCounter((int) Math.ceil(player.maxHealth * (HP_THRESHOLD / 100f))) > player.currentHealth)
            {
                beginLongPulse();
            }
            else
            {
                stopPulse();
            }
        });
    }
}