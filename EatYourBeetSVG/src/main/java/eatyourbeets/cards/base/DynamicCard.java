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
        DynamicCard copy = (DynamicCard) super.makeStatEquivalentCopy();

        copy.onUse = this.onUse;
        copy.onUpgrade = this.onUpgrade;

        return copy;
    }
}