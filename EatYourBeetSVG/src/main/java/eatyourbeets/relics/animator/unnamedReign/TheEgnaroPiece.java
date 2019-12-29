package eatyourbeets.relics.animator.unnamedReign;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JavaUtilities;

public class TheEgnaroPiece extends UnnamedReignRelic
{
    public static final String ID = CreateFullID(TheEgnaroPiece.class.getSimpleName());

    private static final int INITIAL_TEMPORARY_HP = 1;

    public TheEgnaroPiece()
    {
        super(ID, RelicTier.SPECIAL, LandingSound.CLINK);
    }

    @Override
    public String getUpdatedDescription()
    {
        return JavaUtilities.Format(DESCRIPTIONS[0], INITIAL_TEMPORARY_HP);
    }

    @Override
    public void atBattleStart()
    {
        super.atBattleStart();

        this.counter = INITIAL_TEMPORARY_HP;
    }

    @Override
    public void onVictory()
    {
        super.onVictory();

        this.counter = -1;
    }

    @Override
    public void atTurnStart()
    {
        super.atTurnStart();

        GameActions.Bottom.Draw(1);
    }

    @Override
    public void onPlayerEndTurn()
    {
        super.onPlayerEndTurn();

        GameActions.Bottom.GainTemporaryHP(this.counter);
        this.counter += 1;
        this.flash();
    }

    @Override
    public void OnManualEquip()
    {
        AbstractDungeon.player.energy.energyMaster += 1;
    }

    @Override
    public void onUnequip()
    {
        super.onUnequip();

        AbstractDungeon.player.energy.energyMaster -= 1;
    }
}