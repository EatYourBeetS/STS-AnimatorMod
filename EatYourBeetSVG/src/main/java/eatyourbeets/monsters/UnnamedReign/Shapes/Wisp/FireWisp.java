package eatyourbeets.monsters.UnnamedReign.Shapes.Wisp;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.monsters.SharedMoveset.Move_Attack;
import eatyourbeets.monsters.SharedMoveset.Move_AttackMultiple;
import eatyourbeets.monsters.SharedMoveset.Move_ShuffleDazed;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterElement;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterTier;
import eatyourbeets.powers.UnnamedReign.FireWispPower;

public class FireWisp extends Wisp
{
    public FireWisp(MonsterTier tier, float x, float y)
    {
        super(MonsterElement.Fire, tier, x, y);

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
                amount = 4;
                break;

            case Normal:
                amount = 6;
                break;

            case Advanced:
                amount = 8;
                break;

            case Ultimate:
                amount = 10;
                break;
        }

        GameActionsHelper.ApplyPower(this, this, new FireWispPower(this, amount), amount);
    }
}
