package eatyourbeets.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;

public class EYBCardPreview
{
    public boolean initialized;
    public AbstractCard defaultPreview;
    public AbstractCard upgradedPreview;

    public void Initialize(AbstractCard defaultPreview, boolean upgrade)
    {
        this.defaultPreview = defaultPreview;

        if (upgrade)
        {
            this.upgradedPreview = defaultPreview.makeStatEquivalentCopy();
            this.upgradedPreview.upgrade();
        }
        else
        {
            this.upgradedPreview = null;
        }

        this.initialized = true;
    }

    public void Initialize(AbstractCard defaultPreview, AbstractCard upgradedPreview)
    {
        this.defaultPreview = defaultPreview;
        this.upgradedPreview = upgradedPreview;
        this.initialized = true;
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
