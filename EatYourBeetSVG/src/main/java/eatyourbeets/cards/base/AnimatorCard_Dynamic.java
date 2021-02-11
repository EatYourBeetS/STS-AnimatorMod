package eatyourbeets.cards.base;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.interfaces.delegates.ActionT1;
import eatyourbeets.interfaces.delegates.ActionT3;
import eatyourbeets.interfaces.delegates.FuncT0;

public class AnimatorCard_Dynamic extends AnimatorCard
{
    protected final AnimatorCardBuilder builder;

    public boolean canSelect;
    public int attributeMultiplier;
    public final ActionT1<AnimatorCard> constructor;
    public final ActionT1<AnimatorCard> onUpgrade;
    public final ActionT3<AnimatorCard, AbstractPlayer, AbstractMonster> onUse;
    public final FuncT0<AbstractAttribute> getSpecialInfo;
    public final FuncT0<AbstractAttribute> getDamageInfo;
    public final FuncT0<AbstractAttribute> getBlockInfo;

    public AnimatorCard_Dynamic(AnimatorCardBuilder builder)
    {
        super(new EYBCardData(AnimatorCard_Dynamic.class, builder.id, builder.cardStrings), builder.id, builder.imagePath,
            builder.cost, builder.cardType, builder.cardColor, builder.cardRarity, builder.cardTarget);

        Initialize(builder.damage, builder.block, builder.magicNumber, builder.secondaryValue);
        SetUpgrade(builder.damageUpgrade, builder.blockUpgrade, builder.magicNumberUpgrade, builder.secondaryValueUpgrade);
        SetCostUpgrade(builder.costUpgrade);

        this.attributeMultiplier = builder.attributeMultiplier;
        this.intellectScaling = builder.intellectScaling;
        this.agilityScaling = builder.agilityScaling;
        this.forceScaling = builder.forceScaling;
        this.attackTarget = builder.attackTarget;
        this.attackType = builder.attackType;
        this.builder = builder;
        this.onUse = builder.onUse;
        this.onUpgrade = builder.onUpgrade;
        this.constructor = builder.constructor;
        this.isMultiDamage = builder.isMultiDamage;
        this.tags.addAll(builder.tags);
        this.cropPortrait = false;

        this.getSpecialInfo = builder.getSpecialInfo;
        this.getDamageInfo = builder.getDamageInfo;
        this.getBlockInfo = builder.getBlockInfo;

        if (constructor != null)
        {
            constructor.Invoke(this);
        }

        SetSynergy(builder.synergy);
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        if (getDamageInfo != null)
        {
            return getDamageInfo.Invoke();
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
            return getBlockInfo.Invoke();
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
            return getSpecialInfo.Invoke();
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
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        if (onUse != null)
        {
            onUse.Invoke(this, p, m);
        }
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new AnimatorCard_Dynamic(builder);
    }
}