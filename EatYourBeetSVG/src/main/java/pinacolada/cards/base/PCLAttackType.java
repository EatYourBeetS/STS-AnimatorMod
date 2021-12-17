package pinacolada.cards.base;

public enum PCLAttackType
{
    None(false, false),
    Normal(false, false),
    Brutal(false, false),
    Elemental(false, true),
    Piercing(true, true),
    Ranged(false, true);

    public final boolean bypassThorns;
    public final boolean bypassBlock;

    PCLAttackType(boolean bypassBlock, boolean bypassThorns)
    {
        this.bypassThorns = bypassThorns;
        this.bypassBlock = bypassBlock;
    }
}
