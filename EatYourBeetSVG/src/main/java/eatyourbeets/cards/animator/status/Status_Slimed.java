package eatyourbeets.cards.animator.status;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.EnergizedPower;
import eatyourbeets.cards.base.AnimatorCard_Status;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.utilities.GameActions;

public class Status_Slimed extends AnimatorCard_Status
{
    public static final EYBCardData DATA = Register(Status_Slimed.class)
            .SetStatus(1, CardRarity.COMMON, EYBCardTarget.None);

    public Status_Slimed()
    {
        super(DATA, false);

        Initialize(0, 0, 3);

        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (auxiliaryData.form == 1) {
            GameActions.Bottom.StackPower(new EnergizedPower(player, magicNumber));
        }
    }
}