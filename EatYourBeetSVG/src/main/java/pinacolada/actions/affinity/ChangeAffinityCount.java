package pinacolada.actions.affinity;

import com.megacrit.cardcrawl.core.Settings;
import eatyourbeets.actions.EYBActionWithCallback;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.effects.affinity.FlashAffinityEffect;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLGameUtilities;

public class ChangeAffinityCount extends EYBActionWithCallback<PCLAffinity>
{
    protected PCLAffinity affinity;
    public boolean showEffect;

    public ChangeAffinityCount(PCLAffinity affinity, int amount)
    {
        super(ActionType.POWER, Settings.ACTION_DUR_XFAST);

        this.affinity = affinity;
        this.amount = amount;
    }

    public ChangeAffinityCount ShowEffect(boolean showEffect)
    {
        this.showEffect = showEffect;

        return this;
    }

    @Override
    protected void FirstUpdate()
    {
        PCLGameUtilities.AddAffinity(affinity, amount, showEffect);
        if (showEffect) {
            PCLGameEffects.List.Add(new FlashAffinityEffect(affinity));
        }
    }

    @Override
    protected void UpdateInternal(float deltaTime)
    {
        if (TickDuration(deltaTime))
        {
            Complete(affinity);
        }
    }
}
