package eatyourbeets.cards.animator.curse.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.status.Status_Dazed;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.GameActions;

public class Curse_Dizziness extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Curse_Dizziness.class)
            .SetCurse(UNPLAYABLE_COST, EYBCardTarget.None, true)
            .SetSeries(CardSeries.TouhouProject);

    public Curse_Dizziness()
    {
        super(DATA);

        SetAffinity_Dark(1);

        SetEndOfTurnPlay(false);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        GameActions.Bottom.MakeCardInDrawPile(new Status_Dazed());
        GameActions.Bottom.Flash(this);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {

    }
}