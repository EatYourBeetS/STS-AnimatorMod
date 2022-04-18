package eatyourbeets.ui.animator.characterSelection;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import eatyourbeets.cards.base.EYBCardTooltip;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.AnimatorImages;
import eatyourbeets.resources.animator.AnimatorStrings;
import eatyourbeets.resources.animator.misc.AnimatorLoadout;
import eatyourbeets.resources.animator.misc.AnimatorTrophies;
import eatyourbeets.ui.GUIElement;
import eatyourbeets.ui.controls.GUI_Image;
import eatyourbeets.ui.hitboxes.AdvancedHitbox;

public class AnimatorTrophiesRenderer extends GUIElement
{
    protected static final AnimatorStrings.Trophies trophyStrings = GR.Animator.Strings.Trophies;
    protected static final AnimatorImages images = GR.Animator.Images;

    protected final GUI_Image trophy1_image;
    protected final GUI_Image trophy2_image;
    protected final GUI_Image trophy3_image;

    protected final Hitbox trophy1Hb;
    protected final Hitbox trophy2Hb;
    protected final Hitbox trophy3Hb;
    protected final EYBCardTooltip tooltip;

    protected AnimatorLoadout loadout;
    protected AnimatorTrophies trophies;

    public AnimatorTrophiesRenderer()
    {
        final float size = Scale(48);
        trophy1Hb = new AdvancedHitbox(ScreenW(0.28958f), ScreenH(0.564815f), size, size);
        trophy2Hb = new AdvancedHitbox(ScreenW(0.32083f), ScreenH(0.564815f), size, size);
        trophy3Hb = new AdvancedHitbox(ScreenW(0.35208f), ScreenH(0.564815f), size, size);
        tooltip = new EYBCardTooltip("", "");

        trophy1_image = new GUI_Image(images.LOCKED_TROPHY.Texture(), trophy1Hb);
        trophy2_image = new GUI_Image(images.LOCKED_TROPHY.Texture(), trophy2Hb);
        trophy3_image = new GUI_Image(images.LOCKED_TROPHY.Texture(), trophy3Hb);

//
//        float baseX = 200f * Settings.scale;
//        float baseY = (float) Settings.HEIGHT / 2f;
//
//        trophy1Hb.move(baseX + 380f * Settings.scale, baseY + 94f * Settings.scale);
//        trophy2Hb.move(baseX + 440f * Settings.scale, baseY + 94f * Settings.scale);
//        trophy3Hb.move(baseX + 500f * Settings.scale, baseY + 94f * Settings.scale);
    }

    public void Refresh(AnimatorLoadout loadout)
    {
        this.loadout = loadout;
        this.trophies = loadout.GetTrophies();
    }

    public void Update()
    {
        if (trophies == null)
        {
            return;
        }

        UpdateTrophy(trophy1_image, images.BRONZE_TROPHY.Texture(), trophies.Trophy1);
        UpdateTrophy(trophy2_image, images.SILVER_TROPHY.Texture(), trophies.Trophy2);
        UpdateTrophy(trophy3_image, images.GOLD_TROPHY.Texture(), trophies.Trophy3);

        if (trophy1_image.hb.hovered)
        {
            EYBCardTooltip.QueueTooltip(tooltip.SetText(trophyStrings.Bronze, loadout.GetTrophyMessage(1)), false);
        }
        else if (trophy2_image.hb.hovered)
        {
            EYBCardTooltip.QueueTooltip(tooltip.SetText(trophyStrings.Silver, loadout.GetTrophyMessage(2)), false);
        }
        else if (trophy3_image.hb.hovered)
        {
            EYBCardTooltip.QueueTooltip(tooltip.SetText(trophyStrings.Gold, loadout.GetTrophyMessage(3)), false);
        }
    }

    public void Render(SpriteBatch sb)
    {
        if (trophies == null)
        {
            return;
        }

        FontHelper.tipHeaderFont.getData().setScale(0.6f);
        RenderTrophy(trophy1_image, trophies.Trophy1, sb);
        RenderTrophy(trophy2_image, trophies.Trophy2, sb);
        RenderTrophy(trophy3_image, trophies.Trophy3, sb);
        FontHelper.tipHeaderFont.getData().setScale(1);
    }

    private void UpdateTrophy(GUI_Image image, Texture texture, int trophyLevel)
    {
        Texture slotTexture;
        if (trophyLevel <= 0)
        {
            slotTexture = images.BRONZE_TROPHY_SLOT.Texture();
        }
        else
        {
            slotTexture = images.GOLD_TROPHY_SLOT.Texture();
        }

        Texture trophyTexture;
        if (trophyLevel < 0)
        {
            trophyTexture = images.LOCKED_TROPHY.Texture();
        }
        else
        {
            trophyTexture = texture;
        }

        image.SetBackgroundTexture(slotTexture).SetTexture(trophyTexture).Update();
    }

    private void RenderTrophy(GUI_Image image, int trophyLevel, SpriteBatch sb)
    {
        image.Render(sb);

        if (trophyLevel > 0)
        {
            FontHelper.renderFontCentered(sb, FontHelper.tipHeaderFont, trophyLevel + "/20",
            image.hb.cX, image.hb.y - (4 * Settings.scale), Settings.GOLD_COLOR);
        }
    }
}