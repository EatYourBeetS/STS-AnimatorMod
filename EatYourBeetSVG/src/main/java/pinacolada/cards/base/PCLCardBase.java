package pinacolada.cards.base;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.SoulboundField;
import com.evacipated.cardcrawl.modthespire.lib.SpireOverride;
import com.evacipated.cardcrawl.modthespire.lib.SpireSuper;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import eatyourbeets.cards.base.EYBCardBase;
import eatyourbeets.utilities.AdvancedTexture;
import eatyourbeets.utilities.ColoredString;
import eatyourbeets.utilities.EYBFontHelper;
import pinacolada.cards.pcl.colorless.QuestionMark;
import pinacolada.effects.card.PCLCardGlowBorderEffect;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLGameUtilities;

import java.util.ArrayList;
import java.util.Iterator;

public abstract class PCLCardBase extends EYBCardBase
{
    protected static final Color HOVER_IMG_COLOR = new Color(1f, 0.815f, 0.314f, 0.8f);
    protected static final Color SELECTED_CARD_COLOR = new Color(0.5f, 0.9f, 0.9f, 1f);
    protected static final float SHADOW_OFFSET_X = 18f * Settings.scale;
    protected static final float SHADOW_OFFSET_Y = EYBCardBase.SHADOW_OFFSET_Y;

    protected static final Color COLOR_COMMON = new Color(0.65f, 0.65f, 0.65f, 1f);
    protected static final Color COLOR_UNCOMMON = new Color(0.5f, 0.85f, 0.95f, 1f);
    protected static final Color COLOR_RARE = new Color(0.99f, 0.8f, 0.35f, 1f);
    protected static final Color COLOR_SPECIAL = new Color(1f, 1f, 1f, 1f);

    public static boolean canCropPortraits = true;
    public static AbstractPlayer player = null;

    public float hoverDuration;
    public boolean renderTip;
    public boolean hovered;
    public boolean cropPortrait = true;
    public boolean isPopup = false;
    public boolean isPreview = false;
    public boolean isSecondaryValueModified = false;
    public boolean isHitCountModified = false;
    public boolean upgradedSecondaryValue = false;
    public boolean upgradedCooldownValue = false;
    public boolean upgradedHitCount = false;
    public int baseSecondaryValue = 0;
    public int secondaryValue = 0;
    public int baseCooldownValue = 0;
    public int cooldownValue = 0;
    public int hitCount = 1;
    public int baseHitCount = 1;

    public PCLCardCooldown cooldown;
    protected AdvancedTexture portraitImg;
    protected AdvancedTexture portraitForeground;
    protected final ArrayList<PCLCardGlowBorderEffect> glowList = new ArrayList<>();

    public PCLCardBase(String id, String name, String imagePath, int cost, String rawDescription, CardType type, CardColor color, CardRarity rarity, CardTarget target)
    {
        super(id, name, imagePath, cost, rawDescription, type, color, rarity, target);

        portrait = null;
        assetUrl = imagePath;

        if (rarity == CardRarity.SPECIAL)
        {
            SoulboundField.soulbound.set(this, true);
        }

        if (imagePath != null)
        {
            LoadImage(null);
        }
    }

    public void LoadImage(String suffix)
    {
        portraitImg = new AdvancedTexture(GR.GetTexture(suffix == null ? assetUrl : assetUrl.replace(".png", suffix + ".png"), true), null);
    }

    public boolean IsOnScreen()
    {
        return current_y >= -200f * Settings.scale && current_y <= Settings.HEIGHT + 200f * Settings.scale;
    }

    @Override
    public void update()
    {
        super.update();

        // Adding this because UPDATEHOVERLOGIC() gets called at arbitrary times...
        if (PCLGameUtilities.InGame() && player != null && player.hoveredCard != this && !AbstractDungeon.isScreenUp)
        {
            this.hovered = false;
            this.renderTip = false;
        }
    }

    @Override
    public void untip()
    {
        this.hoverDuration = 0f;
        this.renderTip = false;
    }

    public void stopGlowing(float delay)
    {
        super.stopGlowing();

        _glowTimer.Set(this, delay);
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

        if (PCLGameUtilities.CanShowUpgrades(library) && !isPreview && !isPopup && canUpgrade())
        {
            updateGlow();
            renderGlow(sb);
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
        final BitmapFont font = pinacolada.utilities.PCLRenderHelpers.GetTitleFont(this);

        Color color;
        String text;
        if (isLocked || !isSeen)
        {
            color = Color.WHITE;
            text = isLocked ? LOCKED_STRING : UNKNOWN_STRING;
        }
        else
        {
            color = upgraded ? Settings.GREEN_TEXT_COLOR : Color.WHITE;
            text = name;
        }

        pinacolada.utilities.PCLRenderHelpers.WriteOnCard(sb, this, font, text, 0, RAW_H * 0.416f, color, false);
        pinacolada.utilities.PCLRenderHelpers.ResetFont(font);
    }

    @SpireOverride
    protected void renderType(SpriteBatch sb)
    {
        BitmapFont font = EYBFontHelper.CardIconFont_Small;
        Color color = Color.WHITE.cpy();//_typeColor.Get(this);
        color.a = _renderColor.Get(this).a;
        font.getData().setScale(drawScale * 0.9f);
        FontHelper.renderRotatedText(sb, font, GetTypeText(), current_x, current_y - 22.0f * drawScale * Settings.scale, 0.0F, -1.0F * this.drawScale * Settings.scale, angle, false, color);
    }

    @SpireOverride
    protected void updateGlow()
    {
        float newValue = _glowTimer.Get(this);
        if (this.isGlowing) {
            newValue -= Gdx.graphics.getDeltaTime();
            if (newValue < 0.0F) {
                glowList.add(new PCLCardGlowBorderEffect(this, this.glowColor));
                newValue = 0.5F;
            }
            _glowTimer.Set(this,newValue);
        }

        Iterator<PCLCardGlowBorderEffect> i = this.glowList.iterator();

        while(i.hasNext()) {
            PCLCardGlowBorderEffect e = i.next();
            e.update();
            if (e.isDone) {
                i.remove();
            }
        }
    }

    @SpireOverride
    protected void renderGlow(SpriteBatch sb)
    {
        if (transparency < 0.7f)
        {
            return;
        }

        renderMainBorder(sb);

        for (PCLCardGlowBorderEffect glowBorder : glowList)
        {
            glowBorder.render(sb);
        }

        sb.setBlendFunction(770, 771);
    }

    @SpireOverride
    protected void renderMainBorder(SpriteBatch sb)
    {
        if (!this.isGlowing)
        {
            return;
        }

        final TextureAtlas.AtlasRegion img;
        switch (this.type)
        {
            case ATTACK:
                img = ImageMaster.CARD_ATTACK_BG_SILHOUETTE;
                break;

            case POWER:
                img = ImageMaster.CARD_POWER_BG_SILHOUETTE;
                break;

            default:
                img = ImageMaster.CARD_SKILL_BG_SILHOUETTE;
                break;
        }

        if (PCLGameUtilities.InBattle(false))
        {
            sb.setColor(this.glowColor);
        }
        else
        {
            sb.setColor(GREEN_BORDER_GLOW_COLOR);
        }

        sb.setBlendFunction(770, 1);
        sb.draw(img, this.current_x + img.offsetX - (img.originalWidth / 2f), this.current_y + img.offsetY - (img.originalWidth / 2f),
                (img.originalWidth / 2f) - img.offsetX, (img.originalWidth / 2f) - img.offsetY, img.packedWidth, img.packedHeight,
                this.drawScale * Settings.scale * 1.04f, this.drawScale * Settings.scale * 1.03f, this.angle);
    }

    @SpireOverride
    protected void renderImage(SpriteBatch sb, boolean hovered, boolean selected)
    {
        if (player != null)
        {
            if (selected)
            {
                RenderAtlas(sb, Color.SKY, getCardBgAtlas(), current_x, current_y, 1.03f);
            }

            RenderAtlas(sb, new Color(0, 0, 0, transparency * 0.25f), getCardBgAtlas(), current_x + SHADOW_OFFSET_X * drawScale, current_y - SHADOW_OFFSET_Y * drawScale);
            if ((player.hoveredCard == this) && ((player.isDraggingCard && player.isHoveringDropZone) || player.inSingleTargetMode))
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
        if (!isSeen || isLocked)
        {
            RenderPortraitImage(sb, GR.GetTexture(QuestionMark.DATA.ImagePath), _renderColor.Get(this), 1, false, false, false);
            return;
        }

        final boolean cropPortrait = canCropPortraits && (this.cropPortrait && GR.PCL.Config.CropCardImages.Get());
        AdvancedTexture image = GetPortraitImage();
        if (image != null)
        {
            RenderPortraitImage(sb, image.texture, image.color, image.GetScale(), cropPortrait, false, false);
        }
        image = GetPortraitForeground();
        if (image != null)
        {
            RenderPortraitImage(sb, image.texture, image.color, image.GetScale(), cropPortrait, image.GetScale() != 1, true);
        }
    }

    protected void RenderPortraitImage(SpriteBatch sb, Texture texture, Color color, float scale, boolean cropPortrait, boolean useTextureSize, boolean foreground)
    {
        if (color == null)
        {
            color = _renderColor.Get(this);
        }

        final float render_width = useTextureSize ? texture.getWidth() : 250;
        final float render_height = useTextureSize ? texture.getWidth() : 190;
        if (cropPortrait && drawScale > 0.6f && drawScale < 1)
        {
            final int width = texture.getWidth();
            final int offset_x = (int) ((1 - drawScale) * (0.5f * width));
            TextureAtlas.AtlasRegion region = foreground ? jokePortrait : portrait;
            if (region == null || texture != region.getTexture() || (region.getRegionX() != offset_x) || GR.UI.Elapsed50())
            {
                final int height = texture.getHeight();
                final int offset_y1 = 0;//(int) ((1-drawScale) * (0.5f * height));
                final int offset_y2 = (int) ((1 - drawScale) * (1f * height));
                if (region == null)
                {
                    region = new TextureAtlas.AtlasRegion(texture, offset_x, offset_y1, width - (2 * offset_x), height - offset_y1 - offset_y2);
                    if (foreground)
                    {
                        jokePortrait = region; // let's just reuse this.
                    }
                    else
                    {
                        portrait = region;
                    }
                }
                else
                {
                    region.setRegion(texture);
                    region.setRegion(offset_x, offset_y1, width - (2 * offset_x), height - offset_y1 - offset_y2);
                }
            }

            pinacolada.utilities.PCLRenderHelpers.DrawOnCardAuto(sb, this, region, new Vector2(0, 72), render_width, render_height, color, transparency, scale);
        }
        else if (isPopup)
        {
            pinacolada.utilities.PCLRenderHelpers.DrawOnCardAuto(sb, this, texture, new Vector2(0, 72), render_width * 2, render_height * 2, color, transparency, scale * 0.5f);
        }
        else
        {
            pinacolada.utilities.PCLRenderHelpers.DrawOnCardAuto(sb, this, texture, new Vector2(0, 72), render_width, render_height, color, transparency, scale);
        }
    }

    @SpireOverride
    protected void renderBannerImage(SpriteBatch sb, float drawX, float drawY)
    {
        if (!TryRenderCentered(sb, GetCardBanner(), isPopup ? 0.5f : 1f))
        {
            SpireSuper.call(sb, drawX, drawY);
        }
    }

    @SpireOverride
    protected void renderPortraitFrame(SpriteBatch sb, float x, float y)
    {
        if (!TryRenderCentered(sb, GetPortraitFrame(), isPopup ? 0.5f : 1f))
        {
            SpireSuper.call(sb, x, y);
        }
    }

    @SpireOverride
    protected void renderCardBg(SpriteBatch sb, float x, float y)
    {
        float popUpMultiplier = isPopup ? 0.5f : 1f;
        boolean result = this.color == CardColor.COLORLESS ? pinacolada.utilities.PCLRenderHelpers.DrawGrayscale(sb, () -> TryRenderCentered(sb, GetCardBackground(), popUpMultiplier)) : TryRenderCentered(sb, GetCardBackground(), popUpMultiplier);
        if (!result)
        {
            SpireSuper.call(sb, x, y);
        }

        TryRenderCentered(sb, GetCardBorderIndicator());
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
                BitmapFont font = pinacolada.utilities.PCLRenderHelpers.GetEnergyFont(this);
                pinacolada.utilities.PCLRenderHelpers.WriteOnCard(sb, this, font, costString.text, -132f, 192f, costString.color);
                pinacolada.utilities.PCLRenderHelpers.ResetFont(font);
            }
        }
    }

    public AdvancedTexture GetCardAttributeBanner()
    {
        return null;
    }

    protected AdvancedTexture GetPortraitImage()
    {
        return portraitImg;
    }

    protected AdvancedTexture GetPortraitForeground()
    {
        return portraitForeground;
    }

    protected AdvancedTexture GetPortraitFrame()
    {
        return null;
    }

    protected AdvancedTexture GetCardBanner()
    {
        return null;
    }

    protected AdvancedTexture GetCardBorderIndicator()
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

    protected String GetTypeText()
    {
        switch (this.type)
        {
            case ATTACK:
                return TEXT[0];

            case CURSE:
                return TEXT[3];

            case STATUS:
                return TEXT[7];

            case SKILL:
                return TEXT[1];

            case POWER:
                return TEXT[2];

            default:
                return TEXT[5];
        }
    }

    public ColoredString GetMagicNumberString()
    {
        if (isMagicNumberModified)
        {
            return new ColoredString(magicNumber, magicNumber >= baseMagicNumber ? Settings.GREEN_TEXT_COLOR : Settings.RED_TEXT_COLOR);
        }

        return new ColoredString(baseMagicNumber, Settings.CREAM_COLOR);
    }

    public ColoredString GetBlockString()
    {
        if (isBlockModified)
        {
            return new ColoredString(block, block >= baseBlock ? Settings.GREEN_TEXT_COLOR : Settings.RED_TEXT_COLOR);
        }

        return new ColoredString(baseBlock, Settings.CREAM_COLOR);
    }

    public ColoredString GetDamageString()
    {
        if (isDamageModified)
        {
            return new ColoredString(damage, damage >= baseDamage ? Settings.GREEN_TEXT_COLOR : Settings.RED_TEXT_COLOR);
        }

        return new ColoredString(baseDamage, Settings.CREAM_COLOR);
    }

    public ColoredString GetSecondaryValueString()
    {
        if (isSecondaryValueModified)
        {
            return new ColoredString(secondaryValue, secondaryValue >= baseSecondaryValue ? Settings.GREEN_TEXT_COLOR : Settings.RED_TEXT_COLOR);
        }
        else
        {
            return new ColoredString(baseSecondaryValue, Settings.CREAM_COLOR);
        }
    }

    public ColoredString GetCooldownString()
    {
        return cooldown != null ? cooldown.GetCooldownValueString() : null;
    }

    public ColoredString GetSpecialVariableString()
    {
        return new ColoredString(misc, misc > 0 ? Settings.GREEN_TEXT_COLOR : Settings.CREAM_COLOR);
    }

    public Color GetRarityColor(boolean alt)
    {
        switch (rarity)
        {
            case SPECIAL:
                return COLOR_SPECIAL;

            case UNCOMMON:
                return COLOR_UNCOMMON;

            case RARE:
                return COLOR_RARE;

            case BASIC:
            case COMMON:
            case CURSE:
            default:
                return COLOR_COMMON;
        }
    }

    protected ColoredString GetCostString()
    {
        final ColoredString result = new ColoredString();

        if (cost == -1)
        {
            result.text = "X";
        }
        else
        {
            result.text = freeToPlay() ? "0" : Integer.toString(Math.max(0, this.costForTurn));
        }

        if (player != null && player.hand.contains(this) && (!this.hasEnoughEnergy() || PCLGameUtilities.IsUnplayableThisTurn(this)))
        {
            result.color = new Color(1f, 0.3f, 0.3f, transparency);
        }
        else if ((upgradedCost && isCostModified) || costForTurn < cost || (cost > 0 && this.freeToPlay()))
        {
            result.color = new Color(0.4f, 1f, 0.4f, transparency);
        }
        else
        {
            result.color = new Color(1f, 1f, 1f, transparency);
        }

        return result;
    }

    public ColoredString GetXString()
    {
        if (PCLGameUtilities.InBattle() && player != null) {
            int value = GetXValue();
            if (value >= 0)
            {
                return new ColoredString(value, Settings.GREEN_TEXT_COLOR);
            }
        }

        return new ColoredString("X", Settings.CREAM_COLOR);
    }

    public int GetXValue() {
        return -1;
    }

    protected void upgradeSecondaryValue(int amount)
    {
        this.baseSecondaryValue += amount;
        this.secondaryValue = this.baseSecondaryValue;
        this.upgradedSecondaryValue = true;
    }

    protected void upgradeCooldownValue(int amount)
    {
        this.baseCooldownValue += amount;
        this.cooldownValue = Math.min(this.cooldownValue, this.baseCooldownValue);
        this.upgradedCooldownValue = true;
    }

    protected void upgradeHitCount(int amount)
    {
        this.baseHitCount += amount;
        this.hitCount = this.baseHitCount;
        this.upgradedHitCount = true;
    }

    protected void SetTag(CardTags tag, boolean enable)
    {
        if (!enable)
        {
            tags.remove(tag);
        }
        else if (!tags.contains(tag))
        {
            tags.add(tag);
        }
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    protected boolean TryRenderCentered(SpriteBatch sb, AdvancedTexture texture)
    {
        if (texture != null)
        {
            pinacolada.utilities.PCLRenderHelpers.DrawOnCardAuto(sb, this, texture, 0, 0, texture.GetWidth(), texture.GetHeight());

            return true;
        }

        return false;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    protected boolean TryRenderCentered(SpriteBatch sb, AdvancedTexture texture, float scale)
    {
        if (texture != null)
        {
            pinacolada.utilities.PCLRenderHelpers.DrawOnCardAuto(sb, this, texture, 0, 0, texture.GetWidth(), texture.GetHeight(), scale);

            return true;
        }

        return false;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    protected boolean TryRenderCentered(SpriteBatch sb, Texture texture)
    {
        if (texture != null)
        {
            pinacolada.utilities.PCLRenderHelpers.DrawOnCardAuto(sb, this, texture, _renderColor.Get(this), 0, 0, texture.getWidth(), texture.getHeight());

            return true;
        }

        return false;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    protected boolean TryRenderCentered(SpriteBatch sb, Texture texture, float scale)
    {
        if (texture != null)
        {
            pinacolada.utilities.PCLRenderHelpers.DrawOnCardAuto(sb, this, texture, new Vector2(0, 0), texture.getWidth(), texture.getHeight(), _renderColor.Get(this), transparency, scale);

            return true;
        }

        return false;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    protected boolean TryRender(SpriteBatch sb, Texture texture, float scale, Vector2 offset)
    {
        if (texture != null)
        {
            pinacolada.utilities.PCLRenderHelpers.DrawOnCardAuto(sb, this, texture, offset, texture.getWidth(), texture.getHeight(), _renderColor.Get(this), transparency, scale);

            return true;
        }

        return false;
    }

    protected void RenderAtlas(SpriteBatch sb, Color color, TextureAtlas.AtlasRegion img, float drawX, float drawY)
    {
        sb.setColor(color);
        sb.draw(img, drawX + img.offsetX - (float) img.originalWidth / 2f, drawY + img.offsetY - (float) img.originalHeight / 2f, (float) img.originalWidth / 2f - img.offsetX, (float) img.originalHeight / 2f - img.offsetY, (float) img.packedWidth, (float) img.packedHeight, this.drawScale * Settings.scale, this.drawScale * Settings.scale, this.angle);
    }

    protected void RenderAtlas(SpriteBatch sb, Color color, TextureAtlas.AtlasRegion img, float drawX, float drawY, float scale)
    {
        sb.setColor(color);
        sb.draw(img, drawX + img.offsetX - (float) img.originalWidth / 2f, drawY + img.offsetY - (float) img.originalHeight / 2f, (float) img.originalWidth / 2f - img.offsetX, (float) img.originalHeight / 2f - img.offsetY, (float) img.packedWidth, (float) img.packedHeight, this.drawScale * Settings.scale * scale, this.drawScale * Settings.scale * scale, this.angle);
    }
}