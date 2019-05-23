package eatyourbeets.monsters.UnnamedReign.Wisp;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.monsters.Bosses.TheUnnamedMinionMoveset.Move;

import java.util.ArrayList;

public class HealingWisp extends Wisp
{
    public static final String ID = "Animator_Cube_Dark_0";
    public static final String NAME = "";

    private final ArrayList<Move> moveset = new ArrayList<>();

    public HealingWisp(float x, float y)
    {
        super(x, y);

        this.type = EnemyType.NORMAL;

        int level = AbstractDungeon.ascensionLevel;

//        moveset.add(new Move_Shield(0, level, this, theUnnamed));
//        moveset.add(new Move_BuffArtifact(1, level, this, theUnnamed));
//        moveset.add(new Move_BuffThorns(2, level, this, theUnnamed));
//        moveset.add(new Move_DebuffVulnerable(3, level, this, theUnnamed));
//        moveset.add(new Move_DebuffWeak(4, level, this, theUnnamed));
    }
}
