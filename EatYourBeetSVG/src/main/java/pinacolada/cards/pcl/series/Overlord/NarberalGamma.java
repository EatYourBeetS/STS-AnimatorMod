package pinacolada.cards.pcl.series.Overlord;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Lightning;
import com.megacrit.cardcrawl.powers.ElectroPower;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.powers.common.TemporaryElectroPower;
import pinacolada.stances.DesecrationStance;
import pinacolada.stances.InvocationStance;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

public class NarberalGamma extends PCLCard
{
    public static final PCLCardData DATA = Register(NarberalGamma.class)
            .SetSkill(1, CardRarity.UNCOMMON, eatyourbeets.cards.base.EYBCardTarget.Normal)
            .SetSeriesFromClassPackage();

    public NarberalGamma()
    {
        super(DATA);

        Initialize(0, 1, 1, 2);
        SetUpgrade(0,3,0,0);

        SetAffinity_Dark(1, 0, 1);
        SetAffinity_Light(1,0,1);

        SetEvokeOrbCount(1);
    }

    @Override
    public int GetXValue() {
        return secondaryValue * PCLJUtils.Count(player.hand.group, c -> PCLGameUtilities.GetPCLAffinityLevel(c, PCLAffinity.Dark, true) > 0);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);
        PCLActions.Bottom.ChannelOrb(new Lightning());

        if (!p.hasPower(ElectroPower.POWER_ID))
        {
            PCLActions.Bottom.ApplyPower(p, p, new TemporaryElectroPower(p));
        }

        if ((InvocationStance.IsActive() || DesecrationStance.IsActive()) && info.TryActivateSemiLimited()) {
            PCLActions.Bottom.ApplyElectrified(TargetHelper.Normal(m), GetXValue());
        }
    }
}