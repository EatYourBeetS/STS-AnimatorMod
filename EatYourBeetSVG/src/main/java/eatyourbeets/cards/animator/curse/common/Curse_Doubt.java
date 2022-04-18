package eatyourbeets.cards.animator.curse.common;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.utilities.GameActions;

public class Curse_Doubt extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Curse_Doubt.class)
            .SetCurse(UNPLAYABLE_COST, EYBCardTarget.None, false);

    public Curse_Doubt()
    {
        super(DATA);

        Initialize(0, 0, 1);

        SetAffinity_Dark(1);

        SetEndOfTurnPlay(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (dontTriggerOnUseCard)
        {
            GameActions.Bottom.ApplyWeak(null, p, magicNumber);
        }
    }
}