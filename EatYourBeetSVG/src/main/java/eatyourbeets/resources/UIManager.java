package eatyourbeets.resources;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import eatyourbeets.ui.screens.AbstractScreen;
import eatyourbeets.ui.screens.animator.seriesSelection.SeriesSelectionScreen;

public class UIManager
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
