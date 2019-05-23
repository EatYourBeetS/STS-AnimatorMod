package eatyourbeets.monsters.UnnamedReign.Crystal;

import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.BobEffect;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.monsters.Bosses.TheUnnamedMinionMoveset.Move;

import java.util.ArrayList;

public class Crystal extends CustomMonster
{
    private static final String MODEL_ATLAS = "images/monsters/Animator_TheUnnamed/TheUnnamedMinion.atlas";
    private static final String MODEL_JSON = "images/monsters/Animator_TheUnnamed/TheUnnamedMinion.json";

    public static final String ID = "Animator_Cube_Dark_0";
    public static final String NAME = "";

    private final ArrayList<Move> moveset = new ArrayList<>();
    private final BobEffect bobEffect = new BobEffect(1);

    private static int GetMaxHealth()
    {
        return 180;
    }

    public Crystal(float x, float y)
    {
        super(NAME, ID, GetMaxHealth(), 0.0F, -20.0F, 120.0F, 140.0f, null, x, y + 60.0F);

        this.type = EnemyType.NORMAL;

        int level = AbstractDungeon.ascensionLevel;

//        moveset.add(new Move_Shield(0, level, this, theUnnamed));
//        moveset.add(new Move_BuffArtifact(1, level, this, theUnnamed));
//        moveset.add(new Move_BuffThorns(2, level, this, theUnnamed));
//        moveset.add(new Move_DebuffVulnerable(3, level, this, theUnnamed));
//        moveset.add(new Move_DebuffWeak(4, level, this, theUnnamed));
    }

    @Override
    public void render(SpriteBatch sb)
    {
        animY = this.bobEffect.y;
        super.render(sb);
    }

    @Override
    public void update()
    {
        this.bobEffect.update();
        super.update();
    }

    @Override
    public void takeTurn()
    {
        moveset.get(nextMove).Execute(AbstractDungeon.player);

        GameActionsHelper.AddToBottom(new RollMoveAction(this));
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

        ArrayList<Move> moves = new ArrayList<>();
        for (Move move : moveset)
        {
            if (move.CanUse(previousMove))
            {
                moves.add(move);
            }
        }

        moves.get(i % moves.size()).SetMove();
    }
}
