package eatyourbeets.monsters.UnnamedReign.Shapes.Wisp;

import eatyourbeets.monsters.Moveset;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.monsters.SharedMoveset.*;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterElement;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterShape;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterTier;
import eatyourbeets.powers.UnnamedReign.UltimateWispPower;
import eatyourbeets.utilities.GameUtilities;

public class UltimateWisp extends Wisp
{
    public static final String ID = CreateFullID(MonsterShape.Wisp, MonsterElement.Ultimate, MonsterTier.Ultimate);
    public static final String NAME = "Ultimate Wisp";

    public UltimateWisp()
    {
        super(MonsterElement.Ultimate, MonsterTier.Ultimate, 0, 0);

        boolean asc4 = GameUtilities.GetActualAscensionLevel() >= 4;

        moveset.mode = Moveset.Mode.Sequential;
        moveset.AddNormal(new Move_AttackMultiple( 8,3));
        moveset.AddNormal(new Move_GainStrengthAndArtifactAll( 3, 1));
        moveset.AddNormal(new Move_AttackMultipleFrail(2,8, 2));
        moveset.AddNormal(new Move_AttackMultipleHex( 6,4, 1));
    }

    @Override
    public void usePreBattleAction()
    {
        super.usePreBattleAction();

        GameActions.Bottom.ApplyPower(this, this, new UltimateWispPower(this));
    }
}