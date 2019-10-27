package eatyourbeets.cards;

import basemod.abstracts.CustomCard;
import basemod.helpers.TooltipInfo;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.evacipated.cardcrawl.modthespire.lib.SpireOverride;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.GetAllInBattleInstances;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.CardStrings;
import eatyourbeets.resources.AbstractResources;
import eatyourbeets.resources.Resources_Animator_Images;
import eatyourbeets.utilities.Utilities;
import org.apache.commons.lang3.StringUtils;
import patches.AbstractEnums;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public abstract class EYBCard extends CustomCard
{
    private final static Color FRAME_COLOR = Color.WHITE.cpy();
    private final static Map<String, EYBCardPreview> cardPreviews = new HashMap<>();
    private final List<TooltipInfo> customTooltips = new ArrayList<>();
    private boolean lastHovered = false;
    private boolean hoveredInHand = false;

    protected final CardStrings cardStrings;
    protected final String upgradedDescription;
    protected EYBCardPreview cardPreview;

    public boolean isSecondaryValueModified = false;
    public boolean upgradedSecondaryValue = false;
    public int baseSecondaryValue = 0;
    public int secondaryValue = 0;

    protected EYBCard(CardStrings strings, String id, String imagePath, int cost, CardType type, CardColor color, CardRarity rarity, CardTarget target)
    {
        super(id, strings.NAME, imagePath, cost, strings.DESCRIPTION, type, color, rarity, target);

        this.cardStrings = strings;
        if (StringUtils.isNotEmpty(strings.UPGRADE_DESCRIPTION))
        {
            this.upgradedDescription = strings.UPGRADE_DESCRIPTION;
        }
        else
        {
            this.upgradedDescription = null;
        }
    }

    @Override
    public List<TooltipInfo> getCustomTooltips()
    {
        if (isLocked || !isSeen || isFlipped)
        {
            return super.getCustomTooltips();
        }

        return customTooltips;
    }

    @Override
    public AbstractCard makeCopy()
    {
        try
        {
            return getClass().getConstructor().newInstance();
        }
        catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public AbstractCard makeStatEquivalentCopy()
    {
        AbstractCard result = super.makeStatEquivalentCopy();

        EYBCard copy = Utilities.SafeCast(result, EYBCard.class);
        if (copy != null)
        {
            copy.magicNumber = this.magicNumber;
            copy.isMagicNumberModified = this.isMagicNumberModified;

            copy.secondaryValue = this.secondaryValue;
            copy.baseSecondaryValue = this.baseSecondaryValue;
            copy.isSecondaryValueModified = this.isSecondaryValueModified;
        }

        return result;
    }

    @Override
    public boolean isHoveredInHand(float scale)
    {
        hoveredInHand = super.isHoveredInHand(scale);

        return hoveredInHand;
    }

    @Override
    public void hover()
    {
        super.hover();

        lastHovered = true;
    }

    @Override
    public void unhover()
    {
        super.unhover();

        lastHovered = false;
    }

    @SpireOverride
    protected void renderAttackPortrait(SpriteBatch sb, float x, float y)
    {
        switch (this.rarity)
        {
            case BASIC:
            case CURSE:
            case COMMON:
                this.renderHelper(sb, FRAME_COLOR, ImageMaster.CARD_FRAME_ATTACK_COMMON, x, y);
                return;

            case SPECIAL:
                this.renderHelper(sb, FRAME_COLOR, Resources_Animator_Images.CARD_FRAME_ATTACK_SPECIAL, x, y);
                return;

            case UNCOMMON:
                this.renderHelper(sb, FRAME_COLOR, ImageMaster.CARD_FRAME_ATTACK_UNCOMMON, x, y);
                return;

            case RARE:
                this.renderHelper(sb, FRAME_COLOR, ImageMaster.CARD_FRAME_ATTACK_RARE, x, y);
        }
    }

    @SpireOverride
    protected void renderSkillPortrait(SpriteBatch sb, float x, float y)
    {
        switch (this.rarity)
        {
            case BASIC:
            case CURSE:
            case COMMON:
                this.renderHelper(sb, FRAME_COLOR, ImageMaster.CARD_FRAME_SKILL_COMMON, x, y);
                return;

            case SPECIAL:
                this.renderHelper(sb, FRAME_COLOR, Resources_Animator_Images.CARD_FRAME_SKILL_SPECIAL, x, y);
                return;

            case UNCOMMON:
                this.renderHelper(sb, FRAME_COLOR, ImageMaster.CARD_FRAME_SKILL_UNCOMMON, x, y);
                return;

            case RARE:
                this.renderHelper(sb, FRAME_COLOR, ImageMaster.CARD_FRAME_SKILL_RARE, x, y);
        }
    }

    @SpireOverride
    protected void renderPowerPortrait(SpriteBatch sb, float x, float y)
    {
        switch (this.rarity)
        {
            case BASIC:
            case CURSE:
            case COMMON:
                this.renderHelper(sb, FRAME_COLOR, ImageMaster.CARD_FRAME_POWER_COMMON, x, y);
                break;

            case SPECIAL:
                this.renderHelper(sb, FRAME_COLOR, Resources_Animator_Images.CARD_FRAME_POWER_SPECIAL, x, y);
                return;

            case UNCOMMON:
                this.renderHelper(sb, FRAME_COLOR, ImageMaster.CARD_FRAME_POWER_UNCOMMON, x, y);
                break;

            case RARE:
                this.renderHelper(sb, FRAME_COLOR, ImageMaster.CARD_FRAME_POWER_RARE, x, y);
        }
    }
    
    @Override
    public void render(SpriteBatch sb)
    {
        super.render(sb);
        RenderHeader(sb);
        RenderCardPreview(sb);
    }

    @Override
    public void renderInLibrary(SpriteBatch sb)
    {
        super.renderInLibrary(sb);
        RenderHeader(sb);
        RenderCardPreview(sb);
    }

    public void renderInSingleCardPopup(SpriteBatch sb)
    {
        RenderHeader_Popup(sb);
        RenderCardPreview_Popup(sb);
    }

    public HashSet<AbstractCard> GetAllInBattleInstances()
    {
        HashSet<AbstractCard> cards = GetAllInBattleInstances.get(uuid);

        cards.add(this);

        return cards;
    }

    public HashSet<AbstractCard> GetAllInstances()
    {
        HashSet<AbstractCard> cards = GetAllInBattleInstances();

        AbstractCard masterDeckInstance = GetMasterDeckInstance();
        if (masterDeckInstance != null)
        {
            cards.add(masterDeckInstance);
        }

        return cards;
    }

    public HashSet<AbstractCard> GetOtherCardsInHand()
    {
        HashSet<AbstractCard> cards = new HashSet<>();
        for (AbstractCard c : AbstractDungeon.player.hand.group)
        {
            if (c != this)
            {
                cards.add(c);
            }
        }

        return cards;
    }

    public HashSet<AbstractCard> GetAllCopies()
    {
        HashSet<AbstractCard> cards = new HashSet<>();
        AbstractCard c;

        c = AbstractDungeon.player.cardInUse;
        if (c != null && c.cardID.equals(cardID))
        {
            cards.add(c);
        }

        Iterator var2 = AbstractDungeon.player.drawPile.group.iterator();
        while (var2.hasNext())
        {
            c = (AbstractCard) var2.next();
            if (c.cardID.equals(cardID))
            {
                cards.add(c);
            }
        }

        var2 = AbstractDungeon.player.discardPile.group.iterator();
        while (var2.hasNext())
        {
            c = (AbstractCard) var2.next();
            if (c.cardID.equals(cardID))
            {
                cards.add(c);
            }
        }

        var2 = AbstractDungeon.player.exhaustPile.group.iterator();
        while (var2.hasNext())
        {
            c = (AbstractCard) var2.next();
            if (c.cardID.equals(cardID))
            {
                cards.add(c);
            }
        }

        var2 = AbstractDungeon.player.limbo.group.iterator();
        while (var2.hasNext())
        {
            c = (AbstractCard) var2.next();
            if (c.cardID.equals(cardID))
            {
                cards.add(c);
            }
        }

        var2 = AbstractDungeon.player.hand.group.iterator();
        while (var2.hasNext())
        {
            c = (AbstractCard) var2.next();
            if (c.cardID.equals(cardID))
            {
                cards.add(c);
            }
        }

        return cards;
    }

    public AbstractCard GetMasterDeckInstance()
    {
        for (AbstractCard c : AbstractDungeon.player.masterDeck.group)
        {
            if (c.uuid == uuid)
            {
                return c;
            }
        }

        return null;
    }

    protected void Initialize(int baseDamage, int baseBlock)
    {
        Initialize(baseDamage, baseBlock, -1, -1);
    }

    protected void Initialize(int baseDamage, int baseBlock, int baseMagicNumber)
    {
        Initialize(baseDamage, baseBlock, baseMagicNumber, -1);
    }

    protected void Initialize(int baseDamage, int baseBlock, int baseMagicNumber, int baseSecondaryValue)
    {
        this.baseDamage = baseDamage;
        this.baseBlock = baseBlock;
        this.baseMagicNumber = this.magicNumber = baseMagicNumber;
        this.baseSecondaryValue = this.secondaryValue = baseSecondaryValue;
    }

    protected Boolean TryUpgrade()
    {
        return TryUpgrade(true);
    }

    protected Boolean TryUpgrade(boolean updateDescription)
    {
        if (!this.upgraded)
        {
            upgradeName();

            if (updateDescription && StringUtils.isNotEmpty(upgradedDescription))
            {
                this.rawDescription = upgradedDescription;
                this.initializeDescription();
            }

            return true;
        }

        return false;
    }

    protected void AddExtendedDescription()
    {
        AddExtendedDescription(0, 1);
    }

    protected void AddExtendedDescription(Object param)
    {
        String[] info = this.cardStrings.EXTENDED_DESCRIPTION;
        AddTooltip(new TooltipInfo(info[0], info[1] + param + info[2]));
    }

    protected void AddExtendedDescription(int headerIndex, int contentIndex)
    {
        String[] info = this.cardStrings.EXTENDED_DESCRIPTION;
        if (info != null && info.length >= 2 && info[headerIndex].length() > 0)
        {
            AddTooltip(new TooltipInfo(info[headerIndex], info[contentIndex]));
        }
    }

    protected void AddTooltip(TooltipInfo tooltip)
    {
        customTooltips.add(tooltip);
    }

    protected void SetMultiDamage(boolean value)
    {
        this.isMultiDamage = value;
    }

    protected void SetExhaust(boolean value)
    {
        this.exhaust = value;
    }

    protected void SetEthereal(boolean value)
    {
        this.isEthereal = value;
    }

    protected void SetHealing(boolean value)
    {
        if (value)
        {
            if (!tags.contains(CardTags.HEALING))
            {
                tags.add(CardTags.HEALING);
            }
        }
        else
        {
            tags.remove(CardTags.HEALING);
        }
    }

    protected void SetPurge(boolean value)
    {
        if (value)
        {
            if (!tags.contains(AbstractEnums.CardTags.PURGE))
            {
                tags.add(AbstractEnums.CardTags.PURGE);
            }
        }
        else
        {
            tags.remove(AbstractEnums.CardTags.PURGE);
        }
    }

    protected void SetUnique(boolean value)
    {
        if (value)
        {
            if (!tags.contains(AbstractEnums.CardTags.UNIQUE))
            {
                tags.add(AbstractEnums.CardTags.UNIQUE);
                Keyword unique = AbstractResources.GetKeyword("~Unique");
                AddTooltip(new TooltipInfo(unique.PROPER_NAME, unique.DESCRIPTION));
            }
        }
        else
        {
            tags.remove(AbstractEnums.CardTags.UNIQUE);
        }
    }

    protected Color GetHeaderColor()
    {
        return Settings.CREAM_COLOR.cpy();
    }

    protected String GetHeaderText()
    {
        return null;
    }

    protected boolean InitializingPreview()
    {
        Keyword preview = AbstractResources.GetKeyword("~Preview");
        AddTooltip(new TooltipInfo(preview.PROPER_NAME, preview.DESCRIPTION));

        cardPreview = cardPreviews.get(cardID);

        if (cardPreview == null)
        {
            cardPreview = new EYBCardPreview();
            cardPreviews.put(cardID, cardPreview);
        }

        return !cardPreview.initialized;
    }

    protected void RenderCardPreview(SpriteBatch sb)
    {
        final int DEFAULT_KEY = Input.Keys.SHIFT_LEFT;

        if (cardPreview != null && lastHovered && !Settings.hideCards && !hoveredInHand && Gdx.input.isKeyPressed(DEFAULT_KEY))
        {
            AbstractCard preview = cardPreview.GetCardPreview(this);
            if ((preview != null))
            {
                preview.current_x = this.current_x;
                preview.current_y = this.current_y;
                preview.drawScale = this.drawScale;

                preview.render(sb);
            }
        }
    }

    protected void RenderCardPreview_Popup(SpriteBatch sb)
    {
        final int DEFAULT_KEY = Input.Keys.SHIFT_LEFT;

        if (cardPreview != null && !Settings.hideCards && Gdx.input.isKeyPressed(DEFAULT_KEY))
        {
            AbstractCard preview = cardPreview.GetCardPreview(this);
            if ((preview != null))
            {
                preview.current_x = (float)Settings.WIDTH / 5.0F - 10.0F * Settings.scale;
                preview.current_y = (float)Settings.HEIGHT / 4.0F;
                preview.drawScale = 1f;

                preview.render(sb);
            }
        }
    }

    protected void RenderHeader(SpriteBatch sb)
    {
        String text = GetHeaderText();
        if (text != null && !this.isFlipped && !this.isLocked)
        {
            BitmapFont.BitmapFontData fontData = FontHelper.cardTitleFont_small.getData();

            float originalScale = fontData.scaleX;
            float scaleMulti = 0.8f;

            int length = text.length();
            if (length > 20)
            {
                scaleMulti -= 0.02f * (length - 20);
                if (scaleMulti < 0.5f)
                {
                    scaleMulti = 0.5f;
                }
            }

            fontData.setScale(this.drawScale * scaleMulti);

            FontHelper.renderRotatedText(sb, FontHelper.cardTitleFont_small, text,
                    this.current_x, this.current_y, 0.0F, 400.0F * Settings.scale * this.drawScale / 2.0F,
                    this.angle, true, GetHeaderColor());

            fontData.setScale(originalScale);
        }
    }

    protected void RenderHeader_Popup(SpriteBatch sb)
    {
        String text = GetHeaderText();
        if (text != null)
        {
            BitmapFont.BitmapFontData fontData = FontHelper.SCP_cardTitleFont_small.getData();

            float originalScale = fontData.scaleX;
            float scaleMulti = 0.8f;

            int length = text.length();
            if (length > 20)
            {
                scaleMulti -= 0.02f * (length - 20);
                if (scaleMulti < 0.5f)
                {
                    scaleMulti = 0.5f;
                }
            }

            fontData.setScale(scaleMulti);

            float xPos = (float) Settings.WIDTH / 2.0F + (10 * Settings.scale);
            float yPos = (float) Settings.HEIGHT / 2.0F + ((338.0F + 55) * Settings.scale);

            FontHelper.renderRotatedText(sb, FontHelper.SCP_cardTitleFont_small, text,
                    xPos, yPos, 0.0F, 0,
                    angle, true, GetHeaderColor());

            fontData.setScale(originalScale);
        }
    }

    protected void renderHelper(SpriteBatch sb, Color color, TextureAtlas.AtlasRegion img, float drawX, float drawY)
    {
        sb.setColor(color);
        sb.draw(img, drawX + img.offsetX - (float) img.originalWidth / 2.0F, drawY + img.offsetY - (float) img.originalHeight / 2.0F, (float) img.originalWidth / 2.0F - img.offsetX, (float) img.originalHeight / 2.0F - img.offsetY, (float) img.packedWidth, (float) img.packedHeight, this.drawScale * Settings.scale, this.drawScale * Settings.scale, this.angle);
    }

    protected void renderHelper(SpriteBatch sb, Color color, Texture img, float drawX, float drawY)
    {
        sb.setColor(color);
        sb.draw(img, drawX - (img.getWidth() / 2f), drawY - (img.getHeight() / 2f), 256.0F, 256.0F, 512.0F, 512.0F,
                this.drawScale * Settings.scale, this.drawScale * Settings.scale,
                this.angle, 0, 0, 512, 512, false, false);
    }

    protected void upgradeSecondaryValue(int amount)
    {
        this.baseSecondaryValue += amount;
        this.secondaryValue = this.baseSecondaryValue;
        this.upgradedSecondaryValue = true;
    }
}
