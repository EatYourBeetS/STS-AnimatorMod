package pinacolada.ui.characterSelection;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import eatyourbeets.ui.GUIElement;
import eatyourbeets.utilities.Colors;
import pinacolada.blights.common.AbstractGlyphBlight;
import pinacolada.cards.base.PCLCardTooltip;
import pinacolada.resources.GR;
import pinacolada.ui.config.ConfigOption_Integer;
import pinacolada.ui.controls.GUI_Button;
import pinacolada.ui.controls.GUI_Image;
import pinacolada.ui.hitboxes.RelativeHitbox;

public class PCLGlyphEditor extends GUIElement
{
    protected static final float ICON_SIZE = 48f * Settings.scale;

    private final AbstractGlyphBlight blight;
    private final ConfigOption_Integer configOption;
    protected Hitbox hb;
    protected GUI_Button decrease_button;
    protected GUI_Button increase_button;
    protected GUI_Image image;
    protected boolean enabled;
    protected int minimumLevel = 0;
    protected int maximumLevel = 99;
    protected PCLCardTooltip tooltip;

    public PCLGlyphEditor(AbstractGlyphBlight blight, ConfigOption_Integer configOption, Hitbox hb)
    {
        this.hb = hb;
        this.blight = blight;
        this.configOption = configOption;

        final float w = hb.width;
        final float h = hb.height;

        decrease_button = new GUI_Button(ImageMaster.CF_LEFT_ARROW, new RelativeHitbox(hb, ICON_SIZE * 0.9f, ICON_SIZE * 0.9f, (ICON_SIZE * 0.3f), h * -0.35f, false))
                .SetOnClick(this::Decrease)
                .SetText(null);

        increase_button = new GUI_Button(ImageMaster.CF_RIGHT_ARROW, new RelativeHitbox(hb, ICON_SIZE * 0.9f, ICON_SIZE * 0.9f, w - (ICON_SIZE * 0.3f), h * -0.35f, false))
                .SetOnClick(this::Increase)
                .SetText(null);

        image = new GUI_Image(blight.img, Color.WHITE).SetHitbox(hb);

        tooltip = new PCLCardTooltip(blight.strings.NAME, blight.GetAscensionTooltipDescription(0));
    }

    @Override
    public void Update() {
        this.hb.update();
        image.Update();
        decrease_button.SetInteractable(enabled && blight.counter > minimumLevel).Update();
        increase_button.SetInteractable(enabled && blight.counter < maximumLevel).Update();
    }

    @Override
    public void Render(SpriteBatch sb)
    {
        image.Render(sb);
        FontHelper.renderFontRightTopAligned(sb, FontHelper.cardTitleFont, String.valueOf(blight.counter), image.hb.cX + image.hb.width / 4, image.hb.y, 1f, Colors.White(1f));
        decrease_button.TryRender(sb);
        increase_button.TryRender(sb);
        if (hb.hovered)
        {
            PCLCardTooltip.QueueTooltip(tooltip);
        }
    }

    public void Refresh(int ascensionLevel) {
        enabled = GR.PCL.GetUnlockLevel() >= blight.ascensionRequirement;
        minimumLevel = blight.GetMinimumLevel(ascensionLevel);
        if (blight.counter < minimumLevel) {
            blight.setAmount(minimumLevel);
            configOption.Set(blight.counter, true);
        }
        tooltip.description = enabled ? blight.GetAscensionTooltipDescription(ascensionLevel) : blight.GetLockedTooltipDescription();
        image.SetGrayscale(!enabled);
    }

    public void Decrease()
    {
        if (enabled && blight.counter > minimumLevel) {
            blight.addAmount(-1);
            configOption.Set(blight.counter, true);
        }
    }

    public void Increase()
    {
        if (enabled && blight.counter < maximumLevel) {
            blight.addAmount(1);
            configOption.Set(blight.counter, true);
        }
    }
}
