package eatyourbeets.screens;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import eatyourbeets.screens.animator.SeriesSelectionScreen;

public class ScreenManager
{
    protected boolean isDragging;

    public AbstractScreen CurrentScreen;
    public SeriesSelectionScreen SeriesSelection;

    public void Initialize()
    {
        SeriesSelection = new SeriesSelectionScreen();
    }

    public void Dispose()
    {
        if (CurrentScreen != null)
        {
            CurrentScreen.Dispose();
        }
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
        return (!CardCrawlGame.isPopupOpen && !isDragging) && (isDragging = true);
    }
}
