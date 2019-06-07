package eatyourbeets.cards;

import basemod.abstracts.CustomCard;
import basemod.helpers.TooltipInfo;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireOverride;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.ExceptionHandler;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.GetAllInBattleInstances;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import eatyourbeets.AnimatorResources;
import eatyourbeets.Utilities;
import eatyourbeets.powers.PlayerStatistics;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import patches.AbstractEnums;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

public abstract class AnimatorCard extends CustomCard
{
    protected static final Logger logger = LogManager.getLogger(AnimatorCard.class.getName());
    //private static final Color SYNERGY_COLOR = new Color(0.565f, 0.933f, 0.565f, 1);

    public static int SynergyReserves;
    public static int SynergiesActivatedThisTurn;

    private static AnimatorCard previousCard = null;
    private static AnimatorCard lastCardPlayed = null;

    private String upgradedDescription = null;
    private final List<TooltipInfo> customTooltips = new ArrayList<>();
    private Synergy synergy;
    private boolean lastHovered = false;

    protected final CardStrings cardStrings;
    protected final Color RENDER_COLOR = Color.WHITE.cpy();

    public boolean anySynergy;
    public boolean isSecondaryValueModified = false;
    public boolean upgradedSecondaryValue = false;
    public int baseSecondaryValue = 0;
    public int secondaryValue = 0;

    public static String CreateFullID(String cardID)
    {
        return "animator_" + cardID;
    }

    public static void SetLastCardPlayed(AbstractCard card)
    {
        if (SynergyReserves > 0)
        {
            SynergyReserves -= 1;
        }

        if (card == null)
        {
            previousCard = null;
            lastCardPlayed = null;
        }
        else
        {
            previousCard = lastCardPlayed;
            lastCardPlayed = Utilities.SafeCast(card, AnimatorCard.class);
        }
    }

    public boolean HasActiveSynergy()
    {
        if (SynergyReserves > 0)
        {
            return true;
        }
        else if (this == lastCardPlayed)
        {
            return previousCard != null && previousCard.HasSynergy(this);
        }
        else
        {
            return lastCardPlayed != null && lastCardPlayed.HasSynergy(this);
        }
    }

    public boolean HasSynergy(AbstractCard other)
    {
        AnimatorCard card = Utilities.SafeCast(other, AnimatorCard.class);
        if (card != null && card.synergy != null)
        {
            return this.synergy != null && (HasExactSynergy(card.synergy) || this.anySynergy || card.anySynergy);
        }

        return false;
    }

    public boolean HasExactSynergy(Synergy synergy)
    {
        return Objects.equals(this.synergy, synergy);
    }

    public Synergy GetSynergy()
    {
        return synergy;
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
    public boolean isHoveredInHand(float scale)
    {
        boolean hovered = super.isHoveredInHand(scale);

        if (hovered && !lastHovered)
        {
            logger.info("Hovered: " + name);
            ArrayList<AbstractCard> hand = AbstractDungeon.player.hand.group;
            for (AbstractCard c : hand)
            {
                if (c != this)
                {
                    AnimatorCard card = Utilities.SafeCast(c, AnimatorCard.class);
                    if ((card != null && card.HasSynergy(this)))
                    {
                        c.targetDrawScale = 0.9f;
                    }
                }
            }
        }

        lastHovered = hovered;

        return hovered;
    }

    @Override
    public void triggerOnOtherCardPlayed(AbstractCard c)
    {
        super.triggerOnOtherCardPlayed(c);

        if (HasSynergy(c))
        {
            this.targetDrawScale = 0.9f;
        }
    }

    @Override
    public void render(SpriteBatch sb)
    {
        super.render(sb);
        RenderSynergy(sb);
    }

    @Override
    public void renderInLibrary(SpriteBatch sb)
    {
        super.renderInLibrary(sb);
        RenderSynergy(sb);
    }

    private void RenderSynergy(SpriteBatch sb)
    {
        AbstractRoom room = PlayerStatistics.GetCurrentRoom();
        if (this.synergy != null)
        {
            if (!this.isFlipped)//room == null || !(room.event instanceof GremlinMatchGame))
            {
                float originalScale = FontHelper.cardTitleFont_small_N.getData().scaleX;

                Color textColor;
                if (HasActiveSynergy())
                {
                    FontHelper.cardTitleFont_small_N.getData().setScale(this.drawScale * 0.85f);
                    textColor = Color.YELLOW.cpy();
                }
                else
                {
                    FontHelper.cardTitleFont_small_N.getData().setScale(this.drawScale * 0.8f);
                    textColor = Settings.CREAM_COLOR.cpy();
                }

                FontHelper.renderRotatedText(sb, FontHelper.cardTitleFont_small_N, this.synergy.NAME,
                        this.current_x, this.current_y, 0.0F, 400.0F * Settings.scale * this.drawScale / 2.0F,
                        this.angle, true, textColor);

                FontHelper.cardTitleFont_small_N.getData().setScale(originalScale);
            }
        }
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
        AnimatorCard copy = Utilities.SafeCast(result, AnimatorCard.class);
        if (copy != null)
        {
            copy.secondaryValue = this.secondaryValue;
            copy.baseSecondaryValue = this.baseSecondaryValue;
            copy.isSecondaryValueModified = this.isSecondaryValueModified;
        }

        return result;
    }

    public void Initialize(int baseDamage, int baseBlock)
    {
        Initialize(baseDamage, baseBlock, -1);
    }

    public void Initialize(int baseDamage, int baseBlock, int baseMagicNumber)
    {
        this.baseDamage = baseDamage;
        this.baseBlock = baseBlock;
        this.baseMagicNumber = baseMagicNumber;
        this.magicNumber = baseMagicNumber;
    }

    public void Initialize(int baseDamage, int baseBlock, int baseMagicNumber, int baseSecondaryValue)
    {
        Initialize(baseDamage, baseBlock, baseMagicNumber);

        this.baseSecondaryValue = this.secondaryValue = baseSecondaryValue;
    }

    public Boolean TryUpgrade()
    {
        if (!this.upgraded)
        {
            upgradeName();

            if (StringUtils.isNotEmpty(upgradedDescription))
            {
                this.rawDescription = upgradedDescription;
                this.initializeDescription();
            }

            return true;
        }

        return false;
    }

    protected void upgradeSecondaryValue(int amount)
    {
        this.baseSecondaryValue += amount;
        this.secondaryValue = this.baseSecondaryValue;
        this.upgradedSecondaryValue = true;
    }

    public void SetSynergy(Synergy synergy)
    {
        SetSynergy(synergy, false);
    }

    public void SetSynergy(Synergy synergy, boolean shapeshifter)
    {
        this.synergy = synergy;
        this.anySynergy = shapeshifter;
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

    protected void AddExtendedDescription()
    {
        AddExtendedDescription(0, 1);
    }

    protected void AddTooltip(TooltipInfo tooltip)
    {
        customTooltips.add(tooltip);
    }

    protected AnimatorCard(String id, int cost, CardType type, CardRarity rarity, CardTarget target)
    {
        this(AnimatorResources.GetCardStrings(id), id, AnimatorResources.GetCardImage(id), cost, type, AbstractEnums.Cards.THE_ANIMATOR, rarity, target);
    }

    protected AnimatorCard(String id, int cost, CardType type, CardColor color, CardRarity rarity, CardTarget target)
    {
        this(AnimatorResources.GetCardStrings(id), id, AnimatorResources.GetCardImage(id), cost, type, color, rarity, target);
    }

    protected AnimatorCard(CardStrings strings, String id, String imagePath, int cost, CardType type, CardColor color, CardRarity rarity, CardTarget target)
    {
        super(id, strings.NAME, imagePath, cost, strings.DESCRIPTION, type, color, rarity, target);

        if (this instanceof AnimatorCard_UltraRare)
        {
            setBannerTexture("images\\cardui\\512\\banner_special2.png", "images\\cardui\\1024\\banner_special2.png");
        }
        else if (rarity == CardRarity.SPECIAL)
        {
            setBannerTexture("images\\cardui\\512\\banner_special.png", "images\\cardui\\1024\\banner_special.png");
        }

        cardStrings = strings;
        if (StringUtils.isNotEmpty(strings.UPGRADE_DESCRIPTION))
        {
            this.upgradedDescription = strings.UPGRADE_DESCRIPTION;
        }
    }

    protected void renderHelper(SpriteBatch sb, Color color, Texture img, float drawX, float drawY)
    {
        sb.setColor(color);

        try
        {
            sb.draw(img, drawX, drawY, 256.0F, 256.0F, 512.0F, 512.0F, this.drawScale * Settings.scale, this.drawScale * Settings.scale, this.angle, 0, 0, 512, 512, false, false);
        }
        catch (Exception var7)
        {
            ExceptionHandler.handleException(var7, logger);
        }

    }

    @SpireOverride
    protected void renderAttackPortrait(SpriteBatch sb, float x, float y)
    {
        switch (this.rarity)
        {
            case BASIC:
            case CURSE:
            case COMMON:
                this.renderHelper(sb, RENDER_COLOR, ImageMaster.CARD_FRAME_ATTACK_COMMON, x, y);
                return;

            case SPECIAL:
                this.renderHelper(sb, RENDER_COLOR, AnimatorResources.CARD_FRAME_ATTACK_SPECIAL, x, y);
                return;

            case UNCOMMON:
                this.renderHelper(sb, RENDER_COLOR, ImageMaster.CARD_FRAME_ATTACK_UNCOMMON, x, y);
                return;

            case RARE:
                this.renderHelper(sb, RENDER_COLOR, ImageMaster.CARD_FRAME_ATTACK_RARE, x, y);
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
                this.renderHelper(sb, RENDER_COLOR, ImageMaster.CARD_FRAME_SKILL_COMMON, x, y);
                return;

            case SPECIAL:
                this.renderHelper(sb, RENDER_COLOR, AnimatorResources.CARD_FRAME_SKILL_SPECIAL, x, y);
                return;

            case UNCOMMON:
                this.renderHelper(sb, RENDER_COLOR, ImageMaster.CARD_FRAME_SKILL_UNCOMMON, x, y);
                return;

            case RARE:
                this.renderHelper(sb, RENDER_COLOR, ImageMaster.CARD_FRAME_SKILL_RARE, x, y);
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
                this.renderHelper(sb, RENDER_COLOR, ImageMaster.CARD_FRAME_POWER_COMMON, x, y);
                break;

            case SPECIAL:
                this.renderHelper(sb, RENDER_COLOR, AnimatorResources.CARD_FRAME_POWER_SPECIAL, x, y);
                return;

            case UNCOMMON:
                this.renderHelper(sb, RENDER_COLOR, ImageMaster.CARD_FRAME_POWER_UNCOMMON, x, y);
                break;

            case RARE:
                this.renderHelper(sb, RENDER_COLOR, ImageMaster.CARD_FRAME_POWER_RARE, x, y);
        }
    }

    public HashSet<AbstractCard> GetAllInstances()
    {
        HashSet<AbstractCard> cards = GetAllInBattleInstances.get(uuid);

        AbstractCard masterDeckInstance = GetMasterDeckInstance();
        if (masterDeckInstance != null)
        {
            cards.add(masterDeckInstance);
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
}