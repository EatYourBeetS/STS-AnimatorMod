package eatyourbeets.monsters.UnnamedReign.Shapes.Crystal;

import eatyourbeets.cards.animator.status.Crystallize;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterElement;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterTier;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.powers.monsters.DarkCrystalPower;
import eatyourbeets.utilities.GameActions;

public class DarkCrystal extends Crystal
{
    public DarkCrystal(MonsterTier tier, float x, float y)
    {
        super(MonsterElement.Dark, tier, x, y);

        moveset.Normal.ShuffleCard(new Crystallize(), 3);

        moveset.Normal.Attack(tier.Add(4, 1), 2)
        .SetDamageScaling(0.25f);

        moveset.Normal.AttackDebuff(tier.Add(5, 2), PowerHelper.Weak, 1)
        .SetMiscBonus(4, 1)
        .SetDamageScaling(0.25f);
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

        GameActions.Bottom.ApplyPower(this, this, new DarkCrystalPower(this, amount), amount);
    }
}
