package eatyourbeets.monsters.UnnamedReign.Shapes.Wisp;

import eatyourbeets.utilities.GameActions;
import eatyourbeets.monsters.SharedMoveset_Old.*;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterElement;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterTier;
import eatyourbeets.powers.monsters.LightningWispPower;
import eatyourbeets.utilities.GameUtilities;

public class LightningWisp extends Wisp
{
    public LightningWisp(MonsterTier tier, float x, float y)
    {
        super(MonsterElement.Lightning, tier, x, y);

        boolean asc4 = GameUtilities.GetAscensionLevel() >= 4;

        int debuffAmount = asc4 ? 2 : 1;

        moveset.AddNormal(new Move_AttackWeak(tier.Add(6,3), debuffAmount));
        moveset.AddNormal(new Move_AttackFrail(tier.Add(6,3), debuffAmount));
        moveset.AddNormal(new Move_GainStrengthAndArtifact(tier.Add(2,3), 1));
    }

    @Override
    public void usePreBattleAction()
    {
        super.usePreBattleAction();

        int amount = 0;
        switch (data.tier)
        {
            case Small:
                amount = 6;
                break;

            case Normal:
                amount = 7;
                break;

            case Advanced:
                amount = 9;
                break;

            case Ultimate:
                amount = 10;
                break;
        }

        GameActions.Bottom.ApplyPower(this, this, new LightningWispPower(this, amount), amount);
    }
}
