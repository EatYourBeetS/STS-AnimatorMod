package eatyourbeets.monsters.UnnamedReign.Shapes.Cube;

import eatyourbeets.effects.AttackEffects;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterElement;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterTier;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.powers.monsters.FireCubePower;
import eatyourbeets.utilities.GameActions;

public class FireCube extends Cube
{
    public FireCube(MonsterTier tier, float x, float y)
    {
        super(MonsterElement.Fire, tier, x, y);

        moveset.SetAttackEffect(AttackEffects.FIRE);

        moveset.Normal.Attack(tier.Add(2, 2), 2)
        .SetDamageScaling(0.2f);

        moveset.Normal.Defend(tier.Add(4, 3))
        .SetBlockScaling(0.2f);

        moveset.Normal.Buff(PowerHelper.Regen, tier.Add(3, 2));
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

        GameActions.Bottom.StackPower(new FireCubePower(this, amount));
    }
}
