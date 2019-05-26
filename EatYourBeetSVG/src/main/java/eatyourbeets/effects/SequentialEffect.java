package eatyourbeets.effects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

import java.util.ArrayDeque;

public class SequentialEffect extends AbstractGameEffect
{
    private AbstractGameEffect current;
    private ArrayDeque<AbstractGameEffect> effects;

    public SequentialEffect()
    {
        this.duration = 1f;
        effects = new ArrayDeque<>();
    }

    public void Enqueue(AbstractGameEffect effect)
    {
        effects.add(effect);
    }

    public void update()
    {
        if (UpdateCurrent())
        {
            if (effects.size() > 0)
            {
                current = effects.pop();
            }
            else
            {
                this.isDone = true;
            }
        }
    }

    public void render(SpriteBatch sb)
    {
        if (current != null)
        {
            current.render(sb);
        }
    }

    public void dispose()
    {

    }

    private boolean UpdateCurrent()
    {
        if (current == null || current.isDone)
        {
            return true;
        }

        current.update();

        if (current.isDone)
        {
            current.dispose();
            return true;
        }

        return false;
    }
}