package eatyourbeets.monsters.UnnamedReign.Shapes.Wisp;

import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.monsters.SharedMoveset.*;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterElement;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterTier;
import eatyourbeets.powers.UnnamedReign.LightningWispPower;

public class LightningWisp extends Wisp
{
    public LightningWisp(MonsterTier tier, float x, float y)
    {
        super(MonsterElement.Lightning, tier, x, y);

        boolean asc4 = PlayerStatistics.GetAscensionLevel() >= 4;

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

        GameActionsHelper.ApplyPower(this, this, new LightningWispPower(this, amount), amount);
    }
}