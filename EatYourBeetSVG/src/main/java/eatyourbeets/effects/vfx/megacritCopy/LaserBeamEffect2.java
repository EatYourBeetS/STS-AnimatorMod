package eatyourbeets.effects.vfx.megacritCopy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.BorderLongFlashEffect;
import eatyourbeets.effects.EYBEffect;
import eatyourbeets.effects.SFX;
import eatyourbeets.utilities.GameEffects;

public class LaserBeamEffect2 extends EYBEffect
{
    private final float x;
    private final float y;
    private static AtlasRegion img;
    private boolean playedSfx = false;

    public LaserBeamEffect2(float x, float y)
    {
        super(2);

        if (img == null)
        {
            img = ImageMaster.vfxAtlas.findRegion("combat/laserThick");
        }

        this.x = x;
        this.y = y;
        this.color = Color.CYAN.cpy();
    }

    public LaserBeamEffect2 SetColor(Color color)
    {
        this.color.set(color);

        return this;
    }

    public void update()
    {
        if (!this.playedSfx)
        {
            GameEffects.Queue.Add(new BorderLongFlashEffect(color));
            this.playedSfx = true;
            CardCrawlGame.sound.play(SFX.ATTACK_MAGIC_BEAM);
            CardCrawlGame.screenShake.rumble(2f);
        }

        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration > this.startingDuration / 2f)
        {
            this.color.a = Interpolation.pow2In.apply(1f, 0f, this.duration - 1f);
        }
        else
        {
            this.color.a = Interpolation.pow2Out.apply(0f, 1f, this.duration);
        }

        if (this.duration < 0f)
        {
            this.isDone = true;
        }
    }

    public void render(SpriteBatch sb)
    {
        sb.setBlendFunction(770, 1);
        sb.setColor(new Color(0.5f, 0.7f, 1f, this.color.a));
        sb.draw(img, this.x, this.y - (float) (img.packedHeight / 2), 0f, (float) img.packedHeight / 2f, (float) img.packedWidth, (float) img.packedHeight, this.scale * 2f + Random(-0.05f, 0.05f), this.scale * 1.5f + Random(-0.1f, 0.1f), Random(6f, 9f));
        sb.draw(img, this.x, this.y - (float) (img.packedHeight / 2), 0f, (float) img.packedHeight / 2f, (float) img.packedWidth, (float) img.packedHeight, this.scale * 2f + Random(-0.05f, 0.05f), this.scale * 1.5f + Random(-0.1f, 0.1f), Random(6f, 9f));
        sb.setColor(this.color);
        sb.draw(img, this.x, this.y - (float) (img.packedHeight / 2), 0f, (float) img.packedHeight / 2f, (float) img.packedWidth, (float) img.packedHeight, this.scale * 2f, this.scale / 2f, -Random(7f, 8f));
        sb.draw(img, this.x, this.y - (float) (img.packedHeight / 2), 0f, (float) img.packedHeight / 2f, (float) img.packedWidth, (float) img.packedHeight, this.scale * 2f, this.scale / 2f, -Random(7f, 8f));
        sb.setBlendFunction(770, 771);
    }
}
