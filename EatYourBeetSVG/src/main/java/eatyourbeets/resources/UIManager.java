package eatyourbeets.resources;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import eatyourbeets.interfaces.delegates.ActionT1;
import eatyourbeets.ui.AbstractScreen;
import eatyourbeets.ui.animator.cardReward.AnimatorCardRewardAffinities;
import eatyourbeets.ui.animator.combat.EYBCombatScreen;
import eatyourbeets.ui.animator.seriesSelection.AnimatorSeriesSelectScreen;
import eatyourbeets.ui.common.EYBSingleCardPopup;

import java.util.ArrayList;

public class UIManager
{
    protected final ArrayList<ActionT1<SpriteBatch>> preRenderList = new ArrayList<>();
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
        CardAffinities = new AnimatorCardRewardAffinities();
        CombatScreen = new EYBCombatScreen();
        CardPopup = new EYBSingleCardPopup();
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
        timer += Gdx.graphics.getRawDeltaTime();
        isDragging = false;

        if (CurrentScreen != null)
        {
            CurrentScreen.Update();
        }

        CombatScreen.TryUpdate();
        CardPopup.TryUpdate();
    }

    public void PreRender(SpriteBatch sb)
    {
        for (ActionT1<SpriteBatch> toRender : preRenderList)
        {
            toRender.Invoke(sb);
        }

        preRenderList.clear();
    }

    public void Render(SpriteBatch sb)
    {
        if (CurrentScreen != null)
        {
            CurrentScreen.Render(sb);
        }

        CardPopup.TryRender(sb);
    }

    public void PostRender(SpriteBatch sb)
    {
        for (ActionT1<SpriteBatch> toRender : postRenderList)
        {
            toRender.Invoke(sb);
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

    public void AddPreRender(ActionT1<SpriteBatch> toRender)
    {
        preRenderList.add(toRender);
    }

    public void AddPostRender(ActionT1<SpriteBatch> toRender)
    {
        postRenderList.add(toRender);
    }
}
