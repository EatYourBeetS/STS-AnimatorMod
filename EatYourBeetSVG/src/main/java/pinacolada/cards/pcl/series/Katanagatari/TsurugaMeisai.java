package pinacolada.cards.pcl.series.Katanagatari;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.NextTurnBlockPower;
import eatyourbeets.cards.base.EYBCardTarget;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class TsurugaMeisai extends PCLCard
{
    public static final PCLCardData DATA = Register(TsurugaMeisai.class)
            .SetSkill(1, CardRarity.COMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage();

    public TsurugaMeisai()
    {
        super(DATA);

        Initialize(0, 2, 6);
        SetUpgrade(0, 2, 0);

        SetAffinity_Light(1);
        SetAffinity_Green(1, 0, 1);

        SetExhaust(true);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);
        PCLActions.Bottom.StackPower(new NextTurnBlockPower(p, magicNumber));
        PCLActions.Bottom.SelectFromHand(name, 1, false)
        .SetOptions(true, true, true)
        .SetMessage(GR.PCL.Strings.HandSelection.Copy)
        .SetFilter(c -> PCLGameUtilities.IsLowCost(c) && c.type == CardType.ATTACK)
        .AddCallback(cards ->
        {
            for (AbstractCard c : cards)
            {
                PCLActions.Bottom.MakeCardInDrawPile(PCLGameUtilities.Imitate(c))
                .AddCallback(card ->
                {
                    if (upgraded)
                    {
                        PCLGameUtilities.SetCardTag(card, HASTE, true);
                    }
                });
            }
        });
    }
}