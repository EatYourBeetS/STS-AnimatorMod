package pinacolada.ui.characterSelection;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import pinacolada.cards.base.PCLCardTooltip;
import pinacolada.resources.GR;
import pinacolada.resources.pcl.PCLImages;
import pinacolada.resources.pcl.PCLStrings;
import pinacolada.resources.pcl.misc.PCLTrophies;
import pinacolada.rewards.PCLReward;
import pinacolada.ui.GUIElement;
import pinacolada.ui.controls.GUI_Image;
import pinacolada.ui.hitboxes.AdvancedHitbox;

public class PCLSpecialTrophiesRenderer extends GUIElement
{
    protected static final PCLStrings.Trophies trophyStrings = GR.PCL.Strings.Trophies;
    protected static final PCLImages images = GR.PCL.Images;

    protected final GUI_Image trophy_image;
    protected final Hitbox trophySpecialHb;
    protected final PCLCardTooltip tooltip;
    protected PCLTrophies specialTrophies;
    protected String trophyString = "";

    public PCLSpecialTrophiesRenderer()
    {
        final float size = Scale(64);
        trophySpecialHb = new AdvancedHitbox(ScreenW(0.34375f), ScreenH(0.61296f), size, size);
        tooltip = new PCLCardTooltip(trophyStrings.Platinum, "");
        trophy_image = new GUI_Image(images.LOCKED_TROPHY.Texture(), trophySpecialHb).SetBackgroundTexture(images.PLATINUM_TROPHY_SLOT.Texture());
//
//        float baseX = 200f * Settings.scale;
//        float baseY = (float) Settings.HEIGHT / 2f;
//
//        trophySpecialHb.move(baseX + 492f * Settings.scale, baseY + 154f * Settings.scale);
    }

    public void Refresh()
    {
        this.specialTrophies = GR.PCL.Data.SpecialTrophies;
    }

    public void Update()
    {
        if (specialTrophies == null)
        {
            return;
        }

        trophyString = (specialTrophies.Trophy1 > 0) ? (" " + String.format("%.2f", PCLReward.GetUltraRareChance(GR.PCL.Data.SelectedLoadout)) + "%") : null;
        trophy_image.SetTexture(trophyString != null ? images.PLATINUM_TROPHY.Texture() : images.LOCKED_TROPHY.Texture()).Update();

        if (trophy_image.hb.hovered)
        {
            tooltip.description = specialTrophies.Trophy1 > 0 ? trophyStrings.PlatinumDescription : trophyStrings.PlatinumHint;
            PCLCardTooltip.QueueTooltip(tooltip);
        }
    }

    public void Render(SpriteBatch sb)
    {
        if (specialTrophies == null)
        {
            return;
        }

        trophy_image.Render(sb);

        if (trophyString != null)
        {
            FontHelper.renderFontLeft(sb, FontHelper.tipHeaderFont, trophyString,
            trophy_image.hb.x + (trophy_image.hb.width * 1.1f), trophy_image.hb.cY, Settings.GOLD_COLOR);
        }
    }
}