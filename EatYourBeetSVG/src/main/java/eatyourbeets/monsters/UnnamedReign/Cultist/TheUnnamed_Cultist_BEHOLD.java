package eatyourbeets.monsters.UnnamedReign.Cultist;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.EscapeAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.BorderLongFlashEffect;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.actions.SummonMonsterAction;
import eatyourbeets.actions.WaitRealtimeAction;
import eatyourbeets.monsters.AbstractMove;
import eatyourbeets.monsters.UnnamedReign.Crystal.UltimateCrystal;
import eatyourbeets.monsters.UnnamedReign.Cultist.Moveset.Move_Talk;

import java.util.ArrayList;

public class TheUnnamed_Cultist_BEHOLD extends TheUnnamed_Cultist
{
    private final ArrayList<AbstractMove> moveset = new ArrayList<>();

    public TheUnnamed_Cultist_BEHOLD(float x, float y)
    {
        super(x, y);

        int level = AbstractDungeon.ascensionLevel;

        moveset.add(new Move_Talk(0, level, this));
    }

    @Override
    public void usePreBattleAction()
    {
        super.usePreBattleAction();

        GameActionsHelper.AddToBottom(new WaitRealtimeAction(1f));
        GameActionsHelper.AddToBottom(new TalkAction(this, "BEHOLD! The Ultimate Crystal!", 0.5f, 2f));
        GameActionsHelper.AddToBottom(new WaitRealtimeAction(2f));
        GameActionsHelper.AddToBottom(new EscapeAction(this));
        GameActionsHelper.AddToBottom(new SFXAction("ORB_LIGHTNING_EVOKE", 0.1f));
        GameActionsHelper.AddToBottom(new VFXAction(new BorderLongFlashEffect(Color.valueOf("3d0066"))));
        GameActionsHelper.AddToBottom(new SFXAction("ORB_DARK_EVOKE", 0.1f));
        GameActionsHelper.AddToBottom(new SummonMonsterAction(new UltimateCrystal(), false));
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
        moveset.get(0).SetMove();
    }
}
