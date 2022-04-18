package eatyourbeets.monsters.UnnamedReign.Shapes.Wisp;

import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterElement;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterTier;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.powers.monsters.HealingWispPower;
import eatyourbeets.utilities.GameActions;

public class HealingWisp extends Wisp
{
    public HealingWisp(MonsterTier tier, float x, float y)
    {
        super(MonsterElement.Healing, tier, x, y);

        moveset.Normal.Attack(3, tier.Add(2, 1));

        moveset.Normal.Buff(PowerHelper.Strength, Math.max(2, tier.Add(0, 2)))
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

        GameActions.Bottom.StackPower(new HealingWispPower(this, amount));
    }
}
