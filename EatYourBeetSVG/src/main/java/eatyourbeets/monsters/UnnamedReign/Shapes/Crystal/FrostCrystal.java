package eatyourbeets.monsters.UnnamedReign.Shapes.Crystal;

import eatyourbeets.utilities.GameActions;
import eatyourbeets.cards.animator.status.Crystallize;
import eatyourbeets.monsters.SharedMoveset_Old.Move_AttackMultiple;
import eatyourbeets.monsters.SharedMoveset_Old.Move_GainStrength;
import eatyourbeets.monsters.SharedMoveset_Old.Move_ShuffleCard;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterElement;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterTier;
import eatyourbeets.powers.monsters.FrostCrystalPower;

public class FrostCrystal extends Crystal
{
    public FrostCrystal(MonsterTier tier, float x, float y)
    {
        super(MonsterElement.Frost, tier, x, y);

        moveset.AddNormal(new Move_GainStrength(tier.Add(1,1)));
        moveset.AddNormal(new Move_AttackMultiple(tier.Add(7, 3),2));
        moveset.AddNormal(new Move_ShuffleCard(new Crystallize(), 3));
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

        GameActions.Bottom.ApplyPower(this, this, new FrostCrystalPower(this, amount), amount);
    }
}
