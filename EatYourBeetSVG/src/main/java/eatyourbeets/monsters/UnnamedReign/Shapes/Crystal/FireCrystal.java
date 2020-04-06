package eatyourbeets.monsters.UnnamedReign.Shapes.Crystal;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import eatyourbeets.cards.animator.status.Crystallize;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterElement;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterTier;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.powers.monsters.FireCrystalPower;
import eatyourbeets.utilities.GameActions;

public class FireCrystal extends Crystal
{
    public FireCrystal(MonsterTier tier, float x, float y)
    {
        super(MonsterElement.Fire, tier, x, y);

        moveset.SetAttackEffect(AbstractGameAction.AttackEffect.FIRE);

        moveset.Normal.ShuffleCard(new Crystallize(), 3);

        moveset.Normal.Attack(tier.Add(10, 4))
        .SetDamageScaling(0.2f);

        moveset.Normal.AttackDebuff(tier.Add(6, 3), PowerHelper.Weak, 1)
        .SetMiscBonus(4, 1)
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
                amount = 1;
                break;

            case Normal:
                amount = 2;
                break;

            case Advanced:
                amount = 3;
                break;

            case Ultimate:
                amount = 4;
                break;
        }

        GameActions.Bottom.StackPower(new FireCrystalPower(this, amount));
    }
}
