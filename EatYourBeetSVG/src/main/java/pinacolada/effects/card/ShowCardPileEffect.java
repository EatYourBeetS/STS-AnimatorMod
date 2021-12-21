package pinacolada.effects.card;

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
import eatyourbeets.utilities.EYBFontHelper;
import pinacolada.resources.GR;
import pinacolada.resources.pcl.misc.PCLRuntimeLoadout;
import pinacolada.ui.controls.GUI_CardGrid;
import pinacolada.ui.controls.GUI_Toggle;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLInputManager;

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
        .SetBackground(GR.PCL.Images.Panel.Texture(), Color.DARK_GRAY)
        .SetPosition(Settings.WIDTH * 0.075f, Settings.HEIGHT * 0.65f)
        .SetFont(EYBFontHelper.CardDescriptionFont_Large, 0.5f)
        .SetText(SingleCardViewPopup.TEXT[6])
        .SetOnToggle(ShowCardPileEffect::ToggleViewUpgrades);

        zoomToggle  = new GUI_Toggle(new Hitbox(Settings.scale * 256f, Settings.scale * 48f))
        .SetBackground(GR.PCL.Images.Panel.Texture(), Color.DARK_GRAY)
        .SetPosition(Settings.WIDTH * 0.075f, upgradeToggle.hb.y - upgradeToggle.hb.height)
        .SetFont(EYBFontHelper.CardDescriptionFont_Large, 0.475f)
        .SetText(GR.PCL.Strings.Misc.DynamicPortraits)
        .SetOnToggle(ShowCardPileEffect::ToggleCardZoom);

        simplifyCardUIToggle = new GUI_Toggle(new Hitbox(Settings.scale * 256f, Settings.scale * 48f))
        .SetBackground(GR.PCL.Images.Panel.Texture(), Color.DARK_GRAY)
        .SetPosition(Settings.WIDTH * 0.075f, zoomToggle.hb.y - zoomToggle.hb.height)
        .SetFont(EYBFontHelper.CardDescriptionFont_Large, 0.475f)
        .SetText(GR.PCL.Strings.Misc.SimplifyCardUI)
        .SetOnToggle(ShowCardPileEffect::ToggleSimplifyCardUI);

        toggleExpansion = new GUI_Toggle(new Hitbox(Settings.scale * 256f, Settings.scale * 48f))
                .SetBackground(GR.PCL.Images.Panel.Texture(), Color.DARK_GRAY)
                .SetPosition(Settings.WIDTH * 0.075f, simplifyCardUIToggle.hb.y - simplifyCardUIToggle.hb.height * 2)
                .SetFont(EYBFontHelper.CardDescriptionFont_Large, 0.475f)
                .SetText(GR.PCL.Strings.SeriesSelectionButtons.EnableExpansion);
    }

    private CardGroup cards;
    private ActionT0 invokeFunc;
    private PCLRuntimeLoadout loadout;
    private boolean draggingScreen;
    private boolean showTopPanelOnComplete;
    private Color screenColor;
    private GUI_CardGrid grid;

    public ShowCardPileEffect(ArrayList<AbstractCard> cards)
    {
        this(PCLGameUtilities.CreateCardGroup(cards));
    }

    public ShowCardPileEffect(CardGroup cards)
    {
        this(cards, null);
    }

    public ShowCardPileEffect(CardGroup cards, PCLRuntimeLoadout loadout)
    {
        super(0.7f);

        this.cards = cards;
        this.isRealtime = true;
        this.screenColor = Color.BLACK.cpy();
        this.screenColor.a = 0.8f;
        this.loadout = loadout;

        if (PCLGameUtilities.InGame()) {
            AbstractDungeon.overlayMenu.proceedButton.hide();
        }

        if (cards.isEmpty())
        {
            this.grid = new GUI_CardGrid().CanDragScreen(false);
            Complete(cards);
            return;
        }

        if (PCLGameUtilities.IsTopPanelVisible())
        {
            showTopPanelOnComplete = true;
            PCLGameUtilities.SetTopPanelVisible(false);
        }

        this.grid = new GUI_CardGrid()
        .CanDragScreen(false)
        .AddCards(cards.group);

        if (loadout != null) {
            toggleExpansion.SetOnToggle(this::ToggleExpansion);
        }
    }

    public ShowCardPileEffect SetLoadout(PCLRuntimeLoadout loadout, ActionT0 invokeFunc) {
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
        zoomToggle.SetToggle(GR.PCL.Config.CropCardImages.Get()).Update();
        simplifyCardUIToggle.SetToggle(GR.PCL.Config.SimplifyCardUI.Get()).Update();
        if (loadout != null && loadout.canEnableExpansion) {
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
            if (PCLInputManager.LeftClick.IsJustReleased() || PCLInputManager.RightClick.IsJustReleased())
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
        if (loadout != null && loadout.canEnableExpansion) {
            toggleExpansion.Render(sb);
        }
    }

    @Override
    protected void Complete()
    {
        super.Complete();

        if (showTopPanelOnComplete)
        {
            PCLGameUtilities.SetTopPanelVisible(true);
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
        GR.PCL.Config.CropCardImages.Set(value, true);
    }

    private static void ToggleSimplifyCardUI(boolean value)
    {
        GR.PCL.Config.SimplifyCardUI.Set(value, true);
    }
}