package eatyourbeets.cards.animator.status;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard_Status;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
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
    public int SetForm(Integer form, int timesUpgraded) {
        SetEthereal(form == 1);
        return super.SetForm(form, timesUpgraded);
    };

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        GameActions.Bottom.RecoverHP(1 + auxiliaryData.form);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {

    }
}