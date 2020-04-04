package eatyourbeets.monsters.UnnamedReign.Shapes.Crystal;

import eatyourbeets.utilities.GameActions;
import eatyourbeets.monsters.SharedMoveset_Old.*;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterElement;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterTier;
import eatyourbeets.powers.monsters.LightningCrystalPower;

public class LightningCrystal extends Crystal
{
    public LightningCrystal(MonsterTier tier, float x, float y)
    {
        super(MonsterElement.Lightning, tier, x, y);

        moveset.AddNormal(new Move_AttackMultiple(tier.Add(2,1),3));
        moveset.AddNormal(new Move_AttackWeak(tier.Add(3,2),1));
        moveset.AddNormal(new Move_AttackStrength(tier.Add(3,2),1));
    }

    @Override
    public void usePreBattleAction()
    {
        super.usePreBattleAction();

        int amount = 0;
        switch (data.tier)
        {
            case Small:
                amount = 2;
                break;

            case Normal:
                amount = 3;
                break;

            case Advanced:
                amount = 4;
                break;

            case Ultimate:
                amount = 5;
                break;
        }

        GameActions.Bottom.ApplyPower(this, this, new LightningCrystalPower(this, amount), amount);
    }
}
