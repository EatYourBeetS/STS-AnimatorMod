package eatyourbeets.cards.base;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.localization.CardStrings;

public abstract class DynamicCardBuilder
{
    public String id;
    public String imagePath;

    public int cost = -2;
    public int damage;
    public int block;
    public int magicNumber;

    public AbstractCard.CardTarget cardTarget = AbstractCard.CardTarget.NONE;
    public AbstractCard.CardRarity cardRarity = AbstractCard.CardRarity.BASIC;
    public AbstractCard.CardColor cardColor = AbstractCard.CardColor.COLORLESS;
    public AbstractCard.CardType cardType = AbstractCard.CardType.SKILL;
    public CardStrings cardStrings;

    public DynamicCardBuilder(String id)
    {
        this.id = id;
    }
}