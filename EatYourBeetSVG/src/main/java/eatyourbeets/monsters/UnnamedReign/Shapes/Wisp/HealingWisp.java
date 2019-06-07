package eatyourbeets.monsters.UnnamedReign.Shapes.Wisp;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.monsters.SharedMoveset.Move_AttackMultiple;
import eatyourbeets.monsters.SharedMoveset.Move_GainStrength;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterElement;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterTier;
import eatyourbeets.powers.UnnamedReign.HealingWispPower;

public class HealingWisp extends Wisp
{
    public HealingWisp(MonsterTier tier, float x, float y)
    {
        super(MonsterElement.Healing, tier, x, y);

        int level = AbstractDungeon.ascensionLevel;

        moveset.AddNormal(new Move_GainStrength(tier.Add(1, 1)));
        moveset.AddNormal(new Move_AttackMultiple(3, tier.Add(1, 2)));
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
                amount = 12;
                break;

            case Advanced:
                amount = 16;
                break;

            case Ultimate:
                amount = 20;
                break;
        }

        GameActionsHelper.ApplyPower(this, this, new HealingWispPower(this, amount), amount);
    }
}
