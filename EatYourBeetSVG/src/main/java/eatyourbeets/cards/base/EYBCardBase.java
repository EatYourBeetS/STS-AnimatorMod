package eatyourbeets.cards.base;

import basemod.BaseMod;
import basemod.abstracts.CustomCard;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.modthespire.lib.SpireOverride;
import com.evacipated.cardcrawl.modthespire.lib.SpireSuper;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.ColoredString;
import eatyourbeets.utilities.FieldInfo;
import eatyourbeets.utilities.JavaUtilities;
import eatyourbeets.utilities.RenderHelpers;

import java.util.HashMap;
import java.util.Map;

public abstract class EYBCardBase extends AbstractCard
{
    protected static final FieldInfo<Boolean> _darken = JavaUtilities.GetField("darken", AbstractCard.class);
    protected static final FieldInfo<Color> _renderColor = JavaUtilities.GetField("renderColor", AbstractCard.class);
    protected static AbstractPlayer player = null;

    public static final String PORTRAIT_ENDING = "_p";
    public String textureImg;
    public String textureOrbSmallImg = null;
    public String textureOrbLargeImg = null;
    public String textureBackgroundSmallImg = null;
    public String textureBackgroundLargeImg = null;
    public String textureBannerSmallImg = null;
    public String textureBannerLargeImg = null;
    private static Map<Class<? extends EYBCardBase>, BitmapFont> titleFontMap = new HashMap<>();

    private static void loadTextureFromString(String textureString)
    {
        if (!CustomCard.imgMap.containsKey(textureString))
        {
            CustomCard.imgMap.put(textureString, ImageMaster.loadImage(textureString));
        }
    }

    private static Texture getTextureFromString(String textureString)
    {
        loadTextureFromString(textureString);
        return CustomCard.imgMap.get(textureString);
    }

    public EYBCardBase(String id, String name, String img, int cost, String rawDescription, CardType type, CardColor color, CardRarity rarity, CardTarget target)
    {
        super(id, name, "status/beta", "status/beta", cost, rawDescription, type, color, rarity, target);
        this.textureImg = img;
        if (img != null)
        {
            this.loadCardImage(img);
        }
    }

    public EYBCardBase(String id, String name, CustomCard.RegionName img, int cost, String rawDescription, CardType type, CardColor color, CardRarity rarity, CardTarget target)
    {
        super(id, name, "status/beta", img.name, cost, rawDescription, type, color, rarity, target);
    }

    public Texture getOrbSmallTexture()
    {
        return this.textureOrbSmallImg == null ? BaseMod.getEnergyOrbTexture(this.color) : getTextureFromString(this.textureOrbSmallImg);
    }

    public Texture getOrbLargeTexture()
    {
        return this.textureOrbLargeImg == null ? BaseMod.getEnergyOrbPortraitTexture(this.color) : getTextureFromString(this.textureOrbLargeImg);
    }

    public void setOrbTexture(String orbSmallImg, String orbLargeImg)
    {
        this.textureOrbSmallImg = orbSmallImg;
        this.textureOrbLargeImg = orbLargeImg;
        loadTextureFromString(orbSmallImg);
        loadTextureFromString(orbLargeImg);
    }

    public Texture getBackgroundSmallTexture()
    {
        if (this.textureBackgroundSmallImg == null)
        {
            switch (this.type)
            {
                case ATTACK:
                    return BaseMod.getAttackBgTexture(this.color);
                case POWER:
                    return BaseMod.getPowerBgTexture(this.color);
                default:
                    return BaseMod.getSkillBgTexture(this.color);
            }
        }
        else
        {
            return getTextureFromString(this.textureBackgroundSmallImg);
        }
    }

    public Texture getBackgroundLargeTexture()
    {
        if (this.textureBackgroundLargeImg == null)
        {
            switch (this.type)
            {
                case ATTACK:
                    return BaseMod.getAttackBgPortraitTexture(this.color);
                case POWER:
                    return BaseMod.getPowerBgPortraitTexture(this.color);
                default:
                    return BaseMod.getSkillBgPortraitTexture(this.color);
            }
        }
        else
        {
            return getTextureFromString(this.textureBackgroundLargeImg);
        }
    }

    public void setBackgroundTexture(String backgroundSmallImg, String backgroundLargeImg)
    {
        this.textureBackgroundSmallImg = backgroundSmallImg;
        this.textureBackgroundLargeImg = backgroundLargeImg;
        loadTextureFromString(backgroundSmallImg);
        loadTextureFromString(backgroundLargeImg);
    }

    public Texture getBannerSmallTexture()
    {
        return this.textureBannerSmallImg == null ? null : getTextureFromString(this.textureBannerSmallImg);
    }

    public Texture getBannerLargeTexture()
    {
        return this.textureBannerLargeImg == null ? null : getTextureFromString(this.textureBannerLargeImg);
    }

    public void setBannerTexture(String bannerSmallImg, String bannerLargeImg)
    {
        this.textureBannerSmallImg = bannerSmallImg;
        this.textureBannerLargeImg = bannerLargeImg;
        loadTextureFromString(bannerSmallImg);
        loadTextureFromString(bannerLargeImg);
    }

    public void loadCardImage(String img)
    {
        Texture texture = GR.GetTexture(img);

        portrait = new TextureAtlas.AtlasRegion(GR.GetTexture(img), 0, 0, texture.getWidth(), texture.getHeight());
    }

    @SpireOverride
    protected void renderPortrait(SpriteBatch sb)
    {
        SpireSuper.call(sb);
    }

    @SpireOverride
    protected void renderJokePortrait(SpriteBatch sb)
    {
        renderPortrait(sb);
    }

    @SpireOverride
    protected void renderCardBg(SpriteBatch sb, float x, float y)
    {
        Texture background = GetCardBackground();
        if (background != null)
        {
            RenderHelpers.DrawOnCardAuto(sb, this, background, 0, 0, background.getWidth(), background.getHeight());
            //renderHelper(sb, _renderColor.Get(this), background, x, y);
        }
        else
        {
            SpireSuper.call(sb, x, y);
        }
    }

    @SpireOverride
    protected void renderEnergy(SpriteBatch sb)
    {
        if (this.cost > -2 && !_darken.Get(this) && !this.isLocked && this.isSeen)
        {
            Texture energyOrb = GetEnergyOrb();
            if (energyOrb != null)
            {
                //RenderHelpers.DrawOnCard(sb, this, GetEnergyOrb(), -(AbstractCard.IMG_WIDTH * 0.58f) * drawScale, (AbstractCard.IMG_HEIGHT * 0.365f) * drawScale, 80);
                RenderHelpers.DrawOnCardAuto(sb, this, GetEnergyOrb(), -(AbstractCard.RAW_W * 0.445f), (AbstractCard.RAW_H * 0.455f), 80, 80);
            }
            else
            {
                switch (this.color)
                {
                    case RED:
                        this.renderHelper(sb, _renderColor.Get(this), ImageMaster.CARD_RED_ORB, this.current_x, this.current_y);
                        break;
                    case GREEN:
                        this.renderHelper(sb, _renderColor.Get(this), ImageMaster.CARD_GREEN_ORB, this.current_x, this.current_y);
                        break;
                    case BLUE:
                        this.renderHelper(sb, _renderColor.Get(this), ImageMaster.CARD_BLUE_ORB, this.current_x, this.current_y);
                        break;
                    case PURPLE:
                        this.renderHelper(sb, _renderColor.Get(this), ImageMaster.CARD_PURPLE_ORB, this.current_x, this.current_y);
                        break;
                    case COLORLESS:
                    default:
                        this.renderHelper(sb, _renderColor.Get(this), ImageMaster.CARD_COLORLESS_ORB, this.current_x, this.current_y);
                        break;
                }
            }

            ColoredString costString = GetCostString();
            if (costString != null)
            {
                final float scale = this.drawScale * Settings.scale;
                final BitmapFont font = FontHelper.cardEnergyFont_L;

                font.getData().setScale(drawScale);
                FontHelper.renderRotatedText(sb, font, costString.text, this.current_x, this.current_y, -132.0F * scale, 192.0F * scale, this.angle, false, costString.color);
            }
        }
    }

    protected Texture GetCardBackground()
    {
        return null;
    }

    protected Texture GetEnergyOrb()
    {
        return null;
    }

    protected ColoredString GetCostString()
    {
        ColoredString result = new ColoredString();

        if (cost == -1)
        {
            result.text = "X";
        }
        else
        {
            result.text = freeToPlay() ? "0" : Integer.toString(this.costForTurn);
        }

        if (player != null && player.hand.contains(this) && !this.hasEnoughEnergy())
        {
            result.color = new Color(1.0F, 0.3F, 0.3F, transparency);
        }
        else if (this.isCostModified || this.isCostModifiedForTurn || this.freeToPlay())
        {
            result.color = new Color(0.4F, 1.0F, 0.4F, transparency);
        }
        else
        {
            result.color = new Color(1f, 1f, 1f, transparency);
        }

        return result;
    }

    private void renderHelper(SpriteBatch sb, Color color, Texture img, float drawX, float drawY)
    {
        sb.setColor(color);
        sb.draw(img, drawX - 256.0F, drawY - 256.0F, 256.0F, 256.0F, 512.0F, 512.0F, this.drawScale * Settings.scale, this.drawScale * Settings.scale, this.angle, 0, 0, 512, 512, false, false);
    }

    private void renderHelper(SpriteBatch sb, Color color, TextureAtlas.AtlasRegion img, float drawX, float drawY)
    {
        sb.setColor(color);
        sb.draw(img, drawX + img.offsetX - (float) img.originalWidth / 2.0F, drawY + img.offsetY - (float) img.originalHeight / 2.0F, (float) img.originalWidth / 2.0F - img.offsetX, (float) img.originalHeight / 2.0F - img.offsetY, (float) img.packedWidth, (float) img.packedHeight, this.drawScale * Settings.scale, this.drawScale * Settings.scale, this.angle);
    }
}
