package eatyourbeets.monsters.UnnamedReign.Shapes;

import eatyourbeets.monsters.EYBMonster;
import eatyourbeets.resources.GR;

public abstract class UnnamedShape extends EYBMonster
{
    public final MonsterData_Shape data;

    public static String CreateFullID(MonsterShape shape, MonsterElement element, MonsterTier tier)
    {
        return GR.Animator.CreateID(element + "_" + shape + "_" + tier.GetId());
    }

    public UnnamedShape(MonsterShape shape, MonsterElement element, MonsterTier tier, float x, float y)
    {
        super(new MonsterData_Shape(shape, element, tier), tier == MonsterTier.Ultimate ? EnemyType.ELITE : EnemyType.NORMAL, x, y);

        data = (MonsterData_Shape) super.data;
        data.SetIdleAnimation(this, 1f);
    }
}
