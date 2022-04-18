package eatyourbeets.monsters.UnnamedReign.Shapes;

import eatyourbeets.utilities.RandomizedList;

public enum MonsterElement
{
    Ultimate,
    Healing,
    Lightning,
    Dark,
    Fire,
    Frost;

    public static RandomizedList<MonsterElement> GetRandomizedList(MonsterElement... exceptions)
    {
        final RandomizedList<MonsterElement> result = GetRandomizedList();
        for (MonsterElement element : exceptions)
        {
            result.GetInnerList().remove(element);
        }

        return result;
    }

    public static RandomizedList<MonsterElement> GetRandomizedList()
    {
        final RandomizedList<MonsterElement> result = new RandomizedList<>();
        result.Add(MonsterElement.Dark);
        result.Add(MonsterElement.Fire);
        result.Add(MonsterElement.Frost);
        result.Add(MonsterElement.Healing);
        result.Add(MonsterElement.Lightning);

        return result;
    }
}
