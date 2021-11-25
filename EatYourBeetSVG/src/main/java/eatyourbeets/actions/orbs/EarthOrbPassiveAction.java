package eatyourbeets.actions.orbs;

import com.evacipated.cardcrawl.mod.stslib.actions.defect.EvokeSpecificOrbAction;
import eatyourbeets.actions.EYBAction;
import eatyourbeets.effects.SFX;
import eatyourbeets.orbs.animator.Earth;
import eatyourbeets.utilities.GameActions;

public class EarthOrbPassiveAction extends EYBAction
{
    private final Earth orb;

    public EarthOrbPassiveAction(Earth orb, int amount)
    {
        super(ActionType.DEBUFF);

        this.orb = orb;

        Initialize(amount);
    }

    @Override
    protected void FirstUpdate()
    {
        if (orb.projectiles.size() < orb.projectilesCount)
        {
            orb.projectiles.clear();
            orb.AddProjectiles(orb.projectilesCount + amount);
        }
        else if (amount < 0)
        {
            orb.RemoveProjectiles(-amount);
        }
        else
        {
            orb.AddProjectiles(amount);
        }

        if (orb.projectilesCount <= 0) {
            GameActions.Top.Add(new EvokeSpecificOrbAction(orb));
        }
        else if (orb.projectilesCount != orb.projectiles.size())
        {
            SFX.Play(SFX.ANIMATOR_ORB_EARTH_CHANNEL, 0.8f, 1.2f, 0.6f);
            orb.projectilesCount = orb.projectiles.size();
        }

        Complete();
    }
}