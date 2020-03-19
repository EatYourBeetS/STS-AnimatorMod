package eatyourbeets.relics.animator;

import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JavaUtilities;

public class Buoy extends AnimatorRelic
{
    public static final String ID = CreateFullID(Buoy.class);

    private static final int HP_THRESHOLD = 30;
    private static final int BLOCK_AMOUNT = 4;

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

        counter = -1;
    }

    @Override
    public void atBattleStart()
    {
        super.atBattleStart();

        counter = -1;
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
            this.flash();
        }
    }

    private void UpdateThreshold()
    {
        GameActions.Bottom.Callback(() ->
        {
            counter = (int) Math.ceil(player.maxHealth * (HP_THRESHOLD / 100f));
            if (player.currentHealth < counter)
            {
                this.beginLongPulse();
            }
            else
            {
                this.stopPulse();
            }
        });
    }
}