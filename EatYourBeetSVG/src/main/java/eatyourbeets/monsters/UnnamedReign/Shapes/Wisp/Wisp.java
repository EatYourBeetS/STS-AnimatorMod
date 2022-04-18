package eatyourbeets.monsters.UnnamedReign.Shapes.Wisp;

import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterElement;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterShape;
import eatyourbeets.monsters.UnnamedReign.Shapes.MonsterTier;
import eatyourbeets.monsters.UnnamedReign.Shapes.UnnamedShape;

public abstract class Wisp extends UnnamedShape
{
    public Wisp(MonsterElement element, MonsterTier tier, float x, float y)
    {
        super(MonsterShape.Wisp, element, tier, x, y);

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
                return new HealingWisp(tier, x, y);

            case Lightning:
                return new LightningWisp(tier, x, y);

            case Dark:
                return new DarkWisp(tier, x, y);

            case Fire:
                return new FireWisp(tier, x, y);

            case Frost:
                return new FrostWisp(tier, x, y);

            case Ultimate:
                return new UltimateWisp(x, y);

            default:
                throw new EnumConstantNotPresentException(MonsterElement.class, "element");
        }
    }
}
