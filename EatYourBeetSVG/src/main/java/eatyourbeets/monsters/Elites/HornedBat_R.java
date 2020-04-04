package eatyourbeets.monsters.Elites;

import eatyourbeets.monsters.EYBMonsterData;
import eatyourbeets.monsters.SharedMoveset_Old.Move_AttackFrail;
import eatyourbeets.monsters.SharedMoveset_Old.Move_GainStrengthAndBlock;
import eatyourbeets.monsters.SharedMoveset_Old.Move_ShuffleDazed;
import eatyourbeets.powers.monsters.HornedBat_RedPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class HornedBat_R extends HornedBat
{
    public static final String ID = CreateFullID(HornedBat_R.class);

    public HornedBat_R(CommonMoveset commonMoveset, float x, float y)
    {
        super(new Data(ID), x, y);

        if (GameUtilities.GetAscensionLevel() >= 7)
        {
            moveset.AddNormal(new Move_GainStrengthAndBlock(4, 11));
        }
        else
        {
            moveset.AddNormal(new Move_GainStrengthAndBlock(3, 9));
        }

        moveset.AddNormal(new Move_ShuffleDazed(1, true));
        moveset.AddNormal(new Move_AttackFrail(4, 1));
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

            atlasUrl = "images/monsters/animator/HornedBat/HornedBat_R.atlas";
            jsonUrl = "images/monsters/animator/HornedBat/HornedBat.json";
            scale = 1f;

            SetHB(0,0,100,140, 0, 50);
        }
    }
}
