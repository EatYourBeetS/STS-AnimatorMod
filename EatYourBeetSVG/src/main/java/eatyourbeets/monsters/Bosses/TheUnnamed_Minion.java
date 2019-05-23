package eatyourbeets.monsters.Bosses;

import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.BobEffect;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.monsters.Bosses.TheUnnamedMinionMoveset.*;
import eatyourbeets.powers.UnnamedDollPower;

import java.util.ArrayList;

public class TheUnnamed_Minion extends CustomMonster
{
    private static final String MODEL_ATLAS = "images/monsters/Animator_TheUnnamed/TheUnnamedMinion.atlas";
    private static final String MODEL_JSON = "images/monsters/Animator_TheUnnamed/TheUnnamedMinion.json";

    public static final String ID = "Animator_TheUnnamedMinion";
    public static final String NAME = "";

    private final ArrayList<Move> moveset = new ArrayList<>();
    private final BobEffect bobEffect = new BobEffect(1);

    private static int GetMaxHealth()
    {
        return 180;
    }

    private final TheUnnamed theUnnamed;

    public TheUnnamed_Minion(TheUnnamed theUnnamed)
    {
        this(0, 0, theUnnamed);
    }

    public TheUnnamed_Minion(float x, float y, TheUnnamed theUnnamed)
    {
        super(NAME, ID, GetMaxHealth(), 0.0F, -20.0F, 120.0F, 140.0f, null, x, y + 60.0F);

        this.theUnnamed = theUnnamed;

        loadAnimation(MODEL_ATLAS, MODEL_JSON, 2);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "Idle", true);
        e.setTime(e.getEndTime() * MathUtils.random());

        this.type = EnemyType.NORMAL;

        int level = AbstractDungeon.ascensionLevel;

        moveset.add(new Move_Shield(0, level, this, theUnnamed));
        moveset.add(new Move_BuffArtifact(1, level, this, theUnnamed));
        moveset.add(new Move_BuffThorns(2, level, this, theUnnamed));
        moveset.add(new Move_DebuffVulnerable(3, level, this, theUnnamed));
        moveset.add(new Move_DebuffWeak(4, level, this, theUnnamed));
    }

    @Override
    public void init()
    {
        super.init();

        GameActionsHelper.ApplyPower(this, this, new UnnamedDollPower(this, 30), 30);
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
    public void die()
    {
        super.die();

        theUnnamed.minionsCount -= 1;
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
