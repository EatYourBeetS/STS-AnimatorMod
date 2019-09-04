package eatyourbeets.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;

public class EYBCardPreview
{
    public AbstractCard defaultPreview;
    public AbstractCard upgradedPreview;

    public EYBCardPreview(AbstractCard defaultPreview, AbstractCard upgradedPreview)
    {
        this.defaultPreview = defaultPreview;
        this.upgradedPreview = upgradedPreview;
    }

    public AbstractCard GetCardPreview(AbstractCard card)
    {
        if (upgradedPreview != null && card.upgraded)
        {
            return upgradedPreview;
        }
        else
        {
            return defaultPreview;
        }
    }
}
