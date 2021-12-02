package eatyourbeets.powers;

import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.HealthBarRenderPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.actions.damage.LoseHP;
import eatyourbeets.powers.animator.ElementalExposurePower;
import eatyourbeets.ui.animator.combat.CombatHelper;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

import java.util.HashMap;
import java.util.UUID;

public abstract class CommonTriggerablePower extends CommonPower implements HealthBarRenderPower
{
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

        if (multiplier > 0)
        {
            EFFECT_BONUSES.merge(powerID, multiplier, Integer::sum);

            GameUtilities.UpdatePowerDescriptions();
        }
    }

    public static void AddPlayerDamageBonus(String powerID, int multiplier)
    {
        CheckForNewBattle();

        if (multiplier > 0)
        {
            PASSIVE_DAMAGE_BONUSES.merge(powerID, multiplier, Integer::sum);

            GameUtilities.UpdatePowerDescriptions();
        }
    }

    public CommonTriggerablePower(AbstractCreature owner, AbstractCreature source, String id, int defaultMultiplier)
    {
        this(owner, source, id, defaultMultiplier, 9999);
    }

    public CommonTriggerablePower(AbstractCreature owner, AbstractCreature source, String id, int defaultMultiplier, int maxMultiplier)
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
    public int getHealthBarAmount()
    {
        return CombatHelper.GetHealthBarAmount(owner, GetPassiveDamage(), false, true);
    }

    @Override
    public void atStartOfTurn()
    {
        this.flashWithoutSound();

        GameActions.Bottom.Add(Trigger());
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
        return ((GameUtilities.IsPlayer(owner)) ? 100 : (100 + PASSIVE_DAMAGE_BONUSES.getOrDefault(ID, 0))) / 100f * GetElementalExposure();
    }

    public float GetEffectMultiplier()
    {
        return (GameUtilities.IsPlayer(owner)) ? defaultMultiplier : Math.min(maxMultiplier, defaultMultiplier + EFFECT_BONUSES.getOrDefault(ID, 0)) * GetElementalExposure();
    }

    protected float GetElementalExposure() {
        return ElementalExposurePower.CalculatePercentage(GameUtilities.GetPowerAmount(owner, ElementalExposurePower.POWER_ID));
    }

    public abstract AbstractGameAction.AttackEffect GetAttackEffect();
}
