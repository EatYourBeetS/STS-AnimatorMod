package eatyourbeets.cards.base;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.resources.AnimatorResources;
import org.apache.logging.log4j.util.TriConsumer;

import java.util.function.Consumer;

public class DynamicCardBuilder
{
    public String id;
    public String imagePath;

    public int cost = -2;
    public int damage;
    public int block;
    public int magicNumber;
    public int secondaryValue;

    public int upgradedCost = -2;
    public int upgradedDamage;
    public int upgradedBlock;
    public int upgradedMagicNumber;
    public int upgradedSecondaryValue;

    public AbstractCard.CardTarget cardTarget = AbstractCard.CardTarget.NONE;
    public AbstractCard.CardRarity cardRarity = AbstractCard.CardRarity.BASIC;
    public AbstractCard.CardColor cardColor = AbstractCard.CardColor.COLORLESS;
    public AbstractCard.CardType cardType = AbstractCard.CardType.SKILL;
    public CardStrings cardStrings;
    public EYBCardBadge[] cardBadges;
    public Consumer<AnimatorCard> onUpgrade;
    public TriConsumer<AnimatorCard, AbstractPlayer, AbstractMonster> onUse;
    public boolean isShapeshifter;
    public Synergy synergy;

    public DynamicCardBuilder(String id)
    {
        this.id = id;
    }

    public DynamicCard Build()
    {
        if (cardBadges == null)
        {
            SetBadges();
        }

        if (cardStrings == null)
        {
            SetText("", "", "");
        }

        if (imagePath == null)
        {
            SetImage(AnimatorResources.GetCardImage(id));
        }

        return new DynamicCard(this);
    }

    public DynamicCardBuilder SetProperties(int cost, AbstractCard.CardType type, AbstractCard.CardColor color, AbstractCard.CardRarity rarity, AbstractCard.CardTarget target)
    {
        this.upgradedCost = this.cost = cost;
        this.cardType = type;
        this.cardColor = color;
        this.cardRarity = rarity;
        this.cardTarget = target;

        return this;
    }

    public DynamicCardBuilder SetNumbers(int damage, int block, int magicNumber)
    {
        return SetNumbers(damage, block, magicNumber, 0);
    }

    public DynamicCardBuilder SetNumbers(int damage, int block, int magicNumber, int secondaryValue)
    {
        this.upgradedDamage = this.damage = damage;
        this.upgradedBlock = this.block = block;
        this.upgradedMagicNumber = this.magicNumber = magicNumber;
        this.upgradedSecondaryValue = this.secondaryValue = secondaryValue;

        return this;
    }

    public DynamicCardBuilder SetImage(String imagePath)
    {
        this.imagePath = imagePath;

        return this;
    }

    public DynamicCardBuilder SetBadges(EYBCardBadge... badges)
    {
        this.cardBadges = badges;

        return this;
    }

    public DynamicCardBuilder SetText(CardStrings cardStrings)
    {
        return SetText(cardStrings.NAME, cardStrings.DESCRIPTION, cardStrings.UPGRADE_DESCRIPTION);
    }

    public DynamicCardBuilder SetText(String name, String description, String upgradeDescription)
    {
        return SetText(name, description, upgradeDescription, new String[0]);
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

    public DynamicCardBuilder SetOnUpgrade(Consumer<AnimatorCard> onUpgrade)
    {
        this.onUpgrade = onUpgrade;

        return this;
    }

    public DynamicCardBuilder SetOnUse(TriConsumer<AnimatorCard, AbstractPlayer, AbstractMonster> onUseAction)
    {
        this.onUse = onUseAction;

        return this;
    }

    public DynamicCardBuilder SetSynergy(Synergy synergy, boolean isShapeshifter)
    {
        this.synergy = synergy;
        this.isShapeshifter = isShapeshifter;

        return this;
    }
}