package eatyourbeets.effects.vfx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import eatyourbeets.effects.EYBEffect;
import eatyourbeets.utilities.RenderHelpers;

public class GenericRenderEffect extends EYBEffect
{
    public final TextureRegion image;

    private float x;
    private float y;

    public GenericRenderEffect(Texture texture, float x, float y)
    {
        this(new TextureRegion(texture), x, y);
    }

    public GenericRenderEffect(TextureRegion region, float x, float y)
    {
        super(0.6f, true);

        this.image = region;
        this.color = Color.WHITE.cpy();
        this.scale = 1;
        this.x = x;
        this.y = y;

        if (image == null)
        {
            Complete();
        }
    }

    @Override
    protected void FirstUpdate()
    {
        UpdateInternal(GetDeltaTime());
    }

    @Override
    public void render(SpriteBatch sb)
    {
        if (this.image != null)
        {
            RenderHelpers.DrawCentered(sb, color, image, x, y, image.getRegionWidth(), image.getRegionHeight(), scale, rotation);
        }
    }

    @Override
    public void dispose()
    {
    }
}
