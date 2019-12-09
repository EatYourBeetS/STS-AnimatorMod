package eatyourbeets.monsters.UnnamedReign.Shapes.Cube;

import eatyourbeets.utilities.GameActionsHelper_Legacy;
import eatyourbeets.monsters.SharedMoveset.*;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterElement;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterTier;
import eatyourbeets.powers.UnnamedReign.FrostCubePower;

public class FrostCube extends Cube
{
    public FrostCube(MonsterTier tier, float x, float y)
    {
        super(MonsterElement.Frost, tier, x, y);

        moveset.AddNormal(new Move_GainStrength(tier.Add(2,1)));
        moveset.AddNormal(new Move_Attack(tier.Add(12,4)));
        moveset.AddNormal(new Move_AttackDefend(tier.Add(7,3), tier.Add(4,2)));
    }

    @Override
    public void usePreBattleAction()
    {
        super.usePreBattleAction();

        int amount = 0;
        switch (data.tier)
        {
            case Small:
                amount = 1;
                break;

            case Normal:
                amount = 3;
                break;

            case Advanced:
                amount = 4;
                break;

            case Ultimate:
                amount = 5;
                break;
        }

        GameActionsHelper_Legacy.ApplyPower(this, this, new FrostCubePower(this, amount), amount);
    }
}
