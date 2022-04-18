package eatyourbeets.cards.animator.curse.common;

import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Curse_Nutcracker extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Curse_Nutcracker.class)
            .SetCurse(UNPLAYABLE_COST, EYBCardTarget.None, false)
            .SetSeries(CardSeries.YoujoSenki);

    public Curse_Nutcracker()
    {
        super(DATA);

        Initialize(0, 0, 3);

        SetAffinity_Dark(1);

        SetEndOfTurnPlay(true);
    }

    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (dontTriggerOnUseCard)
        {
            for (AbstractMonster m1 : GameUtilities.GetEnemies(true))
            {
                GameActions.Bottom.Add(new HealAction(m1, null, magicNumber));
            }
        }
    }
}