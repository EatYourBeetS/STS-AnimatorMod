package pinacolada.resources;

import basemod.DevConsole;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.Hitbox;
import eatyourbeets.interfaces.delegates.ActionT1;
import pinacolada.cards.base.PCLCardTooltip;
import pinacolada.powers.PCLCombatStats;
import pinacolada.ui.AbstractScreen;
import pinacolada.ui.cardReward.CardAffinityPanel;
import pinacolada.ui.characterSelection.PCLLoadoutEditor;
import pinacolada.ui.combat.PCLCombatScreen;
import pinacolada.ui.common.CardKeywordFilters;
import pinacolada.ui.common.CardPoolScreen;
import pinacolada.ui.common.CustomCardLibSortHeader;
import pinacolada.ui.common.EYBSingleCardPopup;
import pinacolada.ui.seriesSelection.PCLSeriesSelectScreen;
import pinacolada.utilities.PCLGameUtilities;

import java.util.ArrayList;

public class UIManager extends eatyourbeets.resources.UIManager
{
    // TODO merge with EYB UIManager
    protected final ArrayList<ActionT1<SpriteBatch>> preRenderList = new ArrayList<>();
    protected final ArrayList<ActionT1<SpriteBatch>> postRenderList = new ArrayList<>();
    protected float delta = 0;
    protected float timer = 0;
    protected float dropdownX = 0;
    protected float dropdownY = 0;
    protected boolean isDragging;
    protected Hitbox lastHovered;
    protected Hitbox lastHoveredTemp;

    public PCLCombatScreen CombatScreen;
    public EYBSingleCardPopup CardPopup;
    public AbstractScreen CurrentScreen;
    public CardPoolScreen CardsScreen;
    public PCLSeriesSelectScreen SeriesSelection;
    public PCLLoadoutEditor LoadoutEditor;
    public CardAffinityPanel CardAffinities;
    public CardKeywordFilters CardFilters;
    public CustomCardLibSortHeader CustomHeader;
    public boolean IsDropdownOpen;

    public void Initialize()
    {
        CardAffinities = new CardAffinityPanel();
        CombatScreen = new PCLCombatScreen();
        CardPopup = new EYBSingleCardPopup();
        CardsScreen = new CardPoolScreen();
        SeriesSelection = new PCLSeriesSelectScreen();
        LoadoutEditor = new PCLLoadoutEditor();
        CardFilters = new CardKeywordFilters();
        CustomHeader = new CustomCardLibSortHeader(null);
    }

    public void Dispose()
    {
        if (CurrentScreen != null)
        {
            CurrentScreen.Dispose();
            IsDropdownOpen = false;
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
            PCLCombatStats.RefreshPCL();

            // Power description updates are handled by the base mod's GR
        }
    }

    public void Update()
    {
        if ((Settings.isDebug || DevConsole.infiniteEnergy) && PCLGameUtilities.InGame())
        {
            GR.PCL.Dungeon.SetCheating();
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
            PCLCardTooltip.CanRenderTooltips(false);
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
