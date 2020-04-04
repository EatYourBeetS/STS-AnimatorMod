package eatyourbeets.monsters;

import basemod.BaseMod;
import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.TheCity;
import eatyourbeets.monsters.Bosses.KrulTepes;
import eatyourbeets.monsters.Elites.HornedBat;
import eatyourbeets.monsters.UnnamedReign.UnnamedEnemyGroup;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JavaUtilities;

import java.util.ArrayList;
import java.util.List;

public abstract class EYBMonster extends CustomMonster
{
    public static final List<EYBMonsterInfo> Encounters = new ArrayList<>();
    public final EYBMoveset moveset = new EYBMoveset(this);
    public final EYBMonsterData data;

    public static String CreateFullID(Class<? extends EYBMonster> type)
    {
        return GR.Animator.CreateID(type.getSimpleName());
    }

    public EYBMonster(EYBMonsterData data, EnemyType type)
    {
        this(data, type, 0, 0);
    }

    public EYBMonster(EYBMonsterData data, EnemyType type, float x, float y)
    {
        super(data.strings.NAME, data.id, data.maxHealth, data.hb_x, data.hb_y, data.hb_w, data.hb_h,
                data.imgUrl, data.offsetX + x, data.offsetY + y);

        this.data = data;
        this.type = type;
    }

    public static void RegisterMonsters()
    {
        UnnamedEnemyGroup.RegisterMonsterGroups();
        BaseMod.addMonster(KrulTepes.ID, KrulTepes::new);
        BaseMod.addMonster(HornedBat.ID, HornedBat::CreateMonsterGroup);

        Encounters.add(new EYBMonsterInfo(TheCity.ID, EnemyType.ELITE, HornedBat.ID, 0.8f));
    }

    protected void ExecuteNextMove()
    {
        EYBAbstractMove move = moveset.GetMove(nextMove);
        if (move != null)
        {
            moveset.GetMove(nextMove).Execute(AbstractDungeon.player);
        }
        else
        {
            JavaUtilities.GetLogger(getClass()).warn("The move was not present in the moveset: " + nextMove);
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
        moveset.FindNextMove(roll, previousMove).Select();
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
