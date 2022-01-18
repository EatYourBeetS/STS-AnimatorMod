package pinacolada.cards.base;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.evacipated.cardcrawl.modthespire.lib.SpireOverride;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.interfaces.delegates.ActionT1;
import eatyourbeets.interfaces.delegates.ActionT3;
import eatyourbeets.interfaces.delegates.FuncT1;
import eatyourbeets.utilities.ColoredString;
import pinacolada.cards.base.attributes.AbstractAttribute;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLRenderHelpers;

public class PCLCard_Dynamic extends PCLCard
{
    protected final PCLCardBuilder builder;

    public boolean canUpgrade;
    public boolean canSelect;
    public int attributeMultiplier;
    public final ActionT1<PCLCard> constructor;
    public final ActionT1<PCLCard> onUpgrade;
    public final ActionT3<PCLCard, AbstractPlayer, AbstractMonster> onUse;
    public final FuncT1<AbstractAttribute, PCLCard> getSpecialInfo;
    public final FuncT1<AbstractAttribute, PCLCard> getDamageInfo;
    public final FuncT1<AbstractAttribute, PCLCard> getBlockInfo;

    public PCLCard_Dynamic(PCLCardBuilder builder)
    {
        super(new PCLCardData(PCLCard_Dynamic.class, builder.id, builder.cardStrings), builder.id, builder.imagePath,
            builder.cost, builder.cardType, builder.cardColor, builder.cardRarity, builder.cardTarget);

        Initialize(builder.damage, builder.block, builder.magicNumber, builder.secondaryValue);
        SetUpgrade(builder.damageUpgrade, builder.blockUpgrade, builder.magicNumberUpgrade, builder.secondaryValueUpgrade);
        SetCostUpgrade(builder.costUpgrade);
        SetHitCount(builder.hitCount,builder.hitCountUpgrade);

        this.attributeMultiplier = builder.attributeMultiplier;
        this.affinities.Initialize(builder.affinities);
        this.attackTarget = builder.attackTarget;
        this.attackType = builder.attackType;
        this.builder = builder;
        this.onUse = builder.onUse;
        this.onUpgrade = builder.onUpgrade;
        this.constructor = builder.constructor;
        this.isMultiDamage = builder.isMultiDamage;
        this.cropPortrait = false;
        this.canUpgrade = builder.canUpgrade;
        this.showTypeText = builder.showTypeText;

        if (builder.portraitImage != null)
        {
            this.portraitImg = builder.portraitImage;
        }
        if (builder.portraitForeground != null)
        {
            this.portraitForeground = builder.portraitForeground;
        }
        if (builder.fakePortrait != null) {
            this.fakePortrait = builder.fakePortrait;
        }

        this.getSpecialInfo = builder.getSpecialInfo;
        this.getDamageInfo = builder.getDamageInfo;
        this.getBlockInfo = builder.getBlockInfo;

        if (constructor != null)
        {
            constructor.Invoke(this);
        }

        for (CardTags tag : builder.tags) {
            PCLGameUtilities.ModifyCardTag(this, tag, true);
        }

        SetSeries(builder.series);
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        if (getDamageInfo != null)
        {
            return getDamageInfo.Invoke(this);
        }

        AbstractAttribute info = super.GetDamageInfo();
        if (info != null && attributeMultiplier > 1)
        {
            info.AddMultiplier(attributeMultiplier);
        }

        return info;
    }

    @Override
    public AbstractAttribute GetBlockInfo()
    {
        if (getBlockInfo != null)
        {
            return getBlockInfo.Invoke(this);
        }

        AbstractAttribute info = super.GetBlockInfo();
        if (info != null && attributeMultiplier > 1)
        {
            info.AddMultiplier(attributeMultiplier);
        }

        return info;
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        if (getSpecialInfo != null)
        {
            return getSpecialInfo.Invoke(this);
        }

        return super.GetSpecialInfo();
    }

    @Override
    protected void OnUpgrade()
    {
        if (onUpgrade != null)
        {
            onUpgrade.Invoke(this);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (onUse != null)
        {
            onUse.Invoke(this, p, m);
        }
    }

    @Override
    public boolean canUpgrade()
    {
        return canUpgrade && super.canUpgrade();
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new PCLCard_Dynamic(builder);
    }

    @Override
    protected void renderCardBg(SpriteBatch sb, float x, float y)
    {
        if (type != CardType.CURSE) {
            super.renderCardBg(sb, x, y);
            return;
        }
        Texture card = GetCardBackground();
        float popUpMultiplier = isPopup ? 0.5f : 1f;
        PCLRenderHelpers.DrawGrayscale(sb, () ->
            PCLRenderHelpers.DrawOnCardAuto(sb, this, card, new Vector2(0,0), card.getWidth(), card.getHeight(), new Color(0.2f, 0.2f, 0.2f, transparency), transparency, popUpMultiplier));
    }

    @SpireOverride
    protected void renderEnergy(SpriteBatch sb)
    {
        if (type != CardType.CURSE) {
            super.renderEnergy(sb);
            return;
        }
        if (this.cost > -2 && !_darken.Get(this) && !this.isLocked && this.isSeen)
        {
            this.RenderAtlas(sb, _renderColor.Get(this), ImageMaster.CARD_COLORLESS_ORB, this.current_x, this.current_y);

            ColoredString costString = GetCostString();
            if (costString != null)
            {
                BitmapFont font = PCLRenderHelpers.GetEnergyFont(this);
                pinacolada.utilities.PCLRenderHelpers.WriteOnCard(sb, this, font, costString.text, -132f, 192f, costString.color);
                PCLRenderHelpers.ResetFont(font);
            }
        }
    }
}