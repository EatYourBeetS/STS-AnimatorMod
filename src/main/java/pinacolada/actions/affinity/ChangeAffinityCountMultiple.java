package pinacolada.actions.affinity;

import com.megacrit.cardcrawl.core.Settings;
import eatyourbeets.actions.EYBActionWithCallback;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCardAffinities;
import pinacolada.effects.affinity.FlashAffinityEffect;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLGameUtilities;

public class ChangeAffinityCountMultiple extends EYBActionWithCallback<PCLCardAffinities>
{
    protected PCLCardAffinities affinities;
    public boolean showEffect;

    public ChangeAffinityCountMultiple(PCLCardAffinities affinities)
    {
        super(ActionType.POWER, Settings.ACTION_DUR_XFAST);

        this.affinities = affinities;
    }

    public ChangeAffinityCountMultiple ShowEffect(boolean showEffect)
    {
        this.showEffect = showEffect;

        return this;
    }

    @Override
    protected void FirstUpdate()
    {
        PCLGameUtilities.AddAffinities(affinities, showEffect);
        if (showEffect) {
            for (PCLAffinity af : affinities.GetAffinities()) {
                PCLGameEffects.List.Add(new FlashAffinityEffect(af));
            }
        }
    }

    @Override
    protected void UpdateInternal(float deltaTime)
    {
        if (TickDuration(deltaTime))
        {
            Complete(affinities);
        }
    }
}
