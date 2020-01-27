package eatyourbeets.utilities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.ui.controls.GUI_Image;

public class RenderHelpers
{
    public static void DrawOnCardCentered(SpriteBatch sb, AbstractCard card, Color color, TextureAtlas.AtlasRegion img, float drawX, float drawY)
    {
        sb.setColor(color);
        sb.draw(img, drawX + img.offsetX - img.originalWidth / 2.0F, drawY + img.offsetY - img.originalHeight / 2.0F,
                img.originalWidth / 2.0F - img.offsetX, img.originalHeight / 2.0F - img.offsetY,
                img.packedWidth, img.packedHeight, card.drawScale * Settings.scale, card.drawScale * Settings.scale, card.angle);
    }

    public static void DrawOnCardCentered(SpriteBatch sb, AbstractCard card, Color color, Texture img, float drawX, float drawY)
    {
        int width = img.getWidth();
        int height = img.getHeight();

        sb.setColor(color);
        sb.draw(img, drawX - (width / 2f), drawY - (height / 2f), width / 2f, height / 2f, width, height,
                card.drawScale * Settings.scale, card.drawScale * Settings.scale,
                card.angle, 0, 0, width, height, false, false);
    }

    public static void DrawOnCard(SpriteBatch sb, AbstractCard card, Texture img, float drawX, float drawY)
    {
        DrawOnCard(sb, card, Color.WHITE, img, drawX, drawY);
    }

    public static void DrawOnCard(SpriteBatch sb, AbstractCard card, Texture img, float drawX, float drawY, float size)
    {
        DrawOnCard(sb, card, Color.WHITE, img, drawX, drawY, size, size);
    }

    public static void DrawOnCard(SpriteBatch sb, AbstractCard card, Color color, Texture img, float drawX, float drawY)
    {
        DrawOnCard(sb, card, color, img, drawX, drawY, img.getWidth(), img.getHeight());
    }

    public static void DrawOnCard(SpriteBatch sb, AbstractCard card, Color color, Texture img, float drawX, float drawY, float size)
    {
        DrawOnCard(sb, card, color, img, drawX, drawY, size, size);
    }

    public static void DrawOnCard(SpriteBatch sb, AbstractCard card, Color color, Texture img, float drawX, float drawY, float width, float height)
    {
        int srcWidth = img.getWidth();
        int srcHeight = img.getHeight();

        sb.setColor(color);
        sb.draw(img, drawX, drawY, 0, 0, width, height,
                card.drawScale * Settings.scale, card.drawScale * Settings.scale,
                card.angle, 0, 0, srcWidth, srcHeight, false, false);
    }

    public static void Draw(SpriteBatch sb, Texture img, float drawX, float drawY, float size)
    {
        Draw(sb, img, Color.WHITE, drawX, drawY, size, size);
    }

    public static void Draw(SpriteBatch sb, Texture img, float x, float y, float width, float height)
    {
        Draw(sb, img, Color.WHITE, x, y, width, height);
    }

    public static void Draw(SpriteBatch sb, Texture img, Color color, float x, float y, float width, float height)
    {
        int srcWidth = img.getWidth();
        int srcHeight = img.getHeight();

        sb.setColor(color);
        sb.draw(img, x, y, 0, 0, width, height, Settings.scale, Settings.scale, 0, 0, 0,
                srcWidth, srcHeight, false, false);
    }

    public static void WriteCentered(SpriteBatch sb, BitmapFont font, String text, Hitbox hb, Color color)
    {
        FontHelper.renderFontCentered(sb, font, text, hb.cX, hb.cY, color);
    }

    public static void WriteCentered(SpriteBatch sb, BitmapFont font, String text, Hitbox hb, Color color, float scale)
    {
        FontHelper.renderFontCentered(sb, font, text, hb.cX, hb.cY, color, scale);
    }

    public static GUI_Image ForTexture(Texture texture)
    {
        return ForTexture(texture, Color.WHITE);
    }

    public static GUI_Image ForTexture(Texture texture, Color color)
    {
        return new GUI_Image(texture, color);
    }

    public static Color CopyColor(AbstractCard card, Color color)
    {
        Color result = color.cpy();
        result.a = card.transparency;
        return result;
    }

    public static ColoredString GetSecondaryValueString(EYBCard card)
    {
        ColoredString result;

        if (card.isSecondaryValueModified)
        {
            result = new ColoredString(card.secondaryValue >= card.baseSecondaryValue ? Settings.GREEN_TEXT_COLOR : Settings.RED_TEXT_COLOR, card.secondaryValue);
        }
        else
        {
            result = new ColoredString(Settings.CREAM_COLOR, card.baseSecondaryValue);
        }

        result.color.a = card.transparency;

        return result;
    }

    public static ColoredString GetMagicNumberString(AbstractCard card)
    {
        ColoredString result;

        if (card.isMagicNumberModified)
        {
            result = new ColoredString(card.magicNumber >= card.baseMagicNumber ? Settings.GREEN_TEXT_COLOR : Settings.RED_TEXT_COLOR, card.magicNumber);
        }
        else
        {
            result = new ColoredString(Settings.CREAM_COLOR, card.baseMagicNumber);
        }

        result.color.a = card.transparency;

        return result;
    }

    public static ColoredString GetBlockString(AbstractCard card)
    {
        ColoredString result;

        if (card.isBlockModified)
        {
            result = new ColoredString(card.block >= card.baseBlock ? Settings.GREEN_TEXT_COLOR : Settings.RED_TEXT_COLOR, card.block);
        }
        else
        {
            result = new ColoredString(Settings.CREAM_COLOR, card.baseBlock);
        }

        result.color.a = card.transparency;

        return result;
    }

    public static ColoredString GetDamageString(AbstractCard card)
    {
        ColoredString result;

        if (card.isDamageModified)
        {
            result = new ColoredString(card.damage >= card.baseDamage ? Settings.GREEN_TEXT_COLOR : Settings.RED_TEXT_COLOR, card.damage);
        }
        else
        {
            result = new ColoredString(Settings.CREAM_COLOR, card.baseDamage);
        }

        result.color.a = card.transparency;

        return result;
    }

    public static ColoredString GetCardAttributeString(AbstractCard card, char attributeID)
    {
        switch (attributeID)
        {
            case 'D': return GetDamageString(card);
            case 'B': return GetBlockString(card);
            case 'M': return GetMagicNumberString(card);
            case 'S': return GetSecondaryValueString((EYBCard) card);
            default: return new ColoredString("?", Settings.RED_TEXT_COLOR);
        }
    }
}
