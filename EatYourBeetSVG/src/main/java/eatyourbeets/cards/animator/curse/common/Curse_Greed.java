package eatyourbeets.cards.animator.curse.common;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.cardManipulation.RandomCostIncrease;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.GameActions;

public class Curse_Greed extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Curse_Greed.class)
            .SetCurse(UNPLAYABLE_COST, EYBCardTarget.None, false)
            .SetSeries(CardSeries.Konosuba);

    public Curse_Greed()
    {
        super(DATA);

        Initialize(0, 0);

        SetAffinity_Dark(1);

        SetEndOfTurnPlay(false);
        SetEthereal(true);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        GameActions.Bottom.Add(new RandomCostIncrease(1, false));
        GameActions.Bottom.Flash(this);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {

    }
}