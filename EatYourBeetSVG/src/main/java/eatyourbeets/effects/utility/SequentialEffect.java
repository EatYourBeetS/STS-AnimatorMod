package eatyourbeets.effects.utility;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import eatyourbeets.effects.EYBEffect;

import java.util.ArrayDeque;

public class SequentialEffect extends EYBEffect
{
    private final ArrayDeque<AbstractGameEffect> effects;
    private AbstractGameEffect current;

    public SequentialEffect()
    {
        super();

        effects = new ArrayDeque<>();
    }

    public void Enqueue(AbstractGameEffect effect)
    {
        effects.add(effect);
    }

    @Override
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
                Complete();
            }
        }
    }

    @Override
    public void render(SpriteBatch sb)
    {
        if (current != null)
        {
            current.render(sb);
        }
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