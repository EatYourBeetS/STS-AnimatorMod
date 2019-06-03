package eatyourbeets.monsters.UnnamedReign.Shapes.Wisp;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.monsters.SharedMoveset.Move_Attack;
import eatyourbeets.monsters.SharedMoveset.Move_AttackMultiple;
import eatyourbeets.monsters.SharedMoveset.Move_ShuffleDazed;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterElement;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterTier;
import eatyourbeets.powers.UnnamedReign.DarkCubePower;
import eatyourbeets.powers.UnnamedReign.DarkWispPower;

public class DarkWisp extends Wisp
{
    public DarkWisp(MonsterTier tier, float x, float y)
    {
        super(MonsterElement.Dark, tier, x, y);

        int level = AbstractDungeon.ascensionLevel;

        moveset.AddNormal(new Move_Attack(tier.Add(9,4)));
        moveset.AddNormal(new Move_ShuffleDazed(3));
        moveset.AddNormal(new Move_AttackMultiple(tier.Add(3,1), 3));
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

        GameActionsHelper.ApplyPower(this, this, new DarkWispPower(this, amount), amount);
    }
}
