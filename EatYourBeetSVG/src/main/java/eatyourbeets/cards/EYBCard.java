package eatyourbeets.cards;

import basemod.abstracts.CustomCard;
import basemod.helpers.TooltipInfo;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GetAllInBattleInstances;
import com.megacrit.cardcrawl.localization.CardStrings;
import eatyourbeets.utilities.Utilities;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public abstract class EYBCard extends CustomCard
{
    private final List<TooltipInfo> customTooltips = new ArrayList<>();

    protected final CardStrings cardStrings;
    protected final String upgradedDescription;

    public boolean isSecondaryValueModified = false;
    public boolean upgradedSecondaryValue = false;
    public int baseSecondaryValue = 0;
    public int secondaryValue = 0;

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

    public void Initialize(int baseDamage, int baseBlock)
    {
        Initialize(baseDamage, baseBlock, -1);
    }

    public void Initialize(int baseDamage, int baseBlock, int baseMagicNumber)
    {
        this.baseDamage = baseDamage;
        this.baseBlock = baseBlock;
        this.baseMagicNumber = this.magicNumber = baseMagicNumber;
    }

    public void Initialize(int baseDamage, int baseBlock, int baseMagicNumber, int baseSecondaryValue)
    {
        Initialize(baseDamage, baseBlock, baseMagicNumber);

        this.baseSecondaryValue = this.secondaryValue = baseSecondaryValue;
    }

    public Boolean TryUpgrade(boolean updateDescription)
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

    public Boolean TryUpgrade()
    {
        return TryUpgrade(true);
    }

    protected void upgradeSecondaryValue(int amount)
    {
        this.baseSecondaryValue += amount;
        this.secondaryValue = this.baseSecondaryValue;
        this.upgradedSecondaryValue = true;
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

// TODO: Implement this
//
//    private boolean lastHovered = false;
//    private boolean hoveredInHand = false;
//
//    @Override
//    public boolean isHoveredInHand(float scale)
//    {
//        hoveredInHand = super.isHoveredInHand(scale);
//
//        return hoveredInHand;
//    }
//
//
//    @Override
//    public void hover()
//    {
//        super.hover();
//
//        lastHovered = true;
//    }
//
//    @Override
//    public void unhover()
//    {
//        super.unhover();
//
//        lastHovered = false;
//    }
//
//    @Override
//    public void update()
//    {
//        super.update();
//
//        if (lastHovered && !Settings.hideCards && !hoveredInHand)
//        {
//            AbstractCard preview = GetCardPreview();
//            if (preview != null)
//            {
//                final float HB_W = 300.0F * Settings.scale;
//                final float HB_H = 420.0F * Settings.scale;
//
//                preview.hb.move(preview.current_x, preview.current_y);
//                preview.hb.resize(HB_W * preview.drawScale, HB_H * preview.drawScale);
//                preview.hb.update();
//
//                if (preview.hb.hovered)
//                {
//                    logger.info("display preview");
//                }
//            }
//        }
//    }
//
//    protected void RenderCardPreview(SpriteBatch sb)
//    {
//        if (lastHovered && !Settings.hideCards && !hoveredInHand)
//        {
//            AbstractCard preview = GetCardPreview();
//            if ((preview != null))
//            {
//                final float CARD_TIP_PAD = 16.0F;
//                float tmpScale = this.drawScale / 1.5F;
//
//                if ((AbstractDungeon.player != null) && (AbstractDungeon.player.isDraggingCard))
//                {
//                    return;
//                }
//
//                preview.drawScale = 0.15f;
//                preview.current_y = this.current_y + (AbstractCard.IMG_HEIGHT / 2.0F) - (Settings.scale * 18);
//                preview.current_x = this.current_x + (AbstractCard.IMG_WIDTH / 2.0F) - (Settings.scale * 18);
//
////                //x = card center + half the card width + half the preview width + Padding * Viewport scale * draw scale
////                if (this.current_x > Settings.WIDTH * 0.75F)
////                {
////                    preview.current_x = this.current_x + (((AbstractCard.IMG_WIDTH / 2.0F)
////                            + ((AbstractCard.IMG_WIDTH / 2.0F) / 1.5F) + (CARD_TIP_PAD)) * this.drawScale);
////                }
////                else
////                {
////                    preview.current_x = this.current_x - (((AbstractCard.IMG_WIDTH / 2.0F)
////                            + ((AbstractCard.IMG_WIDTH / 2.0F) / 1.5F) + (CARD_TIP_PAD)) * this.drawScale);
////                }
////
////                preview.current_y = this.current_y + ((AbstractCard.IMG_HEIGHT / 2.0F)
////                        - (AbstractCard.IMG_HEIGHT / 2.0F / 1.5F)) * this.drawScale;
////
////                preview.drawScale = tmpScale;
//                preview.render(sb);
//            }
//        }
//    }
}
