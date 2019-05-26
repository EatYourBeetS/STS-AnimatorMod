package eatyourbeets.monsters.UnnamedReign.Cultist;

import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.actions.SummonMonsterAction;
import eatyourbeets.actions.WaitRealtimeAction;
import eatyourbeets.monsters.AbstractMove;
import eatyourbeets.monsters.UnnamedReign.Cultist.Moveset.Move_GuardedAttack;
import eatyourbeets.monsters.UnnamedReign.Cultist.Moveset.Move_Talk;
import eatyourbeets.monsters.UnnamedReign.UnnamedDoll.TheUnnamed_Doll;

import java.util.ArrayList;

public class TheUnnamed_Cultist_DollSummoner extends TheUnnamed_Cultist
{
    private final ArrayList<AbstractMove> moveset = new ArrayList<>();

    private Move_Talk moveTalk = new Move_Talk(0, 0, this);

    private boolean summoningDoll = false;

    public TheUnnamed_Cultist_DollSummoner(float x, float y)
    {
        super(x, y);

        int level = AbstractDungeon.ascensionLevel;

        moveset.add(moveTalk);
        moveset.add(new Move_GuardedAttack(1, 18, 6, this));
    }

    @Override
    public void usePreBattleAction()
    {
        super.usePreBattleAction();

        GameActionsHelper.AddToBottom(new WaitRealtimeAction(0.2f));
        GameActionsHelper.AddToBottom(new TalkAction(this, "I have a surprise for you, give me some time to... prepare.", 1f, 2.5f));
    }

    @Override
    public void takeTurn()
    {
        moveset.get(nextMove).Execute(AbstractDungeon.player);

        if (summoningDoll)
        {
            GameActionsHelper.AddToBottom(new TalkAction(this, "BEHOLD! The Ultimate Weapon!", 0.5f, 2f));
            GameActionsHelper.AddToBottom(new WaitRealtimeAction(2f));
            GameActionsHelper.AddToBottom(new SummonMonsterAction(new TheUnnamed_Doll(-40, 135, null)));
            summoningDoll = false;
        }

        GameActionsHelper.AddToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(int i)
    {
        int history = moveHistory.size();

        if (!summoningDoll)
        {
            if (history == 0)
            {
                moveTalk.SetLine("Not ready yet!");
                moveTalk.SetMove();
            }
            else if (history == 1)
            {
                moveTalk.SetLine("Almost Ready!");
                moveTalk.SetMove();
            }
            else if (history == 2)
            {
                moveTalk.SetLine("Here it is!");
                moveTalk.SetMove();

                moveTalk.disabled = true;
                summoningDoll = true;
            }
        }
    }
}
