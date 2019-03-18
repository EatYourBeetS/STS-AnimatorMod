package eatyourbeets.misc.NanamiEffects;

import eatyourbeets.cards.animator.Nanami;

public abstract class NanamiEffect
{
    protected static final int DAMAGE = 0;
    protected static final int BLOCK = 1;
    protected static final int STRENGTH = 2;
    protected static final int WEAK = 3;
    protected static final int VULNERABLE = 4;
    protected static final int STUN = 5;
    protected static final int RANDOM_ORB = 6;
    protected static final int POISON = 9;
    protected static final int ENERGY = 10;
    protected static final int TEMP_HP = 11;
    protected static final int DRAW = 12;

    protected static String Desc(int index, int value)
    {
        return Desc(index, value, false);
    }

    protected static String Desc(int index, int value, boolean newLine)
    {
        if (value > 0)
        {
            return Nanami.DESCRIPTIONS[index].replace("#", String.valueOf(value)) + (newLine ? " NL " : "");
        }
        else
        {
            return "";
        }
    }
}
