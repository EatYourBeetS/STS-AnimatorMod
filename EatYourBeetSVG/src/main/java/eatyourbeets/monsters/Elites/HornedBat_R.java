package eatyourbeets.monsters.Elites;

import eatyourbeets.effects.AttackEffects;
import com.megacrit.cardcrawl.powers.AngryPower;
import eatyourbeets.monsters.EYBMonsterData;
import eatyourbeets.monsters.SharedMoveset.EYBMove_Buff;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.powers.monsters.HornedBat_RedPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class HornedBat_R extends HornedBat
{
    public static final String ID = CreateFullID(HornedBat_R.class);

    public HornedBat_R(float x, float y)
    {
        super(new Data(ID), x, y);

        moveset.Normal.Attack(1, 3)
        .SetAttackEffect(AttackEffects.SLASH_DIAGONAL, null)
        .SetDamageBonus(3, 1);

        moveset.Normal.Add(new EYBMove_Buff(1))
        .SetOnUse((m, t) -> GameActions.Bottom.StackPower(new AngryPower(this, 1)));

        moveset.Normal.AttackBuff(4, 1, PowerHelper.Strength, 1)
        .SetDamageBonus(3, 2)
        .SetAttackEffect(AttackEffects.SLASH_VERTICAL, null);
    }

    @Override
    public void usePreBattleAction()
    {
        super.usePreBattleAction();

        GameActions.Bottom.ApplyPower(new HornedBat_RedPower(this));
    }

    protected static class Data extends EYBMonsterData
    {
        public Data(String id)
        {
            super(id);

            if (GameUtilities.GetAscensionLevel() >= 8)
            {
                SetMaxHP(36, 39);
            }
            else
            {
                SetMaxHP(32, 35);
            }

            atlasUrl = "images/animator/monsters/HornedBat/HornedBat_R.atlas";
            jsonUrl = "images/animator/monsters/HornedBat/HornedBat.json";
            scale = 1f;

            SetHB(0, 0, 120, 150, 0, 50);
        }
    }
}
