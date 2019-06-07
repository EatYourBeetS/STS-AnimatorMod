package eatyourbeets.monsters.UnnamedReign.Shapes.Crystal;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.monsters.SharedMoveset.*;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterElement;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterTier;
import eatyourbeets.powers.UnnamedReign.FireCrystalPower;

public class FireCrystal extends Crystal
{
    public FireCrystal(MonsterTier tier, float x, float y)
    {
        super(MonsterElement.Fire, tier, x, y);

        int level = AbstractDungeon.ascensionLevel;

        moveset.AddNormal(new Move_Attack( tier.Add(10,4)));
        moveset.AddNormal(new Move_AttackWeak( tier.Add(6,3),1));
        moveset.AddNormal(new Move_Defend( tier.Add(7,3)));
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

        GameActionsHelper.ApplyPower(this, this, new FireCrystalPower(this, amount), amount);
    }
}
