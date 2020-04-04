package eatyourbeets.monsters.UnnamedReign.UnnamedCultist;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.EscapeAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.BorderLongFlashEffect;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.actions.monsters.SummonMonsterAction;
import eatyourbeets.monsters.UnnamedReign.Shapes.Crystal.UltimateCrystal;
import eatyourbeets.monsters.SharedMoveset_Old.Move_Talk;

public class TheUnnamed_Cultist_BEHOLD extends TheUnnamed_Cultist
{
    public TheUnnamed_Cultist_BEHOLD(float x, float y)
    {
        super(x, y);

        moveset.AddNormal(new Move_Talk());
        moveset.AddNormal(new Move_Talk());
    }

    @Override
    public void usePreBattleAction()
    {
        super.usePreBattleAction();

        AbstractDungeon.scene.fadeOutAmbiance();
        CardCrawlGame.music.silenceBGM();

        GameActions.Bottom.WaitRealtime(1f);
        GameActions.Bottom.Talk(this, data.strings.DIALOG[0], 0.5f, 2f);
        GameActions.Bottom.WaitRealtime(2f);
        GameActions.Bottom.Add(new EscapeAction(this));
        GameActions.Bottom.SFX("ORB_LIGHTNING_EVOKE", 0.1f);
        GameActions.Bottom.VFX(new BorderLongFlashEffect(Color.valueOf("3d0066")));
        GameActions.Bottom.SFX("ORB_DARK_EVOKE", 0.1f);
        GameActions.Bottom.Add(new SummonMonsterAction(new UltimateCrystal(), false));
    }
}
