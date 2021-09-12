package eatyourbeets.cards.animator.status;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.GameActions;

public class Status_Wound extends AnimatorCard_Status
{
    public static final EYBCardData DATA = Register(Status_Wound.class)
            .SetStatus(-2, CardRarity.COMMON, EYBCardTarget.None);

    public Status_Wound()
    {
        super(DATA, false);

        Initialize(0, 0);

        SetUnplayable(true);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        GameActions.Bottom.RecoverHP(1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {

    }
}