package eatyourbeets.monsters.Elites;

import com.megacrit.cardcrawl.cards.status.Dazed;
import eatyourbeets.monsters.EYBMonsterData;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.powers.common.TemporaryConfusionPower;
import eatyourbeets.powers.monsters.HornedBat_PurplePower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class HornedBat_P extends HornedBat
{
    public static final String ID = CreateFullID(HornedBat_P.class);

    public HornedBat_P(CommonMoveset commonMoveset, float x, float y)
    {
        super(new Data(ID), x, y);

        moveset.Special.ShuffleCard(new Dazed(), 1)
        .SetOnUse((m, t) -> GameActions.Bottom.StackPower(new TemporaryConfusionPower(t)));

        moveset.Special.StrongDebuff(PowerHelper.Strength, -1);

        moveset.Normal.ShuffleCard(new Dazed(), 1)
        .SkipAnimation(true);

        moveset.Normal.AttackDebuff(4, PowerHelper.Frail, 1)
        .SetDamageBonus(3, 2);
    }

    @Override
    public void usePreBattleAction()
    {
        super.usePreBattleAction();

        GameActions.Bottom.ApplyPower(new HornedBat_PurplePower(this));
    }

    protected static class Data extends EYBMonsterData
    {
        public Data(String id)
        {
            super(id);

            if (GameUtilities.GetAscensionLevel() >= 8)
            {
                SetMaxHP(32, 36);
            }
            else
            {
                SetMaxHP(28, 32);
            }

            atlasUrl = "images/monsters/animator/HornedBat/HornedBat_P.atlas";
            jsonUrl = "images/monsters/animator/HornedBat/HornedBat.json";
            scale = 1f;

            SetHB(0,0,100,140, 0, 50);
        }
    }
}
