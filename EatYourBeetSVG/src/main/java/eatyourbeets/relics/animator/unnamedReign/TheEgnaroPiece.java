package eatyourbeets.relics.animator.unnamedReign;

import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

public class TheEgnaroPiece extends UnnamedReignRelic
{
    public static final String ID = CreateFullID(TheEgnaroPiece.class);
    public static final int VITALITY_AMOUNT = 1;
    public static final int CARDS_STEP = 6;

    public TheEgnaroPiece()
    {
        super(ID, RelicTier.SPECIAL, LandingSound.CLINK);
    }

    @Override
    public String getUpdatedDescription()
    {
        return JUtils.Format(DESCRIPTIONS[0], VITALITY_AMOUNT, CARDS_STEP);
    }

    @Override
    public void atTurnStart()
    {
        super.atTurnStart();

        GameActions.Bottom.Draw(1);
    }

    @Override
    public void onEquip()
    {
        super.onEquip();

        if (GameUtilities.InBattle())
        {
            atBattleStart();
        }
    }

    @Override
    public void atBattleStart()
    {
        super.atBattleStart();

        this.counter = Math.min(10, VITALITY_AMOUNT * player.masterDeck.size() / CARDS_STEP);
        if (counter > 0)
        {
            GameActions.Bottom.GainVitality(counter);
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
}