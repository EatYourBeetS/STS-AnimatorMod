package pinacolada.powers.affinity;

import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.powers.PCLCombatStats;
import pinacolada.powers.PCLPowerHelper;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InvocationPower extends AbstractPCLAffinityPower
{
    public static final String POWER_ID = CreateFullID(InvocationPower.class);
    public static final PCLAffinity AFFINITY_TYPE = PCLAffinity.Light;
    protected static final HashMap<String, Integer> POWERS = new HashMap<>();
    protected static final HashMap<String, Integer> EFFECT_BONUSES = new HashMap<>();
    protected static final HashMap<String, Integer> PASSIVE_DAMAGE_BONUSES = new HashMap<>();
    protected static UUID battleID;

    protected static boolean CheckForNewBattle() {
        if (CombatStats.BattleID != battleID)
        {
            battleID = CombatStats.BattleID;
            POWERS.clear();
            EFFECT_BONUSES.clear();
            PASSIVE_DAMAGE_BONUSES.clear();
            return true;
        }
        return false;
    }

    public InvocationPower()
    {
        super(AFFINITY_TYPE, POWER_ID);
    }

    @Override
    public void OnUse(AbstractMonster m, int cost)
    {
        super.OnUse(m, cost);

        POWERS.clear();
        EFFECT_BONUSES.clear();
        PASSIVE_DAMAGE_BONUSES.clear();
        if (player.powers != null) {
            for (AbstractPower po : player.powers) {
                if ((PCLGameUtilities.IsCommonBuff(po))) {
                    AddPower(po.ID, (int) (po.amount * GetEffectiveMultiplier()));
                }
            }
        }

        for (Map.Entry<String,Integer> entry : PCLCombatStats.GetAllEffectBonuses()) {
            AddEffectBonus(entry.getKey(), (int) (entry.getValue() * GetEffectiveMultiplier()));
        }

        for (Map.Entry<String,Integer> entry : PCLCombatStats.GetAllPassiveDamageBonuses()) {
            AddPassiveDamageBonus(entry.getKey(), (int) (entry.getValue() * GetEffectiveMultiplier()));
        }
    }

    @Override
    public void atStartOfTurn()
    {
        if (!CheckForNewBattle() && isActive) {
            for (Map.Entry<String,Integer> entry : POWERS.entrySet()) {
                AddPower(entry.getKey(), -entry.getValue());
            }

            for (Map.Entry<String,Integer> entry : EFFECT_BONUSES.entrySet()) {
                AddEffectBonus(entry.getKey(), -entry.getValue());
            }

            for (Map.Entry<String,Integer> entry : PASSIVE_DAMAGE_BONUSES.entrySet()) {
                AddPassiveDamageBonus(entry.getKey(), -entry.getValue());
            }
        }
        super.atStartOfTurn();
    }

    @Override
    protected float GetEffectiveMultiplier() {
        return GetEffectiveIncrease();
    }

    protected void AddPower(String powerID, int amount) {
        CheckForNewBattle();
        PCLPowerHelper ph = PCLPowerHelper.ALL.get(powerID);
        if (ph != null) {
            POWERS.merge(powerID, amount, Integer::sum);
            PCLActions.Bottom.StackPower(TargetHelper.Player(), ph, amount);
        }
    }

    protected void AddEffectBonus(String powerID, int amount) {
        CheckForNewBattle();
        EFFECT_BONUSES.merge(powerID, amount, Integer::sum);
        PCLCombatStats.AddEffectBonus(powerID, amount);
    }

    protected void AddPassiveDamageBonus(String powerID, int amount) {
        CheckForNewBattle();
        PASSIVE_DAMAGE_BONUSES.merge(powerID, amount, Integer::sum);
        PCLCombatStats.AddPassiveDamageBonus(powerID, amount);
    }
}