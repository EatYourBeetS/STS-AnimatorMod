package eatyourbeets.monsters.UnnamedReign.Shapes.Crystal;

import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import eatyourbeets.monsters.UnnamedReign.*;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterElement;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterShape;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterTier;

public abstract class Crystal extends UnnamedShape
{
    public Crystal(MonsterElement element, MonsterTier tier, float x, float y)
    {
        super(MonsterShape.Crystal, element, tier, x, y);

        loadAnimation(data.atlasUrl, data.jsonUrl, data.scale);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "Idle", true);
        e.setTimeScale(0.5f);
        e.setTime(e.getEndTime() * MathUtils.random());
    }

    public static UnnamedShape CreateEnemy(MonsterTier tier, MonsterElement element, float x, float y)
    {
        switch (element)
        {
            case Healing:
                return new HealingCrystal(tier, x, y);

            case Lightning:
                return new LightningCrystal(tier, x, y);

            case Dark:
                return new DarkCrystal(tier, x, y);

            case Fire:
                return new FireCrystal(tier, x, y);

            case Frost:
                return new FrostCrystal(tier, x, y);

            default:
                return null;
        }
    }
}
