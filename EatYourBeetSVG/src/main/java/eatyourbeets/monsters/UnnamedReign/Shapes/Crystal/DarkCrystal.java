package eatyourbeets.monsters.UnnamedReign.Shapes.Crystal;

import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.monsters.SharedMoveset.Move_AttackMultiple;
import eatyourbeets.monsters.SharedMoveset.Move_AttackWeak;
import eatyourbeets.monsters.SharedMoveset.Move_Defend;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterElement;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterTier;
import eatyourbeets.powers.UnnamedReign.DarkCrystalPower;

public class DarkCrystal extends Crystal
{
    public DarkCrystal(MonsterTier tier, float x, float y)
    {
        super(MonsterElement.Dark, tier, x, y);

        moveset.AddNormal(new Move_AttackMultiple(tier.Add(4, 1), 2));
        moveset.AddNormal(new Move_AttackWeak(tier.Add(5, 2), 1));
        moveset.AddNormal(new Move_Defend(tier.Add(7, 1)));
    }

    @Override
    public void usePreBattleAction()
    {
        super.usePreBattleAction();

        int amount = 0;
        switch (data.tier)
        {
            case Small:
                amount = 2;
                break;

            case Normal:
                amount = 3;
                break;

            case Advanced:
                amount = 4;
                break;

            case Ultimate:
                amount = 5;
                break;
        }

        GameActionsHelper.ApplyPower(this, this, new DarkCrystalPower(this, amount), amount);
    }
}
