package eatyourbeets.effects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class ObtainRelicEffect extends AbstractGameEffect
{
    private final AbstractRelic relic;

    public ObtainRelicEffect(AbstractRelic relic)
    {
        this.relic = relic;
        this.duration = this.startingDuration = 1f;
    }

    public void update()
    {
        if (!this.isDone)
        {
            relic.instantObtain();
            CardCrawlGame.metricData.addRelicObtainData(relic);
        }

        this.isDone = true;
    }

    public void render(SpriteBatch sb)
    {

    }

    public void dispose()
    {

    }
}