package pinacolada.cards.pcl.series.GATE;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.pcl.special.ThrowingKnife;
import pinacolada.utilities.PCLActions;

public class TukaLunaMarceau extends PCLCard
{
    public static final PCLCardData DATA = Register(TukaLunaMarceau.class)
            .SetSkill(0, CardRarity.COMMON, eatyourbeets.cards.base.EYBCardTarget.None)
            .SetSeriesFromClassPackage().PostInitialize(data ->
            {
                for (ThrowingKnife knife : ThrowingKnife.GetAllCards())
                {
                    data.AddPreview(knife, true);
                }
            });

    public TukaLunaMarceau()
    {
        super(DATA);

        Initialize(0, 2, 3);
        SetUpgrade(0, 3);

        SetAffinity_Green(1, 0, 1);

        SetAffinityRequirement(PCLAffinity.Light, 2);
        SetAffinityRequirement(PCLAffinity.Green, 2);
    }


    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (p.currentBlock <= 0)
        {
            PCLActions.Bottom.Draw(1);
        }

        PCLActions.Bottom.GainBlock(block);

        if (TrySpendAffinity(PCLAffinity.Light, PCLAffinity.Green))
        {
            PCLActions.Bottom.CreateThrowingKnives(1);
        }
    }
}