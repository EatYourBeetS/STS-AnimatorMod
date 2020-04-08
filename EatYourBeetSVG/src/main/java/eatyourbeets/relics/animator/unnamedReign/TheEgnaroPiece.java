package eatyourbeets.relics.animator.unnamedReign;

import eatyourbeets.utilities.GameActions;

public class TheEgnaroPiece extends UnnamedReignRelic
{
    public static final String ID = CreateFullID(TheEgnaroPiece.class);
    public static final int INITIAL_TEMPORARY_HP = 1;

    public TheEgnaroPiece()
    {
        super(ID, RelicTier.SPECIAL, LandingSound.CLINK);
    }

    @Override
    public String getUpdatedDescription()
    {
        return FormatDescription(INITIAL_TEMPORARY_HP);
    }

    @Override
    public void atBattleStart()
    {
        super.atBattleStart();

        SetCounter(INITIAL_TEMPORARY_HP);
    }

    @Override
    public void onVictory()
    {
        super.onVictory();

        SetCounter(-1);
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
        AddCounter(1);
        flash();
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