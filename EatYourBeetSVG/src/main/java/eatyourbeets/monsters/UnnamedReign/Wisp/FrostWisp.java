package eatyourbeets.monsters.UnnamedReign.Wisp;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.monsters.UnnamedReign.Cube.Moveset.Move_GuardedAttack;
import eatyourbeets.monsters.UnnamedReign.MonsterElement;
import eatyourbeets.monsters.UnnamedReign.MonsterTier;
import eatyourbeets.powers.UnnamedReign.FrostWispPower;

public class FrostWisp extends Wisp
{
    public FrostWisp(MonsterTier tier, float x, float y)
    {
        super(MonsterElement.Frost, tier, x, y);

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
                amount = 5;
                break;

            case Normal:
                amount = 6;
                break;

            case Advanced:
                amount = 7;
                break;

            case Ultimate:
                amount = 8;
                break;
        }

        GameActionsHelper.ApplyPower(this, this, new FrostWispPower(this, amount), amount);
    }
}
