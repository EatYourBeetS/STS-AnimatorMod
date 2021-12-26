package pinacolada.powers.affinity;

import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.interfaces.subscribers.OnOrbApplyFocusSubscriber;
import pinacolada.powers.PCLCombatStats;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class WisdomPower extends AbstractPCLAffinityPower implements OnOrbApplyFocusSubscriber
{
    public static final String POWER_ID = CreateFullID(WisdomPower.class);
    public static final PCLAffinity AFFINITY_TYPE = PCLAffinity.Blue;

    public WisdomPower()
    {
        super(AFFINITY_TYPE, POWER_ID);
    }

    @Override
    public void OnUse(AbstractMonster m, int cost)
    {
        super.OnUse(m, cost);
        PCLCombatStats.onOrbApplyFocus.Subscribe(this);
        PCLActions.Bottom.GainFocus(1, true);
    }

    @Override
    public void atStartOfTurn()
    {
        super.atStartOfTurn();
        PCLCombatStats.onOrbApplyFocus.Unsubscribe(this);
    }

    @Override
    public void OnApplyFocus(AbstractOrb orb) {
        orb.passiveAmount *= GetEffectiveMultiplier();
        if (PCLGameUtilities.CanOrbApplyFocusToEvoke(orb)) {
            orb.evokeAmount *= GetEffectiveMultiplier();
        }
    }
}