package eatyourbeets.ui.animator.characterSelection;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import eatyourbeets.cards.base.EYBCardTooltip;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.AnimatorImages;
import eatyourbeets.resources.animator.AnimatorStrings;
import eatyourbeets.resources.animator.misc.AnimatorTrophies;
import eatyourbeets.rewards.AnimatorReward;
import eatyourbeets.ui.GUIElement;
import eatyourbeets.ui.controls.GUI_Image;
import eatyourbeets.ui.hitboxes.AdvancedHitbox;

public class AnimatorSpecialTrophiesRenderer extends GUIElement
{
    protected static final AnimatorStrings.Trophies trophyStrings = GR.Animator.Strings.Trophies;
    protected static final AnimatorImages images = GR.Animator.Images;

    protected final GUI_Image trophy_image;
    protected final Hitbox trophySpecialHb;
    protected final EYBCardTooltip tooltip;
    protected AnimatorTrophies specialTrophies;
    protected String trophyString = "";

    public AnimatorSpecialTrophiesRenderer()
    {
        final float size = Scale(64);
        trophySpecialHb = new AdvancedHitbox(ScreenW(0.34375f), ScreenH(0.61296f), size, size);
        tooltip = new EYBCardTooltip(trophyStrings.Platinum, "");
        trophy_image = new GUI_Image(images.LOCKED_TROPHY.Texture(), trophySpecialHb).SetBackgroundTexture(images.PLATINUM_TROPHY_SLOT.Texture());
//
//        float baseX = 200f * Settings.scale;
//        float baseY = (float) Settings.HEIGHT / 2f;
//
//        trophySpecialHb.move(baseX + 492f * Settings.scale, baseY + 154f * Settings.scale);
    }

    public void Refresh()
    {
        this.specialTrophies = GR.Animator.Data.SpecialTrophies;
    }

    public void Update()
    {
        if (specialTrophies == null)
        {
            return;
        }

        trophyString = (specialTrophies.Trophy1 > 0) ? (" " + String.format("%.2f", AnimatorReward.GetUltraRareChance(GR.Animator.Data.SelectedLoadout)) + "%") : null;
        trophy_image.SetTexture(trophyString != null ? images.PLATINUM_TROPHY.Texture() : images.LOCKED_TROPHY.Texture()).Update();

        if (trophy_image.hb.hovered)
        {
            tooltip.description = specialTrophies.Trophy1 > 0 ? trophyStrings.PlatinumDescription : trophyStrings.PlatinumHint;
            EYBCardTooltip.QueueTooltip(tooltip, false);
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