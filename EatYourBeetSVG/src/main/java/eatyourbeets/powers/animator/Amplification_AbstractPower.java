package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.interfaces.subscribers.OnOrbApplyFocusSubscriber;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.CombatStats;

import static eatyourbeets.ui.animator.combat.EYBCardAffinitySystem.SCALING_DIVISION;

public abstract class Amplification_AbstractPower extends AnimatorPower implements OnOrbApplyFocusSubscriber
{
    public final String orbID;
    public final Affinity affinity;
    public final int evokeMultiplier;

    public Amplification_AbstractPower(AbstractCreature owner, String powerID, String orbID, Affinity affinity, int scaling) {
        this(owner,powerID,orbID,affinity,scaling,2);
    }

    public Amplification_AbstractPower(AbstractCreature owner, String powerID, String orbID, Affinity affinity, int scaling, int evokeMultiplier)
    {
        super(owner, powerID);

        this.orbID = orbID;
        this.affinity = affinity;
        this.evokeMultiplier = evokeMultiplier;

        Initialize(scaling);
        refreshOrbs();
    }

    @Override
    public void onInitialApplication()
    {
        super.onInitialApplication();

        CombatStats.onOrbApplyFocus.Subscribe(this);
    }


    @Override
    public void onRemove()
    {
        super.onRemove();

        CombatStats.onOrbApplyFocus.Unsubscribe(this);
    }

    @Override
    public void updateDescription()
    {
        String powerName = CombatStats.Affinities.GetPower(affinity).name;
        description = FormatDescription(0, amount, powerName, this.evokeMultiplier * amount);
    }

    @Override
    public void atStartOfTurn()
    {
        super.atStartOfTurn();

        refreshOrbs();
    }

    @Override
    public void OnApplyFocus(AbstractOrb orb)
    {
        if (orb != null && orb.ID.equals(orbID)) {
            orb.passiveAmount += GetScaledIncrease();
            orb.evokeAmount += this.evokeMultiplier * GetScaledIncrease();
        }
    }

    private float GetScaledIncrease() {
        return CombatStats.Affinities.GetPowerAmount(affinity) * amount / (float)SCALING_DIVISION;
    }

    private void refreshOrbs() {
        for (AbstractOrb orb : player.orbs) {
            if (orb != null && !(orb instanceof EmptyOrbSlot)) {
                orb.applyFocus();
            }
        }
    }
}