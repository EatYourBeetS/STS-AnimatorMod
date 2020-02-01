package eatyourbeets.cards.base;

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
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
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
    protected static final Color HOVER_IMG_COLOR = new Color(1.0F, 0.815F, 0.314F, 0.8F);
    protected static final Color SELECTED_CARD_COLOR = new Color(0.5F, 0.9F, 0.9F, 1.0F);
    protected static final float SHADOW_OFFSET_X = 18.0F * Settings.scale;
    protected static final float SHADOW_OFFSET_Y = 14.0F * Settings.scale;

    protected static final FieldInfo<Boolean> _renderTip = JavaUtilities.GetField("renderTip", AbstractCard.class);
    protected static final FieldInfo<Boolean> _darken = JavaUtilities.GetField("darken", AbstractCard.class);
    protected static final FieldInfo<Color> _renderColor = JavaUtilities.GetField("renderColor", AbstractCard.class);
    protected static AbstractPlayer player = null;

    public boolean isPopup = false;
    public boolean isPreview = false;
    public boolean isSecondaryValueModified = false;
    public boolean upgradedSecondaryValue = false;
    public int baseSecondaryValue = 0;
    public int secondaryValue = 0;

    public static final String PORTRAIT_ENDING = "_p";

    private static Map<Class<? extends EYBCardBase>, BitmapFont> titleFontMap = new HashMap<>();

    public static void RefreshPlayer()
    {
        player = AbstractDungeon.player;
    }

    public EYBCardBase(String id, String name, String imagePath, int cost, String rawDescription, CardType type, CardColor color, CardRarity rarity, CardTarget target)
    {
        super(id, name, "status/beta", "status/beta", cost, rawDescription, type, color, rarity, target);

        portrait = null;
        assetUrl = imagePath;

        LoadImage(null);
    }

    public void LoadImage(String suffix)
    {
        if (suffix == null)
        {
            if (isPopup)
            {
                // Do not cache high res picture
                portraitImg = ImageMaster.loadImage(assetUrl.replace(".png", "_p.png"));
            }
            else
            {
                portraitImg = GR.GetTexture(assetUrl);
            }
        }
        else if (suffix.equals("_p"))
        {
            // Do not cache high res picture
            portraitImg = ImageMaster.loadImage(assetUrl.replace(".png", "_p.png"));
        }
        else
        {
            portraitImg = GR.GetTexture(assetUrl.replace(".png", suffix + ".png"));
        }
    }

    public boolean isOnScreen()
    {
        return this.current_y >= -200.0F * Settings.scale && this.current_y <= (float) Settings.HEIGHT + 200.0F * Settings.scale;
    }

    public boolean CanRenderTip()
    {
        return isPopup || _renderTip.Get(this);
    }

    @SpireOverride
    protected void renderCard(SpriteBatch sb, boolean hovered, boolean selected)
    {
        if (!Settings.hideCards)
        {
            if (isFlipped)
            {
                this.renderBack(sb, hovered, selected);
                return;
            }

            this.updateGlow();
            this.renderGlow(sb);
            this.renderImage(sb, hovered, selected);
            this.renderTitle(sb);
            this.renderType(sb);
            this.renderDescription(sb);
            this.renderTint(sb);
            this.renderEnergy(sb);
        }
    }

    @SpireOverride
    protected void renderBack(SpriteBatch sb, boolean hovered, boolean selected)
    {
        SpireSuper.call(sb, hovered, selected);
    }

    @SpireOverride
    public void renderTint(SpriteBatch sb)
    {
        SpireSuper.call(sb);
    }

    @SpireOverride
    public void renderDescription(SpriteBatch sb)
    {
        SpireSuper.call(sb);
    }

    @SpireOverride
    public void renderDescriptionCN(SpriteBatch sb)
    {
        throw new RuntimeException("Not Implemented");
    }

    @SpireOverride
    protected void renderType(SpriteBatch sb)
    {
        SpireSuper.call(sb);
    }

    @SpireOverride
    protected void renderTitle(SpriteBatch sb)
    {
        SpireSuper.call(sb);
    }

    @SpireOverride
    protected void renderImage(SpriteBatch sb, boolean hovered, boolean selected)
    {
        if (AbstractDungeon.player != null)
        {
            if (selected)
            {
                RenderAtlas(sb, Color.SKY, this.getCardBgAtlas(), this.current_x, this.current_y, 1.03F);
            }

            RenderAtlas(sb, new Color(0, 0, 0, transparency * 0.25f), this.getCardBgAtlas(), this.current_x + SHADOW_OFFSET_X * this.drawScale, this.current_y - SHADOW_OFFSET_Y * this.drawScale);
            if (AbstractDungeon.player.hoveredCard == this && (AbstractDungeon.player.isDraggingCard && AbstractDungeon.player.isHoveringDropZone || AbstractDungeon.player.inSingleTargetMode))
            {
                RenderAtlas(sb, HOVER_IMG_COLOR, this.getCardBgAtlas(), this.current_x, this.current_y);
            }
            else if (selected)
            {
                RenderAtlas(sb, SELECTED_CARD_COLOR, this.getCardBgAtlas(), this.current_x, this.current_y);
            }
        }

        this.renderPortrait(sb);
        this.renderCardBg(sb, this.current_x, this.current_y);
        this.renderPortraitFrame(sb, this.current_x, this.current_y);
        this.renderBannerImage(sb, this.current_x, this.current_y);
    }

    @SpireOverride
    protected void renderGlow(SpriteBatch sb)
    {
        SpireSuper.call(sb);
    }

    @SpireOverride
    protected void updateGlow()
    {
        SpireSuper.call();
    }

    @SpireOverride
    protected void renderPortrait(SpriteBatch sb)
    {
        final float scale = drawScale * Settings.scale;
        final Texture img = this.portraitImg;
        float drawX = this.current_x - 125.0F;
        float drawY = this.current_y - 95.0F;

        if (!this.isLocked)
        {
            sb.setColor(_renderColor.Get(this));
            if (isPopup)
            {
                sb.draw(img, (float)Settings.WIDTH / 2.0F - 250.0F, (float)Settings.HEIGHT / 2.0F - 190.0F + 136.0F * Settings.scale, 250.0F, 190.0F, 500.0F, 380.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 500, 380, false, false);
            }
            else
            {
                sb.draw(img, drawX, drawY + 72.0F, 125.0F, 23.0F, 250.0F, 190.0F, scale, scale, this.angle,
                        0, 0, 250, 190, false, false);
            }
        }
        else
        {
            sb.draw(img, drawX, drawY + 72.0F, 125.0F, 23.0F, 250.0F, 190.0F, scale, scale, this.angle, 0, 0, 250, 190, false, false);
        }
    }

    @SpireOverride
    protected void renderJokePortrait(SpriteBatch sb)
    {
        renderPortrait(sb);
    }

    @SpireOverride
    protected void renderBannerImage(SpriteBatch sb, float drawX, float drawY)
    {
        if (!TryRenderCentered(sb, GetCardBanner()))
        {
            SpireSuper.call(sb, drawX, drawY);
        }
    }

    @SpireOverride
    protected void renderPortraitFrame(SpriteBatch sb, float x, float y)
    {
        if (!TryRenderCentered(sb, GetPortraitFrame()))
        {
            SpireSuper.call(sb, x, y);
        }
    }

    @SpireOverride
    protected void renderCardBg(SpriteBatch sb, float x, float y)
    {
        if (!TryRenderCentered(sb, GetCardBackground()))
        {
            SpireSuper.call(sb, x, y);
        }
    }

    @SpireOverride
    protected void renderEnergy(SpriteBatch sb)
    {
        if (this.cost > -2 && !_darken.Get(this) && !this.isLocked && this.isSeen)
        {
            if (!TryRenderCentered(sb, GetEnergyOrb()))
            {
                switch (this.color)
                {
                    case RED:
                        this.RenderAtlas(sb, _renderColor.Get(this), ImageMaster.CARD_RED_ORB, this.current_x, this.current_y);
                        break;
                    case GREEN:
                        this.RenderAtlas(sb, _renderColor.Get(this), ImageMaster.CARD_GREEN_ORB, this.current_x, this.current_y);
                        break;
                    case BLUE:
                        this.RenderAtlas(sb, _renderColor.Get(this), ImageMaster.CARD_BLUE_ORB, this.current_x, this.current_y);
                        break;
                    case PURPLE:
                        this.RenderAtlas(sb, _renderColor.Get(this), ImageMaster.CARD_PURPLE_ORB, this.current_x, this.current_y);
                        break;
                    case COLORLESS:
                    default:
                        this.RenderAtlas(sb, _renderColor.Get(this), ImageMaster.CARD_COLORLESS_ORB, this.current_x, this.current_y);
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

    protected Texture GetPortraitFrame()
    {
        return null;
    }

    protected Texture GetCardBanner()
    {
        return null;
    }

    protected Texture GetCardBackground()
    {
        return null;
    }

    protected Texture GetEnergyOrb()
    {
        return null;
    }

    public ColoredString GetMagicNumberString()
    {
        if (isMagicNumberModified)
        {
            return new ColoredString(magicNumber, magicNumber >= baseMagicNumber ? Settings.GREEN_TEXT_COLOR : Settings.RED_TEXT_COLOR, transparency);
        }
        else
        {
            return new ColoredString(baseMagicNumber, Settings.CREAM_COLOR, transparency);
        }
    }

    public ColoredString GetBlockString()
    {
        if (isBlockModified)
        {
            return new ColoredString(block, block >= baseBlock ? Settings.GREEN_TEXT_COLOR : Settings.RED_TEXT_COLOR, transparency);
        }
        else
        {
            return new ColoredString(baseBlock, Settings.CREAM_COLOR, transparency);
        }
    }

    public ColoredString GetDamageString()
    {
        if (isDamageModified)
        {
            return new ColoredString(damage, damage >= baseDamage ? Settings.GREEN_TEXT_COLOR : Settings.RED_TEXT_COLOR, transparency);
        }
        else
        {
            return new ColoredString(baseDamage, Settings.CREAM_COLOR, transparency);
        }
    }

    public ColoredString GetSecondaryValueString()
    {
        if (isSecondaryValueModified)
        {
            return new ColoredString(secondaryValue, secondaryValue >= baseSecondaryValue ? Settings.GREEN_TEXT_COLOR : Settings.RED_TEXT_COLOR, transparency);
        }
        else
        {
            return new ColoredString(baseSecondaryValue, Settings.CREAM_COLOR, transparency);
        }
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

    protected void upgradeSecondaryValue(int amount)
    {
        this.baseSecondaryValue += amount;
        this.secondaryValue = this.baseSecondaryValue;
        this.upgradedSecondaryValue = true;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private boolean TryRenderCentered(SpriteBatch sb, Texture texture)
    {
        if (texture != null)
        {
            RenderHelpers.DrawOnCardAuto(sb, this, texture, 0, 0, texture.getWidth(), texture.getHeight());

            return true;
        }

        return false;
    }

    private void RenderAtlas(SpriteBatch sb, Color color, TextureAtlas.AtlasRegion img, float drawX, float drawY)
    {
        sb.setColor(color);
        sb.draw(img, drawX + img.offsetX - (float) img.originalWidth / 2.0F, drawY + img.offsetY - (float) img.originalHeight / 2.0F, (float) img.originalWidth / 2.0F - img.offsetX, (float) img.originalHeight / 2.0F - img.offsetY, (float) img.packedWidth, (float) img.packedHeight, this.drawScale * Settings.scale, this.drawScale * Settings.scale, this.angle);
    }

    private void RenderAtlas(SpriteBatch sb, Color color, TextureAtlas.AtlasRegion img, float drawX, float drawY, float scale)
    {
        sb.setColor(color);
        sb.draw(img, drawX + img.offsetX - (float) img.originalWidth / 2.0F, drawY + img.offsetY - (float) img.originalHeight / 2.0F, (float) img.originalWidth / 2.0F - img.offsetX, (float) img.originalHeight / 2.0F - img.offsetY, (float) img.packedWidth, (float) img.packedHeight, this.drawScale * Settings.scale * scale, this.drawScale * Settings.scale * scale, this.angle);
    }
}
