package eatyourbeets.cards.animator.status;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.GameActions;

public class Status_Wound extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Status_Wound.class)
            .SetStatus(UNPLAYABLE_COST, CardRarity.COMMON, EYBCardTarget.None);

    public Status_Wound()
    {
        super(DATA);

        Initialize(0, 0, 2);

        SetEndOfTurnPlay(false);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        GameActions.Bottom.RecoverHP(magicNumber);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {

    }
}