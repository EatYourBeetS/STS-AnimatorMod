package eatyourbeets.cards.base;

public final class Synergy
{
    public final String Name;
    public final int ID;

    public Synergy(int id, String name)
    {
        this.ID = id;
        this.Name = name;
    }

    public boolean Equals(Synergy other)
    {
        return other != null && ID == other.ID;
    }
}
