package eatyourbeets.effects.player;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import eatyourbeets.effects.EYBEffect;

public class ObtainRelicEffect extends EYBEffect
{
    private final AbstractRelic relic;

    public ObtainRelicEffect(AbstractRelic relic)
    {
        this.relic = relic;
    }

    @Override
    protected void FirstUpdate()
    {
        relic.instantObtain();
        CardCrawlGame.metricData.addRelicObtainData(relic);

        Complete();
    }
}