package eatyourbeets.resources;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import eatyourbeets.interfaces.delegates.ActionT1;
import eatyourbeets.ui.AbstractScreen;
import eatyourbeets.ui.EYBCombatScreen;
import eatyourbeets.ui.animator.cardReward.AnimatorCardRewardAffinities;
import eatyourbeets.ui.animator.seriesSelection.AnimatorSeriesSelectScreen;
import eatyourbeets.ui.common.EYBSingleCardPopup;

import java.util.ArrayList;

public class UIManager
{
    protected final ArrayList<ActionT1<SpriteBatch>> postRenderList = new ArrayList<>();
    protected float timer = 0;
    protected boolean isDragging;

    public EYBCombatScreen CombatScreen;
    public EYBSingleCardPopup CardPopup;
    public AbstractScreen CurrentScreen;
    public AnimatorSeriesSelectScreen SeriesSelection;
    public AnimatorCardRewardAffinities CardAffinities;

    public void Initialize()
    {
        CombatScreen = new EYBCombatScreen();
        CardPopup = new EYBSingleCardPopup();
        SeriesSelection = new AnimatorSeriesSelectScreen();
        CardAffinities = new AnimatorCardRewardAffinities();
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
        timer += Gdx.graphics.getRawDeltaTime();
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

    public float Time_Sin(float distance, float speed)
    {
        return MathUtils.sin(timer * speed) * distance;
    }

    public float Time_Cos(float distance, float speed)
    {
        return MathUtils.cos(timer * speed) * distance;
    }

    public float Time_Multi(float value)
    {
        return timer * value;
    }

    public void AddPostRender(ActionT1<SpriteBatch> postRender)
    {
        postRenderList.add(postRender);
    }
}
