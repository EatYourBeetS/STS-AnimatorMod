package eatyourbeets.monsters.UnnamedReign.Shapes.Crystal;

import eatyourbeets.cards.animator.status.Crystallize;
import eatyourbeets.monsters.SharedMoveset_Old.Move_ShuffleCard;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.monsters.SharedMoveset_Old.Move_AttackMultiple;
import eatyourbeets.monsters.SharedMoveset_Old.Move_AttackWeak;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterElement;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterTier;
import eatyourbeets.powers.monsters.DarkCrystalPower;
import eatyourbeets.utilities.GameUtilities;

public class DarkCrystal extends Crystal
{
    public DarkCrystal(MonsterTier tier, float x, float y)
    {
        super(MonsterElement.Dark, tier, x, y);

        boolean asc4 = GameUtilities.GetAscensionLevel() >= 4;

        int debuffAmount = asc4 ? 2 : 1;

        moveset.AddNormal(new Move_AttackMultiple(tier.Add(4, 1), 2));
        moveset.AddNormal(new Move_AttackWeak(tier.Add(5, 2), debuffAmount));
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

        GameActions.Bottom.ApplyPower(this, this, new DarkCrystalPower(this, amount), amount);
    }
}
