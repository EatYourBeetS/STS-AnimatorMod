package eatyourbeets.resources;

import basemod.DevConsole;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import eatyourbeets.cards.base.EYBCardTooltip;
import eatyourbeets.interfaces.delegates.ActionT1;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.ui.AbstractScreen;
import eatyourbeets.ui.animator.cardReward.CardAffinityPanel;
import eatyourbeets.ui.animator.characterSelection.AnimatorLoadoutEditor;
import eatyourbeets.ui.animator.combat.EYBCombatScreen;
import eatyourbeets.ui.animator.seriesSelection.AnimatorSeriesSelectScreen;
import eatyourbeets.ui.common.CardKeywordFilters;
import eatyourbeets.ui.common.EYBSingleCardPopup;
import eatyourbeets.utilities.GameUtilities;

import java.util.ArrayList;

public class UIManager
{
    protected final ArrayList<ActionT1<SpriteBatch>> preRenderList = new ArrayList<>();
    protected final ArrayList<ActionT1<SpriteBatch>> postRenderList = new ArrayList<>();
    protected float delta = 0;
    protected float timer = 0;
    protected boolean isDragging;
    protected Hitbox lastHovered;
    protected Hitbox lastHoveredTemp;

    public EYBCombatScreen CombatScreen;
    public EYBSingleCardPopup CardPopup;
    public AbstractScreen CurrentScreen;
    public AnimatorSeriesSelectScreen SeriesSelection;
    public AnimatorLoadoutEditor LoadoutEditor;
    public CardAffinityPanel CardAffinities;
    public CardKeywordFilters CardFilters;

    public void Initialize()
    {
        CardAffinities = new CardAffinityPanel();
        CombatScreen = new EYBCombatScreen();
        CardPopup = new EYBSingleCardPopup();
        SeriesSelection = new AnimatorSeriesSelectScreen();
        LoadoutEditor = new AnimatorLoadoutEditor();
        CardFilters = new CardKeywordFilters();
    }

    public void Dispose()
    {
        if (CurrentScreen != null)
        {
            CurrentScreen.Dispose();
        }

        CurrentScreen = null;
        lastHovered = null;
    }

    public void PreUpdate()
    {
        delta = Gdx.graphics.getRawDeltaTime();
        timer += delta;
        isDragging = false;
        lastHoveredTemp = null;

        if (Elapsed(0.4f))
        {
            CombatStats.Refresh();

            if (Elapsed(1.2f) && CombatStats.BattleID != null && AbstractDungeon.actionManager.phase == GameActionManager.Phase.WAITING_ON_USER)
            {
                GameUtilities.UpdatePowerDescriptions();
            }
        }
    }

    public void Update()
    {
        if ((Settings.isDebug || DevConsole.infiniteEnergy) && GameUtilities.InGame())
        {
            GR.Common.Dungeon.SetCheating();
        }

        if (CurrentScreen != null)
        {
            CurrentScreen.Update();
        }

        CombatScreen.TryUpdate();
        CardPopup.TryUpdate();
    }

    public void PostUpdate()
    {
        lastHovered = lastHoveredTemp;
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

    public boolean IsDragging()
    {
        return isDragging;
    }

    public boolean TryDragging()
    {
        final boolean drag = !CardCrawlGame.isPopupOpen && (CurrentScreen == null || !isDragging) && (isDragging = true);
        if (drag)
        {
            EYBCardTooltip.CanRenderTooltips(false);
        }

        return drag;
    }

    public boolean TryHover(Hitbox hitbox)
    {
        if (hitbox != null && hitbox.justHovered && hitbox != lastHovered)
        {
            hitbox.hovered = hitbox.justHovered = false;
            lastHoveredTemp = hitbox;
            return false;
        }

        if (hitbox == null || hitbox.hovered)
        {
            lastHoveredTemp = hitbox;
            return hitbox == lastHovered;
        }

        return false;
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

    public float Time()
    {
        return timer;
    }

    public float Delta()
    {
        return delta;
    }

    public float Delta(float multiplier)
    {
        return delta * multiplier;
    }

    public boolean Elapsed(float value)
    {
        return (delta >= value) || (((timer % value) - delta) < 0);
    }

    public boolean Elapsed25()
    {
        return Elapsed(0.25f);
    }

    public boolean Elapsed50()
    {
        return Elapsed(0.50f);
    }

    public boolean Elapsed75()
    {
        return Elapsed(0.75f);
    }

    public boolean Elapsed100()
    {
        return Elapsed(1.00f);
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
