package eatyourbeets.cards.base;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.interfaces.delegates.ActionT1;
import eatyourbeets.interfaces.delegates.ActionT3;
import eatyourbeets.interfaces.delegates.FuncT1;
import eatyourbeets.interfaces.delegates.FuncT2;
import eatyourbeets.resources.GR;

public class AnimatorClassicCard_Dynamic extends AnimatorClassicCard
{
    protected final AnimatorClassicCardBuilder builder;

    public boolean canUpgrade;
    public boolean canSelect;
    public int attributeMultiplier;
    public final ActionT1<EYBCard> constructor;
    public final ActionT1<EYBCard> onUpgrade;
    public final ActionT3<EYBCard, AbstractPlayer, AbstractMonster> onUse;
    public final FuncT2<Boolean, EYBCard, AbstractMonster> canUse;
    public final FuncT1<AbstractAttribute, EYBCard> getSpecialInfo;
    public final FuncT1<AbstractAttribute, EYBCard> getDamageInfo;
    public final FuncT1<AbstractAttribute, EYBCard> getBlockInfo;

    public AnimatorClassicCard_Dynamic(AnimatorClassicCardBuilder builder)
    {
        super(new EYBCardData(AnimatorClassicCard_Dynamic.class, builder.id, builder.cardStrings, GR.AnimatorClassic), builder.id, builder.imagePath,
            builder.cost, builder.cardType, builder.cardColor, builder.cardRarity, builder.cardTarget);

        Initialize(builder.damage, builder.block, builder.magicNumber, builder.secondaryValue);
        SetUpgrade(builder.damageUpgrade, builder.blockUpgrade, builder.magicNumberUpgrade, builder.secondaryValueUpgrade);
        SetCostUpgrade(builder.costUpgrade);

        this.attributeMultiplier = builder.attributeMultiplier;
        this.affinities.Initialize(builder.affinities);
        this.attackTarget = builder.attackTarget;
        this.attackType = builder.attackType;
        this.builder = builder;
        this.onUse = builder.onUse;
        this.canUse = builder.canUse;
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

        this.series = builder.series;
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
    public boolean canUse(AbstractPlayer p, AbstractMonster m)
    {
        return super.canUse(p, m) && (canUse == null || canUse.Invoke(this, m));
    }

    @Override
    public boolean canUpgrade()
    {
        return canUpgrade && super.canUpgrade();
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new AnimatorClassicCard_Dynamic(builder);
    }
}