package eatyourbeets.monsters.UnnamedReign.Shapes.Wisp;

import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterElement;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterTier;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.powers.monsters.DarkWispPower;
import eatyourbeets.utilities.GameActions;

public class DarkWisp extends Wisp
{
    public DarkWisp(MonsterTier tier, float x, float y)
    {
        super(MonsterElement.Dark, tier, x, y);

        moveset.Normal.Attack(tier.Add(8, 4))
        .SetDamageScaling(0.2f);

        moveset.Normal.Attack(tier.Add(3, 1), 2)
        .SetDamageScaling(0.25f);

        moveset.Normal.Buff(PowerHelper.Strength, Math.max(2, tier.Add(1, 3)))
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
                amount = 9;
                break;

            case Normal:
                amount = 10;
                break;

            case Advanced:
                amount = 12;
                break;

            case Ultimate:
                amount = 14;
                break;
        }

        GameActions.Bottom.StackPower(new DarkWispPower(this, amount));
    }
}
