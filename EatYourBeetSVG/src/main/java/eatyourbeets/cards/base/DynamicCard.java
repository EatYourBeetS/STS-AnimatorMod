package eatyourbeets.cards.base;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import org.apache.logging.log4j.util.TriConsumer;

import java.util.function.Consumer;

public class DynamicCard extends AnimatorCard
{
    protected DynamicCardBuilder builder;

    public Consumer<AbstractCard> onUpgrade;
    public TriConsumer<AbstractCard, AbstractPlayer, AbstractMonster> onUse;

    public DynamicCard(DynamicCardBuilder builder)
    {
        this(builder.id, builder.cardStrings, builder.imagePath, builder.cost, builder.cardType, builder.cardColor, builder.cardRarity, builder.cardTarget, builder.cardBadges);

        this.builder = builder;
    }

    private DynamicCard(String id, CardStrings strings, String imagePath, int cost, CardType type, CardColor color, CardRarity rarity, CardTarget target, EYBCardBadge... badges)
    {
        super(new EYBCardData(badges, strings), id, imagePath, cost, type, color, rarity, target);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        if (onUse != null)
        {
            onUse.accept(this, p, m);
        }
    }

    @Override
    public void upgrade()
    {
        if (onUpgrade != null)
        {
            onUpgrade.accept(this);
        }
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new DynamicCard(builder);
    }

    @Override
    public AbstractCard makeStatEquivalentCopy()
    {
        DynamicCard copy = new DynamicCard(builder);

        for (int i = 0; i < this.timesUpgraded; ++i)
        {
            copy.upgrade();
        }

        copy.onUpgrade = this.onUpgrade;
        copy.onUse = this.onUse;
        copy.name = this.name;
        copy.target = this.target;
        copy.upgraded = this.upgraded;
        copy.timesUpgraded = this.timesUpgraded;
        copy.baseDamage = this.baseDamage;
        copy.baseBlock = this.baseBlock;
        copy.baseMagicNumber = this.baseMagicNumber;
        copy.baseSecondaryValue = this.secondaryValue;
        copy.cost = this.cost;
        copy.costForTurn = this.costForTurn;
        copy.isCostModified = this.isCostModified;
        copy.isCostModifiedForTurn = this.isCostModifiedForTurn;
        copy.inBottleLightning = this.inBottleLightning;
        copy.inBottleFlame = this.inBottleFlame;
        copy.inBottleTornado = this.inBottleTornado;
        copy.isSeen = this.isSeen;
        copy.isLocked = this.isLocked;
        copy.misc = this.misc;
        copy.freeToPlayOnce = this.freeToPlayOnce;

        return copy;
    }
}