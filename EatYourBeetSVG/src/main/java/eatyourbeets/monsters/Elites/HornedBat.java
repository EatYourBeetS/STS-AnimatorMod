package eatyourbeets.monsters.Elites;

import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import eatyourbeets.monsters.EYBMonsterData;
import eatyourbeets.monsters.EYBAbstractMove;
import eatyourbeets.monsters.EYBMonster;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.powers.common.PlayerFlightPower;
import eatyourbeets.utilities.GameActions;

import java.util.ArrayList;
import java.util.List;

public abstract class HornedBat extends EYBMonster
{
    protected final static List<Vector2> positions = new ArrayList<>();

    public static final String ID = CreateFullID(HornedBat.class);

    public static MonsterGroup CreateMonsterGroup()
    {
        if (positions.isEmpty())
        {
            int x;
            positions.add(new Vector2(x =170, 2));
            positions.add(new Vector2(x-=170, -49));
            positions.add(new Vector2(x-=170, 4));
            positions.add(new Vector2(x-=170, -51));
            positions.add(new Vector2(x-=170, 3));
        }

        final CommonMoveset commonMoveset = new CommonMoveset();
        final AbstractMonster[] m = new AbstractMonster[positions.size()];
        if (AbstractDungeon.miscRng == null || AbstractDungeon.miscRng.random.nextBoolean())
        {
            m[0] = new HornedBat_P(commonMoveset, positions.get(0).x, positions.get(0).y);
            m[1] = new HornedBat_R(commonMoveset, positions.get(1).x, positions.get(1).y);
            m[2] = new HornedBat_P(commonMoveset, positions.get(2).x, positions.get(2).y);
            m[3] = new HornedBat_R(commonMoveset, positions.get(3).x, positions.get(3).y);
            m[4] = new HornedBat_P(commonMoveset, positions.get(4).x, positions.get(4).y);
        }
        else
        {
            m[0] = new HornedBat_R(commonMoveset, positions.get(0).x, positions.get(0).y);
            m[1] = new HornedBat_P(commonMoveset, positions.get(1).x, positions.get(1).y);
            m[2] = new HornedBat_R(commonMoveset, positions.get(2).x, positions.get(2).y);
            m[3] = new HornedBat_P(commonMoveset, positions.get(3).x, positions.get(3).y);
            m[4] = new HornedBat_R(commonMoveset, positions.get(4).x, positions.get(4).y);
        }

        return new MonsterGroup(m);
    }

    public HornedBat(EYBMonsterData data, float x, float y)
    {
        super(data, EnemyType.ELITE, x, y);

        this.data.SetIdleAnimation(this, 1);
    }

    @Override
    public void usePreBattleAction()
    {
        super.usePreBattleAction();

        GameActions.Bottom.StackPower(new PlayerFlightPower(this, 5));
    }

    protected static class CommonMoveset
    {
        protected int moveOffset;
        protected int index = 0;

        public CommonMoveset()
        {
            moveOffset = AbstractDungeon.aiRng.random(100);
        }

        public EYBAbstractMove GetNextMove(HornedBat owner)
        {
            int offset = PlayerStatistics.TurnCount() + moveOffset;

            return owner.moveset.rotation.get(offset % owner.moveset.rotation.size());
        }
    }
}
