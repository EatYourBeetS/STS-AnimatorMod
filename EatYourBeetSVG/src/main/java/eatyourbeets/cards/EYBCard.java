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
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import eatyourbeets.resources.AbstractResources;
import eatyourbeets.resources.Resources_Animator_Images;
import eatyourbeets.resources.Resources_Common_Strings;
import eatyourbeets.utilities.RenderHelpers;
import eatyourbeets.utilities.Utilities;
import patches.AbstractEnums;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public abstract class EYBCard extends CustomCard
{
    protected final static Map<String, EYBCardData> staticCardData = new HashMap<>();

    private final static Color FRAME_COLOR = Color.WHITE.cpy();
    private final List<TooltipInfo> customTooltips = new ArrayList<>();
    private boolean lastHovered = false;
    private boolean hoveredInHand = false;

    protected final EYBCardText cardText;
    protected EYBCardData cardData;

    public boolean isSecondaryValueModified = false;
    public boolean upgradedSecondaryValue = false;
    public int baseSecondaryValue = 0;
    public int secondaryValue = 0;

    protected static String RegisterCard(String cardID, EYBCardBadge[] badges)
    {
        staticCardData.put(cardID, new EYBCardData(badges, AbstractResources.GetCardStrings(cardID)));

        return cardID;
    }

    protected EYBCard(EYBCardData cardData, String id, String imagePath, int cost, CardType type, CardColor color, CardRarity rarity, CardTarget target)
    {
        super(id, cardData.strings.NAME, imagePath, cost, "", type, color, rarity, target);

        this.cardData = cardData;
        this.cardText = new EYBCardText(this, cardData.strings);
        this.cardText.Update(0, true);
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
                RenderHelpers.RenderOnCardCentered(sb, this, FRAME_COLOR, ImageMaster.CARD_FRAME_ATTACK_COMMON, x, y);
                return;

            case SPECIAL:
                RenderHelpers.RenderOnCardCentered(sb, this, FRAME_COLOR, Resources_Animator_Images.CARD_FRAME_ATTACK_SPECIAL, x, y);
                return;

            case UNCOMMON:
                RenderHelpers.RenderOnCardCentered(sb, this, FRAME_COLOR, ImageMaster.CARD_FRAME_ATTACK_UNCOMMON, x, y);
                return;

            case RARE:
                RenderHelpers.RenderOnCardCentered(sb, this, FRAME_COLOR, ImageMaster.CARD_FRAME_ATTACK_RARE, x, y);
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
                RenderHelpers.RenderOnCardCentered(sb, this, FRAME_COLOR, ImageMaster.CARD_FRAME_SKILL_COMMON, x, y);
                return;

            case SPECIAL:
                RenderHelpers.RenderOnCardCentered(sb, this, FRAME_COLOR, Resources_Animator_Images.CARD_FRAME_SKILL_SPECIAL, x, y);
                return;

            case UNCOMMON:
                RenderHelpers.RenderOnCardCentered(sb, this, FRAME_COLOR, ImageMaster.CARD_FRAME_SKILL_UNCOMMON, x, y);
                return;

            case RARE:
                RenderHelpers.RenderOnCardCentered(sb, this, FRAME_COLOR, ImageMaster.CARD_FRAME_SKILL_RARE, x, y);
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
                RenderHelpers.RenderOnCardCentered(sb, this, FRAME_COLOR, ImageMaster.CARD_FRAME_POWER_COMMON, x, y);
                break;

            case SPECIAL:
                RenderHelpers.RenderOnCardCentered(sb, this, FRAME_COLOR, Resources_Animator_Images.CARD_FRAME_POWER_SPECIAL, x, y);
                return;

            case UNCOMMON:
                RenderHelpers.RenderOnCardCentered(sb, this, FRAME_COLOR, ImageMaster.CARD_FRAME_POWER_UNCOMMON, x, y);
                break;

            case RARE:
                RenderHelpers.RenderOnCardCentered(sb, this, FRAME_COLOR, ImageMaster.CARD_FRAME_POWER_RARE, x, y);
        }
    }
    
    @Override
    public void render(SpriteBatch sb)
    {
        UpdateCardText();
        super.render(sb);
        RenderHeader(sb, false);
        RenderBadges(sb, false);
        RenderCardPreview(sb, false);
    }

    @Override
    public void renderInLibrary(SpriteBatch sb)
    {
        UpdateCardText();
        super.renderInLibrary(sb);
        RenderHeader(sb, false);
        RenderBadges(sb, false);
        RenderCardPreview(sb, false);
    }

    public void renderInSingleCardPopup(SpriteBatch sb, boolean preRender)
    {
        if (preRender)
        {
            UpdateCardText();
        }
        else
        {
            RenderHeader(sb, true);
            RenderBadges(sb, true);
            RenderCardPreview(sb, true);
        }
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
        Initialize(baseDamage, baseBlock, -1, 0);
    }

    protected void Initialize(int baseDamage, int baseBlock, int baseMagicNumber)
    {
        Initialize(baseDamage, baseBlock, baseMagicNumber, 0);
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

            if (updateDescription)
            {
                cardText.Update(0, true);
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
        String[] info = this.cardData.strings.EXTENDED_DESCRIPTION;
        AddTooltip(new TooltipInfo(info[0], info[1] + param + info[2]));
    }

    protected void AddExtendedDescription(int headerIndex, int contentIndex)
    {
        String[] info = this.cardData.strings.EXTENDED_DESCRIPTION;
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

        cardData = staticCardData.get(cardID);

        if (!cardData.previewInitialized)
        {
            cardData.previewInitialized = true;
            return true;
        }

        return false;
    }

    protected void RenderBadges(SpriteBatch sb, boolean isCardPopup)
    {
        if (cardData.badges.length == 0)
        {
            return;
        }

        float x, y, scale;
        if (isCardPopup)
        {
            scale = Settings.scale;
            y = (float)Settings.HEIGHT / 2.0F + 160 * scale;
            x = (float)Settings.WIDTH / 2.0F - 320.0F * scale;

            float mX = Gdx.input.getX();
            float mY = Settings.HEIGHT - Gdx.input.getY();

            //Utilities.Logger.info("mY: " + mY + ", mY(s): " + mY * scale + ", Y: " + y);

            for (EYBCardBadge badge : cardData.badges)
            {
                RenderHelpers.RenderOnScreen(sb, badge.texture, x, y, 96);

                if (mX > (x + 10 * scale) && mX < (x + 90 * scale))
                {
                    if (mY < (y + 76 * scale) && mY > (y + 20 * scale))
                    {
                        String[] text = Resources_Common_Strings.CardBadges.TEXT;
                        TipHelper.renderGenericTip(x - (360 * scale), y + (96 * scale), text[badge.id + 1], text[0]);
                    }
                }

                y -= 48 * scale;
            }
        }
        else
        {
            scale = this.drawScale * Settings.scale;
            y = this.current_y + 80 * scale;
            x = this.current_x - 160 * scale;

            for (EYBCardBadge badge : cardData.badges)
            {
                RenderHelpers.RenderOnCard(sb, this, badge.texture, x, y, 48);
                y -= 24 * scale;
            }
        }
    }

    protected void RenderCardPreview(SpriteBatch sb, boolean isCardPopup)
    {
        final int DEFAULT_KEY = Input.Keys.SHIFT_LEFT;

        if (cardData.previewInitialized && (isCardPopup || (lastHovered && !hoveredInHand)) && !Settings.hideCards && Gdx.input.isKeyPressed(DEFAULT_KEY))
        {
            AbstractCard preview = cardData.GetCardPreview(this);
            if ((preview != null))
            {
                if (isCardPopup)
                {
                    preview.current_x = (float)Settings.WIDTH / 5.0F - 10.0F * Settings.scale;
                    preview.current_y = (float)Settings.HEIGHT / 4.0F;
                    preview.drawScale = 1f;
                }
                else
                {
                    preview.current_x = this.current_x;
                    preview.current_y = this.current_y;
                    preview.drawScale = this.drawScale;
                }

                preview.render(sb);
            }
        }
    }

    protected void RenderHeader(SpriteBatch sb, boolean isCardPopup)
    {
        String text = GetHeaderText();
        if (text != null && !this.isFlipped && !this.isLocked)
        {
            float xPos, yPos, offsetY;
            BitmapFont font;
            if (isCardPopup)
            {
                font = FontHelper.SCP_cardTitleFont_small;
                xPos = (float) Settings.WIDTH / 2.0F + (10 * Settings.scale);
                yPos = (float) Settings.HEIGHT / 2.0F + ((338.0F + 55) * Settings.scale);
                offsetY = 0;
            }
            else
            {
                font = FontHelper.cardTitleFont_small;
                xPos = current_x;
                yPos = current_y;
                offsetY = 400.0F * Settings.scale * this.drawScale / 2.0F;
            }

            BitmapFont.BitmapFontData fontData = font.getData();
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

            fontData.setScale(scaleMulti * (isCardPopup ? 1 : this.drawScale));

            FontHelper.renderRotatedText(sb, font, text,
                    xPos, yPos, 0.0F, offsetY,
                    this.angle, true, GetHeaderColor());

            fontData.setScale(originalScale);
        }
    }

    protected void UpdateCardText()
    {
        if (cardText.canUpdate)
        {
            int effectIndex = 0;
            if (EYBCardText.Toggled || Gdx.input.isButtonPressed(1))
            {
                effectIndex = 1;
            }

            cardText.Update(effectIndex, false);
        }
    }

    protected void upgradeSecondaryValue(int amount)
    {
        this.baseSecondaryValue += amount;
        this.secondaryValue = this.baseSecondaryValue;
        this.upgradedSecondaryValue = true;
    }
}
