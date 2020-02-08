package eatyourbeets.interfaces.subscribers;

import com.megacrit.cardcrawl.relics.AbstractRelic;

public interface OnRelicObtainedSubscriber
{
    enum Trigger
    {
        Equip,
        Obtain,
        BossChest,
        MetricData
    }

    void OnRelicObtained(AbstractRelic relic, Trigger trigger);
}
