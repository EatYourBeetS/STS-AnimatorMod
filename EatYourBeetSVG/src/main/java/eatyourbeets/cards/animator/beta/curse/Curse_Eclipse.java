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
        Initialize(0,0,1,5);
        SetAffinity_Dark(1);
        SetUnplayable(true);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        if (CombatStats.TryActivateLimited(cardID)) {
            GameActions.Bottom.StackAffinityPower(Affinity.Dark, 5);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (dontTriggerOnUseCard)
        {
            GameActions.Bottom.StackPower(new SelfImmolationPower(player, magicNumber, true));
        }
    }

}