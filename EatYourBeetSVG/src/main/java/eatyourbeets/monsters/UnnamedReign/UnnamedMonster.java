package eatyourbeets.monsters.UnnamedReign;

import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.monsters.AbstractMove;

import java.util.ArrayList;

public abstract class UnnamedMonster extends CustomMonster
{
    protected ArrayList<AbstractMove> moveset = new ArrayList<>();

    public MonsterData data;

    public static String CreateFullID(MonsterShape shape, MonsterElement element, MonsterTier tier)
    {
        return "Animator_" + element + "_" + shape + "_" +  tier.GetId();
    }

    public static String GetResourcePath(MonsterShape shape, MonsterElement element, MonsterTier tier)
    {
        return "images/monsters/Animator_" + shape + "/" + element + "_" + tier.GetId();
    }

    public UnnamedMonster(MonsterData data, float x, float y)
    {
        super(data.strings.NAME, data.id, data.maxHealth, data.hb_x, data.hb_y, data.hb_w, data.hb_h, data.imgUrl, data.offsetX + x, data.offsetY + y);

        this.data = data;

        if (data.tier == MonsterTier.Ultimate)
        {
            this.type = EnemyType.ELITE;
        }
        else
        {
            this.type = EnemyType.NORMAL;
        }
    }

    @Override
    public void takeTurn()
    {
        moveset.get(nextMove).Execute(AbstractDungeon.player);

        GameActionsHelper.AddToBottom(new RollMoveAction(this));
    }
}
