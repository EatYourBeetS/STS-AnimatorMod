package pinacolada.cards.pcl.curse;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.powers.CombatStats;
import pinacolada.cards.base.*;
import pinacolada.powers.special.SelfImmolationPower;
import pinacolada.utilities.PCLActions;

public class Curse_Eclipse extends PCLCard_Curse
{
    public static final PCLCardData DATA = Register(Curse_Eclipse.class)
            .SetCurse(-2, eatyourbeets.cards.base.EYBCardTarget.None, false).SetSeries(CardSeries.Berserk);

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
            PCLActions.Bottom.StackAffinityPower(PCLAffinity.Dark, 5);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (dontTriggerOnUseCard)
        {
            PCLActions.Bottom.StackPower(new SelfImmolationPower(player, magicNumber, true));
        }
    }

}