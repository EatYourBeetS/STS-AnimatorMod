package eatyourbeets.cards.animator.curse.common;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;

public class Curse_Writhe extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Curse_Writhe.class)
            .SetCurse(UNPLAYABLE_COST, EYBCardTarget.None, false);

    public Curse_Writhe()
    {
        super(DATA);

        Initialize(0, 0);

        SetAffinity_Dark(1);

        SetEndOfTurnPlay(false);
        SetInnate(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {

    }
}