package eatyourbeets.monsters.UnnamedReign.Shapes.Cube;

import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterElement;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterTier;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.powers.monsters.FrostCubePower;
import eatyourbeets.utilities.GameActions;

public class FrostCube extends Cube
{
    public FrostCube(MonsterTier tier, float x, float y)
    {
        super(MonsterElement.Frost, tier, x, y);

        moveset.Normal.Buff(PowerHelper.Strength, tier.Add(2, 1));

        moveset.Normal.Attack(tier.Add(12, 4))
        .SetDamageScaling(0.2f);

        moveset.Normal.AttackDefend(tier.Add(7, 3), tier.Add(4, 2))
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

        GameActions.Bottom.StackPower(new FrostCubePower(this, amount));
    }
}
