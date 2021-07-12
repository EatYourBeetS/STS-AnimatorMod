package eatyourbeets.effects.vfx;

import com.badlogic.gdx.Gdx;
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
    private float effectDuration;
    private float x;
    private float y;
    private float alpha;
    private Texture img;

    public StarParticleEffect(float x, float y, Color mainColor) {
        switch (MathUtils.random(0, 2))
        {
            case 0:
                this.img = ImageMaster.loadImage("images/effects/Sparkle.png");
            case 1:
                this.img = ImageMaster.loadImage("images/effects/Sparkle2.png");
            default:
                this.img = ImageMaster.loadImage("images/effects/Sparkle3.png");
        }


        float offsetX = MathUtils.random(-12.0F, 12.0F) * Settings.scale;
        float offsetY = MathUtils.random(-12.0F, 12.0F) * Settings.scale;
        this.x = x + offsetX - (float) (SIZE / 2);
        this.y = y + offsetY - (float) (SIZE / 2);
        this.effectDuration = MathUtils.random(0.4F, 0.8F);
        this.duration = this.effectDuration;
        this.startingDuration = this.effectDuration;
        if (offsetX > 0.0F) {
            this.renderBehind = true;
        }

        this.scale = Random(0.05f, 0.3f) * Settings.scale;
        this.alpha = MathUtils.random(0.5F, 1.0F);
        this.color = mainColor;
        this.color.a = this.alpha;
        this.rotation = Random(-10f, 10f);
    }

    @Override
    protected void UpdateInternal(float deltaTime) {

        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0.0F) {
            this.isDone = true;
        } else if (this.duration < this.effectDuration / 2.0F) {
            this.color.a = Interpolation.exp5In.apply(0.0F, this.alpha, this.duration / (this.effectDuration / 2.0F));
        }

        super.UpdateInternal(deltaTime);
    }

    public void render(SpriteBatch sb) {
        sb.setColor(this.color);
        sb.setBlendFunction(770, 1);
        sb.draw(img, x, y, SIZE * 0.5f, SIZE * 0.5f, SIZE, SIZE, scale, scale, rotation, 0, 0, SIZE, SIZE, MathUtils.randomBoolean(), false);
        sb.setBlendFunction(770, 771);
    }
}
