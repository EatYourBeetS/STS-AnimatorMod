package eatyourbeets.cards.base;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import org.apache.logging.log4j.util.TriConsumer;

import java.util.function.Consumer;

public class AnimatorCard_Dynamic extends AnimatorCard
{
    protected final AnimatorCardBuilder builder;

    public Consumer<AnimatorCard> onUpgrade;
    public TriConsumer<AnimatorCard, AbstractPlayer, AbstractMonster> onUse;

    public AnimatorCard_Dynamic(AnimatorCardBuilder builder)
    {
        super(new EYBCardData(builder.cardBadges, builder.cardStrings), builder.id, builder.imagePath,
            builder.cost, builder.cardType, builder.cardColor, builder.cardRarity, builder.cardTarget);

        Initialize(builder.damage, builder.block, builder.magicNumber, builder.secondaryValue);
        SetUpgrade(builder.damageUpgrade, builder.blockUpgrade, builder.magicNumberUpgrade, builder.secondaryValueUpgrade);

        this.builder = builder;
        this.onUse = builder.onUse;
        this.onUpgrade = builder.onUpgrade;

        SetSynergy(builder.synergy, builder.isShapeshifter);
    }

    @Override
    protected void OnUpgrade()
    {
        if (onUpgrade != null)
        {
            onUpgrade.accept(this);
        }
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
    public AbstractCard makeCopy()
    {
        return new AnimatorCard_Dynamic(builder);
    }
}