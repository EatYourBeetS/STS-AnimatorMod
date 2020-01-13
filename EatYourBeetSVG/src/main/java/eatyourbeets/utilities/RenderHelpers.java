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

    public static TextureRenderer ForTexture(Texture texture)
    {
        return ForTexture(texture, Color.WHITE);
    }

    public static TextureRenderer ForTexture(Texture texture, Color color)
    {
        return new TextureRenderer(texture, color);
    }

    public static class TextureRenderer
    {
        public Hitbox hb;
        public Texture texture;
        public Color color;
        public float rotation;
        public float scaleX = 1;
        public float scaleY = 1;
        public int srcWidth;
        public int srcHeight;
        public boolean flipX;
        public boolean flipY;

        public TextureRenderer(Texture texture, Color color)
        {
            this.texture = texture;
            this.color = color;
            this.srcWidth = texture.getWidth();
            this.srcHeight = texture.getHeight();
        }

        public void Draw(SpriteBatch sb)
        {
            Draw(sb, hb);
        }

        public void Draw(SpriteBatch sb, Hitbox hb)
        {
            sb.setColor(color);
            sb.draw(texture, hb.x, hb.y, 0, 0, hb.width, hb.height, scaleX, scaleY, rotation, 0, 0, srcWidth, srcHeight, flipX, flipY);
        }

        public void Draw(SpriteBatch sb, float x, float y, float width, float height)
        {
            sb.setColor(color);
            sb.draw(texture, x, y, 0, 0, width, height, scaleX, scaleY, rotation, 0, 0, srcWidth, srcHeight, flipX, flipY);
        }

        public TextureRenderer SetHitbox(Hitbox hb)
        {
            this.hb = hb;

            return this;
        }

        public TextureRenderer SetColor(float r, float g, float b, float a)
        {
            this.color = new Color(r, g, b, a);

            return this;
        }

        public TextureRenderer SetColor(Color color)
        {
            this.color = color;

            return this;
        }

        public TextureRenderer SetFlipping(boolean flipX, boolean flipY)
        {
            this.flipX = flipX;
            this.flipY = flipY;

            return this;
        }

        public TextureRenderer SetOriginalDimensions(int srcWidth, int srcHeight)
        {
            this.srcWidth = srcWidth;
            this.srcHeight = srcHeight;

            return this;
        }

        public TextureRenderer SetScale(float scaleX, float scaleY)
        {
            this.scaleX = scaleX;
            this.scaleY = scaleY;

            return this;
        }

        public TextureRenderer SetRotation(float rotation)
        {
            this.rotation = rotation;

            return this;
        }
    }
}
