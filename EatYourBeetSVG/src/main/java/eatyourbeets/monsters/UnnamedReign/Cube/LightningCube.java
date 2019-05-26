package eatyourbeets.monsters.UnnamedReign.Cube;

import eatyourbeets.GameActionsHelper;
import eatyourbeets.monsters.UnnamedReign.Cube.Moveset.Move_GuardedAttack;
import eatyourbeets.monsters.UnnamedReign.MonsterElement;
import eatyourbeets.monsters.UnnamedReign.MonsterTier;
import eatyourbeets.powers.UnnamedReign.LightningCubePower;

public class LightningCube extends Cube
{
    public LightningCube(MonsterTier tier, float x, float y)
    {
        super(MonsterElement.Lightning, tier, x, y);

        moveset.add(new Move_GuardedAttack(0, 18, 16, this));
    }

    @Override
    protected void getMove(int i)
    {
        moveset.get(0).SetMove();
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
                amount = 5;
                break;

            case Ultimate:
                amount = 6;
                break;
        }

        GameActionsHelper.ApplyPower(this, this, new LightningCubePower(this, amount), amount);
    }
}
