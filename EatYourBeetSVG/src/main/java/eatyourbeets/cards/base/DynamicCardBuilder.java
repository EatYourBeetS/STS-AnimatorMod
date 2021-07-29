package eatyourbeets.cards.base;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.localization.CardStrings;
import eatyourbeets.utilities.ColoredTexture;

import java.util.ArrayList;

public abstract class DynamicCardBuilder
{
    public String id;
    public String imagePath;
    public ColoredTexture portraitImage;
    public ColoredTexture portraitForeground;

    public int cost = -2;
    public int damage;
    public int block;
    public int magicNumber;
    public boolean isMultiDamage;
    public boolean canUpgrade = true;

    public ArrayList<AbstractCard.CardTags> tags = new ArrayList<>();
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