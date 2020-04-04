package eatyourbeets.monsters.UnnamedReign.Shapes.Cube;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterElement;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterTier;
import eatyourbeets.powers.monsters.LightningCubePower;
import eatyourbeets.utilities.GameActions;

public class LightningCube extends Cube
{
    public LightningCube(MonsterTier tier, float x, float y)
    {
        super(MonsterElement.Lightning, tier, x, y);

        moveset.SetAttackEffect(AbstractGameAction.AttackEffect.LIGHTNING);

        moveset.Normal.AttackWeak(tier.Add(2, 4), 1)
        .SetDamageScaling(0.2f);

        moveset.Normal.AttackDefend(tier.Add(2, 4), tier.Add(2, 4))
        .SetDamageScaling(0.25f)
        .SetBlockScaling(0.25f);

        moveset.Normal.AttackFrail(tier.Add(2, 4), 1)
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
                amount = 3;
                break;

            case Normal:
                amount = 4;
                break;

            case Advanced:
                amount = 6;
                break;

            case Ultimate:
                amount = 8;
                break;
        }

        GameActions.Bottom.ApplyPower(this, this, new LightningCubePower(this, amount), amount);
    }
}
