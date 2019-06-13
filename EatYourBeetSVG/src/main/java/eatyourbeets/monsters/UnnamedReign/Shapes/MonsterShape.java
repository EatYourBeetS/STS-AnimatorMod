package eatyourbeets.monsters.UnnamedReign.Shapes;

import eatyourbeets.utilities.RandomizedList;

public enum MonsterShape
{
    Cube,
    Crystal,
    Wisp;

    public static RandomizedList<MonsterShape> GetRandomizedList(MonsterShape exception)
    {
        RandomizedList<MonsterShape> result = GetRandomizedList();
        result.GetInnerList().remove(exception);
        return result;
    }

    public static RandomizedList<MonsterShape> GetRandomizedList()
    {
        RandomizedList<MonsterShape> result = new RandomizedList<>();
        result.Add(MonsterShape.Crystal);
        result.Add(MonsterShape.Cube);
        result.Add(MonsterShape.Wisp);
        return result;
    }
}
