package eatyourbeets.cards.animator.beta.status;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DrawCardNextTurnPower;
import eatyourbeets.cards.base.AnimatorCard_Status;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.utilities.GameActions;

public class Ginko_Wound extends AnimatorCard_Status
{
    public static final EYBCardData DATA = Register(Ginko_Wound.class)
            .SetStatus(0, CardRarity.COMMON, EYBCardTarget.None);

    public Ginko_Wound()
    {
        super(DATA, false);

        Initialize(0, 0, 2);
        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.StackPower(p, new DrawCardNextTurnPower(p, magicNumber));
    }
}