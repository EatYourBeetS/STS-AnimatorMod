package eatyourbeets.effects.card;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import eatyourbeets.effects.EYBEffect;
import eatyourbeets.utilities.AdvancedTexture;

import java.util.ArrayList;

public class RenderProjectilesEffect extends EYBEffect
{
    private final ArrayList<AdvancedTexture> projectiles;

    public RenderProjectilesEffect(ArrayList<AdvancedTexture> projectiles, float duration, boolean isRealtime)
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
        else for (AdvancedTexture p : projectiles)
        {
            p.Update(deltaTime);
        }
    }

    public void render(SpriteBatch sb)
    {
        for (AdvancedTexture p : projectiles)
        {
            p.Render(sb);
        }
    }

    public void dispose()
    {

    }
}