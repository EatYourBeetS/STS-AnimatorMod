package eatyourbeets.cards.animator.beta.curse;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Curse_Depression extends AnimatorCard_Curse
{
    public static final EYBCardData DATA = Register(Curse_Depression.class)
            .SetCurse(-2, EYBCardTarget.None, false).SetSeries(CardSeries.DateALive);
    static
    {
        DATA.CardRarity = CardRarity.SPECIAL;
    }

    public Curse_Depression()
    {
        super(DATA, true);
        SetAffinity_Blue(1);
        SetUnplayable(true);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        GameActions.Bottom.DiscardFromHand(name, 1, true)
        .ShowEffect(true, true)
        .SetOptions(false, false, false)
        .SetFilter(card -> !GameUtilities.IsHindrance(card));

        GameActions.Bottom.Flash(this);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
    }

}