package eatyourbeets.cards.base;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.evacipated.cardcrawl.modthespire.lib.SpireOverride;
import com.evacipated.cardcrawl.modthespire.lib.SpireSuper;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.*;

public abstract class EYBCardBase extends AbstractCard
{
    //@Formatter: Off
    @SpireOverride protected void renderGlow(SpriteBatch sb) { SpireSuper.call(sb); }
    @SpireOverride protected void updateGlow() { SpireSuper.call(); }
    @SpireOverride protected void renderBack(SpriteBatch sb, boolean hovered, boolean selected) { SpireSuper.call(sb, hovered, selected); }
    @SpireOverride protected void renderTint(SpriteBatch sb) { SpireSuper.call(sb); }
    @SpireOverride protected void renderDescription(SpriteBatch sb) { SpireSuper.call(sb); }
    @SpireOverride protected void renderType(SpriteBatch sb) { SpireSuper.call(sb); }
    @SpireOverride protected void renderJokePortrait(SpriteBatch sb) { renderPortrait(sb); }
    @SpireOverride private void renderDescriptionCN(SpriteBatch sb) { throw new RuntimeException("Not Implemented"); }
    @SpireOverride private void renderCard(SpriteBatch sb, boolean hovered, boolean selected) { Render(sb, hovered, selected, false); }
    @Override public final void render(SpriteBatch sb) { Render(sb, hovered,false, false); }
    @Override public final void render(SpriteBatch sb, boolean selected) { Render(sb, hovered, selected, false); }
    @Override public final void renderInLibrary(SpriteBatch sb) { Render(sb, hovered, false, true); }
    @Override public final void renderWithSelections(SpriteBatch sb) { Render(sb, false, true, false); }
    @Override public final void renderSmallEnergy(SpriteBatch sb, TextureAtlas.AtlasRegion region, float x, float y) { throw new RuntimeException("Not Implemented"); }
    @Override public final void renderCardPreviewInSingleView(SpriteBatch sb) { throw new RuntimeException("Not Implemented"); }
    @Override public final void renderCardPreview(SpriteBatch sb) { throw new RuntimeException("Not Implemented"); }
    @Override public final void initializeDescriptionCN() { initializeDescription(); }
    @Override public void calculateDamageDisplay(AbstractMonster mo) { calculateCardDamage(mo); }
    @Override public abstract void renderUpgradePreview(SpriteBatch sb);
    @Override public abstract void initializeDescription();
    @Override public abstract void calculateCardDamage(AbstractMonster mo);
    //@Formatter: On

    protected static final FieldInfo<Boolean> _darken = JavaUtilities.GetField("darken", AbstractCard.class);
    protected static final FieldInfo<Color> _renderColor = JavaUtilities.GetField("renderColor", AbstractCard.class);
    protected static final Color HOVER_IMG_COLOR = new Color(1.0F, 0.815F, 0.314F, 0.8F);
    protected static final Color SELECTED_CARD_COLOR = new Color(0.5F, 0.9F, 0.9F, 1.0F);
    protected static final float SHADOW_OFFSET_X = 18.0F * Settings.scale;
    protected static final float SHADOW_OFFSET_Y = 14.0F * Settings.scale;
    protected static AbstractPlayer player = null;

    public float hoverDuration;
    public boolean renderTip;
    public boolean hovered;
    public boolean cropPortrait = true;
    public boolean isPopup = false;
    public boolean isPreview = false;
    public boolean isSecondaryValueModified = false;
    public boolean upgradedSecondaryValue = false;
    public int baseSecondaryValue = 0;
    public int secondaryValue = 0;

    public static void RefreshPlayer()
    {
        player = AbstractDungeon.player;
    }

    public EYBCardBase(String id, String name, String imagePath, int cost, String rawDescription, CardType type, CardColor color, CardRarity rarity, CardTarget target)
    {
        super(id, name, "status/beta", "status/beta", cost, rawDescription, type, color, rarity, target);

        portrait = null;
        assetUrl = imagePath;

        if (imagePath != null)
        {
            LoadImage(null);
        }
    }

    public void LoadImage(String suffix)
    {
        portraitImg = GR.GetTextureMipMap(suffix == null ? assetUrl : assetUrl.replace(".png", suffix + ".png"));
    }

    public boolean IsOnScreen()
    {
        return current_y >= -200.0F * Settings.scale && current_y <= Settings.HEIGHT + 200.0F * Settings.scale;
    }

    @Override
    public void update()
    {
        super.update();

        // Adding this because UPDATEHOVERLOGIC() gets called at arbitrary times...
        if (GameUtilities.IsInGame() && player.hoveredCard != this && !AbstractDungeon.isScreenUp)
        {
            this.hovered = false;
            this.renderTip = false;
        }
    }

    @Override
    public void updateHoverLogic()
    {
        this.hb.update();

        if (this.hb.hovered)
        {
            hover();

            this.hoverDuration += Gdx.graphics.getRawDeltaTime();
            this.renderTip = (this.hoverDuration > 0.2F && !Settings.hideCards);
        }
        else
        {
            unhover();
        }
    }

    @Override
    public void unhover()
    {
        if (hovered)
        {
            this.hoverDuration = 0.0F;
            this.targetDrawScale = 0.75F;
        }

        this.hovered = false;
        this.renderTip = false;
    }

    @Override
    public void hover()
    {
        if (!hovered)
        {
            this.drawScale = 1.0F;
            this.targetDrawScale = 1.0F;
        }

        this.hovered = true;
    }

    @Override
    public void untip()
    {
        this.hoverDuration = 0.0F;
        this.renderTip = false;
    }

    public void Render(SpriteBatch sb, boolean hovered, boolean selected, boolean library)
    {
        if (Settings.hideCards || !IsOnScreen())
        {
            return;
        }

        if (flashVfx != null)
        {
            flashVfx.render(sb);
        }

        if (isFlipped)
        {
            renderBack(sb, hovered, selected);
            return;
        }

        if (SingleCardViewPopup.isViewingUpgrade && !isPreview && !isPopup && (library || AbstractDungeon.screen == AbstractDungeon.CurrentScreen.CARD_REWARD) && canUpgrade())
        {
            renderUpgradePreview(sb);
            return;
        }

        updateGlow();
        renderGlow(sb);
        renderImage(sb, hovered, selected);
        renderTitle(sb);
        renderType(sb);
        renderDescription(sb);
        renderTint(sb);
        renderEnergy(sb);
        hb.render(sb);
    }

    @SpireOverride
    protected void renderTitle(SpriteBatch sb)
    {
        final BitmapFont font = RenderHelpers.GetTitleFont(this);

        Color color;
        String text;
        if (isLocked || !isSeen)
        {
            color = Color.WHITE.cpy();
            text = isLocked ? LOCKED_STRING : UNKNOWN_STRING;
        }
        else
        {
            color = upgraded ? Settings.GREEN_TEXT_COLOR.cpy() : Color.WHITE.cpy();
            text = name;
        }

        RenderHelpers.WriteOnCard(sb, this, font, text, 0, RAW_H * 0.416f, color, false);
        RenderHelpers.ResetFont(font);
    }

    @SpireOverride
    protected void renderImage(SpriteBatch sb, boolean hovered, boolean selected)
    {
        if (player != null)
        {
            if (selected)
            {
                RenderAtlas(sb, Color.SKY, getCardBgAtlas(), current_x, current_y, 1.03F);
            }

            RenderAtlas(sb, new Color(0, 0, 0, transparency * 0.25f), getCardBgAtlas(), current_x + SHADOW_OFFSET_X * drawScale, current_y - SHADOW_OFFSET_Y * drawScale);
            if (player.hoveredCard == this && (player.isDraggingCard && player.isHoveringDropZone || player.inSingleTargetMode))
            {
                RenderAtlas(sb, HOVER_IMG_COLOR, getCardBgAtlas(), current_x, current_y);
            }
            else if (selected)
            {
                RenderAtlas(sb, SELECTED_CARD_COLOR, getCardBgAtlas(), current_x, current_y);
            }
        }

        renderPortrait(sb);
        renderCardBg(sb, current_x, current_y);
        renderPortraitFrame(sb, current_x, current_y);
        renderBannerImage(sb, current_x, current_y);
    }

    @SpireOverride
    protected void renderPortrait(SpriteBatch sb)
    {
        if (cropPortrait && drawScale > 0.6f && drawScale < 1 && GR.Animator.Config.GetCropCardImages())
        {
            int width = portraitImg.getWidth();
            int height = portraitImg.getHeight();
            int offset_x = (int) ((1 - drawScale) * (0.5f * width));
            int offset_y1 = 0;//(int) ((1-drawScale) * (0.5f * height));
            int offset_y2 = (int) ((1 - drawScale) * (1f * height));
            TextureRegion region = new TextureRegion(portraitImg, offset_x, offset_y1, width - (2 * offset_x), height - offset_y1 - offset_y2);
            RenderHelpers.DrawOnCardAuto(sb, this, region, new Vector2(0, 72), 250, 190, _renderColor.Get(this), transparency, 1);
        }
        else if (isPopup)
        {
            RenderHelpers.DrawOnCardAuto(sb, this, portraitImg, new Vector2(0, 72), 500, 380, _renderColor.Get(this), transparency, 0.5f);
        }
        else
        {
            RenderHelpers.DrawOnCardAuto(sb, this, portraitImg, new Vector2(0, 72), 250, 190, _renderColor.Get(this), transparency, 1f);
        }
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
                BitmapFont font = RenderHelpers.GetEnergyFont(this);
                RenderHelpers.WriteOnCard(sb, this, font, costString.text, -132.0F, 192.0F, costString.color);
                RenderHelpers.ResetFont(font);
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
