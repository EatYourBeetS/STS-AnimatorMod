package pinacolada.cards.pcl.series.Konosuba;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.ui.combat.PCLAffinityMeter;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

public class Cecily extends PCLCard
{
    public static final PCLCardData DATA = Register(Cecily.class)
            .SetSkill(0, CardRarity.UNCOMMON, eatyourbeets.cards.base.EYBCardTarget.None)
            .SetSeriesFromClassPackage();

    public Cecily()
    {
        super(DATA);

        Initialize(0, 0, 2, 2);
        SetUpgrade(0,0,1);

        SetAffinity_Light(1);

        SetExhaust(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetHaste(true);
    }


    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {
        if (info.IsSynergizing) {
            PCLActions.Top.Cycle(name, magicNumber);
        }

        PCLActions.Bottom.RerollAffinity(PCLAffinityMeter.Target.CurrentAffinity).AddCallback(a -> {
            PCLActions.Last.SelectFromHand(name, secondaryValue, false)
                    .SetOptions(false, false, false)
                    .SetMessage(cardData.Strings.EXTENDED_DESCRIPTION[0])
                    .SetFilter(c -> c instanceof PCLCard && !(PCLGameUtilities.GetPCLAffinityLevel(c, a, true) > 1))
                    .AddCallback(cards ->
                    {
                        PCLCard card = PCLJUtils.SafeCast(cards.get(0), PCLCard.class);
                        if (card != null)
                        {
                            card.affinities.Add(a, 1);
                            card.flash();
                        }
                    });
        });
    }
}