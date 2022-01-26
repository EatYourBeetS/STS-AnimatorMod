package pinacolada.cards.pcl.series.GATE;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.*;
import pinacolada.orbs.pcl.Air;
import pinacolada.powers.PCLPowerHelper;
import pinacolada.utilities.PCLActions;

public class CatoElAltestan extends PCLCard
{
    public static final PCLCardData DATA = Register(CatoElAltestan.class)
            .SetSkill(1, CardRarity.UNCOMMON, PCLCardTarget.Normal)
            .SetSeriesFromClassPackage();

    public CatoElAltestan()
    {
        super(DATA);

        Initialize(0, 1, 3, 3);
        SetUpgrade(0, 0, 1, 1);

        SetAffinity_Blue(1, 0, 2);

        SetExhaust(true);

        SetAffinityRequirement(PCLAffinity.Red, 2);
        SetAffinityRequirement(PCLAffinity.Blue, 2);
        SetAffinityRequirement(PCLAffinity.Green, 3);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);

        PCLActions.Bottom.StackPower(TargetHelper.Player(), PCLPowerHelper.Sorcery, magicNumber);

        PCLActions.Bottom.TryChooseSpendAffinity(this, PCLAffinity.Red, PCLAffinity.Blue, PCLAffinity.Green).AddConditionalCallback(afChoices -> {
            for (AffinityChoice af : afChoices) {
                switch (af.Affinity) {
                    case Red:
                        PCLActions.Bottom.ApplyBurning(TargetHelper.Normal(m), secondaryValue);
                        break;
                    case Blue:
                        PCLActions.Bottom.ApplyFreezing(TargetHelper.Normal(m), secondaryValue);
                        break;
                    case Green:
                        PCLActions.Bottom.ChannelOrb(new Air());
                        break;
                }
            }
        });
    }
}