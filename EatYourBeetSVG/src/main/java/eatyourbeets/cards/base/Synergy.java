package eatyourbeets.cards.base;

public final class Synergy
{
    public final String Name;
    public final String LocalizedName;
    public final int ID;

    public Synergy(int id, String name, String localizedName)
    {
        ID = id;
        Name = name;
        LocalizedName = localizedName != null ? localizedName : name;
    }

    public boolean Equals(Synergy other)
    {
        return other != null && ID == other.ID;
    }
}
