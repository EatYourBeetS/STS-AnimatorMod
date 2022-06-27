package eatyourbeets.cards.animator.colorless.uncommon;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class QuestionMark extends AnimatorCard
{
    public static final EYBCardData DATA = Register(QuestionMark.class)
            .SetSkill(-2, CardRarity.UNCOMMON, EYBCardTarget.ALL)
            .SetMaxCopies(0)
            .SetColor(CardColor.COLORLESS);

    public AnimatorCard copy = null;

    public QuestionMark()
    {
        super(DATA);

        Initialize(0, 0);

        SetAffinity_Star(1, 1, 0);
        SetVolatile(true);
        SetUnplayable(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {

    }

    @Override
    public void triggerOnAffinitySeal(boolean reshuffle)
    {
        super.triggerOnAffinitySeal(reshuffle);

        AnimatorCard card = GameUtilities.GetRandomElement(CardSeries.GetNonColorlessCard());
        if (card != null)
        {
            GameActions.Bottom.MakeCardInHand(card.makeCopy()).SetUpgrade(upgraded, false);
            GameActions.Last.Exhaust(this);
        }
    }
}