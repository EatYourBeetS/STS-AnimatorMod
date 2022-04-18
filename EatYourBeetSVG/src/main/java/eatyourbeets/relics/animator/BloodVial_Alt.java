package eatyourbeets.relics.animator;

import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import eatyourbeets.interfaces.subscribers.OnRelicObtainedSubscriber;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.JUtils;

public class BloodVial_Alt extends AnimatorRelic implements OnRelicObtainedSubscriber
{
    public static final String ID = CreateFullID(BloodVial_Alt.class);
    public static final int HEAL_AMOUNT = 2;

    public BloodVial_Alt()
    {
        super(ID, RelicTier.SPECIAL, LandingSound.CLINK);

        setCounter(HEAL_AMOUNT);
        updateDescription();
    }

    @Override
    public void OnRelicObtained(AbstractRelic relic, Trigger trigger)
    {
        if (trigger == Trigger.Equip && relic != this && relic instanceof BloodVial_Alt)
        {
            relic.currentX = relic.targetX = Settings.WIDTH * 100;
            GameEffects.Queue.RemoveRelic(relic).AddCallback(() ->
            {
                AddCounter(HEAL_AMOUNT);
                updateDescription();
            });
        }
    }

    @Override
    public String getUpdatedDescription()
    {
        return JUtils.Format(this.DESCRIPTIONS[0], counter);
    }

    @Override
    public void atBattleStart()
    {
        super.atBattleStart();

        GameActions.Top.Add(new RelicAboveCreatureAction(player, this));
        GameActions.Top.Heal(counter);
        flash();
    }
}