package eatyourbeets.powers.common;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.powers.CommonPower;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JUtils;

import java.util.HashMap;
import java.util.StringJoiner;

public class DelayedBuffPower extends CommonPower
{
    public static final String POWER_ID = CreateFullID(DelayedBuffPower.class);

    protected final HashMap<PowerHelper, Integer> powers;

    public DelayedBuffPower(AbstractCreature owner, HashMap<PowerHelper, Integer> powers)
    {
        super(owner, POWER_ID);

        this.powers = new HashMap<>(powers);

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

        ModifyPowers(powers);
    }

    @Override
    protected void OnSamePowerApplied(AbstractPower power)
    {
        super.OnSamePowerApplied(power);

        ModifyPowers(((DelayedBuffPower)power).powers);
    }

    @Override
    public void atStartOfTurnPostDraw()
    {
        super.atStartOfTurnPostDraw();

        GameActions.Bottom.Callback(() ->
        {
            RemovePower();
            for (PowerHelper power : powers.keySet())
            {
                final int amount = powers.get(power);
                if (amount != 0)
                {
                    GameActions.Bottom.StackPower(owner, power.Create(owner, source, amount));
                }
            }
        });
    }

    @Override
    public AbstractPower makeCopy()
    {
        return new DelayedBuffPower(owner, powers);
    }

    protected void ModifyPowers(HashMap<PowerHelper, Integer> addPowers)
    {
        for (PowerHelper key : addPowers.keySet())
        {
            this.powers.merge(key, addPowers.get(key), Integer::sum);
        }
    }
}
