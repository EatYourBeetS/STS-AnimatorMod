package eatyourbeets.cards.base;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.RenderHelpers;

import static com.megacrit.cardcrawl.cards.AbstractCard.IMG_HEIGHT;
import static com.megacrit.cardcrawl.cards.AbstractCard.IMG_WIDTH;

public class EYBCardPreview
{
    public EYBCardBase defaultPreview;
    public EYBCardBase upgradedPreview;
    public boolean isMultiPreview;

    public EYBCardPreview(EYBCardBase card, boolean upgrade)
    {
        this.defaultPreview = card;
        this.defaultPreview.isPreview = true;

        if (upgrade)
        {
            this.upgradedPreview = (EYBCardBase) defaultPreview.makeStatEquivalentCopy();
            this.upgradedPreview.isPreview = true;
            this.upgradedPreview.upgrade();
            this.upgradedPreview.displayUpgrades();
        }
    }

    public EYBCardBase GetPreview(boolean upgraded)
    {
        return upgraded && upgradedPreview != null ? upgradedPreview : defaultPreview;
    }

    public void Render(SpriteBatch sb, EYBCardBase card, boolean upgraded)
    {
        EYBCardBase preview = GetPreview(upgraded);

        if (card.isPopup)
        {
            preview.current_x = (float) Settings.WIDTH * 0.2f - 10f * Settings.scale;
            preview.current_y = (float) Settings.HEIGHT * 0.25f;
            preview.drawScale = 1f;
            preview.render(sb);
        }
        else if (AbstractDungeon.player == null || !AbstractDungeon.player.isDraggingCard)
        {
            final float offset_y = (IMG_HEIGHT * 0.5f - IMG_HEIGHT * 0.4f) * card.drawScale;
            final float offset_x = (IMG_WIDTH * 0.5f + IMG_WIDTH * 0.4f + 16f) * ((card.current_x > Settings.WIDTH * 0.7f) ? card.drawScale : -card.drawScale);

            preview.current_x = card.current_x + offset_x;
            preview.current_y = card.current_y + offset_y;
            preview.drawScale = card.drawScale * 0.8f;
            preview.render(sb);
        }

        if (isMultiPreview)
        {
            String cyclePreviewText = GR.Animator.Strings.Misc.PressControlToCycle;
            BitmapFont font = RenderHelpers.GetDescriptionFont(preview, 0.9f);
            RenderHelpers.DrawOnCardAuto(sb, preview, GR.Common.Images.Panel.Texture(), new Vector2(0, -AbstractCard.RAW_H * 0.55f),
            IMG_WIDTH * 0.6f, font.getLineHeight() * 1.8f, Color.DARK_GRAY, 0.75f, 1);
            RenderHelpers.WriteOnCard(sb, preview, font, cyclePreviewText, 0, -AbstractCard.RAW_H * 0.55f, Color.MAGENTA);
            RenderHelpers.ResetFont(font);
        }
    }
}
