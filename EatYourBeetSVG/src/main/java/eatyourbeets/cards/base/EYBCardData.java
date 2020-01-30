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

    public EYBCard tempCard = null;
    public EYBCard defaultPreview;
    public EYBCard upgradedPreview;
    public boolean previewInitialized;

    public EYBCardData(Class<? extends EYBCard> type, CardStrings cardStrings)
    {
        this.type = type;
        this.strings = cardStrings;
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

    public void InitializePreview(EYBCard defaultPreview, boolean upgrade)
    {
        if (previewInitialized)
        {
            throw new RuntimeException("The preview was already initialized");
        }

        this.previewInitialized = true;
        this.defaultPreview = defaultPreview;
        this.defaultPreview.isPreview = true;

        if (upgrade)
        {
            this.upgradedPreview = (EYBCard) defaultPreview.makeStatEquivalentCopy();
            this.upgradedPreview.isPreview = true;
            this.upgradedPreview.upgrade();
            this.upgradedPreview.displayUpgrades();
        }
    }

    public EYBCard GetCardPreview(EYBCard card)
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
