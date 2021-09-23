package eatyourbeets.cards.animator.curse;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.cardManipulation.RandomCostIncrease;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.GameActions;

public class Curse_Greed extends AnimatorCard_Curse
{
    public static final EYBCardData DATA = Register(Curse_Greed.class)
            .SetCurse(-2, EYBCardTarget.None, false)
            .SetSeries(CardSeries.Konosuba);

    public Curse_Greed()
    {
        super(DATA, false);

        Initialize(0, 0, 2);

        SetAffinity_Dark(1);
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