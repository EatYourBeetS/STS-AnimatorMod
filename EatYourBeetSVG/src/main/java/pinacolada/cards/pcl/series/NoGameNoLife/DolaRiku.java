package pinacolada.cards.pcl.series.NoGameNoLife;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.actions.special.DolaRikuAction;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class DolaRiku extends PCLCard
{
    public static final PCLCardData DATA = Register(DolaRiku.class)
            .SetSkill(1, CardRarity.COMMON, eatyourbeets.cards.base.EYBCardTarget.None)
            .SetSeriesFromClassPackage();

    public DolaRiku()
    {
        super(DATA);

        Initialize(0, 0, 2);
        SetUpgrade(0, 0, 1);

        SetAffinity_Orange(1);

        SetAffinityRequirement(PCLAffinity.Orange, 3);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (TrySpendAffinity(PCLAffinity.Orange) && info.TryActivateSemiLimited())
        {
            PCLActions.Bottom.Draw(1)
            .SetFilter(c -> c.costForTurn == 0 && !PCLGameUtilities.IsHindrance(c), false);
        }
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.ExhaustFromHand(name, 1, false)
        .SetOptions(false, false, false)
        .AddCallback(cards -> PCLActions.Bottom.Add(new DolaRikuAction(cards.get(0), magicNumber)));
    }
}