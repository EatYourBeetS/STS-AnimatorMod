package eatyourbeets.cards;

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
import eatyourbeets.resources.Resources_Animator;
import eatyourbeets.resources.Resources_Animator_Images;
import eatyourbeets.utilities.Utilities;
import eatyourbeets.powers.PlayerStatistics;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import patches.AbstractEnums;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

public abstract class AnimatorCard extends EYBCard
{
    protected static final Logger logger = LogManager.getLogger(AnimatorCard.class.getName());
    //private static final Color SYNERGY_COLOR = new Color(0.565f, 0.933f, 0.565f, 1);

    public static int SynergyReserves;
    public static int SynergiesActivatedThisTurn;

    private static AnimatorCard previousCard = null;
    private static AnimatorCard lastCardPlayed = null;

    private final List<TooltipInfo> customTooltips = new ArrayList<>();
    private Synergy synergy;
    private boolean lastHovered = false;

    protected final Color RENDER_COLOR = Color.WHITE.cpy();

    public boolean anySynergy;

    public static String CreateFullID(String cardID)
    {
        return "animator:" + cardID;
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
            if (!this.isFlipped)
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

    public void SetSynergy(Synergy synergy)
    {
        SetSynergy(synergy, false);
    }

    public void SetSynergy(Synergy synergy, boolean shapeshifter)
    {
        this.synergy = synergy;
        this.anySynergy = shapeshifter;
    }

    protected AnimatorCard(String id, int cost, CardType type, CardRarity rarity, CardTarget target)
    {
        this(Resources_Animator.GetCardStrings(id), id, Resources_Animator.GetCardImage(id), cost, type, AbstractEnums.Cards.THE_ANIMATOR, rarity, target);
    }

    protected AnimatorCard(String id, int cost, CardType type, CardColor color, CardRarity rarity, CardTarget target)
    {
        this(Resources_Animator.GetCardStrings(id), id, Resources_Animator.GetCardImage(id), cost, type, color, rarity, target);
    }

    protected AnimatorCard(CardStrings strings, String id, String imagePath, int cost, CardType type, CardColor color, CardRarity rarity, CardTarget target)
    {
        super(strings, id, imagePath, cost, type, color, rarity, target);

        if (this instanceof AnimatorCard_UltraRare)
        {
            setBannerTexture(Resources_Animator_Images.BANNER_SPECIAL2_PNG, Resources_Animator_Images.BANNER_SPECIAL2_P_PNG);
        }
        else if (rarity == CardRarity.SPECIAL)
        {
            setBannerTexture(Resources_Animator_Images.BANNER_SPECIAL_PNG, Resources_Animator_Images.BANNER_SPECIAL_P_PNG);
        }
    }

    protected void renderHelper(SpriteBatch sb, Color color, Texture img, float drawX, float drawY)
    {
        sb.setColor(color);

        try
        {
            sb.draw(img, drawX, drawY, 256.0F, 256.0F, 512.0F, 512.0F,
                    this.drawScale * Settings.scale, this.drawScale * Settings.scale,
                    this.angle, 0, 0, 512, 512, false, false);
        }
        catch (Exception exception)
        {
            ExceptionHandler.handleException(exception, logger);
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
                this.renderHelper(sb, RENDER_COLOR, Resources_Animator_Images.CARD_FRAME_ATTACK_SPECIAL, x, y);
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
                this.renderHelper(sb, RENDER_COLOR, Resources_Animator_Images.CARD_FRAME_SKILL_SPECIAL, x, y);
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
                this.renderHelper(sb, RENDER_COLOR, Resources_Animator_Images.CARD_FRAME_POWER_SPECIAL, x, y);
                return;

            case UNCOMMON:
                this.renderHelper(sb, RENDER_COLOR, ImageMaster.CARD_FRAME_POWER_UNCOMMON, x, y);
                break;

            case RARE:
                this.renderHelper(sb, RENDER_COLOR, ImageMaster.CARD_FRAME_POWER_RARE, x, y);
        }
    }

    public HashSet<AbstractCard> GetAllInBattleInstances()
    {
        HashSet<AbstractCard> cards = GetAllInBattleInstances.get(uuid);

        if (!cards.contains(this))
        {
            cards.add(this);
        }

        return cards;
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