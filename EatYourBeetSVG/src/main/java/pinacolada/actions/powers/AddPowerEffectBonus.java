 package pinacolada.actions.powers;

 import com.megacrit.cardcrawl.core.Settings;
 import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
 import com.megacrit.cardcrawl.powers.AbstractPower;
 import eatyourbeets.actions.EYBActionWithCallback;
 import pinacolada.powers.PCLCombatStats;

public class AddPowerEffectBonus extends EYBActionWithCallback<AbstractPower>
{
    private String powerID;
    private final PCLCombatStats.Type effectType;

    public AddPowerEffectBonus(String powerID, PCLCombatStats.Type effectType, int amount)
    {
        super(ActionType.POWER, Settings.FAST_MODE ? Settings.ACTION_DUR_XFAST : Settings.ACTION_DUR_FAST);

        this.actionType = ActionType.POWER;
        this.effectType = effectType;
        this.powerID = powerID;

        Initialize(amount);
    }

    public AddPowerEffectBonus(AbstractPower power, PCLCombatStats.Type effectType, int amount)
    {
        super(ActionType.POWER, Settings.FAST_MODE ? Settings.ACTION_DUR_XFAST : Settings.ACTION_DUR_FAST);

        if (power != null) {
            this.powerID = power.ID;
        }
        this.effectType = effectType;

        Initialize(amount);
    }

    @Override
    protected void FirstUpdate()
    {
        if (powerID != null)
        {
            PCLCombatStats.AddBonus(powerID, effectType, amount);
            AbstractDungeon.onModifyPower();
        }
    }
}
