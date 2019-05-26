package eatyourbeets.monsters.UnnamedReign;

public enum MonsterTier
{
    Small(0),
    Normal(1),
    Advanced(2),
    Ultimate(3);

    private final int id;

    MonsterTier(int id)
    {
        this.id = id;
    }

    public int GetId()
    {
        return id;
    }
}