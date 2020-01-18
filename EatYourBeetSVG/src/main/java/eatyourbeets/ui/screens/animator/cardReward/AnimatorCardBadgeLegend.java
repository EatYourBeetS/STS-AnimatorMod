package eatyourbeets.ui.screens.animator.cardReward;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.characters.AnimatorCharacter;
import eatyourbeets.resources.GR;
import eatyourbeets.ui.AdvancedHitbox;
import eatyourbeets.ui.GUIElement;
import eatyourbeets.ui.controls.GUI_Image;
import eatyourbeets.ui.controls.GUI_Toggle;
import eatyourbeets.utilities.RenderHelpers;

public class AnimatorCardBadgeLegend extends GUIElement
{
    public static boolean showUpgrades;

    public final GUI_Image legendImage;
    public final GUI_Toggle upgradeToggle;

    public AnimatorCardBadgeLegend()
    {
        final Texture LegendTexture = GR.Common.Images.CardBadgeLegend.Texture();
        legendImage = new GUI_Image(LegendTexture)
        .SetHitbox(new AdvancedHitbox(Scale(LegendTexture.getWidth()), Scale(LegendTexture.getHeight()))
        .SetDraggable(true)).SetPosition(ScreenW(0.9f), ScreenH(0.4f));

        upgradeToggle = new GUI_Toggle(new Hitbox(legendImage.hb.width, Scale(48.0F)))
        .SetText(SingleCardViewPopup.TEXT[6])
        .SetOnToggle(this::ToggleViewUpgrades);
    }

    public void Close()
    {
        isActive = false;
        upgradeToggle.Toggle(false);
    }

    public void Open()
    {
        isActive = AbstractDungeon.player instanceof AnimatorCharacter;
        upgradeToggle.Toggle(false);
    }

    @Override
    public void Update()
    {
        if (!CardCrawlGame.isPopupOpen)
        {
            legendImage.Update();
            upgradeToggle.SetPosition(legendImage.hb.cX, legendImage.hb.y + legendImage.hb.height + upgradeToggle.hb.height).Update();
        }
    }

    @Override
    public void Render(SpriteBatch sb)
    {
        final Hitbox hb = legendImage.hb;
        final float offset = hb.height * 0.2f;
        final float y = hb.y + hb.height + (offset * 0.5f);

        upgradeToggle.Render(sb);
        legendImage.Render(sb);

        RenderSingleBadge(sb, EYBCardBadge.Synergy, y - (offset * 1));
        RenderSingleBadge(sb, EYBCardBadge.Exhaust, y - (offset * 2));
        RenderSingleBadge(sb, EYBCardBadge.Discard, y - (offset * 3));
        RenderSingleBadge(sb, EYBCardBadge.Drawn  , y - (offset * 4));
        RenderSingleBadge(sb, EYBCardBadge.Special, y - (offset * 5));
    }

    private void RenderSingleBadge(SpriteBatch sb, EYBCardBadge badge, float y)
    {
        Hitbox hb = legendImage.hb;
        RenderHelpers.Draw(sb, badge.texture, hb.x, y - (hb.height * 0.09f), hb.height * 0.22f);
        FontHelper.renderFontCentered(sb, FontHelper.tipHeaderFont, badge.name, hb.x + (hb.width * 0.6f), y);
    }

    private void ToggleViewUpgrades(boolean value)
    {
        SingleCardViewPopup.isViewingUpgrade = showUpgrades = value;
    }
}
