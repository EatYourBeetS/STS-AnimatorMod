package eatyourbeets.cards.base;

import com.megacrit.cardcrawl.cards.AbstractCard;
import eatyourbeets.cards.animator.colorless.uncommon.QuestionMark;
import eatyourbeets.resources.GR;

public class AnimatorClassicCardBuilder extends DynamicCardBuilder
{
    public CardSeries series;

    public AnimatorClassicCardBuilder(String id)
    {
        this(id, null, null, false);
    }

    public AnimatorClassicCardBuilder(EYBCard card, boolean copyNumbers)
    {
        this(card.cardID, card, card.rawDescription, copyNumbers);
    }

    public AnimatorClassicCardBuilder(String id, EYBCard card, String text, boolean copyNumbers)
    {
        super(id, card, text, copyNumbers);

        if (card instanceof AnimatorCard)
        {
             SetSeries(((AnimatorCard)card).series);
        }
    }

    public AnimatorClassicCard_Dynamic Build()
    {
        if (cardStrings == null)
        {
            SetText("", "", "");
        }

        if (imagePath == null)
        {
            imagePath = QuestionMark.DATA.ImagePath;
        }

        return new AnimatorClassicCard_Dynamic(this);
    }

    public AnimatorClassicCardBuilder SetProperties(AbstractCard.CardType type, AbstractCard.CardRarity rarity, AbstractCard.CardTarget target)
    {
        return (AnimatorClassicCardBuilder) SetProperties(type, GR.Enums.Cards.THE_ANIMATOR, rarity, target);
    }

    public AnimatorClassicCardBuilder SetSeries(CardSeries series)
    {
        this.series = series;

        return this;
    }
}