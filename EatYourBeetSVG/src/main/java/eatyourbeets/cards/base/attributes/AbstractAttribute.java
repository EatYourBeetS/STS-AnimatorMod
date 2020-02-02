package eatyourbeets.cards.base.attributes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.common.CommonImages;
import eatyourbeets.utilities.ColoredString;
import eatyourbeets.utilities.RenderHelpers;

public abstract class AbstractAttribute
{
    protected final static CommonImages.CardIcons ICONS = GR.Common.Images.Icons;
    protected final static float DESC_OFFSET_X = (AbstractCard.IMG_WIDTH * 0.5f);
    protected final static float DESC_OFFSET_Y = (AbstractCard.IMG_HEIGHT * 0.10f);
    protected static final GlyphLayout layout = new GlyphLayout();

    public Texture icon;
    public ColoredString mainText;
    public String iconTag;
    public String suffix;

    public abstract AbstractAttribute SetCard(EYBCard card);

    public AbstractAttribute SetIconTag(String iconTag)
    {
        this.iconTag = iconTag;

        return this;
    }

    public AbstractAttribute SetIcon(Texture icon)
    {
        this.icon = icon;

        return this;
    }

    public AbstractAttribute SetText(ColoredString string)
    {
        this.mainText = string;

        return this;
    }

    public AbstractAttribute SetText(String text, Color color)
    {
        this.mainText = new ColoredString(text, color);

        return this;
    }

    public AbstractAttribute AddMultiplier(int times)
    {
        this.suffix = "x" + times;

        return this;
    }

    public AbstractAttribute AddSuffix(String suffix)
    {
        this.suffix = suffix;

        return this;
    }

    public AbstractAttribute Clear()
    {
        this.suffix = null;
        this.iconTag = null;
        this.icon = null;
        this.mainText = null;

        return this;
    }

    public void Render(SpriteBatch sb, EYBCard card, boolean leftAlign)
    {
        final BitmapFont largeFont = RenderHelpers.GetLargeAttributeFont(card);
        final float scale = Settings.scale * card.drawScale;
        float base_x = card.current_x;
        float base_y = card.current_y - (DESC_OFFSET_Y * card.drawScale);

        if (card.angle != 0)
        {
            base_y += Math.abs(card.angle * scale);
        }

        layout.setText(largeFont, mainText.text);
        final float width = layout.width;

        if (leftAlign)
        {
            base_x -= (DESC_OFFSET_X * card.drawScale);

            RenderHelpers.DrawOnCard(sb, card, icon, base_x, base_y, 48);
            FontHelper.renderFont(sb, largeFont, mainText.text, base_x + 42 * scale, base_y + 20 * scale + layout.height / 2f, mainText.color);

            if (suffix != null)
            {
                largeFont.getData().setScale(largeFont.getScaleX() * 0.6f);
                layout.setText(largeFont, suffix);
                FontHelper.renderFont(sb, largeFont, suffix, base_x + 42 * scale + width, base_y + 14 * scale + layout.height / 2f, mainText.color);
            }
        }
        else
        {
            base_x += (DESC_OFFSET_X * card.drawScale) - (48 * scale);

            RenderHelpers.DrawOnCard(sb, card, icon, base_x, base_y, 48);
            FontHelper.renderFont(sb, largeFont, mainText.text, base_x + 10 * scale - width, base_y + 20 * scale + layout.height / 2f, mainText.color);

            if (suffix != null)
            {
                largeFont.getData().setScale(largeFont.getScaleX() * 0.6f);
                layout.setText(largeFont, suffix);
                FontHelper.renderFont(sb, largeFont, suffix, base_x + 10 * scale - width - layout.width, base_y + 14 * scale + layout.height / 2f, mainText.color);
            }
        }
        RenderHelpers.ResetFont(largeFont);

        if (iconTag != null)
        {
            BitmapFont smallFont = RenderHelpers.GetSmallAttributeFont(card);
            FontHelper.renderFontLeft(sb, smallFont, iconTag, base_x + 10 * scale, base_y + 8 * scale, RenderHelpers.CopyColor(card, Settings.CREAM_COLOR));
            RenderHelpers.ResetFont(smallFont);
        }
    }
}
