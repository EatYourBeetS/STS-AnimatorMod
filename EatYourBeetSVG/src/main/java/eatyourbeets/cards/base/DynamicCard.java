package eatyourbeets.cards.base;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import org.apache.logging.log4j.util.TriConsumer;

import java.util.function.Consumer;

public class DynamicCard extends AnimatorCard
{
    protected final DynamicCardBuilder builder;

    public Consumer<AbstractCard> onUpgrade;
    public TriConsumer<AbstractCard, AbstractPlayer, AbstractMonster> onUse;

    public DynamicCard(DynamicCardBuilder builder)
    {
        super(new EYBCardData(builder.cardBadges, builder.cardStrings), builder.id, builder.imagePath,
            builder.cost, builder.cardType, builder.cardColor, builder.cardRarity, builder.cardTarget);

        Initialize(builder.damage, builder.block, builder.magicNumber, builder.secondaryValue);

        this.builder = builder;
        this.onUse = builder.onUse;
        this.onUpgrade = builder.onUpgrade;

        SetSynergy(builder.synergy, builder.isShapeshifter);
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
}