package eatyourbeets.cards.animator.beta.curse;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.animator.SelfImmolationPower;
import eatyourbeets.utilities.GameActions;

public class Curse_Eclipse extends AnimatorCard_Curse
{
    public static final EYBCardData DATA = Register(Curse_Eclipse.class)
            .SetCurse(-2, EYBCardTarget.None, false).SetSeries(CardSeries.Berserk);

    public Curse_Eclipse()
    {
        super(DATA, true);
        SetAffinity_Dark(2);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        GameActions.Bottom.StackPower(new SelfImmolationPower(player, 1));

        GameActions.Bottom.Flash(this);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
    }

    @Override
    public boolean cardPlayable(AbstractMonster m)
    {
        return false;
    }

}