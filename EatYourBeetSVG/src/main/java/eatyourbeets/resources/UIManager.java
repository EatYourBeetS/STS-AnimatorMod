package eatyourbeets.resources;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import eatyourbeets.interfaces.delegates.ActionT1;
import eatyourbeets.ui.AbstractScreen;
import eatyourbeets.ui.EYBCombatScreen;
import eatyourbeets.ui.animator.seriesSelection.AnimatorSeriesSelectScreen;
import eatyourbeets.ui.common.EYBSingleCardPopup;

import java.util.ArrayList;

public class UIManager
{
    protected final ArrayList<ActionT1<SpriteBatch>> postRenderList = new ArrayList<>();
    protected boolean isDragging;

    public EYBCombatScreen CombatScreen;
    public EYBSingleCardPopup CardPopup;
    public AbstractScreen CurrentScreen;
    public AnimatorSeriesSelectScreen SeriesSelection;

    public void Initialize()
    {
        CombatScreen = new EYBCombatScreen();
        SeriesSelection = new AnimatorSeriesSelectScreen();
        CardPopup = new EYBSingleCardPopup();
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

        CombatScreen.TryUpdate();
        CardPopup.TryUpdate();
    }

    public void Render(SpriteBatch sb)
    {
        if (CurrentScreen != null)
        {
            CurrentScreen.Render(sb);
        }

        CombatScreen.TryRender(sb);
        CardPopup.TryRender(sb);
    }

    public void PostRender(SpriteBatch sb)
    {
        for (ActionT1<SpriteBatch> postRender : postRenderList)
        {
            postRender.Invoke(sb);
        }

        postRenderList.clear();
    }

    public boolean TryDragging()
    {
        return !CardCrawlGame.isPopupOpen && (CurrentScreen == null || !isDragging) && (isDragging = true);
    }

    public void AddPostRender(ActionT1<SpriteBatch> postRender)
    {
        postRenderList.add(postRender);
    }
}
