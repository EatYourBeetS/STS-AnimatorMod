package pinacolada.powers.common;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import pinacolada.interfaces.subscribers.OnOrbApplyFocusSubscriber;
import pinacolada.powers.PCLCombatStats;
import pinacolada.powers.PCLPower;
import pinacolada.utilities.PCLGameUtilities;

public class ImpairedPower extends PCLPower implements OnOrbApplyFocusSubscriber
{
    public static final String POWER_ID = CreateFullID(ImpairedPower.class);
    public static final int ORB_MULTIPLIER = 50;
    public boolean justApplied;

    @Override
    public void onInitialApplication()
    {
        super.onInitialApplication();

        PCLCombatStats.onOrbApplyFocus.Subscribe(this);
    }

    @Override
    public void onRemove()
    {
        super.onRemove();

        PCLCombatStats.onOrbApplyFocus.Unsubscribe(this);
    }

    public static int GetOrbMultiplier()
    {
        return (ORB_MULTIPLIER + PCLCombatStats.GetPlayerEffectBonus(POWER_ID));
    }

    public ImpairedPower(AbstractCreature owner, int amount) {
        this(owner, amount, false);
    }

    public ImpairedPower(AbstractCreature owner, int amount, boolean isSourceMonster)
    {
        super(owner, POWER_ID);
        justApplied = isSourceMonster;

        Initialize(amount, PowerType.DEBUFF, true);
    }

    @Override
    public void atStartOfTurnPostDraw()
    {
        super.atStartOfTurnPostDraw();
        if (justApplied) {
            justApplied = false;
        }
        else {
            ReducePower(1);
        }

    }

    @Override
    public void updateDescription() {
        this.description = FormatDescription(0,GetOrbMultiplier(),amount,amount == 1 ? powerStrings.DESCRIPTIONS[1] : powerStrings.DESCRIPTIONS[2]);
    }

    @Override
    public void OnApplyFocus(AbstractOrb orb) {
        if (PCLGameUtilities.CanOrbApplyFocus(orb)) {
            orb.passiveAmount *= Math.max(0,GetOrbMultiplier() / 100f);
            if (PCLGameUtilities.CanOrbApplyFocusToEvoke(orb)) {
                orb.evokeAmount *= Math.max(0,GetOrbMultiplier() / 100f);
            }
        }
    }
}