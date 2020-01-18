package eatyourbeets.resources;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import eatyourbeets.ui.screens.AbstractScreen;
import eatyourbeets.ui.screens.animator.seriesSelection.AnimatorSeriesSelectScreen;

public class UIManager
{
    protected boolean isDragging;

    public AbstractScreen CurrentScreen;
    public AnimatorSeriesSelectScreen SeriesSelection;

    public void Initialize()
    {
        SeriesSelection = new AnimatorSeriesSelectScreen();
    }

    public void Dispose()
    {
        if (CurrentScreen != null)
        {
            CurrentScreen.Dispose();
        }
        CurrentScreen = null;
    }

    public void Update()
    {
        isDragging = false;

        if (CurrentScreen != null)
        {
            CurrentScreen.Update();
        }
    }

    public void Render(SpriteBatch sb)
    {
        if (CurrentScreen != null)
        {
            CurrentScreen.Render(sb);
        }
    }

    public boolean TryDragging()
    {
        return !CardCrawlGame.isPopupOpen && (CurrentScreen == null || !isDragging) && (isDragging = true);
    }
}
