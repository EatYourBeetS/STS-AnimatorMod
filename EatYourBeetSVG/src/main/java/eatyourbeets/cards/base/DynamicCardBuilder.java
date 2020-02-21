package eatyourbeets.cards.base;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.localization.CardStrings;

import java.util.ArrayList;

public abstract class DynamicCardBuilder
{
    public String id;
    public String imagePath;
    public Texture imageTexture;

    public int cost = -2;
    public int damage;
    public int block;
    public int magicNumber;
    public boolean isMultiDamage;

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