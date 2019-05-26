package eatyourbeets.monsters.UnnamedReign.Wisp;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.monsters.UnnamedReign.Cube.Moveset.Move_GuardedAttack;
import eatyourbeets.monsters.UnnamedReign.MonsterElement;
import eatyourbeets.monsters.UnnamedReign.MonsterTier;
import eatyourbeets.powers.UnnamedReign.DarkCubePower;

public class DarkWisp extends Wisp
{
    public DarkWisp(MonsterTier tier, float x, float y)
    {
        super(MonsterElement.Dark, tier, x, y);

        int level = AbstractDungeon.ascensionLevel;

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
                amount = 8;
                break;

            case Normal:
                amount = 10;
                break;

            case Advanced:
                amount = 12;
                break;

            case Ultimate:
                amount = 14;
                break;
        }

        GameActionsHelper.ApplyPower(this, this, new DarkCubePower(this, amount), amount);
    }
}
