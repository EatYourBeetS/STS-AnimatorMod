package eatyourbeets.monsters.UnnamedReign.Shapes.Cube;

import eatyourbeets.monsters.SharedMoveset.Move_GainTempThorns;
import eatyourbeets.utilities.GameActions;
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

        moveset.AddNormal(new Move_Defend(tier.Add(3,5)));
        moveset.AddNormal(new Move_GainTempThorns(tier.Add(3,3)));
        moveset.AddNormal(new Move_AttackDefend(tier.Add(2,4), tier.Add(2, 4)));
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
                amount = 3;
                break;

            case Ultimate:
                amount = 4;
                break;
        }

        GameActions.Bottom.ApplyPower(this, this, new DarkCubePower(this, amount), amount);
    }
}
