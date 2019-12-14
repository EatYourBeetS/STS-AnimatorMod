package eatyourbeets.effects.player;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class RemoveRelicEffect extends AbstractGameEffect
{
    private final int counterIncrease;
    private final AbstractRelic source;
    private final AbstractRelic relic;

    public RemoveRelicEffect(AbstractRelic source, AbstractRelic relic)
    {
        this(source, relic, 0);
    }

    public RemoveRelicEffect(AbstractRelic source, AbstractRelic relic, int counterIncrease)
    {
        this.duration = 1.0F;
        this.source = source;
        this.relic = relic;
        this.counterIncrease = counterIncrease;
    }

    public void update()
    {
        relic.onUnequip();

        AbstractDungeon.player.relics.remove(relic);
        AbstractDungeon.player.reorganizeRelics();

        if (counterIncrease > 0)
        {
            source.setCounter(source.counter + counterIncrease);
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