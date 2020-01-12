package eatyourbeets.screens;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameUtilities;

public abstract class AbstractScreen
{
    public static final AbstractDungeon.CurrentScreen ScreenType = GR.Enums.Screens.EYB_SCREEN;

    public boolean CanOpen()
    {
        return IsNullOrNone(AbstractDungeon.previousScreen) && !CardCrawlGame.isPopupOpen;
    }

    protected void Open()
    {
        GR.Screens.CurrentScreen = this;
        Settings.hideTopBar = true;
        Settings.hideRelics = true;

        AbstractDungeon.previousScreen = AbstractDungeon.screen;
        AbstractDungeon.screen = ScreenType;
        AbstractDungeon.isScreenUp = true;

        if (GameUtilities.InBattle())
        {
            AbstractDungeon.player.releaseCard();
            AbstractDungeon.overlayMenu.hideCombatPanels();
        }

        AbstractDungeon.topPanel.unhoverHitboxes();
        AbstractDungeon.topPanel.potionUi.isHidden = true;
        AbstractDungeon.dynamicBanner.hide();
        AbstractDungeon.overlayMenu.proceedButton.hide();
        AbstractDungeon.overlayMenu.cancelButton.hide();
        AbstractDungeon.overlayMenu.showBlackScreen(0.7f);

        CardCrawlGame.sound.play("DECK_OPEN");
    }

    // Modified Logic from AbstractDungeon.closeCurrentScreen and AbstractDungeon.genericScreenOverlayReset
    public void Dispose()
    {
        GR.Screens.CurrentScreen = null;
        Settings.hideTopBar = false;
        Settings.hideRelics = false;

        AbstractDungeon.CurrentScreen previous = AbstractDungeon.previousScreen;
        if (previous == AbstractDungeon.CurrentScreen.NONE)
        {
            AbstractDungeon.previousScreen = null;
            AbstractDungeon.screen = previous;
        }

        if (IsNullOrNone(previous) || previous == ScreenType)
        {
            if (AbstractDungeon.player.isDead)
            {
                AbstractDungeon.previousScreen = AbstractDungeon.CurrentScreen.DEATH;
            }
            else
            {
                AbstractDungeon.isScreenUp = false;
                AbstractDungeon.overlayMenu.hideBlackScreen();
            }
        }

        AbstractDungeon.overlayMenu.cancelButton.hide();

        if (GameUtilities.InBattle())
        {
            AbstractDungeon.overlayMenu.showCombatPanels();
        }
    }

    public void Update()
    {

    }

    public void Render(SpriteBatch sb)
    {

    }

    protected static boolean IsNullOrNone(AbstractDungeon.CurrentScreen screen)
    {
        return screen == null || screen == AbstractDungeon.CurrentScreen.NONE;
    }
}
