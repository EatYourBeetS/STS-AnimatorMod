package eatyourbeets.effects.vfx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.core.Settings;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.EYBEffect;
import eatyourbeets.utilities.RenderHelpers;

public class ShieldEffect extends EYBEffect
{
    public final TextureRegion image;

    private float x;
    private float y;
    private float sY;
    private float tY;

    public ShieldEffect(float x, float y)
    {
        super(0.6f, true);

        this.image = AttackEffects.GetTextureRegion(AttackEffects.SHIELD);
        this.color = Color.WHITE.cpy();
        this.scale = 1;
        this.x = x;
        this.y = this.sY = y + (80.0F * Settings.scale);
        this.tY = y;
    }

    @Override
    protected void FirstUpdate()
    {
        UpdateInternal(GetDeltaTime());
    }

    @Override
    protected void UpdateInternal(float deltaTime)
    {
        super.UpdateInternal(deltaTime);

        if (isDone)
        {
            this.color.a = 0.0f;
        }
        else if (this.duration < 0.2f)
        {
            this.color.a = this.duration * 5.0f;
        }
        else
        {
            this.color.a = Interpolation.fade.apply(1.0f, 0.0f, duration * 0.75f / startingDuration);
        }

        this.y = Interpolation.exp10In.apply(tY, sY, duration / startingDuration);
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
