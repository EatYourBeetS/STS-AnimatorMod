package pinacolada.cards.pcl.series.MadokaMagica;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.pcl.curse.Curse_GriefSeed;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class MifuyuAsuza extends PCLCard
{
    public static final PCLCardData DATA = Register(MifuyuAsuza.class)
            .SetSkill(-1, CardRarity.RARE, eatyourbeets.cards.base.EYBCardTarget.None)
            .SetMaxCopies(2)
            .SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new Curse_GriefSeed(), false));

    public MifuyuAsuza()
    {
        super(DATA);

        Initialize(0, 0, 0);
        SetUpgrade(0, 0, 1);

        SetAffinity_Blue(2);
        SetAffinity_Light(1, 0, 0);
        SetExhaust(true);

        SetAffinityRequirement(PCLAffinity.General, 9);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        int stacks = PCLGameUtilities.UseXCostEnergy(this);
        PCLActions.Bottom.GainOrbSlots(stacks + magicNumber);
        for (int i = 0; i < stacks + magicNumber; i++) {
            PCLActions.Bottom.MakeCardInHand(new Curse_GriefSeed());
        }

        PCLActions.Bottom.TryChooseSpendAffinity(this).AddConditionalCallback(() -> {
            PCLActions.Bottom.ChannelOrbs(PCLGameUtilities::GetRandomCommonOrb, stacks);
        });
    }
}