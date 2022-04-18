package eatyourbeets.resources.unnamed;

public class UnnamedDungeonData// implements CustomSavable<UnnamedDungeonData>
{
    public static UnnamedDungeonData Register(String id)
    {
        //BaseMod.addSaveField(id, data);
        return new UnnamedDungeonData();
    }
}
