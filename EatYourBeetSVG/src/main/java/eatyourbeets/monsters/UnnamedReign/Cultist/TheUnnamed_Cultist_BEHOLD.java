package eatyourbeets.monsters.UnnamedReign.Cultist;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.EscapeAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.BorderLongFlashEffect;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.actions.SummonMonsterAction;
import eatyourbeets.actions.WaitRealtimeAction;
import eatyourbeets.monsters.UnnamedReign.Shapes.Crystal.UltimateCrystal;
import eatyourbeets.monsters.SharedMoveset.Move_Talk;

public class TheUnnamed_Cultist_BEHOLD extends TheUnnamed_Cultist
{
    public TheUnnamed_Cultist_BEHOLD(float x, float y)
    {
        super(x, y);

        int level = AbstractDungeon.ascensionLevel;

        moveset.AddNormal(new Move_Talk());
        moveset.AddNormal(new Move_Talk());
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
}
