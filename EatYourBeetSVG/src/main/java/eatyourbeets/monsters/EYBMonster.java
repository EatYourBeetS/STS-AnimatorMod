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
import eatyourbeets.utilities.JUtils;

import java.util.ArrayList;
import java.util.List;

public abstract class EYBMonster extends CustomMonster
{
    public static final List<EYBMonsterInfo> Encounters = new ArrayList<>();
    public final EYBMoveset moveset = new EYBMoveset(this);
    public final EYBMonsterData data;

    protected Byte previousMove = -1;

    public static String CreateFullID(Class<? extends EYBMonster> type)
    {
        return GR.Animator.CreateID(type.getSimpleName());
    }

    public static void RegisterMonsters()
    {
        UnnamedEnemyGroup.RegisterMonsterGroups();
        BaseMod.addMonster(KrulTepes.ID, KrulTepes::new);
        BaseMod.addMonster(HornedBat.ID, HornedBat::CreateMonsterGroup);

        Encounters.add(new EYBMonsterInfo(TheCity.ID, EnemyType.ELITE, HornedBat.ID, 0.8f));
        Encounters.add(new EYBMonsterInfo(TheCity.ID, EnemyType.NORMAL, UnnamedEnemyGroup.TWO_NORMAL_SHAPES, 1.8f, 13));
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

    @Override // This changes protected to public
    public void loadAnimation(String atlasUrl, String skeletonUrl, float scale)
    {
        super.loadAnimation(atlasUrl, skeletonUrl, scale);
    }

    @Override
    public void takeTurn()
    {
        final EYBAbstractMove move = moveset.GetMove(nextMove);
        if (move != null)
        {
            move.Execute(AbstractDungeon.player);
        }
        else
        {
            JUtils.LogWarning(this, "The move was not present in the moveset: " + nextMove);
        }

        GameActions.Bottom.Add(new RollMoveAction(this)); // This calls getMove(rng(0, 99))
    }

    @Override
    protected void getMove(int i)
    {
        int size = moveHistory.size();
        if (size > 0)
        {
            previousMove = moveHistory.get(size - 1);
        }

        SetNextMove(i, size);
    }

    protected void SetNextMove(int roll, int historySize)
    {
        moveset.FindNextMove(roll, previousMove).Select(false);
    }

    protected ArrayList<EYBAbstractMove> GetRotation()
    {
        return moveset.Normal.rotation;
    }
}
