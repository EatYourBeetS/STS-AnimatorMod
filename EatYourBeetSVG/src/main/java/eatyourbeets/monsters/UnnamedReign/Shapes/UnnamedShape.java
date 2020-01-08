package eatyourbeets.monsters.UnnamedReign.Shapes;

import eatyourbeets.monsters.AnimatorMonster;
import eatyourbeets.resources.animator.AnimatorResources;

public abstract class UnnamedShape extends AnimatorMonster
{
    protected final MonsterData_Shape data;

    public static String CreateFullID(MonsterShape shape, MonsterElement element, MonsterTier tier)
    {
        return AnimatorResources.CreateID(element + "_" + shape + "_" +  tier.GetId());
    }

    public UnnamedShape(MonsterShape shape, MonsterElement element, MonsterTier tier, float x, float y)
    {
        super(new MonsterData_Shape(shape, element, tier), tier == MonsterTier.Ultimate ? EnemyType.ELITE : EnemyType.NORMAL, x, y);

        data = (MonsterData_Shape) super.data;
        data.SetIdleAnimation(this, 0.5f);
    }
}
