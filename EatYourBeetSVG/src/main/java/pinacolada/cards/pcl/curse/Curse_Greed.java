package pinacolada.cards.pcl.curse;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.actions.cardManipulation.RandomCostIncrease;
import pinacolada.cards.base.CardSeries;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCard_Curse;
import pinacolada.utilities.PCLActions;

public class Curse_Greed extends PCLCard_Curse
{
    public static final PCLCardData DATA = Register(Curse_Greed.class)
            .SetCurse(-2, eatyourbeets.cards.base.EYBCardTarget.None, false)
            .SetSeries(CardSeries.Konosuba);

    public Curse_Greed()
    {
        super(DATA, false);

        Initialize(0, 0, 2);

        SetAffinity_Dark(1);
        SetUnplayable(true);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        PCLActions.Bottom.Add(new RandomCostIncrease(1, false));
        PCLActions.Bottom.Flash(this);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {

    }
}