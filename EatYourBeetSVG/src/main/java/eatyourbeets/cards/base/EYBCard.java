package eatyourbeets.cards.base;

import basemod.abstracts.CustomCard;
import basemod.helpers.TooltipInfo;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireOverride;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.BlockAttribute;
import eatyourbeets.cards.base.attributes.DamageAttribute;
import eatyourbeets.resources.GR;
import eatyourbeets.ui.animator.cardReward.AnimatorCardBadgeLegend;
import eatyourbeets.utilities.FieldInfo;
import eatyourbeets.utilities.JavaUtilities;
import eatyourbeets.utilities.RenderHelpers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class EYBCard extends CustomCard
{
    public enum AttackType
    {
        Normal(false, false),
        Elemental(true, true),
        Piercing(true, true),
        Ranged(false, true);

        public final boolean bypassThorns;
        public final boolean bypassBlock;

        AttackType(boolean bypassBlock, boolean bypassThorns)
        {
            this.bypassThorns = bypassThorns;
            this.bypassBlock = bypassBlock;
        }
    }

    protected static final FieldInfo<Boolean> _renderTip = JavaUtilities.GetField("renderTip", AbstractCard.class);
    protected static final Color FRAME_COLOR = Color.WHITE.cpy();
    protected static final Map<String, EYBCardData> staticCardData = new HashMap<>();
    protected static AbstractPlayer player = null;

    public AttackType attackType = AttackType.Normal;
    public final List<TooltipInfo> customTooltips = new ArrayList<>();
    public boolean hovered = false;
    public boolean hoveredInHand = false;

    protected final EYBCardText cardText;
    protected final EYBCardData cardData;
    protected boolean isMultiUpgrade;
    protected int upgrade_damage;
    protected int upgrade_magicNumber;
    protected int upgrade_secondaryValue;
    protected int upgrade_block;
    protected int upgrade_cost;

    public boolean isSecondaryValueModified = false;
    public boolean upgradedSecondaryValue = false;
    public int baseSecondaryValue = 0;
    public int secondaryValue = 0;

    public static void RefreshPlayer()
    {
        player = AbstractDungeon.player;
    }

    public static EYBCardData GetCardData(String cardID)
    {
        return staticCardData.get(cardID);
    }

    public static String RegisterCard(Class<? extends EYBCard> type, String cardID, EYBCardBadge[] badges)
    {
        staticCardData.put(cardID, new EYBCardData(type, badges, GR.GetCardStrings(cardID)));

        return cardID;
    }

    protected EYBCard(EYBCardData cardData, String id, String imagePath, int cost, CardType type, CardColor color, CardRarity rarity, CardTarget target)
    {
        super(id, cardData.strings.NAME, imagePath, cost, "", type, color, rarity, target);

        this.cardData = cardData;
        this.cardText = new EYBAdvancedCardText(this, cardData.strings);
        this.cardText.ForceRefresh();
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
        return cardData.CreateNewInstance();
    }

    @Override
    public AbstractCard makeStatEquivalentCopy()
    {
        EYBCard copy = (EYBCard) super.makeStatEquivalentCopy();

        copy.magicNumber = this.magicNumber;
        copy.isMagicNumberModified = this.isMagicNumberModified;

        copy.secondaryValue = this.secondaryValue;
        copy.baseSecondaryValue = this.baseSecondaryValue;
        copy.isSecondaryValueModified = this.isSecondaryValueModified;

        return copy;
    }

    @Override
    public void hover()
    {
        super.hover();

        hovered = true;
    }

    @Override
    public void update()
    {
        super.update();
    }

    @Override
    public void unhover()
    {
        super.unhover();

        hovered = false;
    }

    @SpireOverride
    protected void renderAttackPortrait(SpriteBatch sb, float x, float y)
    {
        FRAME_COLOR.a = this.transparency;

        switch (this.rarity)
        {
            case BASIC:
            case CURSE:
            case COMMON:
                RenderHelpers.DrawOnCardCentered(sb, this, FRAME_COLOR, ImageMaster.CARD_FRAME_ATTACK_COMMON, x, y);
                return;

            case SPECIAL:
                RenderHelpers.DrawOnCardCentered(sb, this, FRAME_COLOR, GR.Animator.Images.CARD_FRAME_ATTACK_SPECIAL.Texture(), x, y);
                return;

            case UNCOMMON:
                RenderHelpers.DrawOnCardCentered(sb, this, FRAME_COLOR, ImageMaster.CARD_FRAME_ATTACK_UNCOMMON, x, y);
                return;

            case RARE:
                RenderHelpers.DrawOnCardCentered(sb, this, FRAME_COLOR, ImageMaster.CARD_FRAME_ATTACK_RARE, x, y);
        }
    }

    @SpireOverride
    protected void renderSkillPortrait(SpriteBatch sb, float x, float y)
    {
        FRAME_COLOR.a = this.transparency;

        switch (this.rarity)
        {
            case BASIC:
            case CURSE:
            case COMMON:
                RenderHelpers.DrawOnCardCentered(sb, this, FRAME_COLOR, ImageMaster.CARD_FRAME_SKILL_COMMON, x, y);
                return;

            case SPECIAL:
                RenderHelpers.DrawOnCardCentered(sb, this, FRAME_COLOR, GR.Animator.Images.CARD_FRAME_SKILL_SPECIAL.Texture(), x, y);
                return;

            case UNCOMMON:
                RenderHelpers.DrawOnCardCentered(sb, this, FRAME_COLOR, ImageMaster.CARD_FRAME_SKILL_UNCOMMON, x, y);
                return;

            case RARE:
                RenderHelpers.DrawOnCardCentered(sb, this, FRAME_COLOR, ImageMaster.CARD_FRAME_SKILL_RARE, x, y);
        }
    }

    @SpireOverride
    protected void renderPowerPortrait(SpriteBatch sb, float x, float y)
    {
        FRAME_COLOR.a = this.transparency;

        switch (this.rarity)
        {
            case BASIC:
            case CURSE:
            case COMMON:
                RenderHelpers.DrawOnCardCentered(sb, this, FRAME_COLOR, ImageMaster.CARD_FRAME_POWER_COMMON, x, y);
                break;

            case SPECIAL:
                RenderHelpers.DrawOnCardCentered(sb, this, FRAME_COLOR, GR.Animator.Images.CARD_FRAME_POWER_SPECIAL.Texture(), x, y);
                return;

            case UNCOMMON:
                RenderHelpers.DrawOnCardCentered(sb, this, FRAME_COLOR, ImageMaster.CARD_FRAME_POWER_UNCOMMON, x, y);
                break;

            case RARE:
                RenderHelpers.DrawOnCardCentered(sb, this, FRAME_COLOR, ImageMaster.CARD_FRAME_POWER_RARE, x, y);
        }
    }

    @Override
    public void render(SpriteBatch sb)
    {
        if (!Settings.hideCards)
        {
            if (AnimatorCardBadgeLegend.showUpgrades && canUpgrade() && !CardCrawlGame.isPopupOpen && SingleCardViewPopup.isViewingUpgrade)
            {
                EYBCard copy = (EYBCard) this.makeStatEquivalentCopy();
                copy.current_x = this.current_x;
                copy.current_y = this.current_y;
                copy.drawScale = this.drawScale;
                copy.upgrade();
                copy.displayUpgrades();
                copy.renderAsPreview(sb);
                return;
            }

            boolean isLibrary = AbstractDungeon.player == null && SingleCardViewPopup.isViewingUpgrade;

            UpdateCardText();
            super.render(sb);
            RenderHeader(sb, false);
            RenderCardPreview(sb, false);
        }
    }

    public void renderAsPreview(SpriteBatch sb)
    {
        if (!Settings.hideCards)
        {
            UpdateCardText();
            super.render(sb);
            RenderHeader(sb, false);
        }
    }

    @Override
    public void renderInLibrary(SpriteBatch sb)
    {
        if (this.isOnScreen())
        {
            if (SingleCardViewPopup.isViewingUpgrade && this.isSeen && !this.isLocked)
            {
                super.renderInLibrary(sb);
            }
            else
            {
                UpdateCardText();
                super.renderInLibrary(sb);
                RenderHeader(sb, false);
                RenderCardPreview(sb, false);
            }
        }
    }

    @Override
    public void initializeDescription()
    {
        if (cardText != null)
        {
            this.cardText.InitializeDescription();
        }
    }

    @Override
    public void initializeDescriptionCN()
    {
        if (cardText != null)
        {
            this.cardText.InitializeDescription();
        }
    }

    @SpireOverride
    public void renderDescription(SpriteBatch sb)
    {
        this.cardText.RenderDescription(sb);
    }

    @Override
    public void renderCardTip(SpriteBatch sb)
    {
        this.cardText.RenderTooltips(sb);
    }

    @Override
    public void triggerWhenCopied()
    {
        // this is only used by ShowCardAndAddToHandEffect
        super.triggerWhenCopied();
        triggerWhenDrawn();
    }

    public boolean isOnScreen()
    {
        return this.current_y >= -200.0F * Settings.scale && this.current_y <= (float) Settings.HEIGHT + 200.0F * Settings.scale;
    }

    public boolean CanRenderTip()
    {
        return GR.UI.CardPopup.GetCard() == this || _renderTip.Get(this);
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
        EYBCardTooltip preview = GR.GetTooltip("~preview");
        AddTooltip(new TooltipInfo(preview.title, preview.description));

        if (!cardData.previewInitialized)
        {
            cardData.previewInitialized = true;
            return true;
        }

        return false;
    }

    protected void RenderCardPreview(SpriteBatch sb, boolean isCardPopup)
    {
        final int DEFAULT_KEY = Input.Keys.SHIFT_LEFT;

        if (cardData.previewInitialized && CanRenderTip() && !Settings.hideCards && Gdx.input.isKeyPressed(DEFAULT_KEY))
        {
            AbstractCard preview = cardData.GetCardPreview(this);
            if ((preview != null))
            {
                if (isCardPopup)
                {
                    preview.current_x = (float) Settings.WIDTH / 5.0F - 10.0F * Settings.scale;
                    preview.current_y = (float) Settings.HEIGHT / 4.0F;
                    preview.drawScale = 1f;
                }
                else
                {
                    preview.current_x = this.current_x;
                    preview.current_y = this.current_y;
                    preview.drawScale = this.drawScale;
                }

                EYBCard card = JavaUtilities.SafeCast(preview, EYBCard.class);
                if (card != null)
                {
                    card.renderAsPreview(sb);
                }
                else
                {
                    preview.render(sb);
                }
            }
        }
    }

    protected void RenderHeader(SpriteBatch sb, boolean isCardPopup)
    {
        String text = GetHeaderText();
        if (text == null || this.isFlipped || this.isLocked || transparency <= 0)
        {
            return;
        }

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

        Color color = GetHeaderColor();
        color.a = transparency;
        FontHelper.renderRotatedText(sb, font, text,
                xPos, yPos, 0.0F, offsetY,
                this.angle, true, color);

        fontData.setScale(originalScale);
    }

    protected void UpdateCardText()
    {
        if (cardText.canUpdate)
        {
            cardText.Update(false);
        }
    }

    protected void upgradeSecondaryValue(int amount)
    {
        this.baseSecondaryValue += amount;
        this.secondaryValue = this.baseSecondaryValue;
        this.upgradedSecondaryValue = true;
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

    public boolean IsAoE()
    {
        return isMultiDamage;
    }

    public AbstractAttribute GetDamageInfo()
    {
        if (baseDamage >= 0)
        {
            return DamageAttribute.Instance.SetCard(this);
        }
        else
        {
            return null;
        }
    }

    public AbstractAttribute GetBlockInfo()
    {
        if (baseBlock >= 0)
        {
            return BlockAttribute.Instance.SetCard(this);
        }
        else
        {
            return null;
        }
    }

    public AbstractAttribute GetSpecialInfo()
    {
        return null;
    }

    public void SetAttackType(AttackType attackType)
    {
        this.attackType = attackType;
    }

    public void SetMultiDamage(boolean value)
    {
        this.isMultiDamage = value;
    }

    public void SetRetain(boolean value)
    {
        this.retain = value;
    }

    public void SetInnate(boolean value)
    {
        this.isInnate = value;
    }

    public void SetExhaust(boolean value)
    {
        this.exhaust = value;
    }

    public void SetEthereal(boolean value)
    {
        this.isEthereal = value;
    }

    public void SetEvokeOrbCount(int count)
    {
        this.showEvokeValue = count > 0;
        this.showEvokeOrbCount = count;
    }

    public void SetLoyal(boolean value)
    {
        if (value)
        {
            if (!tags.contains(GR.Enums.CardTags.LOYAL))
            {
                tags.add(GR.Enums.CardTags.LOYAL);
            }
        }
        else
        {
            tags.remove(GR.Enums.CardTags.LOYAL);
        }
    }

    public void SetPiercing(boolean value)
    {
        if (value)
        {
            if (!tags.contains(GR.Enums.CardTags.PIERCING))
            {
                tags.add(GR.Enums.CardTags.PIERCING);
            }
        }
        else
        {
            tags.remove(GR.Enums.CardTags.PIERCING);
        }
    }

    public void SetHealing(boolean value)
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

    public void SetPurge(boolean value)
    {
        if (value)
        {
            if (!tags.contains(GR.Enums.CardTags.PURGE))
            {
                tags.add(GR.Enums.CardTags.PURGE);
            }
        }
        else
        {
            tags.remove(GR.Enums.CardTags.PURGE);
        }
    }

    public void SetUnique(boolean value, boolean multiUpgrade)
    {
        isMultiUpgrade = multiUpgrade;

        if (value)
        {
            if (!tags.contains(GR.Enums.CardTags.UNIQUE))
            {
                tags.add(GR.Enums.CardTags.UNIQUE);
                EYBCardTooltip unique = GR.GetTooltip("~unique");
                AddTooltip(new TooltipInfo(unique.title, unique.description));
            }
        }
        else
        {
            tags.remove(GR.Enums.CardTags.UNIQUE);
        }
    }

    protected boolean TryUpgrade()
    {
        return TryUpgrade(true);
    }

    protected boolean TryUpgrade(boolean updateDescription)
    {
        if (this.canUpgrade())
        {
            this.timesUpgraded += 1;
            this.upgraded = true;

            if (isMultiUpgrade)
            {
                this.name = cardData.strings.NAME + "+" + this.timesUpgraded;
            }
            else
            {
                this.name = cardData.strings.NAME + "+";
            }

            initializeTitle();

            if (updateDescription)
            {
                cardText.ForceRefresh();
            }

            return true;
        }

        return false;
    }

    @Override
    public boolean canUpgrade()
    {
        return !upgraded || isMultiUpgrade;
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            if (upgrade_damage != 0)
            {
                if (baseDamage < 0)
                {
                    baseDamage = 0;
                }

                upgradeDamage(upgrade_damage);
            }

            if (upgrade_block != 0)
            {
                if (baseBlock < 0)
                {
                    baseBlock = 0;
                }

                upgradeBlock(upgrade_block);
            }

            if (upgrade_secondaryValue != 0)
            {
                upgradeSecondaryValue(upgrade_secondaryValue);
            }

            if (upgrade_magicNumber != 0)
            {
                upgradeMagicNumber(upgrade_magicNumber);
            }

            if (upgrade_cost != 0)
            {
                int previousCost = cost;
                int previousCostForTurn = costForTurn;

                this.cost = Math.max(0, previousCost + upgrade_cost);
                this.costForTurn = Math.max(0, previousCostForTurn + upgrade_cost);
                this.upgradedCost = true;
            }

            OnUpgrade();
        }
    }

    @Override
    public void displayUpgrades()
    {
        super.displayUpgrades();

        if (this.upgradedSecondaryValue)
        {
            this.secondaryValue = this.baseSecondaryValue;
            this.isSecondaryValueModified = true;
        }
    }

    protected void Initialize(int damage, int block)
    {
        Initialize(damage, block, -1, 0);
    }

    protected void Initialize(int damage, int block, int magicNumber)
    {
        Initialize(damage, block, magicNumber, 0);
    }

    protected void Initialize(int damage, int block, int magicNumber, int secondaryValue)
    {
        this.baseDamage = this.damage = damage > 0 ? damage : -1;
        this.baseBlock = this.block = block > 0 ? block : -1;
        this.baseMagicNumber = this.magicNumber = magicNumber;
        this.baseSecondaryValue = this.secondaryValue = secondaryValue;
    }

    protected void SetUpgrade(int damage, int block)
    {
        SetUpgrade(damage, block, 0, 0);
    }

    protected void SetUpgrade(int damage, int block, int magicNumber)
    {
        SetUpgrade(damage, block, magicNumber, 0);
    }

    protected void SetUpgrade(int damage, int block, int magicNumber, int secondaryValue)
    {
        this.upgrade_damage = damage;
        this.upgrade_block = block;
        this.upgrade_magicNumber = magicNumber;
        this.upgrade_secondaryValue = secondaryValue;
    }

    protected void SetCostUpgrade(int value)
    {
        this.upgrade_cost = value;
    }

    protected void OnUpgrade()
    {

    }
}
