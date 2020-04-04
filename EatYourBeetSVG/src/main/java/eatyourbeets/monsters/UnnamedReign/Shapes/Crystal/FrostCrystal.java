package eatyourbeets.monsters.UnnamedReign.Shapes.Crystal;

import eatyourbeets.cards.animator.status.Crystallize;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterElement;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterTier;
import eatyourbeets.powers.monsters.FrostCrystalPower;
import eatyourbeets.utilities.GameActions;

public class FrostCrystal extends Crystal
{
    public FrostCrystal(MonsterTier tier, float x, float y)
    {
        super(MonsterElement.Frost, tier, x, y);

        moveset.Normal.ShuffleCard(new Crystallize(), 3);

        moveset.Normal.GainStrength(tier.Add(1, 1));

        moveset.Normal.Attack(tier.Add(7, 3), 2)
        .SetDamageScaling(0.2f);
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
