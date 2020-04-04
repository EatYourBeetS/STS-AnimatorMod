package eatyourbeets.monsters.UnnamedReign.Shapes.Wisp;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import eatyourbeets.monsters.SharedMoveset.special.EYBMove_Buff_StrengthAndArtifact;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterElement;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterTier;
import eatyourbeets.powers.monsters.LightningWispPower;
import eatyourbeets.utilities.GameActions;

public class LightningWisp extends Wisp
{
    public LightningWisp(MonsterTier tier, float x, float y)
    {
        super(MonsterElement.Lightning, tier, x, y);

        moveset.SetAttackEffect(AbstractGameAction.AttackEffect.LIGHTNING);

        moveset.Normal.AttackWeak(tier.Add(6, 3), 1)
        .SetMiscBonus(4, 1)
        .SetDamageScaling(0.2f);

        moveset.Normal.AttackFrail(tier.Add(6, 3), 1)
        .SetMiscBonus(4, 1)
        .SetDamageScaling(0.2f);

        moveset.Normal.Add(new EYBMove_Buff_StrengthAndArtifact(tier.Add(2,3), 1));
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
