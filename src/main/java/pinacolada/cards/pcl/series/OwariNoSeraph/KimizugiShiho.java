package pinacolada.cards.pcl.series.OwariNoSeraph;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.*;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class KimizugiShiho extends PCLCard
{
    public static final PCLCardData DATA = Register(KimizugiShiho.class)
            .SetSkill(1, CardRarity.COMMON, PCLCardTarget.None)
            .SetSeries(CardSeries.OwariNoSeraph);

    public KimizugiShiho()
    {
        super(DATA);

        Initialize(0,2, 1,1);
        SetUpgrade(0,0, 0, 0);

        SetAffinity_Red(1, 0, 0);
        SetAffinity_Green(1, 0, 0);
        SetAffinity_Blue(1, 0, 1);

        SetCostUpgrade(-1);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        PCLActions.Bottom.Cycle(name, secondaryValue);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);

        PCLActions.Bottom.SelectFromHand(name, 1, false)
                .SetOptions(true, false, false)
                .SetFilter(c -> c instanceof PCLCard && ((PCLCard) c).series != null)
                .SetMessage(cardData.Strings.EXTENDED_DESCRIPTION[0])
                .AddCallback(cards ->
                {
                    if (cards.size() > 0 && cards.get(0) instanceof PCLCard)
                    {
                        PCLActions.Top.FetchFromPile(name, 1, player.discardPile)
                                .SetOptions(true, false)
                                .SetFilter(cards.get(0), PCLGameUtilities::IsSameSeries);
                    }

                    if (info.IsSynergizing) {
                        PCLActions.Bottom.Cycle(name, secondaryValue);
                    }
                });
    }
}