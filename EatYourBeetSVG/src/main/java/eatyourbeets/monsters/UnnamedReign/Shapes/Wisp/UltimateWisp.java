package eatyourbeets.monsters.UnnamedReign.Shapes.Wisp;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.monsters.SharedMoveset.*;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterElement;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterShape;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterTier;
import eatyourbeets.powers.UnnamedReign.UltimateWispPower;

public class UltimateWisp extends Wisp
{
    public static final String ID = CreateFullID(MonsterShape.Wisp, MonsterElement.Ultimate, MonsterTier.Ultimate);
    public static final String NAME = "Ultimate Wisp";

    public UltimateWisp()
    {
        super(MonsterElement.Ultimate, MonsterTier.Ultimate, 0, 0);

        int level = AbstractDungeon.ascensionLevel;

        movesetMode = Mode.Sequential;
        moveset.AddNormal(new Move_AttackMultiple( 8,3));
        moveset.AddNormal(new Move_GainStrengthAndArtifactAll( 3, 2));
        moveset.AddNormal(new Move_AttackMultipleFrail(2,6, 2));
        moveset.AddNormal(new Move_AttackMultipleHex( 5,4, 1));
    }

    @Override
    public void usePreBattleAction()
    {
        super.usePreBattleAction();

        GameActionsHelper.ApplyPower(this, this, new UltimateWispPower(this));
    }
}