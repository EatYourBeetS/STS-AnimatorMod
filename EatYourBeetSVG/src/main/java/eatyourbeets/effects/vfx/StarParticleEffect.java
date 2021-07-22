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

import java.util.ArrayList;

public class StarParticleEffect extends EYBEffect
{
    protected static final ArrayList<Texture> images = new ArrayList<>();
    protected static final int VARIATION_COUNT = 3;

    protected float x;
    protected float y;
    protected float alpha;
    protected Texture img;
    protected AdvancedTexture particle;


    public StarParticleEffect(float x, float y, Color mainColor)
    {
        super(MathUtils.random(0.4F, 0.8F));

        if (images.size() <= VARIATION_COUNT) {
            for (int i = 1; i <= VARIATION_COUNT; i++) {
                Texture img = ImageMaster.loadImage("images/effects/Sparkle" + i + ".png");
                images.add(img);
            }
        }

        final float offsetX = MathUtils.random(-12.0F, 12.0F) * Settings.scale;
        final float offsetY = MathUtils.random(-12.0F, 12.0F) * Settings.scale;
        this.alpha = Random(0.5F, 1.0F);
        this.color = mainColor.cpy();
        this.color.a = this.alpha;
        this.particle = new AdvancedTexture(images.get(Random(0, VARIATION_COUNT - 1)), 48f, 48f)
                .SetPosition(x + offsetX, y)
                .SetScale( Random(0.04f, 0.31f) * Settings.scale)
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
