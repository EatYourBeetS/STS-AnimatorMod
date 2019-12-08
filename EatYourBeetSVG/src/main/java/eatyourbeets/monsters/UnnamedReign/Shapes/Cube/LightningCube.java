package eatyourbeets.monsters.UnnamedReign.Shapes.Cube;

import eatyourbeets.utilities.GameActionsHelper; import eatyourbeets.utilities.GameActionsHelper2;
import eatyourbeets.monsters.SharedMoveset.Move_AttackDefend;
import eatyourbeets.monsters.SharedMoveset.Move_AttackFrail;
import eatyourbeets.monsters.SharedMoveset.Move_AttackWeak;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterElement;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterTier;
import eatyourbeets.powers.UnnamedReign.LightningCubePower;

public class LightningCube extends Cube
{
    public LightningCube(MonsterTier tier, float x, float y)
    {
        super(MonsterElement.Lightning, tier, x, y);

        moveset.AddNormal(new Move_AttackWeak(tier.Add(2,4),1));
        moveset.AddNormal(new Move_AttackDefend(tier.Add(2,4),tier.Add(2, 4)));
        moveset.AddNormal(new Move_AttackFrail(tier.Add(2,4),1));
    }

    @Override
    public void usePreBattleAction()
    {
        super.usePreBattleAction();

        int amount = 0;
        switch (data.tier)
        {
            case Small:
                amount = 3;
                break;

            case Normal:
                amount = 4;
                break;

            case Advanced:
                amount = 6;
                break;

            case Ultimate:
                amount = 8;
                break;
        }

        GameActionsHelper.ApplyPower(this, this, new LightningCubePower(this, amount), amount);
    }
}
