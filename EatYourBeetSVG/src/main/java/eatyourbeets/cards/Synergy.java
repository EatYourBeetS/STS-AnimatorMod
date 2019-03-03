package eatyourbeets.cards;

public final class Synergy
{
    public final int ID;
    public final String NAME;

    public Synergy(int id, String name)
    {
        ID = id;
        NAME = name;
    }

    public boolean Equals(Synergy other)
    {
        return other != null && ID == other.ID;
    }
}
