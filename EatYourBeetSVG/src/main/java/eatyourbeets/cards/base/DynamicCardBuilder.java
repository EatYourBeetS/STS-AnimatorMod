package eatyourbeets.cards.base;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.interfaces.delegates.ActionT1;
import eatyourbeets.interfaces.delegates.ActionT3;
import eatyourbeets.interfaces.delegates.FuncT1;
import eatyourbeets.interfaces.delegates.FuncT2;
import eatyourbeets.utilities.AdvancedTexture;

import java.util.ArrayList;

public abstract class DynamicCardBuilder
{
    public int secondaryValue;
    public int costUpgrade;
    public int damageUpgrade;
    public int blockUpgrade;
    public int magicNumberUpgrade;
    public int secondaryValueUpgrade;

    public ActionT1<EYBCard> constructor;
    public ActionT1<EYBCard> onUpgrade;
    public ActionT3<EYBCard, AbstractPlayer, AbstractMonster> onUse;
    public FuncT2<Boolean, EYBCard, AbstractMonster> canUse;
    public FuncT1<AbstractAttribute, EYBCard> getSpecialInfo;
    public FuncT1<AbstractAttribute, EYBCard> getDamageInfo;
    public FuncT1<AbstractAttribute, EYBCard> getBlockInfo;
    public EYBAttackType attackType = EYBAttackType.Normal;
    public EYBCardTarget attackTarget = EYBCardTarget.Normal;
    public int attributeMultiplier = 1;

    public String id;
    public String imagePath;
    public AdvancedTexture portraitImage;
    public AdvancedTexture portraitForeground;

    public int cost = -2;
    public int damage;
    public int block;
    public int magicNumber;
    public boolean isMultiDamage;
    public boolean canUpgrade = true;

    public ArrayList<AbstractCard.CardTags> tags = new ArrayList<>();
    public AbstractCard.CardTarget cardTarget = AbstractCard.CardTarget.NONE;
    public AbstractCard.CardRarity cardRarity = AbstractCard.CardRarity.BASIC;
    public AbstractCard.CardColor cardColor = AbstractCard.CardColor.COLORLESS;
    public AbstractCard.CardType cardType = AbstractCard.CardType.SKILL;
    public EYBCardAffinities affinities;
    public CardStrings cardStrings;

    public DynamicCardBuilder(String id)
    {
        this(id, null, null, false);
    }

    public DynamicCardBuilder(EYBCard card, boolean copyNumbers)
    {
        this(card.cardID, card, card.rawDescription, copyNumbers);
    }

    public DynamicCardBuilder(String id, EYBCard card, String text, boolean copyNumbers)
    {
        this.id = id;
        this.affinities = new EYBCardAffinities(null);

        if (copyNumbers && card != null)
        {
            SetNumbers(card.damage, card.block, card.magicNumber, card.secondaryValue);
            SetUpgrades(card.upgrade_damage, card.upgrade_block, card.upgrade_magicNumber, card.upgrade_secondaryValue);
            SetCost(card.cost, card.upgrade_cost);
            affinities.Initialize(card.affinities);
        }
        else
        {
            SetCost(-2, 0);
        }

        if (card != null)
        {
            SetImage(card.portraitImg, card.portraitForeground);
            SetProperties(card.type, card.color, card.rarity, AbstractCard.CardTarget.NONE);
            SetText(card.name, text, text);
        }
    }

    public abstract EYBCard Build();

    public DynamicCardBuilder SetProperties(AbstractCard.CardType type, AbstractCard.CardColor color, AbstractCard.CardRarity rarity, AbstractCard.CardTarget target)
    {
        this.cardType = type;
        this.cardColor = color;
        this.cardRarity = rarity;
        this.cardTarget = target;

        return this;
    }

    public DynamicCardBuilder SetID(String id)
    {
        this.id = id;

        return this;
    }


    public DynamicCardBuilder SetCost(int baseCost, int costUpgrade)
    {
        this.cost = baseCost;
        this.costUpgrade = costUpgrade;

        return this;
    }

    public DynamicCardBuilder SetNumbers(AnimatorCard source)
    {
        this.damage = source.baseDamage;
        this.block = source.baseBlock;
        this.magicNumber = source.baseMagicNumber;
        this.secondaryValue = source.baseSecondaryValue;
        this.damageUpgrade = source.upgrade_damage;
        this.blockUpgrade = source.upgrade_block;
        this.magicNumberUpgrade = source.upgrade_magicNumber;
        this.secondaryValueUpgrade = source.upgrade_secondaryValue;

        return this;
    }

    public DynamicCardBuilder SetNumbers(int damage, int block, int magicNumber, int secondaryValue)
    {
        this.damage = damage;
        this.block = block;
        this.magicNumber = magicNumber;
        this.secondaryValue = secondaryValue;

        return this;
    }

    public DynamicCardBuilder SetUpgrades(int damage, int block, int magicNumber, int secondaryValue)
    {
        this.damageUpgrade = damage;
        this.blockUpgrade = block;
        this.magicNumberUpgrade = magicNumber;
        this.secondaryValueUpgrade = secondaryValue;

        return this;
    }

    public DynamicCardBuilder SetTags(AbstractCard.CardTags... tags)
    {
        for (AbstractCard.CardTags t : tags)
        {
            if (!this.tags.contains(t))
            {
                this.tags.add(t);
            }
        }

        return this;
    }

    public DynamicCardBuilder SetAttackType(EYBAttackType attackType, EYBCardTarget attackTarget)
    {
        this.attackType = attackType;
        this.attackTarget = attackTarget;
        this.isMultiDamage = (attackTarget == EYBCardTarget.ALL);

        return this;
    }

    public DynamicCardBuilder SetDamageInfo(FuncT1<AbstractAttribute, EYBCard> getDamageInfo)
    {
        this.getDamageInfo = getDamageInfo;

        return this;
    }

    public DynamicCardBuilder SetBlockInfo(FuncT1<AbstractAttribute, EYBCard> getBlockInfo)
    {
        this.getBlockInfo = getBlockInfo;

        return this;
    }

    public DynamicCardBuilder SetSpecialInfo(FuncT1<AbstractAttribute, EYBCard> getSpecialInfo)
    {
        this.getSpecialInfo = getSpecialInfo;

        return this;
    }

    public DynamicCardBuilder SetAttackType(EYBAttackType attackType, EYBCardTarget attackTarget, int multiplier)
    {
        this.attackType = attackType;
        this.attackTarget = attackTarget;
        this.attributeMultiplier = multiplier;
        this.isMultiDamage = (attackTarget == EYBCardTarget.ALL);

        return this;
    }

    public DynamicCardBuilder SetImage(AdvancedTexture portraitImage, AdvancedTexture portraitForeground)
    {
        this.portraitImage = portraitImage;
        this.portraitForeground = portraitForeground;

        return this;
    }

    public DynamicCardBuilder SetImagePath(String imagePath)
    {
        this.imagePath = imagePath;

        return this;
    }

    public DynamicCardBuilder SetText(CardStrings cardStrings)
    {
        return SetText(cardStrings.NAME, cardStrings.DESCRIPTION, cardStrings.UPGRADE_DESCRIPTION);
    }

    public DynamicCardBuilder SetText(String name, String description, String upgradeDescription)
    {
        return SetText(name, description, upgradeDescription != null ? upgradeDescription : description, new String[0]);
    }

    public DynamicCardBuilder SetText(String name, String description, String upgradeDescription, String[] extendedDescription)
    {
        this.cardStrings = new CardStrings();
        this.cardStrings.NAME = name;
        this.cardStrings.DESCRIPTION = description;
        this.cardStrings.UPGRADE_DESCRIPTION = upgradeDescription;
        this.cardStrings.EXTENDED_DESCRIPTION = extendedDescription;

        return this;
    }

    public DynamicCardBuilder SetConstructor(ActionT1<EYBCard> constructor)
    {
        this.constructor = constructor;

        return this;
    }

    public DynamicCardBuilder SetOnUpgrade(ActionT1<EYBCard> onUpgrade)
    {
        this.onUpgrade = onUpgrade;

        return this;
    }

    public DynamicCardBuilder SetOnUse(ActionT3<EYBCard, AbstractPlayer, AbstractMonster> onUseAction)
    {
        this.onUse = onUseAction;

        return this;
    }

    public DynamicCardBuilder CanUse(FuncT2<Boolean, EYBCard, AbstractMonster> canUse)
    {
        this.canUse = canUse;

        return this;
    }

    public DynamicCardBuilder CanUpgrade(boolean canUpgrade)
    {
        this.canUpgrade = canUpgrade;

        return this;
    }
}