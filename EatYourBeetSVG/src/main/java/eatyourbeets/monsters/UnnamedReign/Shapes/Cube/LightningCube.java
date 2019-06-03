package eatyourbeets.monsters.UnnamedReign.Shapes.Cube;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.monsters.SharedMoveset.Move_AttackFrail;
import eatyourbeets.monsters.SharedMoveset.Move_AttackVulnerable;
import eatyourbeets.monsters.SharedMoveset.Move_AttackWeak;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterElement;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterTier;
import eatyourbeets.powers.UnnamedReign.LightningCubePower;

public class LightningCube extends Cube
{
    public LightningCube(MonsterTier tier, float x, float y)
    {
        super(MonsterElement.Lightning, tier, x, y);

        int level = AbstractDungeon.ascensionLevel;

        moveset.AddNormal(new Move_AttackWeak(tier.Add(2,4),1));
        moveset.AddNormal(new Move_AttackVulnerable(tier.Add(2,4),1));
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
                amount = 5;
                break;

            case Ultimate:
                amount = 6;
                break;
        }

        GameActionsHelper.ApplyPower(this, this, new LightningCubePower(this, amount), amount);
    }
}
