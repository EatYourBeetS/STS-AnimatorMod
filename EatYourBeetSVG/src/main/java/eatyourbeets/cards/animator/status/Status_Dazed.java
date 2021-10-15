package eatyourbeets.cards.animator.status;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard_Status;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.utilities.GameActions;

public class Status_Dazed extends AnimatorCard_Status
{
    public static final EYBCardData DATA = Register(Status_Dazed.class)
            .SetStatus(-2, CardRarity.COMMON, EYBCardTarget.None);

    public Status_Dazed()
    {
        super(DATA, false);

        Initialize(0, 0, 2);

        SetEthereal(true);
        SetUnplayable(true);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        if (auxiliaryData.form == 1) {
            GameActions.Bottom.GainBlock(magicNumber);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {

    }
}