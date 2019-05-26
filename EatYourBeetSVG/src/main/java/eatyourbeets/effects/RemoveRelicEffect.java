package eatyourbeets.effects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class RemoveRelicEffect extends AbstractGameEffect
{
    private final boolean increaseSourceCounter;
    private final AbstractRelic source;
    private final AbstractRelic relic;

    public RemoveRelicEffect(AbstractRelic source, AbstractRelic relic)
    {
        this(source, relic, true);
    }

    public RemoveRelicEffect(AbstractRelic source, AbstractRelic relic, boolean increaseSourceCounter)
    {
        this.duration = 1.0F;
        this.source = source;
        this.relic = relic;
        this.increaseSourceCounter = increaseSourceCounter;
    }

    public void update()
    {
        relic.onUnequip();

        AbstractDungeon.player.relics.remove(relic);
        AbstractDungeon.player.reorganizeRelics();

        if (increaseSourceCounter)
        {
            source.setCounter(source.counter + 1);
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