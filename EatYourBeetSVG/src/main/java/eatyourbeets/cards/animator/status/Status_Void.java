package eatyourbeets.cards.animator.status;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard_Status;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.utilities.GameActions;

public class Status_Void extends AnimatorCard_Status
{
    public static final EYBCardData DATA = Register(Status_Void.class)
            .SetStatus(-2, CardRarity.COMMON, EYBCardTarget.None);

    public Status_Void()
    {
        super(DATA, false);

        Initialize(0, 0, 2);

        SetEthereal(true);
        SetUnplayable(true);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        if (auxiliaryData.form == 0) {
            GameActions.Bottom.SpendEnergy(1,true);
        }
    }


    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        if (auxiliaryData.form == 1) {
            GameActions.Bottom.GainEnergyNextTurn(magicNumber);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {

    }
}