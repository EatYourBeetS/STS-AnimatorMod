package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Dark;
import com.megacrit.cardcrawl.orbs.Plasma;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import eatyourbeets.interfaces.subscribers.OnOrbApplyFocusSubscriber;
import eatyourbeets.orbs.animator.Chaos;
import eatyourbeets.orbs.animator.Water;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.common.ImpairedPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

import static eatyourbeets.powers.common.ImpairedPower.GetOrbMultiplier;

public class InvertPower extends AnimatorPower implements OnOrbApplyFocusSubscriber
{
    public static final String POWER_ID = CreateFullID(InvertPower.class);
    public int secondaryAmount;

    public InvertPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);
        this.amount = amount;
        this.isTurnBased = true;
        this.priority = 99;
        updateDescription();
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
    public float atDamageReceive(float damage, DamageInfo.DamageType damageType)
    {
        AbstractPower power = GameUtilities.GetPower(owner, VulnerablePower.POWER_ID);
        if (power != null && damage > 0)
        {
            damage /= (float)Math.pow(power.atDamageReceive(1, damageType), 2);
        }

        return damage;
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType damageType) {
        AbstractPower power = GameUtilities.GetPower(owner, WeakPower.POWER_ID);
        if (power != null && damage > 0)
        {
            damage /= (float)Math.pow(power.atDamageGive(1, damageType), 2);
        }
        return damage;
    }

    @Override
    public float modifyBlock(float blockAmount) {
        AbstractPower power = GameUtilities.GetPower(owner, FrailPower.POWER_ID);
        if (power != null && blockAmount > 0)
        {
            blockAmount /= (float)Math.pow(power.modifyBlock(1), 2);
        }
        return blockAmount;
    }

    @Override
    public void atEndOfRound()
    {
        super.atEndOfRound();

        GameActions.Bottom.ReducePower(this, 1);
    }

    @Override
    public void updateDescription()
    {
        this.description = FormatDescription(0, amount);
        this.enabled = (amount > 0);
    }

    @Override
    public void OnApplyFocus(AbstractOrb orb) {
        AbstractPower power = GameUtilities.GetPower(owner, ImpairedPower.POWER_ID);
        if (power != null)
        {
            if (GetOrbMultiplier() > 0 && !Plasma.ORB_ID.equals(orb.ID) && !Chaos.ORB_ID.equals(orb.ID)) {
                float multiplier = 10000f / (GetOrbMultiplier() * GetOrbMultiplier());
                orb.passiveAmount *= multiplier;
                if (!Dark.ORB_ID.equals(orb.ID) && !Water.ORB_ID.equals(orb.ID)) {
                    orb.evokeAmount *= multiplier;
                }
            }
        }
    }
}