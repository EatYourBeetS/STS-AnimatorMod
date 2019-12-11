package eatyourbeets.monsters.UnnamedReign.Shapes.Crystal;

import eatyourbeets.utilities.GameActions;
import eatyourbeets.monsters.SharedMoveset.Move_AttackDefend;
import eatyourbeets.monsters.SharedMoveset.Move_GainStrength;
import eatyourbeets.monsters.SharedMoveset.Move_GainThorns;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterElement;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterTier;
import eatyourbeets.powers.UnnamedReign.HealingCrystalPower;

public class HealingCrystal extends Crystal
{
    public HealingCrystal(MonsterTier tier, float x, float y)
    {
        super(MonsterElement.Healing, tier, x, y);

        moveset.AddNormal(new Move_AttackDefend(tier.Add(4, 2), tier.Add(4, 2)));
        moveset.AddNormal(new Move_GainThorns(tier.Add(2, 1)));
        moveset.AddNormal(new Move_GainStrength(tier.Add(1, 2)));
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
                amount = 5;
                break;

            case Advanced:
                amount = 7;
                break;

            case Ultimate:
                amount = 9;
                break;
        }

        GameActions.Bottom.ApplyPower(this, this, new HealingCrystalPower(this, amount), amount);
    }
}
