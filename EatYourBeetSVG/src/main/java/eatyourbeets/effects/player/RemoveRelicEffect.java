package eatyourbeets.effects.player;

import com.megacrit.cardcrawl.relics.AbstractRelic;
import eatyourbeets.effects.EYBEffectWithCallback;

public class RemoveRelicEffect extends EYBEffectWithCallback<AbstractRelic>
{
    private final AbstractRelic relic;

    public RemoveRelicEffect(AbstractRelic relic)
    {
        this.relic = relic;
    }

    @Override
    protected void FirstUpdate()
    {
        relic.onUnequip();
        player.relics.remove(relic);
        player.reorganizeRelics();

        Complete(relic);
    }
}