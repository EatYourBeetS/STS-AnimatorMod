package eatyourbeets.monsters.UnnamedReign.Shapes.Crystal;

import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterElement;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterTier;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.powers.monsters.HealingCrystalPower;
import eatyourbeets.utilities.GameActions;

public class HealingCrystal extends Crystal
{
    public HealingCrystal(MonsterTier tier, float x, float y)
    {
        super(MonsterElement.Healing, tier, x, y);

        moveset.Normal.AttackDefend(tier.Add(4, 2), tier.Add(4, 2))
        .SetDamageScaling(0.25f)
        .SetBlockScaling(0.25f);

        moveset.Normal.DefendBuff(7, PowerHelper.Thorns, tier.Add(2, 1))
        .SetBlockScaling(0.25f);

        moveset.Normal.Buff(PowerHelper.Strength, tier.Add(1, 2));
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
