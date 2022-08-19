package eatyourbeets.cards.base;

import com.megacrit.cardcrawl.cards.AbstractCard;
import eatyourbeets.cards.animator.colorless.uncommon.QuestionMark;
import eatyourbeets.resources.GR;

public class AnimatorCardBuilder extends DynamicCardBuilder
{
    public CardSeries series;

    public AnimatorCardBuilder(String id)
    {
        this(id, null, null, false);
    }

    public AnimatorCardBuilder(EYBCard card, boolean copyNumbers)
    {
        this(card.cardID, card, card.rawDescription, copyNumbers);
    }

    public AnimatorCardBuilder(String id, EYBCard card, String text, boolean copyNumbers)
    {
        super(id, card, text, copyNumbers);

        if (card instanceof AnimatorCard)
        {
             SetSeries(((AnimatorCard)card).series);
        }
    }

    public AnimatorCard_Dynamic Build()
    {
        if (cardStrings == null)
        {
            SetText("", "", "");
        }

        if (imagePath == null)
        {
            imagePath = QuestionMark.DATA.ImagePath;
        }

        return new AnimatorCard_Dynamic(this);
    }

    public AnimatorCardBuilder SetProperties(AbstractCard.CardType type, AbstractCard.CardRarity rarity, AbstractCard.CardTarget target)
    {
        return (AnimatorCardBuilder) SetProperties(type, GR.Enums.Cards.THE_ANIMATOR, rarity, target);
    }

    public AnimatorCardBuilder SetSeries(CardSeries series)
    {
        this.series = series;

        return this;
    }
}