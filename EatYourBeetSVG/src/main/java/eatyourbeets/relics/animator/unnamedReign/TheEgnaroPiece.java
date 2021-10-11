package eatyourbeets.relics.animator.unnamedReign;

import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JUtils;

public class TheEgnaroPiece extends UnnamedReignRelic
{
    public static final String ID = CreateFullID(TheEgnaroPiece.class);
    public static final int Prayer_AMOUNT = 1;
    public static final int CARDS_STEP = 7;

    public TheEgnaroPiece()
    {
        super(ID, RelicTier.SPECIAL, LandingSound.CLINK);
    }

    @Override
    public String getUpdatedDescription()
    {
        return JUtils.Format(DESCRIPTIONS[0], Prayer_AMOUNT, CARDS_STEP);
    }

    @Override
    public void atTurnStart()
    {
        super.atTurnStart();

        GameActions.Bottom.Draw(1);
    }

    @Override
    protected void ActivateBattleEffect()
    {
        this.counter = 1 + Math.min(9, Prayer_AMOUNT * player.masterDeck.size() / CARDS_STEP);
        if (counter > 0)
        {
            GameActions.Bottom.GainPrayer(counter);
            flash();
        }
    }

    @Override
    public void OnManualEquip()
    {
        player.energy.energyMaster += 1;
    }

    @Override
    public void onUnequip()
    {
        super.onUnequip();

        player.energy.energyMaster -= 1;
    }

    public String GetFalseLifePotionString()
    {
        return " NL #y" + name.replace(" ", " #y") + " increases the power of this potion by #b" + GetFalseLifePotionPowerIncrease() + ".";
    }

    public int GetFalseLifePotionPowerIncrease()
    {
        return 1;
    }
}