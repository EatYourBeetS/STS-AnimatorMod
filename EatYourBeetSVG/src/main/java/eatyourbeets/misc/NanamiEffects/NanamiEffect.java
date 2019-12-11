package eatyourbeets.misc.NanamiEffects;

import eatyourbeets.cards.animator.series.Katanagatari.Nanami;

public abstract class NanamiEffect
{
    protected static final int DAMAGE = 2;
    protected static final int BLOCK = 3;
    protected static final int STRENGTH = 4;
    protected static final int WEAK = 5;
    protected static final int VULNERABLE = 6;
    protected static final int STUN = 7;
    protected static final int RANDOM_ORB = 8;
    protected static final int POISON = 11;
    protected static final int ENERGY = 12;
    protected static final int TEMP_HP = 13;
    protected static final int DRAW = 14;

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
