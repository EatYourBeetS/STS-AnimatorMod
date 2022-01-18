package pinacolada.cards.pcl.ultrarare;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.RandomizedList;
import pinacolada.cards.base.CardSeries;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCard_UltraRare;
import pinacolada.cards.pcl.special.Traveler_Wish;
import pinacolada.orbs.pcl.Air;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

public class Traveler_Aether extends PCLCard_UltraRare
{
    public static final PCLCardData DATA = Register(Traveler_Aether.class)
            .SetSkill(1, CardRarity.SPECIAL, eatyourbeets.cards.base.EYBCardTarget.None)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.GenshinImpact)
            .PostInitialize(data -> data.AddPreview(new Traveler_Wish(), false));
    public static final int UNIQUE_ORB_THRESHOLD = 3;


    public Traveler_Aether()
    {
        super(DATA);

        Initialize(0, 0, 3, 1);
        SetUpgrade(0, 0, 1, 1);
        SetAffinity_Light(1);
        SetAffinity_Dark(1);
        SetAffinity_Green(1);

        SetEthereal(true);
        SetPurge(true);
        SetProtagonist(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.ChannelOrbs(Air::new, secondaryValue).AddCallback(() -> {
            int orbsInduced = 0;

            RandomizedList<AbstractOrb> orbList = new RandomizedList<>(PCLJUtils.Filter(player.orbs, PCLGameUtilities::IsCommonOrb));
            if (orbList.Size() > 0) {
                for (int i = 0; i < magicNumber; i++) {
                    PCLActions.Bottom.InduceOrb(orbList.Retrieve(rng,false).makeCopy(), true);
                }
            }

            if (CheckSpecialCondition(false) && CombatStats.TryActivateSemiLimited(cardID)) {
                PCLActions.Bottom.MakeCardInDrawPile(new Traveler_Wish());
            }
        });
    }

    @Override
    public boolean CheckSpecialCondition(boolean tryUse){
        return PCLGameUtilities.GetUniqueOrbsCount() >= UNIQUE_ORB_THRESHOLD;
    }
}