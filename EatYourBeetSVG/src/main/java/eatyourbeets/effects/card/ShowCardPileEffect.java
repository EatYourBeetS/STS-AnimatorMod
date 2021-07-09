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
import eatyourbeets.resources.GR;
import eatyourbeets.ui.controls.GUI_CardGrid;
import eatyourbeets.ui.controls.GUI_Toggle;
import eatyourbeets.utilities.EYBFontHelper;
import eatyourbeets.utilities.InputManager;

public class ShowCardPileEffect extends EYBEffectWithCallback<CardGroup>
{
    private static final float DUR = 1.5f;
    private static final GUI_Toggle upgradeToggle;
    private static final GUI_Toggle zoomToggle;
    private static final GUI_Toggle simplifyCardUIToggle;
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

    }

    private final CardGroup cards;
    private boolean draggingScreen = false;
    private Color screenColor;
    private GUI_CardGrid grid;

    public ShowCardPileEffect(CardGroup cards)
    {
        super(0.7f);

        this.cards = cards;
        this.isRealtime = true;
        this.screenColor = AbstractDungeon.fadeColor.cpy();
        this.screenColor.a = 0.8f;

        AbstractDungeon.overlayMenu.proceedButton.hide();
        ToggleViewUpgrades(false);

        if (cards.isEmpty())
        {
            Complete(cards);
            return;
        }

        this.grid = new GUI_CardGrid()
        .CanDragScreen(false)
        .AddCards(cards.group);
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

    @Override
    protected void UpdateInternal(float deltaTime)
    {
        grid.TryUpdate();
        upgradeToggle.Update();
        zoomToggle.Update();
        simplifyCardUIToggle.Update();

        if (upgradeToggle.hb.hovered || zoomToggle.hb.hovered || simplifyCardUIToggle.hb.hovered)
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
                ToggleViewUpgrades(false);
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
