package eatyourbeets.cards.base;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import org.apache.logging.log4j.util.TriConsumer;

import java.util.function.Consumer;

public class DynamicCard extends AnimatorCard
{
    public Consumer<AbstractCard> onUpgrade;
    public TriConsumer<AbstractCard, AbstractPlayer, AbstractMonster> onUse;

    protected DynamicCard(String id, CardStrings strings, String imagePath, int cost, CardType type, CardColor color, CardRarity rarity, CardTarget target, EYBCardBadge... badges)
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
}