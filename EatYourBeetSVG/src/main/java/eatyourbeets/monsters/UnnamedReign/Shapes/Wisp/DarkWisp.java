package eatyourbeets.monsters.UnnamedReign.Shapes.Wisp;

import eatyourbeets.utilities.GameActions;
import eatyourbeets.monsters.SharedMoveset.Move_Attack;
import eatyourbeets.monsters.SharedMoveset.Move_AttackMultiple;
import eatyourbeets.monsters.SharedMoveset.Move_GainStrengthAndArtifact;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterElement;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterTier;
import eatyourbeets.powers.UnnamedReign.DarkWispPower;

public class DarkWisp extends Wisp
{
    public DarkWisp(MonsterTier tier, float x, float y)
    {
        super(MonsterElement.Dark, tier, x, y);

        moveset.AddNormal(new Move_Attack(tier.Add(8,4)));
        moveset.AddNormal(new Move_GainStrengthAndArtifact(tier.Add(1,3), 1));
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
                amount = 8;
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

        GameActions.Bottom.ApplyPower(this, this, new DarkWispPower(this, amount), amount);
    }
}
