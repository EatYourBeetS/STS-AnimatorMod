package eatyourbeets.cards.base;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.localization.CardStrings;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class EYBCardData
{
    private Constructor<? extends EYBCard> constructor;

    public final Class<? extends EYBCard> type;
    public final CardStrings strings;
    public final EYBCardBadge[] badges;

    public AbstractCard defaultPreview;
    public AbstractCard upgradedPreview;
    public boolean previewInitialized;

    public EYBCardData(Class<? extends EYBCard> type, EYBCardBadge[] badges, CardStrings cardStrings)
    {
        this.type = type;
        this.strings = cardStrings;
        this.badges = badges;
    }

    public AbstractCard CreateNewInstance() throws RuntimeException
    {
        try
        {
            if (constructor == null)
            {
                constructor = type.getConstructor();
                constructor.setAccessible(true);
            }

            return constructor.newInstance();
        }
        catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e)
        {
            throw new RuntimeException(e);
        }
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
