package eatyourbeets.actions.affinity;

import com.megacrit.cardcrawl.core.Settings;
import eatyourbeets.actions.EYBActionWithCallback;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.effects.affinity.FlashAffinityEffect;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class ChangeAffinityCount extends EYBActionWithCallback<Affinity>
{
    protected Affinity affinity;
    public boolean showEffect;

    public ChangeAffinityCount(Affinity affinity, int amount)
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
        GameUtilities.AddAffinity(affinity, amount, showEffect);
        if (showEffect) {
            GameEffects.List.Add(new FlashAffinityEffect(affinity));
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
