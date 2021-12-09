package eatyourbeets.actions.affinity;

import com.megacrit.cardcrawl.core.Settings;
import eatyourbeets.actions.EYBActionWithCallback;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.ui.animator.combat.EYBAffinityMeter;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class RerollAffinity extends EYBActionWithCallback<Affinity>
{
    protected Affinity affinity;
    protected Affinity[] affinityChoices;
    protected EYBAffinityMeter.Target target;
    public boolean isRandom;
    public boolean showEffect;

    public RerollAffinity(EYBAffinityMeter.Target target)
    {
        super(ActionType.POWER, Settings.ACTION_DUR_XFAST);

        this.target = target;
        this.isRandom = true;
    }

    public RerollAffinity SetAffinityChoices(Affinity... affinities)
    {
        this.affinityChoices = affinities;

        return this;
    }

    public RerollAffinity SetOptions(boolean isRandom, boolean showEffect)
    {
        this.isRandom = isRandom;
        this.showEffect = showEffect;

        return this;
    }

    @Override
    protected void FirstUpdate()
    {
        if (isRandom) {
            affinity = CombatStats.Affinities.AffinityMeter.Set(GameUtilities.GetRandomElement(GetAffinityChoices()), target);
        }
        else {
            GameActions.Top.TryChooseAffinity(name, 1, GetAffinityChoices()).AddConditionalCallback(choices -> {
               if (choices.size() > 0) {
                   affinity = CombatStats.Affinities.AffinityMeter.Set(choices.get(0).Affinity, target);
               }
            });
        }

        if (showEffect) {
            CombatStats.Affinities.AffinityMeter.Flash(target);
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

    protected Affinity[] GetAffinityChoices() {
        return affinityChoices != null ? affinityChoices : isRandom ? Affinity.Basic() : Affinity.Extended();
    }
}
