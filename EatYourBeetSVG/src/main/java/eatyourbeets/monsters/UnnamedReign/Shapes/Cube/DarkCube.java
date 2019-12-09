package eatyourbeets.monsters.UnnamedReign.Shapes.Cube;

import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameActionsHelper_Legacy;
import eatyourbeets.monsters.SharedMoveset.Move_Attack;
import eatyourbeets.monsters.SharedMoveset.Move_AttackDefend;
import eatyourbeets.monsters.SharedMoveset.Move_Defend;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterElement;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterTier;
import eatyourbeets.powers.UnnamedReign.DarkCubePower;

public class DarkCube extends Cube
{
    public DarkCube(MonsterTier tier, float x, float y)
    {
        super(MonsterElement.Dark, tier, x, y);

        moveset.AddNormal(new Move_Attack(tier.Add(3,4)));
        moveset.AddNormal(new Move_Defend(tier.Add(3,4)));
        moveset.AddNormal(new Move_AttackDefend(tier.Add(3,3), tier.Add(3, 2)));
    }

    @Override
    public void usePreBattleAction()
    {
        super.usePreBattleAction();

        int amount = 0;
        switch (data.tier)
        {
            case Small:
                amount = 2;
                break;

            case Normal:
                amount = 3;
                break;

            case Advanced:
                amount = 4;
                break;

            case Ultimate:
                amount = 4;
                break;
        }

        GameActions.Bottom.ApplyPower(this, this, new DarkCubePower(this, amount), amount);
    }
}
