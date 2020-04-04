package eatyourbeets.monsters.UnnamedReign.Shapes.Cube;

import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterElement;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterTier;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.powers.monsters.HealingCubePower;
import eatyourbeets.utilities.GameActions;

public class HealingCube extends Cube
{
    public HealingCube(MonsterTier tier, float x, float y)
    {
        super(MonsterElement.Healing, tier, x, y);

        moveset.Normal.AttackDebuff(tier.Add(1, 1), 6, PowerHelper.Frail, 1);

        moveset.Normal.Attack(tier.Add(6, 2))
        .SetDamageScaling(0.2f);

        moveset.Normal.Defend(tier.Add(6, 3))
        .SetBlockScaling(0.2f);
    }

    @Override
    public void usePreBattleAction()
    {
        super.usePreBattleAction();

        int amount = 0;
        switch (data.tier)
        {
            case Small:
                amount = 5;
                break;

            case Normal:
                amount = 6;
                break;

            case Advanced:
                amount = 10;
                break;

            case Ultimate:
                amount = 12;
                break;
        }

        GameActions.Bottom.ApplyPower(this, this, new HealingCubePower(this, amount), amount);
    }
}
