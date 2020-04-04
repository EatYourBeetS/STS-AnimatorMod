package eatyourbeets.monsters.UnnamedReign.Shapes.Crystal;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterElement;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterTier;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.powers.monsters.LightningCrystalPower;
import eatyourbeets.utilities.GameActions;

public class LightningCrystal extends Crystal
{
    public LightningCrystal(MonsterTier tier, float x, float y)
    {
        super(MonsterElement.Lightning, tier, x, y);

        moveset.SetAttackEffect(AbstractGameAction.AttackEffect.LIGHTNING);

        moveset.Normal.Attack(tier.Add(2, 1), 3)
        .SetDamageScaling(0.25f);

        moveset.Normal.AttackDebuff(tier.Add(3, 2), PowerHelper.Weak, 1)
        .SetDamageScaling(0.25f);

        moveset.Normal.AttackBuff(tier.Add(3, 2), PowerHelper.Strength, 1)
        .SetDamageScaling(0.25f);
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
