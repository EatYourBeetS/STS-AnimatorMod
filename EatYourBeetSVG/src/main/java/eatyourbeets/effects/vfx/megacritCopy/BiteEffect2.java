package eatyourbeets.effects.vfx.megacritCopy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import eatyourbeets.effects.EYBEffect;
import eatyourbeets.effects.SFX;

public class BiteEffect2 extends EYBEffect
{
    private static AtlasRegion top;
    private static AtlasRegion bot;

    private float x;
    private float y;
    private float sY;
    private float dY;
    private float y2;
    private float sY2;
    private float dY2;
    private static final float DUR = 1.0F;
    private boolean playSFX;

    public BiteEffect2(float x, float y, Color c)
    {
        super(1);
        this.duration = 0.7F;

        if (top == null)
        {
            top = ImageMaster.vfxAtlas.findRegion("combat/biteTop");
            bot = ImageMaster.vfxAtlas.findRegion("combat/biteBot");
        }

        this.playSFX = true;
        this.x = x - (float) top.packedWidth / 2.0F;
        this.sY = y - (float) top.packedHeight / 2.0F + 150.0F * Settings.scale;
        this.dY = y - 0.0F * Settings.scale;
        this.y = this.sY;
        this.sY2 = y - (float) (top.packedHeight / 2) - 100.0F * Settings.scale;
        this.dY2 = y - 90.0F * Settings.scale;
        this.y2 = this.sY2;
        this.color = c.cpy();
        this.scale = Settings.scale;
    }

    public BiteEffect2(float x, float y)
    {
        this(x, y, new Color(0.7F, 0.9F, 1.0F, 0.0F));
    }

    public BiteEffect2 PlaySFX(boolean playSFX)
    {
        this.playSFX = playSFX;

        return this;
    }

    public void update()
    {
        this.duration -= Gdx.graphics.getDeltaTime();
        
        final float actualDuration = duration + 0.3f;
        
        if (this.playSFX && actualDuration < this.startingDuration - 0.3F)
        {
            SFX.Play(SFX.EVENT_VAMP_BITE, 0.9f, 1.1f);
            this.playSFX = false;
        }

        if (actualDuration > this.startingDuration / 2.0F)
        {
            this.color.a = Interpolation.fade.apply(1.0F, 0.0F, (actualDuration - 0.5F) * 2.0F);
            this.y = Interpolation.bounceIn.apply(this.dY, this.sY, (actualDuration - 0.5F) * 2.0F);
            this.y2 = Interpolation.bounceIn.apply(this.dY2, this.sY2, (actualDuration - 0.5F) * 2.0F);
        }
        else
        {
            this.color.a = Interpolation.fade.apply(0.0F, 1.0F, actualDuration * 2.0F);
            this.y = Interpolation.fade.apply(this.sY, this.dY, actualDuration * 2.0F);
            this.y2 = Interpolation.fade.apply(this.sY2, this.dY2, actualDuration * 2.0F);
        }

        if (actualDuration <= 0)
        {
            this.isDone = true;
        }
    }

    public void render(SpriteBatch sb)
    {
        sb.setBlendFunction(770, 1);
        sb.setColor(this.color);
        sb.draw(top, this.x, this.y, (float) top.packedWidth / 2.0F, (float) top.packedHeight / 2.0F, (float) top.packedWidth, (float) top.packedHeight, this.scale + MathUtils.random(-0.05F, 0.05F), this.scale + MathUtils.random(-0.05F, 0.05F), 0.0F);
        sb.draw(bot, this.x, this.y2, (float) top.packedWidth / 2.0F, (float) top.packedHeight / 2.0F, (float) top.packedWidth, (float) top.packedHeight, this.scale + MathUtils.random(-0.05F, 0.05F), this.scale + MathUtils.random(-0.05F, 0.05F), 0.0F);
        sb.setBlendFunction(770, 771);
    }

    public void dispose()
    {
    }
}
