package eatyourbeets.monsters.Elites;

import eatyourbeets.monsters.EYBMonsterData;
import eatyourbeets.monsters.Elites.Moveset.Move_TemporaryConfusion;
import eatyourbeets.monsters.SharedMoveset_Old.Move_AttackFrail;
import eatyourbeets.monsters.SharedMoveset_Old.Move_GainStrengthAndBlock;
import eatyourbeets.monsters.SharedMoveset_Old.Move_ShuffleDazed;
import eatyourbeets.monsters.SharedMoveset_Old.Move_StrengthLoss;
import eatyourbeets.powers.monsters.HornedBat_PurplePower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class HornedBat_P extends HornedBat
{
    public static final String ID = CreateFullID(HornedBat_P.class);

    public HornedBat_P(CommonMoveset commonMoveset, float x, float y)
    {
        super(new Data(ID), x, y);

        moveset.AddSpecial(new Move_TemporaryConfusion());
        moveset.AddSpecial(new Move_StrengthLoss(1, false));
        
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
