package eatyourbeets.cards.base;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.interfaces.delegates.ActionT1;
import eatyourbeets.interfaces.delegates.ActionT3;
import eatyourbeets.interfaces.delegates.FuncT1;

public class AnimatorCard_Dynamic extends AnimatorCard
{
    protected final AnimatorCardBuilder builder;

    public boolean canUpgrade;
    public boolean canSelect;
    public int attributeMultiplier;
    public final ActionT1<AnimatorCard> constructor;
    public final ActionT1<AnimatorCard> onUpgrade;
    public final ActionT3<AnimatorCard, AbstractPlayer, AbstractMonster> onUse;
    public final FuncT1<AbstractAttribute, AnimatorCard> getSpecialInfo;
    public final FuncT1<AbstractAttribute, AnimatorCard> getDamageInfo;
    public final FuncT1<AbstractAttribute, AnimatorCard> getBlockInfo;

    public AnimatorCard_Dynamic(AnimatorCardBuilder builder)
    {
        super(new EYBCardData(AnimatorCard_Dynamic.class, builder.id, builder.cardStrings), builder.id, builder.imagePath,
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
        this.tags.addAll(builder.tags);
        this.cropPortrait = false;
        this.canUpgrade = builder.canUpgrade;

        if (builder.portraitImage != null)
        {
            this.portraitImg = builder.portraitImage;
        }
        if (builder.portraitForeground != null)
        {
            this.portraitForeground = builder.portraitForeground;
        }

        this.getSpecialInfo = builder.getSpecialInfo;
        this.getDamageInfo = builder.getDamageInfo;
        this.getBlockInfo = builder.getBlockInfo;

        if (constructor != null)
        {
            constructor.Invoke(this);
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
        return new AnimatorCard_Dynamic(builder);
    }
}