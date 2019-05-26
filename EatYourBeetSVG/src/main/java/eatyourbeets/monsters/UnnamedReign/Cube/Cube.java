package eatyourbeets.monsters.UnnamedReign.Cube;

import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import eatyourbeets.monsters.UnnamedReign.*;

public abstract class Cube extends UnnamedMonster
{
    public Cube(MonsterElement element, MonsterTier tier, float x, float y)
    {
        super(new MonsterData(MonsterShape.Cube, element, tier), x, y);

        loadAnimation(data.atlasUrl, data.jsonUrl, data.scale);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "Idle", true);
        e.setTimeScale(0.5f);
        e.setTime(e.getEndTime() * MathUtils.random());
    }

    public static UnnamedMonster CreateEnemy(MonsterTier tier, MonsterElement element, float x, float y)
    {
        switch (element)
        {
            case Healing:
                return new HealingCube(tier, x, y);

            case Lightning:
                return new LightningCube(tier, x, y);

            case Dark:
                return new DarkCube(tier, x, y);

            case Fire:
                return new FireCube(tier, x, y);

            case Frost:
                return new FrostCube(tier, x, y);

                default:
                    return null;
        }
    }
}
