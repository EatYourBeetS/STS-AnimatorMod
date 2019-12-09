package eatyourbeets.monsters.UnnamedReign.Shapes.Wisp;

import eatyourbeets.utilities.GameActionsHelper_Legacy;
import eatyourbeets.monsters.SharedMoveset.Move_Attack;
import eatyourbeets.monsters.SharedMoveset.Move_AttackMultiple;
import eatyourbeets.monsters.SharedMoveset.Move_GainStrengthAndArtifact;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterElement;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterTier;
import eatyourbeets.powers.UnnamedReign.FrostWispPower;

public class FrostWisp extends Wisp
{
    public FrostWisp(MonsterTier tier, float x, float y)
    {
        super(MonsterElement.Frost, tier, x, y);

        moveset.AddNormal(new Move_Attack(tier.Add(8,4)));
        moveset.AddNormal(new Move_GainStrengthAndArtifact(tier.Add(2,3), 1));
        moveset.AddNormal(new Move_AttackMultiple(tier.Add(3,1), 2));
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

        GameActionsHelper_Legacy.ApplyPower(this, this, new FrostWispPower(this, amount), amount);
    }
}
