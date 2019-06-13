package eatyourbeets.monsters.UnnamedReign.Shapes.Cube;

import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.monsters.SharedMoveset.Move_Attack;
import eatyourbeets.monsters.SharedMoveset.Move_AttackMultipleFrail;
import eatyourbeets.monsters.SharedMoveset.Move_Defend;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterElement;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterTier;
import eatyourbeets.powers.UnnamedReign.HealingCubePower;

public class HealingCube extends Cube
{
    public HealingCube(MonsterTier tier, float x, float y)
    {
        super(MonsterElement.Healing, tier, x, y);

        moveset.AddNormal(new Move_AttackMultipleFrail(tier.Add(1,1), 6, 1));
        moveset.AddNormal(new Move_Attack(tier.Add(6,2)));
        moveset.AddNormal(new Move_Defend(tier.Add(6,3)));
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

        GameActionsHelper.ApplyPower(this, this, new HealingCubePower(this, amount), amount);
    }
}
