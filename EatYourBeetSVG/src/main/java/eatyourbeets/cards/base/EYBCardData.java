package eatyourbeets.cards.base;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.localization.CardStrings;

public class EYBCardData
{
    public final CardStrings strings;
    public final EYBCardBadge[] badges;
    public AbstractCard defaultPreview;
    public AbstractCard upgradedPreview;
    public boolean previewInitialized;

    public EYBCardData(EYBCardBadge[] badges, CardStrings cardStrings)
    {
        this.strings = cardStrings;
        this.badges = badges;
    }

    public void InitializePreview(AbstractCard defaultPreview, boolean upgrade)
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
    }

    public void InitializePreview(AbstractCard defaultPreview, AbstractCard upgradedPreview)
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
