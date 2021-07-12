package eatyourbeets.relics.animator.unnamedReign;

import eatyourbeets.utilities.GameActions;

public class TheEgnaroPiece extends UnnamedReignRelic
{
    public static final String ID = CreateFullID(TheEgnaroPiece.class);

    public TheEgnaroPiece()
    {
        super(ID, RelicTier.SPECIAL, LandingSound.CLINK);
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

        GameActions.Bottom.GainBlessing(1, true);
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