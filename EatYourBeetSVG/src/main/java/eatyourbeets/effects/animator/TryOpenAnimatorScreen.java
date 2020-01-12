package eatyourbeets.effects.animator;

import eatyourbeets.effects.EYBEffect;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.JavaUtilities;

public class TryOpenAnimatorScreen extends EYBEffect
{
    protected boolean keepTrying;

    public TryOpenAnimatorScreen(boolean keepTrying)
    {
        this.keepTrying = keepTrying;
    }

    @Override
    public void update()
    {
        JavaUtilities.Log(this, duration);

        if (keepTrying)
        {
            if (GR.Screens.SeriesSelection.CanOpen())
            {
                GR.Screens.SeriesSelection.Open(false);
                Complete();
            }
        }
        else
        {
            tickDuration();
        }
    }
}
