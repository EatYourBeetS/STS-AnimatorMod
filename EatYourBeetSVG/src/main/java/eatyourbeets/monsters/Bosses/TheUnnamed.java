package eatyourbeets.monsters.Bosses;

import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.monsters.Bosses.TheUnnamedMoveset.*;
import eatyourbeets.powers.InfinitePower;

import java.util.ArrayList;

public class TheUnnamed extends CustomMonster
{
    private static final String MODEL_ATLAS = "images/monsters/Animator_TheUnnamed/TheUnnamed.atlas";
    private static final String MODEL_JSON = "images/monsters/Animator_TheUnnamed/TheUnnamed.json";

    public static final String ID = "Animator_TheUnnamed";
    public static final String NAME = "The Unnamed";

    private final ArrayList<Move> moveset = new ArrayList<>();

    private static int GetMaxHealth()
    {
        return 666;
    }

    public boolean appliedFading = false;
    public int minionsCount = 3;
    public final AbstractMonster[] minions = new AbstractMonster[3];

    public TheUnnamed()
    {
        super(NAME, ID, GetMaxHealth(), 0.0F, -20.0F, 200.0F, 260.0f, null, 0, 80.0F);

        loadAnimation(MODEL_ATLAS, MODEL_JSON, 1);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "Idle", true);
        e.setTime(e.getEndTime() * MathUtils.random());

        this.type = EnemyType.BOSS;

        int level = AbstractDungeon.ascensionLevel;

        moveset.add(new Move_Summon(0, level, this));
        moveset.add(new Move_Fading(1, level, this));
        moveset.add(new Move_SingleAttack(2, level, this));
        moveset.add(new Move_Talk(3, level, this));
        moveset.add(new Move_MultiAttack(4, level, this));
        moveset.add(new Move_Poison(5, level, this));
    }

    @Override
    public void die()
    {
        super.die();

        CardCrawlGame.music.silenceTempBgmInstantly();
        CardCrawlGame.music.silenceBGMInstantly();
        playBossStinger();
    }

    @Override
    public void usePreBattleAction()
    {
        super.usePreBattleAction();

        GameActionsHelper.ApplyPower(this, this, new InfinitePower(this), 1);

        AbstractDungeon.getCurrMapNode().room.playBGM("ANIMATOR_THE_UNNAMED.ogg");
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
        int size = moveHistory.size();
        if (size < 3)
        {
            moveset.get(0).SetMove();
            return;
        }
        else if (!appliedFading && minionsCount <= 0 && currentHealth < maxHealth / 2)
        {
            moveset.get(1).SetMove();
            return;
        }

        Byte previousMove = moveHistory.get(size - 1);

        if (previousMove == 3)
        {
            moveset.get(5).SetMove();
            return;
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
