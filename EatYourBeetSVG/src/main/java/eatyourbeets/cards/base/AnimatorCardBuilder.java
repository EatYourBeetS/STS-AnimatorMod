package eatyourbeets.cards.base;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.interfaces.delegates.ActionT1;
import eatyourbeets.interfaces.delegates.ActionT3;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.AnimatorResources;

public class AnimatorCardBuilder extends DynamicCardBuilder
{
    public int secondaryValue;
    public int costUpgrade;
    public int damageUpgrade;
    public int blockUpgrade;
    public int magicNumberUpgrade;
    public int secondaryValueUpgrade;

    public ActionT1<AnimatorCard> constructor;
    public ActionT1<AnimatorCard> onUpgrade;
    public ActionT3<AnimatorCard, AbstractPlayer, AbstractMonster> onUse;
    public EYBAttackType attackType = EYBAttackType.Normal;
    public EYBCardTarget attackTarget = EYBCardTarget.Normal;
    public int attributeMultiplier = 1;
    public Synergy synergy;
    public float intellectScaling;
    public float agilityScaling;
    public float forceScaling;

    public AnimatorCardBuilder(String id)
    {
        super(id);

        this.id = id;
    }

    public AnimatorCardBuilder(AnimatorCard card, boolean copyNumbers)
    {
        this(card, card.rawDescription, copyNumbers);
    }

    public AnimatorCardBuilder(AnimatorCard card, String text, boolean copyNumbers)
    {
        this(card.cardID);

        if (copyNumbers)
        {
            SetNumbers(card.damage, card.block, card.magicNumber, card.secondaryValue);
            SetUpgrades(card.upgrade_damage, card.upgrade_block, card.upgrade_magicNumber, card.upgrade_secondaryValue);
            SetScaling(card.intellectScaling, card.agilityScaling, card.forceScaling);
            SetCost(card.cost, card.upgrade_cost);
        }
        else
        {
            SetCost(-2, 0);
        }

        SetImage(card.assetUrl);
        SetProperties(card.type, card.rarity, AbstractCard.CardTarget.NONE);
        SetText(card.name, text, text);
        SetSynergy(card.synergy, false);
    }

    public AnimatorCard_Dynamic Build()
    {
        if (cardStrings == null)
        {
            SetText("", "", "");
        }

        if (imagePath == null)
        {
            SetImage(AnimatorResources.GetCardImage(id));
        }

        return new AnimatorCard_Dynamic(this);
    }

    public AnimatorCardBuilder SetProperties(AbstractCard.CardType type, AbstractCard.CardRarity rarity, AbstractCard.CardTarget target)
    {
        return SetProperties(type, GR.Enums.Cards.THE_ANIMATOR, rarity, target);
    }

    public AnimatorCardBuilder SetProperties(AbstractCard.CardType type, AbstractCard.CardColor color, AbstractCard.CardRarity rarity, AbstractCard.CardTarget target)
    {
        this.cardType = type;
        this.cardColor = color;
        this.cardRarity = rarity;
        this.cardTarget = target;

        return this;
    }

    public AnimatorCardBuilder SetID(String id)
    {
        this.id = id;

        return this;
    }


    public AnimatorCardBuilder SetCost(int baseCost, int costUpgrade)
    {
        this.cost = baseCost;
        this.costUpgrade = costUpgrade;

        return this;
    }

    public AnimatorCardBuilder SetNumbers(AnimatorCard source)
    {
        this.damageUpgrade = this.damage = source.baseDamage;
        this.blockUpgrade = this.block = source.baseBlock;
        this.magicNumberUpgrade = this.magicNumber = source.baseMagicNumber;
        this.secondaryValueUpgrade = this.secondaryValue = source.baseSecondaryValue;

        return this;
    }

    public AnimatorCardBuilder SetNumbers(int damage, int block, int magicNumber, int secondaryValue)
    {
        this.damageUpgrade = this.damage = damage;
        this.blockUpgrade = this.block = block;
        this.magicNumberUpgrade = this.magicNumber = magicNumber;
        this.secondaryValueUpgrade = this.secondaryValue = secondaryValue;

        return this;
    }

    public AnimatorCardBuilder SetUpgrades(int damage, int block, int magicNumber, int secondaryValue)
    {
        this.damageUpgrade = damage;
        this.blockUpgrade = block;
        this.magicNumberUpgrade = magicNumber;
        this.secondaryValueUpgrade = secondaryValue;

        return this;
    }

    public AnimatorCardBuilder SetScaling(float intellect, float agility, float force)
    {
        this.intellectScaling = intellect;
        this.agilityScaling = agility;
        this.forceScaling = force;

        return this;
    }

    public AnimatorCardBuilder SetTags(AbstractCard.CardTags... tags)
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

    public AnimatorCardBuilder SetAttackType(EYBAttackType attackType, EYBCardTarget attackTarget)
    {
        this.attackType = attackType;
        this.attackTarget = attackTarget;
        this.isMultiDamage = (attackTarget == EYBCardTarget.ALL);

        return this;
    }

    public AnimatorCardBuilder SetAttackType(EYBAttackType attackType, EYBCardTarget attackTarget, int multiplier)
    {
        this.attackType = attackType;
        this.attackTarget = attackTarget;
        this.attributeMultiplier = multiplier;
        this.isMultiDamage = (attackTarget == EYBCardTarget.ALL);

        return this;
    }

    public AnimatorCardBuilder SetImage(String imagePath)
    {
        this.imagePath = imagePath;

        return this;
    }

    public AnimatorCardBuilder SetText(CardStrings cardStrings)
    {
        return SetText(cardStrings.NAME, cardStrings.DESCRIPTION, cardStrings.UPGRADE_DESCRIPTION);
    }

    public AnimatorCardBuilder SetText(String name, String description, String upgradeDescription)
    {
        return SetText(name, description, upgradeDescription, new String[0]);
    }

    public AnimatorCardBuilder SetText(String name, String description, String upgradeDescription, String[] extendedDescription)
    {
        this.cardStrings = new CardStrings();
        this.cardStrings.NAME = name;
        this.cardStrings.DESCRIPTION = description;
        this.cardStrings.UPGRADE_DESCRIPTION = upgradeDescription;
        this.cardStrings.EXTENDED_DESCRIPTION = extendedDescription;

        return this;
    }

    public AnimatorCardBuilder SetConstructor(ActionT1<AnimatorCard> constructor)
    {
        this.constructor = constructor;

        return this;
    }

    public AnimatorCardBuilder SetOnUpgrade(ActionT1<AnimatorCard> onUpgrade)
    {
        this.onUpgrade = onUpgrade;

        return this;
    }

    public AnimatorCardBuilder SetOnUse(ActionT3<AnimatorCard, AbstractPlayer, AbstractMonster> onUseAction)
    {
        this.onUse = onUseAction;

        return this;
    }

    public AnimatorCardBuilder SetSynergy(Synergy synergy, boolean isShapeshifter)
    {
        this.synergy = synergy;

        if (isShapeshifter)
        {
            tags.add(AnimatorCard.SHAPESHIFTER);
        }

        return this;
    }
}