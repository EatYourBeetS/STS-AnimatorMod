package eatyourbeets.monsters.UnnamedReign.Wisp;

import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import eatyourbeets.monsters.UnnamedReign.*;

public abstract class Wisp extends UnnamedMonster
{
    public Wisp(MonsterElement element, MonsterTier tier, float x, float y)
    {
        super(new MonsterData(MonsterShape.Wisp, element, tier), x, y);

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
                return new HealingWisp(tier, x, y);

            case Lightning:
                return new LightningWisp(tier, x, y);

            case Dark:
                return new DarkWisp(tier, x, y);

            case Fire:
                return new FireWisp(tier, x, y);

            case Frost:
                return new FrostWisp(tier, x, y);

            default:
                return null;
        }
    }
}
