package pinacolada.powers.replacement;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.WeakPower;
import pinacolada.powers.PCLCombatStats;
import pinacolada.utilities.PCLGameUtilities;

public class PCLWeakPower extends WeakPower implements CloneablePowerInterface
{
    private boolean justApplied = false;
    public static final int ATTACK_MULTIPLIER = 25;

    public static float CalculateDamage(float damage, float multiplier)
    {
        return Math.max(0, damage - Math.max(0, damage * (multiplier / 100f)));
    }

    public PCLWeakPower(AbstractCreature owner, int amount) {
        this(owner, amount, false);
    }

    public PCLWeakPower(AbstractCreature owner, int amount, boolean isSourceMonster)
    {
        super(owner,amount,isSourceMonster);
        if (isSourceMonster) {
            this.justApplied = true;
        }
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + GetMultiplier() + DESCRIPTIONS[1] + this.amount + (this.amount == 1 ? DESCRIPTIONS[2] : DESCRIPTIONS[3]);
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type)
    {
        return type == DamageInfo.DamageType.NORMAL ? CalculateDamage(damage, GetMultiplier()) : damage;
    }

    @Override
    public AbstractPower makeCopy()
    {
        return new PCLWeakPower(owner, amount, justApplied);
    }

    public int GetMultiplier()
    {
        return (PCLGameUtilities.IsPlayer(owner)) ? (ATTACK_MULTIPLIER + PCLCombatStats.GetPlayerEffectBonus(ID)) : (ATTACK_MULTIPLIER + PCLCombatStats.GetEffectBonus(ID));
    }
}
