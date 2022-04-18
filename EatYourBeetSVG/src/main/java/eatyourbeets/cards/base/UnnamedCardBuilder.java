package eatyourbeets.cards.base;

import com.megacrit.cardcrawl.cards.AbstractCard;
import eatyourbeets.cards.animator.colorless.uncommon.QuestionMark;
import eatyourbeets.resources.GR;

public class UnnamedCardBuilder extends DynamicCardBuilder
{
    public UnnamedCardBuilder(String id)
    {
        super(id);
    }

    public UnnamedCardBuilder(EYBCard card, boolean copyNumbers)
    {
        super(card, copyNumbers);
    }

    public UnnamedCardBuilder(String id, EYBCard card, String text, boolean copyNumbers)
    {
        super(id, card, text, copyNumbers);
    }

    public UnnamedCard_Dynamic Build()
    {
        if (cardStrings == null)
        {
            SetText("", "", "");
        }

        if (imagePath == null)
        {
            imagePath = QuestionMark.DATA.ImagePath;
        }

        return new UnnamedCard_Dynamic(this);
    }

    public UnnamedCardBuilder SetProperties(AbstractCard.CardType type, AbstractCard.CardRarity rarity, AbstractCard.CardTarget target)
    {
        return (UnnamedCardBuilder) SetProperties(type, GR.Enums.Cards.THE_UNNAMED, rarity, target);
    }
}