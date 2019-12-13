package eatyourbeets.monsters;

import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.resources.AnimatorResources;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public abstract class AnimatorMonster extends CustomMonster
{
    protected static final Logger logger = LogManager.getLogger(AnimatorMonster.class.getName());

    public static String CreateFullID(String id)
    {
        return AnimatorResources.CreateID(id);
    }

    public final Moveset moveset = new Moveset(this);
    public final AbstractMonsterData data;

    public AnimatorMonster(AbstractMonsterData data, EnemyType type)
    {
        this(data, type, 0, 0);
    }

    public AnimatorMonster(AbstractMonsterData data, EnemyType type, float x, float y)
    {
        super(data.strings.NAME, data.id, data.maxHealth, data.hb_x, data.hb_y, data.hb_w, data.hb_h,
                data.imgUrl, data.offsetX + x, data.offsetY + y);

        this.data = data;
        this.type = type;
    }

    protected void ExecuteNextMove()
    {
        AbstractMove move = moveset.GetMove(nextMove);
        if (move != null)
        {

            moveset.GetMove(nextMove).Execute(AbstractDungeon.player);
        }
        else
        {
            GameUtilities.GetLogger(getClass()).warn("The move was not present in the moveset: " + nextMove);
        }
    }

    @Override
    public void takeTurn()
    {
        ExecuteNextMove();

        GameActions.Bottom.Add(new RollMoveAction(this));
    }

    protected void SetNextMove(int roll, int historySize, Byte previousMove)
    {
        moveset.GetNextMove(roll, previousMove).SetMove();
    }

    @Override
    protected void getMove(int i)
    {
        Byte previousMove = -1;
        int size = moveHistory.size();
        if (size > 0)
        {
            previousMove = moveHistory.get(size - 1);
        }

        SetNextMove(i, size, previousMove);
    }

    @Override
    public void loadAnimation(String atlasUrl, String skeletonUrl, float scale)
    {
        super.loadAnimation(atlasUrl, skeletonUrl, scale);
    }
}
