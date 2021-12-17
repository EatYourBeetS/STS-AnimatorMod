package pinacolada.cards.pcl.series.GenshinImpact;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.actions.orbs.EarthOrbEvokeAction;
import pinacolada.actions.orbs.EarthOrbPassiveAction;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.orbs.pcl.Earth;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

public class Ningguang extends PCLCard
{
    public static final PCLCardData DATA = Register(Ningguang.class).SetSkill(1, CardRarity.UNCOMMON, eatyourbeets.cards.base.EYBCardTarget.Self).SetSeriesFromClassPackage(true);

    public Ningguang()
    {
        super(DATA);

        Initialize(0, 2, 5, 4);
        SetUpgrade(0, 1, -1, 0);
        SetAffinity_Blue(1, 0, 2);
        SetAffinity_Orange(1, 0, 0);

        SetAffinityRequirement(PCLAffinity.Orange, 5);
        SetAffinityRequirement(PCLAffinity.Blue, 5);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);
        final Earth next = PCLJUtils.SafeCast(PCLGameUtilities.GetFirstOrb(Earth.ORB_ID), Earth.class);
        if (next != null && next.projectilesCount > magicNumber) {
            PCLActions.Top.Add(new EarthOrbPassiveAction(next, -magicNumber));
            PCLActions.Bottom.Add(new EarthOrbEvokeAction(next, magicNumber));
            PCLActions.Delayed.Callback(() -> {
               next.IncreaseBaseEvokeAmount(1);
               next.evokeAmount = next.GetBaseEvokeAmount();
            });
        }
        else {
            PCLActions.Bottom.ChannelOrb(new Earth());
        }

        if (info.CanActivateSemiLimited && (CheckAffinity(PCLAffinity.Orange) || CheckAffinity(PCLAffinity.Blue)) && info.TryActivateSemiLimited()) {
            PCLActions.Bottom.TryChooseSpendAffinity(this, PCLAffinity.Blue, PCLAffinity.Orange).AddConditionalCallback(() -> {
                PCLActions.Bottom.GainResistance(secondaryValue, true);
            });
        }
    }
}

