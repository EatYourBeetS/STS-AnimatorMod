package eatyourbeets.cards.animator.beta.curse;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.animator.SelfImmolationPower;
import eatyourbeets.utilities.GameActions;

public class Curse_Eclipse extends AnimatorCard_Curse
{
    public static final EYBCardData DATA = Register(Curse_Eclipse.class)
            .SetCurse(-2, EYBCardTarget.None, false).SetSeries(CardSeries.Berserk);

    public Curse_Eclipse()
    {
        super(DATA, true);
        Initialize(0,0,1,1);
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

        if (CombatStats.TryActivateLimited(cardID)) {
            GameActions.Bottom.IncreaseAffinityPowerLevel(Affinity.Dark, 1);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
    }

}