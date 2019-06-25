package eatyourbeets.cards;

import basemod.abstracts.CustomCard;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GetAllInBattleInstances;
import com.megacrit.cardcrawl.localization.CardStrings;
import eatyourbeets.utilities.Utilities;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
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

    public Boolean TryUpgrade()
    {
        if (!this.upgraded)
        {
            upgradeName();

            if (StringUtils.isNotEmpty(upgradedDescription))
            {
                this.rawDescription = upgradedDescription;
                this.initializeDescription();
            }

            return true;
        }

        return false;
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
