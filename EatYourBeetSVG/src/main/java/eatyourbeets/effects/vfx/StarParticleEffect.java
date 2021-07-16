package eatyourbeets.effects.vfx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import eatyourbeets.effects.EYBEffect;

public class StarParticleEffect extends EYBEffect
{
    protected static final int SIZE = 96;

    protected float x;
    protected float y;
    protected float alpha;
    protected Texture img;

    public StarParticleEffect(float x, float y, Color mainColor)
    {
        super(MathUtils.random(0.4F, 0.8F));

        final float offsetX = MathUtils.random(-12.0F, 12.0F) * Settings.scale;
        final float offsetY = MathUtils.random(-12.0F, 12.0F) * Settings.scale;
        if (offsetX > 0.0F)
        {
            this.renderBehind = true;
        }

        this.img = ImageMaster.loadImage("images/effects/Sparkle" + Random(1, 3) + ".png");
        this.x = x + offsetX - (float) (SIZE / 2);
        this.y = y + offsetY - (float) (SIZE / 2);
        this.scale = Random(0.05f, 0.3f) * Settings.scale;
        this.alpha = Random(0.5F, 1.0F);
        this.color = mainColor.cpy();
        this.color.a = this.alpha;
        this.rotation = Random(-10f, 10f);
    }

    @Override
    protected void UpdateInternal(float deltaTime)
    {
        super.UpdateInternal(deltaTime);

        final float halfDuration = startingDuration * 0.5f;
        if (this.duration < halfDuration)
        {
            this.color.a = Interpolation.exp5In.apply(0.0F, this.alpha, this.duration / halfDuration);
        }
    }

    public void render(SpriteBatch sb)
    {
        sb.setColor(this.color);
        sb.setBlendFunction(770, 1);
        sb.draw(img, x, y, SIZE * 0.5f, SIZE * 0.5f, SIZE, SIZE, scale, scale, rotation, 0, 0, SIZE, SIZE, RandomBoolean(0.5f), false);
        sb.setBlendFunction(770, 771);
    }
}
