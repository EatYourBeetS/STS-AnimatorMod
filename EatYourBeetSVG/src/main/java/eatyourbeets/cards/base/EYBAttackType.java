package eatyourbeets.cards.base;

public enum EYBAttackType
{
    Normal(false, false),
    Elemental(true, true),
    Piercing(true, true),
    Ranged(false, true);

    public final boolean bypassThorns;
    public final boolean bypassBlock;

    EYBAttackType(boolean bypassBlock, boolean bypassThorns)
    {
        this.bypassThorns = bypassThorns;
        this.bypassBlock = bypassBlock;
    }
}
