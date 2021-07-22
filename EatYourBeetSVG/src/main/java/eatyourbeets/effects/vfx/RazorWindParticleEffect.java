package eatyourbeets.effects.vfx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import eatyourbeets.effects.EYBEffect;
import eatyourbeets.utilities.AdvancedTexture;

public class RazorWindParticleEffect extends EYBEffect
{
    protected static Texture img;

    protected float x;
    protected float y;
    protected float alpha;
    protected AdvancedTexture particle;


    public RazorWindParticleEffect(float x, float y)
    {
        super(MathUtils.random(0.4F, 0.8F));

        if (img == null) {
            img = ImageMaster.loadImage("images/orbs/animator/AirTrail.png");
        }

        final float offsetX = MathUtils.random(-12.0F, 12.0F) * Settings.scale;
        final float offsetY = MathUtils.random(-12.0F, 12.0F) * Settings.scale;
        this.alpha = Random(0.7F, 1.0F);
        this.color = Color.WHITE.cpy();
        this.color.a = this.alpha;
        this.particle = new AdvancedTexture(img, 48f, 48f)
                .SetPosition(x + offsetX, y)
                .SetScale( Random(0.08f, 0.31f) * Settings.scale)
                .SetColor(this.color)
                .SetRotation(Random(-10f, 10f));

        if (offsetX > 0.0F)
        {
            this.renderBehind = true;
        }
    }

    @Override
    protected void UpdateInternal(float deltaTime)
    {
        super.UpdateInternal(deltaTime);

        final float halfDuration = startingDuration * 0.5f;
        if (this.duration < halfDuration)
        {
            this.color.a = Interpolation.exp5In.apply(0.0F, this.alpha, this.duration / halfDuration);
            this.particle.color.a = this.color.a;
        }

        this.particle.Update(deltaTime);
    }

    public void render(SpriteBatch sb)
    {
        this.particle.Render(sb);
    }
}
