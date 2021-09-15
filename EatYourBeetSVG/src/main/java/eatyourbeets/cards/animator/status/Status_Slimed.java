package eatyourbeets.cards.animator.status;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard_Status;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;

public class Status_Slimed extends AnimatorCard_Status
{
    public static final EYBCardData DATA = Register(Status_Slimed.class)
            .SetStatus(1, CardRarity.COMMON, EYBCardTarget.None);

    public Status_Slimed()
    {
        super(DATA, false);

        Initialize(0, 0);

        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {

    }
}