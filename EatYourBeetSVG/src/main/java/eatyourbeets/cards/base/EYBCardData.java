package eatyourbeets.cards.base;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.localization.CardStrings;
import eatyourbeets.resources.GR;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class EYBCardData
{
    private Constructor<? extends EYBCard> constructor;

    public final Class<? extends EYBCard> type;
    public final CardStrings Strings;

    public String ID;
    public AbstractCard.CardType CardType;
    public int BaseCost;

    public AbstractCard.CardRarity CardRarity;
    public AbstractCard.CardColor CardColor;
    public EYBCardTarget CardTarget;
    public EYBAttackType AttackType;
    public EYBCard tempCard = null;
    public EYBCard defaultPreview;
    public EYBCard upgradedPreview;
    public boolean previewInitialized;

    public EYBCardData(Class<? extends EYBCard> type, String cardID)
    {
        this.type = type;
        this.Strings = GR.GetCardStrings(cardID);
        this.ID = cardID;
    }

    public EYBCardData(Class<? extends EYBCard> type, String cardID, CardStrings strings)
    {
        this.type = type;
        this.Strings = strings;
        this.ID = cardID;
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

    public EYBCardData SetColor(AbstractCard.CardColor color)
    {
        CardColor = color;

        return this;
    }

    public EYBCardData SetAttack(AbstractCard.CardRarity rarity, EYBCardTarget target, EYBAttackType attackType, int cost)
    {
        CardRarity = rarity;
        CardTarget = target;
        CardType = AbstractCard.CardType.ATTACK;
        AttackType = attackType;
        BaseCost = cost;

        return this;
    }

    public EYBCardData SetSkill(AbstractCard.CardRarity rarity, EYBCardTarget target, int cost)
    {
        CardRarity = rarity;
        CardTarget = target;
        CardType = AbstractCard.CardType.SKILL;
        AttackType = EYBAttackType.Normal;
        BaseCost = cost;

        return this;
    }

    public EYBCardData SetPower(AbstractCard.CardRarity rarity, int cost)
    {
        CardRarity = rarity;
        CardTarget = EYBCardTarget.None;
        CardType = AbstractCard.CardType.POWER;
        AttackType = EYBAttackType.Normal;
        BaseCost = cost;

        return this;
    }

    public EYBCardData SetCurse(EYBCardTarget target, int cost)
    {
        CardRarity = AbstractCard.CardRarity.CURSE;
        CardColor = AbstractCard.CardColor.CURSE;
        CardType = AbstractCard.CardType.CURSE;
        AttackType = EYBAttackType.Normal;
        CardTarget = target;
        BaseCost = cost;

        return this;
    }

    public EYBCardData SetStatus(AbstractCard.CardRarity rarity, EYBCardTarget target, int cost)
    {
        CardRarity = rarity;
        CardColor = AbstractCard.CardColor.COLORLESS;
        CardType = AbstractCard.CardType.STATUS;
        AttackType = EYBAttackType.Normal;
        CardTarget = target;
        BaseCost = cost;

        return this;
    }
}
