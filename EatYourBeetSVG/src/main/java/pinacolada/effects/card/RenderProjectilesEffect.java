package pinacolada.effects.card;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import eatyourbeets.effects.EYBEffect;
import pinacolada.effects.Projectile;

import java.util.ArrayList;

public class RenderProjectilesEffect extends EYBEffect
{
    private final ArrayList<Projectile> projectiles;

    public RenderProjectilesEffect(ArrayList<Projectile> projectiles, float duration, boolean isRealtime)
    {
        super(duration, isRealtime);

        this.projectiles = projectiles;
    }

    @Override
    protected void UpdateInternal(float deltaTime)
    {
        super.UpdateInternal(deltaTime);

        if (projectiles.isEmpty())
        {
            Complete();
        }
        else for (Projectile p : projectiles)
        {
            p.Update(deltaTime);
        }
    }

    public void render(SpriteBatch sb)
    {
        for (Projectile p : projectiles)
        {
            p.Render(sb);
        }
    }

    public void dispose()
    {

    }
}