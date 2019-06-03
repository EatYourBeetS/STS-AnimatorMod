package eatyourbeets.monsters.UnnamedReign;

import eatyourbeets.monsters.AnimatorMonster;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterData_Shape;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterElement;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterShape;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterTier;

public abstract class UnnamedShape extends AnimatorMonster
{
    protected MonsterData_Shape data;

    public static String CreateFullID(MonsterShape shape, MonsterElement element, MonsterTier tier)
    {
        return "Animator_" + element + "_" + shape + "_" +  tier.GetId();
    }

    public static String GetResourcePath(MonsterShape shape, MonsterElement element, MonsterTier tier)
    {
        return "images/monsters/Animator_" + shape + "/" + element + "_" + tier.GetId();
    }

    public UnnamedShape(MonsterShape shape, MonsterElement element, MonsterTier tier, float x, float y)
    {
        super(new MonsterData_Shape(shape, element, tier), tier == MonsterTier.Ultimate ? EnemyType.ELITE : EnemyType.NORMAL, x, y);

        data = (MonsterData_Shape) super.data;
        data.SetIdleAnimation(this, 0.5f);
    }
}
