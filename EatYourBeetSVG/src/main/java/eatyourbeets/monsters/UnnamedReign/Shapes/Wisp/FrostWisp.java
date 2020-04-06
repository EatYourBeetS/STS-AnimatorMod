package eatyourbeets.monsters.UnnamedReign.Shapes.Wisp;

import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterElement;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterTier;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.powers.monsters.FrostWispPower;
import eatyourbeets.utilities.GameActions;

public class FrostWisp extends Wisp
{
    public FrostWisp(MonsterTier tier, float x, float y)
    {
        super(MonsterElement.Frost, tier, x, y);

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
                amount = 5;
                break;

            case Normal:
                amount = 6;
                break;

            case Advanced:
                amount = 7;
                break;

            case Ultimate:
                amount = 8;
                break;
        }

        GameActions.Bottom.StackPower(new FrostWispPower(this, amount));
    }
}
