package eatyourbeets.monsters.UnnamedReign.Shapes.Wisp;

import eatyourbeets.utilities.GameActionsHelper; import eatyourbeets.utilities.GameActionsHelper2;
import eatyourbeets.monsters.SharedMoveset.Move_AttackMultiple;
import eatyourbeets.monsters.SharedMoveset.Move_GainStrengthAndArtifact;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterElement;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterTier;
import eatyourbeets.powers.UnnamedReign.HealingWispPower;

public class HealingWisp extends Wisp
{
    public HealingWisp(MonsterTier tier, float x, float y)
    {
        super(MonsterElement.Healing, tier, x, y);

        moveset.AddNormal(new Move_GainStrengthAndArtifact(tier.Add(0, 2), 1));
        moveset.AddNormal(new Move_AttackMultiple(3, tier.Add(2, 1)));
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

        GameActionsHelper.ApplyPower(this, this, new HealingWispPower(this, amount), amount);
    }
}
