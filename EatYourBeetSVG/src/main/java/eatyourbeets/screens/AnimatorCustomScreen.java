package eatyourbeets.screens;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.resources.GR;

public class AnimatorCustomScreen
{
    public static AbstractDungeon.CurrentScreen ScreenType = GR.Enums.Screens.ANIMATOR_SCREEN;

    public void Open()
    {
        AbstractDungeon.player.releaseCard();
        CardCrawlGame.sound.play("DECK_OPEN");
//        this.currentDiffY = this.scrollLowerBound;
//        this.grabStartY = this.scrollLowerBound;
//        this.grabbedScreen = false;
//        this.hideCards();
        AbstractDungeon.dynamicBanner.hide();
        AbstractDungeon.isScreenUp = true;
        AbstractDungeon.screen = ScreenType;
        AbstractDungeon.overlayMenu.proceedButton.hide();
        AbstractDungeon.overlayMenu.hideCombatPanels();
        AbstractDungeon.overlayMenu.showBlackScreen();
//        AbstractDungeon.overlayMenu.cancelButton.show(TEXT[1]);
//        this.calculateScrollBounds();
    }

    public void Update()
    {

    }

    public void Render(SpriteBatch sb)
    {

    }
}
