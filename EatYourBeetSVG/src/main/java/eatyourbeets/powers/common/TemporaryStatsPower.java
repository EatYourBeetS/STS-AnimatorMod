package eatyourbeets.powers.common;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.interfaces.subscribers.OnModifyDebuffSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.CommonPower;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;
import eatyourbeets.utilities.Mathf;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.StringJoiner;

public class TemporaryStatsPower extends CommonPower implements OnModifyDebuffSubscriber
{
    public static final String POWER_ID = CreateFullID(TemporaryStatsPower.class);

    protected final LinkedHashMap<PowerHelper, Integer> powers;

    public TemporaryStatsPower(AbstractCreature owner, HashMap<PowerHelper, Integer> powers)
    {
        super(owner, POWER_ID);

        this.powers = new LinkedHashMap<>(powers);

        Initialize(-1);
    }

    @Override
    public void updateDescription()
    {
        final StringJoiner sj = new StringJoiner(" NL ");
        for (PowerHelper key : powers.keySet())
        {
            final int amount = powers.get(key);
            sj.add(((amount > 0 ? "#g+" : "#r-") + Math.abs(amount)) + " " + JUtils.ModifyString(key.Tooltip.title, w -> "#y" + w));
        }

        this.description = FormatDescription(0) + " NL " + sj.toString();
    }

    @Override
    public void onInitialApplication()
    {
        super.onInitialApplication();

        if (powers.size() > 0)
        {
            GameActions.Top.Callback(() ->
            {
                ModifyPowers(false);
                CombatStats.onModifyDebuff.Subscribe(this);
            });
        }
        else
        {
            RemovePower(GameActions.Top);
        }
    }

    @Override
    public void onRemove()
    {
        super.onRemove();

        GameActions.Top.Callback(() ->
        {
            ModifyPowers(true);
            CombatStats.onModifyDebuff.Unsubscribe(this);
        });
    }

    @Override
    protected void OnSamePowerApplied(AbstractPower power)
    {
        super.OnSamePowerApplied(power);

        GameActions.Top.Callback((TemporaryStatsPower) power, (p, __) -> ModifyPowers(p.powers));
    }

    @Override
    public void atEndOfTurn(boolean isPlayer)
    {
        RemovePower(GameActions.Delayed);
    }

    @Override
    public AbstractPower makeCopy()
    {
        return new TemporaryStatsPower(owner, powers);
    }

    @Override
    public void OnModifyDebuff(AbstractPower power, int initialAmount, int newAmount)
    {
        for (PowerHelper key : powers.keySet())
        {
            if (key.ID.equals(power.ID))
            {
                int difference = newAmount - initialAmount;
                int amount = powers.get(key);
                if (amount >= 0)
                {
                    amount = Mathf.Max(amount - difference, 0);
                }
                else
                {
                    amount = Mathf.Min(amount + difference, 0);
                }

                if (amount != 0)
                {
                    powers.put(key, amount);
                }
                else
                {
                    powers.remove(key);
                }

                break;
            }
        }

        if (powers.isEmpty())
        {
            RemovePower(GameActions.Top);
        }
    }

    protected void ModifyPowers(boolean remove)
    {
        final int sign = remove ? -1 : +1;
        for (PowerHelper key : powers.keySet())
        {
            final AbstractPower power = ModifyPower(key, powers.get(key) * sign);
            if (power != null && !remove)
            {
                power.playApplyPowerSfx();
            }
        }

        Collections.sort(owner.powers);
        AbstractDungeon.onModifyPower();
    }

    protected void ModifyPowers(HashMap<PowerHelper, Integer> addPowers)
    {
        for (PowerHelper key : addPowers.keySet())
        {
            final int amount = addPowers.get(key);

            this.powers.merge(key, amount, Integer::sum);

            final AbstractPower power = ModifyPower(key, amount);
            if (power != null && amount > 0)
            {
                power.playApplyPowerSfx();
            }
        }

        Collections.sort(owner.powers);
        AbstractDungeon.onModifyPower();
    }

    protected AbstractPower ModifyPower(PowerHelper power, int amount)
    {
        return GameUtilities.ApplyPowerInstantly(owner, power.Create(owner, owner, amount), amount);
    }
}
