package eatyourbeets.monsters.UnnamedReign.Shapes.Cube;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.monsters.SharedMoveset.Move_AttackMultiple;
import eatyourbeets.monsters.SharedMoveset.Move_Defend;
import eatyourbeets.monsters.SharedMoveset.Move_GainRegeneration;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterElement;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterTier;
import eatyourbeets.powers.UnnamedReign.FireCubePower;

public class FireCube extends Cube
{
    public FireCube(MonsterTier tier, float x, float y)
    {
        super(MonsterElement.Fire, tier, x, y);

        int level = AbstractDungeon.ascensionLevel;

        moveset.AddNormal(new Move_AttackMultiple(tier.Add(2, 1), 3));
        moveset.AddNormal(new Move_Defend(tier.Add(4, 3)));
        moveset.AddNormal(new Move_GainRegeneration(tier.Add(3, 2)));
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
                amount = 2;
                break;

            case Advanced:
                amount = 3;
                break;

            case Ultimate:
                amount = 4;
                break;
        }

        GameActionsHelper.ApplyPower(this, this, new FireCubePower(this, amount), amount);
    }
}
