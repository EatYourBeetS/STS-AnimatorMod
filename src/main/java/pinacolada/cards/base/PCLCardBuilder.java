package pinacolada.cards.base;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.DynamicCardBuilder;
import eatyourbeets.interfaces.delegates.ActionT1;
import eatyourbeets.interfaces.delegates.ActionT3;
import eatyourbeets.interfaces.delegates.FuncT1;
import eatyourbeets.utilities.AdvancedTexture;
import pinacolada.cards.base.attributes.AbstractAttribute;
import pinacolada.cards.pcl.colorless.QuestionMark;
import pinacolada.resources.GR;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PCLCardBuilder extends DynamicCardBuilder
{
    protected ActionT1<PCLCard> constructor;
    protected ActionT1<PCLCard> onUpgrade;
    protected ActionT3<PCLCard, AbstractPlayer, AbstractMonster> onUse;
    protected CardSeries series;
    protected PCLCardTarget attackTarget = PCLCardTarget.Normal;
    protected FuncT1<AbstractAttribute, PCLCard> getBlockInfo;
    protected FuncT1<AbstractAttribute, PCLCard> getDamageInfo;
    protected FuncT1<AbstractAttribute, PCLCard> getSpecialInfo;
    protected PCLAttackType attackType = PCLAttackType.Normal;
    protected PCLCardAffinities affinities;
    protected TextureAtlas.AtlasRegion fakePortrait;
    protected boolean showTypeText = true;
    protected int attributeMultiplier = 1;
    protected int blockUpgrade;
    protected int costUpgrade;
    protected int damageUpgrade;
    protected int hitCount;
    protected int hitCountUpgrade;
    protected int magicNumberUpgrade;
    protected int secondaryValue;
    protected int secondaryValueUpgrade;

    public PCLCardBuilder(String id)
    {
        super(id);

        this.affinities = new PCLCardAffinities(null);
        this.id = id;
    }

    public PCLCardBuilder(PCLCard card, boolean copyNumbers)
    {
        this(card, card.name, card.rawDescription, copyNumbers);
    }

    public PCLCardBuilder(PCLCard card, String text, boolean copyNumbers)
    {
        this(card, card.name, text, copyNumbers);
    }

    public PCLCardBuilder(PCLCard card, String name, String text, boolean copyNumbers)
    {
        this(card.cardID);

        if (copyNumbers)
        {
            SetNumbers(card.damage, card.block, card.magicNumber, card.secondaryValue, card.hitCount);
            SetUpgrades(card.upgrade_damage, card.upgrade_block, card.upgrade_magicNumber, card.upgrade_secondaryValue, card.upgrade_hitCount);
            SetCost(card.cost, card.upgrade_cost);
            affinities.Initialize(card.affinities);
        }
        else
        {
            SetCost(-2, 0);
        }

        SetImage(card.portraitImg, card.portraitForeground);
        SetProperties(card.type, card.rarity, AbstractCard.CardTarget.NONE);
        SetText(name, text, text);
        SetSeries(card.series);
    }

    public PCLCard_Dynamic Build()
    {
        if (cardStrings == null)
        {
            SetText("", "", "");
        }

        if (imagePath == null)
        {
            imagePath = QuestionMark.DATA.ImagePath;
        }

        return new PCLCard_Dynamic(this);
    }

    public PCLCardBuilder SetProperties(AbstractCard.CardType type, AbstractCard.CardRarity rarity, AbstractCard.CardTarget target)
    {
        return SetProperties(type, GR.Enums.Cards.THE_FOOL, rarity, target);
    }

    public PCLCardBuilder SetProperties(AbstractCard.CardType type, AbstractCard.CardColor color, AbstractCard.CardRarity rarity, AbstractCard.CardTarget target)
    {
        this.cardType = type;
        this.cardColor = color;
        this.cardRarity = rarity;
        this.cardTarget = target;

        return this;
    }

    public PCLCardBuilder SetID(String id)
    {
        this.id = id;

        return this;
    }


    public PCLCardBuilder SetCost(int baseCost, int costUpgrade)
    {
        this.cost = baseCost;
        this.costUpgrade = costUpgrade;

        return this;
    }

    public PCLCardBuilder SetNumbers(PCLCard source)
    {
        this.damageUpgrade = this.damage = source.baseDamage;
        this.blockUpgrade = this.block = source.baseBlock;
        this.magicNumberUpgrade = this.magicNumber = source.baseMagicNumber;
        this.secondaryValueUpgrade = this.secondaryValue = source.baseSecondaryValue;

        return this;
    }

    public PCLCardBuilder SetNumbers(int damage, int block, int magicNumber, int secondaryValue, int hitCount)
    {
        this.damageUpgrade = this.damage = damage;
        this.blockUpgrade = this.block = block;
        this.magicNumberUpgrade = this.magicNumber = magicNumber;
        this.secondaryValueUpgrade = this.secondaryValue = secondaryValue;
        this.hitCountUpgrade = this.hitCount = hitCount;

        return this;
    }

    public PCLCardBuilder SetUpgrades(int damage, int block, int magicNumber, int secondaryValue, int hitCount)
    {
        this.damageUpgrade = damage;
        this.blockUpgrade = block;
        this.magicNumberUpgrade = magicNumber;
        this.secondaryValueUpgrade = secondaryValue;
        this.hitCountUpgrade = hitCount;

        return this;
    }

    public PCLCardBuilder SetTags(AbstractCard card) {
        ArrayList<AbstractCard.CardTags> tags = new ArrayList<>(card.tags);
        if (card.exhaust || card.exhaustOnUseOnce) {
            tags.add(GR.Enums.CardTags.PCL_EXHAUST);
        }
        if (card.retain) {
            tags.add(GR.Enums.CardTags.PCL_RETAIN_ONCE);
        }
        if (card.selfRetain) {
            tags.add(GR.Enums.CardTags.PCL_RETAIN);
        }
        if (card.isEthereal) {
            tags.add(GR.Enums.CardTags.PCL_ETHEREAL);
        }
        if (card.isInnate) {
            tags.add(GR.Enums.CardTags.PCL_INNATE);
        }
        if (card.purgeOnUse) {
            tags.add(GR.Enums.CardTags.PURGE);
        }
        return SetTags(tags);
    }

    public PCLCardBuilder SetTags(List<AbstractCard.CardTags> tags)
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

    public PCLCardBuilder SetTags(AbstractCard.CardTags... tags)
    {
        return SetTags(Arrays.asList(tags));
    }

    public PCLCardBuilder SetAttackType(PCLAttackType attackType, PCLCardTarget attackTarget)
    {
        this.attackType = attackType;
        this.attackTarget = attackTarget;
        this.isMultiDamage = (attackTarget == PCLCardTarget.AoE);

        return this;
    }

    public PCLCardBuilder SetDamageInfo(FuncT1<AbstractAttribute, PCLCard> getDamageInfo)
    {
        this.getDamageInfo = getDamageInfo;

        return this;
    }

    public PCLCardBuilder SetBlockInfo(FuncT1<AbstractAttribute, PCLCard> getBlockInfo)
    {
        this.getBlockInfo = getBlockInfo;

        return this;
    }

    public PCLCardBuilder SetSpecialInfo(FuncT1<AbstractAttribute, PCLCard> getSpecialInfo)
    {
        this.getSpecialInfo = getSpecialInfo;

        return this;
    }

    public PCLCardBuilder SetAttackType(PCLAttackType attackType, PCLCardTarget attackTarget, int multiplier)
    {
        this.attackType = attackType;
        this.attackTarget = attackTarget;
        this.attributeMultiplier = multiplier;
        this.isMultiDamage = (attackTarget == PCLCardTarget.AoE);

        return this;
    }

    public PCLCardBuilder SetCardTarget(PCLCardTarget cardTarget) {
        this.attackTarget = cardTarget;

        return this;
    }

    public PCLCardBuilder SetImage(AdvancedTexture portraitImage, AdvancedTexture portraitForeground)
    {
        this.portraitImage = portraitImage;
        this.portraitForeground = portraitForeground;

        return this;
    }

    public PCLCardBuilder SetImagePath(String imagePath)
    {
        this.imagePath = imagePath;

        return this;
    }

    public PCLCardBuilder SetPortrait(TextureAtlas.AtlasRegion portrait)
    {
        this.fakePortrait = portrait;

        return this;
    }

    public PCLCardBuilder SetText(CardStrings cardStrings)
    {
        return SetText(cardStrings.NAME, cardStrings.DESCRIPTION, cardStrings.UPGRADE_DESCRIPTION);
    }

    public PCLCardBuilder SetText(String name, String description, String upgradeDescription)
    {
        return SetText(name, description, upgradeDescription != null ? upgradeDescription : description, new String[0]);
    }

    public PCLCardBuilder SetText(String name, String description, String upgradeDescription, String[] extendedDescription)
    {
        this.cardStrings = new CardStrings();
        this.cardStrings.NAME = name;
        this.cardStrings.DESCRIPTION = description;
        this.cardStrings.UPGRADE_DESCRIPTION = upgradeDescription;
        this.cardStrings.EXTENDED_DESCRIPTION = extendedDescription;

        return this;
    }

    public PCLCardBuilder SetConstructor(ActionT1<PCLCard> constructor)
    {
        this.constructor = constructor;

        return this;
    }

    public PCLCardBuilder SetOnUpgrade(ActionT1<PCLCard> onUpgrade)
    {
        this.onUpgrade = onUpgrade;

        return this;
    }

    public PCLCardBuilder SetOnUse(ActionT3<PCLCard, AbstractPlayer, AbstractMonster> onUseAction)
    {
        this.onUse = onUseAction;

        return this;
    }

    public PCLCardBuilder SetSeries(CardSeries series)
    {
        this.series = series;

        return this;
    }

    public PCLCardBuilder CanUpgrade(boolean canUpgrade)
    {
        this.canUpgrade = canUpgrade;

        return this;
    }

    public PCLCardBuilder ShowTypeText(boolean showTypeText)
    {
        this.showTypeText = showTypeText;

        return this;
    }
}