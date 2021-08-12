package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.interfaces.subscribers.OnOrbApplyFocusSubscriber;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.CombatStats;

public abstract class Amplification_AbstractPower extends AnimatorPower implements OnOrbApplyFocusSubscriber
{
    public final String orbID;
    public final Affinity affinity;

    public Amplification_AbstractPower(AbstractCreature owner, String powerID, String orbID, Affinity affinity, int scaling)
    {
        super(owner, powerID);

        this.orbID = orbID;
        this.affinity = affinity;

        Initialize(scaling);
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
        FormatDescription(0, amount, powerName, GetScaledIncrease());
    }

    @Override
    public void OnApplyFocus(AbstractOrb orb)
    {
        if (orb != null && orb.ID.equals(orbID)) {
            orb.passiveAmount += GetScaledIncrease();
        }
    }

    public float GetScaledIncrease() {
        return CombatStats.Affinities.GetPowerAmount(affinity) * amount * 0.5f;
    }
}