package eatyourbeets.cards.animator.beta.status;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.EnergizedPower;
import eatyourbeets.cards.base.AnimatorCard_Status;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.utilities.GameActions;

public class Ginko_Slimed extends AnimatorCard_Status
{
    public static final EYBCardData DATA = Register(Ginko_Slimed.class)
            .SetStatus(1, CardRarity.COMMON, EYBCardTarget.None);

    public Ginko_Slimed()
    {
        super(DATA, false);

        Initialize(0, 0, 3);

        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.StackPower(p, new EnergizedPower(p, magicNumber));
    }
}