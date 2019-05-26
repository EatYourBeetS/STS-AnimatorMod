package eatyourbeets.monsters.UnnamedReign.Wisp;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.monsters.UnnamedReign.Cube.Moveset.Move_GuardedAttack;
import eatyourbeets.monsters.UnnamedReign.MonsterElement;
import eatyourbeets.monsters.UnnamedReign.MonsterShape;
import eatyourbeets.monsters.UnnamedReign.MonsterTier;
import eatyourbeets.powers.UnnamedReign.UltimateWispPower;

public class UltimateWisp extends Wisp
{
    public static final String ID = CreateFullID(MonsterShape.Wisp, MonsterElement.Ultimate, MonsterTier.Ultimate);
    public static final String NAME = "Ultimate Wisp";

    public UltimateWisp()
    {
        super(MonsterElement.Ultimate, MonsterTier.Ultimate, 0, 0);

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

        GameActionsHelper.ApplyPower(this, this, new UltimateWispPower(this));
    }
}
