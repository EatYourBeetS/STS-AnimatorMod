package eatyourbeets.cards.animator.curse;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard_Curse;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;

public class Curse_Clumsy extends AnimatorCard_Curse
{
    public static final EYBCardData DATA = Register(Curse_Clumsy.class)
            .SetCurse(-2, EYBCardTarget.None, false);

    public Curse_Clumsy()
    {
        super(DATA, true);

        Initialize(0, 0, 1);

        SetUnplayable(true);
        SetEthereal(true);
    }

    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
    }
}