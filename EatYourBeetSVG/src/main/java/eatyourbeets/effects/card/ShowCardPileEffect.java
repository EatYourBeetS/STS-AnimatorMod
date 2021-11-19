package eatyourbeets.effects.card;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import eatyourbeets.effects.EYBEffectWithCallback;
import eatyourbeets.interfaces.delegates.ActionT0;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.misc.AnimatorRuntimeLoadout;
import eatyourbeets.ui.controls.GUI_CardGrid;
import eatyourbeets.ui.controls.GUI_Toggle;
import eatyourbeets.utilities.EYBFontHelper;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.InputManager;

import java.util.ArrayList;

public class ShowCardPileEffect extends EYBEffectWithCallback<CardGroup>
{
    private static final float DUR = 1.5f;
    private static final GUI_Toggle upgradeToggle;
    private static final GUI_Toggle zoomToggle;
    private static final GUI_Toggle simplifyCardUIToggle;
    private static final GUI_Toggle toggleExpansion;
    static
    {
        upgradeToggle = new GUI_Toggle(new Hitbox(Settings.scale * 256f, Settings.scale * 48f))
        .SetBackground(GR.Common.Images.Panel.Texture(), Color.DARK_GRAY)
        .SetPosition(Settings.WIDTH * 0.075f, Settings.HEIGHT * 0.65f)
        .SetFont(EYBFontHelper.CardDescriptionFont_Large, 0.5f)
        .SetText(SingleCardViewPopup.TEXT[6])
        .SetOnToggle(ShowCardPileEffect::ToggleViewUpgrades);

        zoomToggle  = new GUI_Toggle(new Hitbox(Settings.scale * 256f, Settings.scale * 48f))
        .SetBackground(GR.Common.Images.Panel.Texture(), Color.DARK_GRAY)
        .SetPosition(Settings.WIDTH * 0.075f, upgradeToggle.hb.y - upgradeToggle.hb.height)
        .SetFont(EYBFontHelper.CardDescriptionFont_Large, 0.475f)
        .SetText(GR.Animator.Strings.Misc.DynamicPortraits)
        .SetOnToggle(ShowCardPileEffect::ToggleCardZoom);

        simplifyCardUIToggle = new GUI_Toggle(new Hitbox(Settings.scale * 256f, Settings.scale * 48f))
        .SetBackground(GR.Common.Images.Panel.Texture(), Color.DARK_GRAY)
        .SetPosition(Settings.WIDTH * 0.075f, zoomToggle.hb.y - zoomToggle.hb.height)
        .SetFont(EYBFontHelper.CardDescriptionFont_Large, 0.475f)
        .SetText(GR.Animator.Strings.Misc.SimplifyCardUI)
        .SetOnToggle(ShowCardPileEffect::ToggleSimplifyCardUI);

        toggleExpansion = new GUI_Toggle(new Hitbox(Settings.scale * 256f, Settings.scale * 48f))
                .SetBackground(GR.Common.Images.Panel.Texture(), Color.DARK_GRAY)
                .SetPosition(Settings.WIDTH * 0.075f, simplifyCardUIToggle.hb.y - simplifyCardUIToggle.hb.height * 2)
                .SetFont(EYBFontHelper.CardDescriptionFont_Large, 0.475f)
                .SetText(GR.Animator.Strings.SeriesSelectionButtons.EnableExpansion);
    }

    private CardGroup cards;
    private ActionT0 invokeFunc;
    private AnimatorRuntimeLoadout loadout;
    private boolean draggingScreen;
    private boolean showTopPanelOnComplete;
    private Color screenColor;
    private GUI_CardGrid grid;

    public ShowCardPileEffect(ArrayList<AbstractCard> cards)
    {
        this(GameUtilities.CreateCardGroup(cards));
    }

    public ShowCardPileEffect(CardGroup cards)
    {
        this(cards, null);
    }

    public ShowCardPileEffect(CardGroup cards, AnimatorRuntimeLoadout loadout)
    {
        super(0.7f);

        this.cards = cards;
        this.isRealtime = true;
        this.screenColor = Color.BLACK.cpy();
        this.screenColor.a = 0.8f;
        this.loadout = loadout;

        if (GameUtilities.InGame()) {
            AbstractDungeon.overlayMenu.proceedButton.hide();
        }

        if (cards.isEmpty())
        {
            Complete(cards);
            return;
        }

        if (GameUtilities.IsTopPanelVisible())
        {
            showTopPanelOnComplete = true;
            GameUtilities.SetTopPanelVisible(false);
        }

        this.grid = new GUI_CardGrid()
        .CanDragScreen(false)
        .AddCards(cards.group);

        if (loadout != null) {
            toggleExpansion.SetOnToggle(this::ToggleExpansion);
        }
    }

    public ShowCardPileEffect SetLoadout(AnimatorRuntimeLoadout loadout, ActionT0 invokeFunc) {
        this.loadout = loadout;
        this.invokeFunc = invokeFunc;
        if (loadout != null) {
            toggleExpansion.SetOnToggle(this::ToggleExpansion);
        }
        return this;
    }

    public ShowCardPileEffect SetStartingPosition(float x, float y)
    {
        for (AbstractCard c : cards.group)
        {
            c.current_x = x - (c.hb.width * 0.5f);
            c.current_y = y - (c.hb.height * 0.5f);
        }

        return this;
    }

    public void Refresh(CardGroup cards) {
        this.cards = cards;
        this.grid = new GUI_CardGrid()
                .CanDragScreen(false)
                .AddCards(cards.group);
    }

    @Override
    protected void UpdateInternal(float deltaTime)
    {
        grid.TryUpdate();
        upgradeToggle.SetToggle(SingleCardViewPopup.isViewingUpgrade).Update();
        zoomToggle.SetToggle(GR.Animator.Config.CropCardImages.Get()).Update();
        simplifyCardUIToggle.SetToggle(GR.Animator.Config.SimplifyCardUI.Get()).Update();
        if (loadout != null) {
            toggleExpansion.SetToggle(loadout.expansionEnabled).Update();
        }

        if (upgradeToggle.hb.hovered || zoomToggle.hb.hovered || simplifyCardUIToggle.hb.hovered || toggleExpansion.hb.hovered)
        {
            duration = startingDuration * 0.1f;
            isDone = false;
            return;
        }

        if (grid.scrollBar.isDragging)
        {
            duration = startingDuration;
            isDone = false;
            return;
        }

        if (TickDuration(deltaTime))
        {
            if (InputManager.LeftClick.IsJustReleased() || InputManager.RightClick.IsJustReleased())
            {
                Complete(this.cards);
                return;
            }

            isDone = false;
        }
    }

    @Override
    public void render(SpriteBatch sb)
    {
        sb.setColor(this.screenColor);
        sb.draw(ImageMaster.WHITE_SQUARE_IMG, 0f, 0f, (float) Settings.WIDTH, (float) Settings.HEIGHT);
        grid.TryRender(sb);
        upgradeToggle.Render(sb);
        zoomToggle.Render(sb);
        simplifyCardUIToggle.Render(sb);
        if (loadout != null) {
            toggleExpansion.Render(sb);
        }
    }

    @Override
    protected void Complete()
    {
        super.Complete();

        if (showTopPanelOnComplete)
        {
            GameUtilities.SetTopPanelVisible(true);
            showTopPanelOnComplete = false;
        }
    }

    private void ToggleExpansion(boolean value)
    {
        if (loadout != null) {
            loadout.ToggleExpansion(value);
            invokeFunc.Invoke();
        }
    }

    private static void ToggleViewUpgrades(boolean value)
    {
        SingleCardViewPopup.isViewingUpgrade = value;
    }

    private static void ToggleCardZoom(boolean value)
    {
        GR.Animator.Config.CropCardImages.Set(value, true);
    }

    private static void ToggleSimplifyCardUI(boolean value)
    {
        GR.Animator.Config.SimplifyCardUI.Set(value, true);
    }
}