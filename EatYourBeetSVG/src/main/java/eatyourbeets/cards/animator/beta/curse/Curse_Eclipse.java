package eatyourbeets.cards.animator.beta.curse;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.powers.animator.SelfImmolationPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.TargetHelper;

public class Curse_Eclipse extends AnimatorCard_Curse
{
    public static final EYBCardData DATA = Register(Curse_Eclipse.class)
            .SetCurse(-2, EYBCardTarget.None, false).SetSeries(CardSeries.Berserk);

    public Curse_Eclipse()
    {
        super(DATA, true);
        Initialize(0,0,1,2);
        SetAffinity_Dark(2);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        GameActions.Bottom.StackPower(new SelfImmolationPower(player, magicNumber));

        GameActions.Bottom.Flash(this);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        GameActions.Bottom.StackPower(TargetHelper.Player(), PowerHelper.TemporaryDesecration, secondaryValue);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
    }

}