package eatyourbeets.monsters.Elites;

import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import eatyourbeets.interfaces.subscribers.OnReceiveEmeraldBonus;
import eatyourbeets.monsters.EYBMonster;
import eatyourbeets.monsters.EYBMonsterData;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.common.PlayerFlightPower;
import eatyourbeets.utilities.GameActions;

import java.util.ArrayList;
import java.util.List;

public abstract class HornedBat extends EYBMonster implements OnReceiveEmeraldBonus
{
    public static final String ID = CreateFullID(HornedBat.class);

    protected final static List<Vector2> positions = new ArrayList<>();

    public static MonsterGroup CreateMonsterGroup()
    {
        if (positions.isEmpty())
        {
            int x;
            positions.add(new Vector2(x =215, 8));
            positions.add(new Vector2(x-=185, -55));
            positions.add(new Vector2(x-=185, 7));
            positions.add(new Vector2(x-=185, -56));
            positions.add(new Vector2(x-=185, 8));
        }

        final AbstractMonster[] m = new AbstractMonster[positions.size()];
        m[0] = new HornedBat_R(positions.get(4).x, positions.get(4).y);
        m[1] = new HornedBat_P(positions.get(3).x, positions.get(3).y);
        m[2] = new HornedBat_R(positions.get(2).x, positions.get(2).y);
        m[3] = new HornedBat_P(positions.get(1).x, positions.get(1).y);
        m[4] = new HornedBat_R(positions.get(0).x, positions.get(0).y);
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

    protected static class TurnData
    {
        public boolean UsedStrengthLoss;
        public boolean UsedConfusion;
        public int TotalAttacks;

        public static TurnData Get()
        {
            TurnData data = CombatStats.GetTurnData(HornedBat.ID, null);
            if (data == null)
            {
                return CombatStats.SetTurnData(HornedBat.ID, new TurnData());
            }

            return data;
        }
    }

    @Override
    public float GetEmeraldMaxHPBonus(float bonus)
    {
        return bonus * 0.8f;
    }

    @Override
    public int GetEmeraldMetallicizeBonus(int bonus)
    {
        return bonus - 2;
    }

    @Override
    public int GetEmeraldRegenBonus(int bonus)
    {
        return bonus - 1;
    }

    @Override
    public int GetEmeraldStrengthBonus(int bonus)
    {
        return bonus - 1;
    }
}
