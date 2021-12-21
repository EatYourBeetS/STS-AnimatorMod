package pinacolada.powers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.HealthBarRenderPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.ColoredString;
import pinacolada.actions.damage.LoseHP;
import pinacolada.powers.common.BurningPower;
import pinacolada.powers.special.ElementalExposurePower;
import pinacolada.ui.combat.CombatHelper;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

import java.util.HashMap;
import java.util.UUID;

public abstract class PCLTriggerablePower extends PCLPower implements HealthBarRenderPower
{
    public enum Type {
        Effect,
        PassiveDamage
    }

    protected static UUID battleID;
    protected static final HashMap<String, Integer> EFFECT_BONUSES = new HashMap<>();
    protected static final HashMap<String, Integer> PASSIVE_DAMAGE_BONUSES = new HashMap<>();
    protected final int defaultMultiplier;
    protected final int maxMultiplier;

    protected static void CheckForNewBattle() {
        if (CombatStats.BattleID != battleID)
        {
            battleID = CombatStats.BattleID;
            EFFECT_BONUSES.clear();
            PASSIVE_DAMAGE_BONUSES.clear();
        }
    }

    public static void AddEffectBonus(String powerID, int multiplier)
    {
        CheckForNewBattle();
        multiplier = PCLCombatStats.OnGainTriggerablePowerBonus(powerID, Type.Effect, multiplier);

        if (multiplier > 0)
        {
            EFFECT_BONUSES.merge(powerID, multiplier, Integer::sum);
            if (BurningPower.POWER_ID.equals(powerID) || eatyourbeets.powers.common.BurningPower.POWER_ID.equals(powerID)) {
                eatyourbeets.powers.common.BurningPower.AddPlayerAttackBonus(multiplier);
            }

            PCLGameUtilities.UpdatePowerDescriptions();
        }
    }

    public static void AddPlayerDamageBonus(String powerID, int multiplier)
    {
        CheckForNewBattle();
        multiplier = PCLCombatStats.OnGainTriggerablePowerBonus(powerID, Type.PassiveDamage, multiplier);

        if (multiplier > 0)
        {
            PASSIVE_DAMAGE_BONUSES.merge(powerID, multiplier, Integer::sum);

            PCLGameUtilities.UpdatePowerDescriptions();
        }
    }

    public PCLTriggerablePower(AbstractCreature owner, AbstractCreature source, String id, int defaultMultiplier)
    {
        this(owner, source, id, defaultMultiplier, 9999);
    }

    public PCLTriggerablePower(AbstractCreature owner, AbstractCreature source, String id, int defaultMultiplier, int maxMultiplier)
    {
        super(owner, id);

        this.source = source;
        this.defaultMultiplier = defaultMultiplier;
        this.maxMultiplier = maxMultiplier;
        Initialize(amount, PowerType.DEBUFF, true);
    }

    @Override
    public void onInitialApplication()
    {
        super.onInitialApplication();

        // Refresh multiplier
        AddEffectBonus(ID, 0);
        AddPlayerDamageBonus(ID, 0);
    }

    @Override
    public void updateDescription()
    {
        this.description = FormatDescription(0, GetPassiveDamage(), GetEffectMultiplier());
    }

    @Override
    protected ColoredString GetSecondaryAmount(Color c)
    {
        return new ColoredString((int) GetEffectMultiplier(), Color.RED, c.a);
    }


    @Override
    public int getHealthBarAmount()
    {
        return CombatHelper.GetHealthBarAmount(owner, GetPassiveDamage(), false, true);
    }

    @Override
    public void atStartOfTurn()
    {
        this.flashWithoutSound();

        PCLActions.Bottom.Add(Trigger());
        ReducePower(1);
    }

    public LoseHP Trigger() {
        return new LoseHP(owner, source, GetPassiveDamage(), GetAttackEffect()).CanKill(owner == null || !owner.isPlayer);
    }

    public int GetPassiveDamage()
    {
        return MathUtils.ceil((amount / 2f) * GetDamageMultiplier());
    }

    public float GetDamageMultiplier()
    {
        return ((PCLGameUtilities.IsPlayer(owner)) ? 100 : (100 + PASSIVE_DAMAGE_BONUSES.getOrDefault(ID, 0))) / 100f * GetElementalExposure();
    }

    public float GetEffectMultiplier()
    {
        return (PCLGameUtilities.IsPlayer(owner)) ? defaultMultiplier : Math.min(maxMultiplier, defaultMultiplier + EFFECT_BONUSES.getOrDefault(ID, 0)) * GetElementalExposure();
    }

    protected float GetElementalExposure() {
        return ElementalExposurePower.CalculatePercentage(PCLGameUtilities.GetPowerAmount(owner, ElementalExposurePower.POWER_ID));
    }

    public abstract AbstractGameAction.AttackEffect GetAttackEffect();
}
