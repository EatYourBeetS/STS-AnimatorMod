package eatyourbeets.cards.animator.series.OwariNoSeraph;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class KimizugiShiho extends AnimatorCard
{
    public static final EYBCardData DATA = Register(KimizugiShiho.class)
            .SetSkill(1, CardRarity.COMMON, EYBCardTarget.None)
            .SetSeries(CardSeries.OwariNoSeraph);

    public KimizugiShiho()
    {
        super(DATA);

        Initialize(0,2, 1,1);
        SetUpgrade(0,0, 0, 0);

        SetAffinity_Red(1, 0, 0);
        SetAffinity_Green(1, 0, 0);
        SetAffinity_Blue(1, 0, 0);

        SetCostUpgrade(-1);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        GameActions.Bottom.Cycle(name, secondaryValue);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);

        GameActions.Bottom.SelectFromHand(name, 1, false)
                .SetOptions(true, false, false)
                .SetFilter(c -> c instanceof AnimatorCard && ((AnimatorCard) c).series != null)
                .SetMessage(cardData.Strings.EXTENDED_DESCRIPTION[0])
                .AddCallback(cards ->
                {
                    if (cards.size() > 0 && cards.get(0) instanceof AnimatorCard)
                    {
                        GameActions.Top.FetchFromPile(name, 1, player.discardPile)
                                .SetOptions(true, false)
                                .SetFilter(cards.get(0), GameUtilities::IsSameSeries);
                    }

                    if (info.IsSynergizing) {
                        GameActions.Bottom.Cycle(name, secondaryValue);
                    }
                });
    }
}