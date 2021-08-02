package eatyourbeets.monsters.UnnamedReign.Shapes.Wisp;

import eatyourbeets.effects.AttackEffects;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterElement;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterTier;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.powers.monsters.FireWispPower;
import eatyourbeets.utilities.GameActions;

public class FireWisp extends Wisp
{
    public FireWisp(MonsterTier tier, float x, float y)
    {
        super(MonsterElement.Fire, tier, x, y);

        moveset.SetAttackEffect(AttackEffects.FIRE);

        moveset.Normal.Attack(tier.Add(8, 4))
        .SetDamageScaling(0.2f);

        moveset.Normal.Attack(tier.Add(3, 1), 2)
        .SetDamageScaling(0.25f);

        moveset.Normal.Buff(PowerHelper.Strength, tier.Add(1, 3))
        .AddPower(PowerHelper.Artifact, 1);
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

        GameActions.Bottom.StackPower(new FireWispPower(this, amount));
    }
}
