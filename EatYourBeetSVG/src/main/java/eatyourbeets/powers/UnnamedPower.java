package eatyourbeets.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.util.StringJoiner;

public abstract class UnnamedPower extends BasePower
{
    public static String CreateFullID(String id)
    {
        return "unnamed:" + id;
    }

    public UnnamedPower(AbstractCreature owner, String id)
    {
        super(owner, id);
    }
}
