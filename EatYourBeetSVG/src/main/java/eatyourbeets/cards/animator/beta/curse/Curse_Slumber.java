package eatyourbeets.cards.animator.beta.curse;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.common.ImpairedPower;
import eatyourbeets.utilities.GameActions;

public class Curse_Slumber extends AnimatorCard_Curse
{
    public static final EYBCardData DATA = Register(Curse_Slumber.class)
            .SetCurse(-2, EYBCardTarget.None, false)
            .SetSeries(CardSeries.NoGameNoLife);

    public Curse_Slumber()
    {
        super(DATA, true);

        Initialize(0, 0, 1, 3);

        SetAffinity_Blue(1);
        SetUnplayable(true);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();
        if (CombatStats.TryActivateLimited(cardID)) {
            GameActions.Bottom.GainFocus(secondaryValue, true);
        }
    }

    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (dontTriggerOnUseCard)
        {
            GameActions.Bottom.StackPower(new ImpairedPower(player, magicNumber, true));
        }
    }
}