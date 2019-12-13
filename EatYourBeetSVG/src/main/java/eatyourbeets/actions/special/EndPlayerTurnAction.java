package eatyourbeets.actions.special;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ExhaustCardEffect;
import com.megacrit.cardcrawl.vfx.combat.TimeWarpTurnEndEffect;
import eatyourbeets.actions.EYBAction;
import eatyourbeets.utilities.GameEffects;

public class EndPlayerTurnAction extends EYBAction
{
    public EndPlayerTurnAction()
    {
        super(ActionType.SPECIAL);

        Initialize(1);
    }

    @Override
    protected void FirstUpdate()
    {
        Complete();

        if (AbstractDungeon.actionManager.turnHasEnded)
        {
            return;
        }

        AbstractDungeon.actionManager.cardQueue.clear();

        for (AbstractCard c : AbstractDungeon.player.limbo.group)
        {
            GameEffects.List.Add(new ExhaustCardEffect(c));
        }

        AbstractDungeon.player.limbo.group.clear();
        AbstractDungeon.player.releaseCard();
        AbstractDungeon.overlayMenu.endTurnButton.disable(true);
        CardCrawlGame.sound.play("POWER_TIME_WARP", 0.05F);
        AbstractDungeon.effectsQueue.add(new BorderFlashEffect(Color.GOLD, true));
        AbstractDungeon.topLevelEffectsQueue.add(new TimeWarpTurnEndEffect());
    }
}
