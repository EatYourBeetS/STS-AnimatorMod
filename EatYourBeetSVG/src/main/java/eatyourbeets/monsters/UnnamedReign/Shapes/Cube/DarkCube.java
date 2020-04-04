package eatyourbeets.monsters.UnnamedReign.Shapes.Cube;

import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterElement;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterTier;
import eatyourbeets.powers.monsters.DarkCubePower;
import eatyourbeets.utilities.GameActions;

public class DarkCube extends Cube
{
    public DarkCube(MonsterTier tier, float x, float y)
    {
        super(MonsterElement.Dark, tier, x, y);

        moveset.Normal.GainTemporaryThorns(tier.Add(3, 3));

        moveset.Normal.Defend(tier.Add(3, 5))
        .SetBlockScaling(0.2f);

        moveset.Normal.AttackDefend(tier.Add(2, 4), tier.Add(2, 4))
        .SetDamageScaling(0.25f)
        .SetBlockScaling(0.25f);
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
