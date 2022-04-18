package eatyourbeets.monsters.UnnamedReign.Shapes.Cube;

import eatyourbeets.effects.AttackEffects;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterElement;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterTier;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.powers.monsters.LightningCubePower;
import eatyourbeets.utilities.GameActions;

public class LightningCube extends Cube
{
    public LightningCube(MonsterTier tier, float x, float y)
    {
        super(MonsterElement.Lightning, tier, x, y);

        moveset.SetAttackEffect(AttackEffects.LIGHTNING);

        moveset.Normal.AttackDebuff(tier.Add(2, 4), PowerHelper.Weak, 1)
        .SetDamageScaling(0.2f);

        moveset.Normal.AttackDefend(tier.Add(2, 4), tier.Add(2, 4))
        .SetDamageScaling(0.25f)
        .SetBlockScaling(0.25f);

        moveset.Normal.AttackDebuff(tier.Add(2, 4), PowerHelper.Frail, 1)
        .SetDamageScaling(0.2f);
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
                amount = 6;
                break;

            case Ultimate:
                amount = 8;
                break;
        }

        GameActions.Bottom.StackPower(new LightningCubePower(this, amount));
    }
}
