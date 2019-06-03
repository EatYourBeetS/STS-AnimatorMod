package eatyourbeets.monsters.UnnamedReign.Shapes;

import eatyourbeets.misc.RandomizedList;

public enum MonsterElement
{
    Ultimate,
    Healing,
    Lightning,
    Dark,
    Fire,
    Frost;

    public static RandomizedList<MonsterElement> GetRandomizedList(MonsterElement exception)
    {
        RandomizedList<MonsterElement> result = GetRandomizedList();
        result.GetInnerList().remove(exception);
        return result;
    }

    public static RandomizedList<MonsterElement> GetRandomizedList()
    {
        RandomizedList<MonsterElement> result = new RandomizedList<>();
        result.Add(MonsterElement.Dark);
        result.Add(MonsterElement.Fire);
        result.Add(MonsterElement.Frost);
        result.Add(MonsterElement.Healing);
        result.Add(MonsterElement.Lightning);
        return result;
    }
}
