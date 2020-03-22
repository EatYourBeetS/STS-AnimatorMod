package eatyourbeets.effects.stance;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import eatyourbeets.effects.EYBEffect;

public class StanceAura extends EYBEffect
{
    protected static boolean switcher = true;
    protected TextureAtlas.AtlasRegion img;
    protected float x;
    protected float y;
    protected float vY;

    public StanceAura(Color color)
    {
        super(1, 2f);

        this.img = ImageMaster.EXHAUST_L;
        this.scale = MathUtils.random(2.7f, 2.5f) * Settings.scale;
        this.color = color.cpy();
        this.x = player.hb.cX + MathUtils.random(-player.hb.width / 16f, player.hb.width / 16f);
        this.y = player.hb.cY + MathUtils.random(-player.hb.height / 16f, player.hb.height / 12f);
        this.x -= img.packedWidth * 0.5f;
        this.y -= img.packedHeight * 0.5f;
        this.renderBehind = true;
        this.rotation = MathUtils.random(360f);

        if (switcher ^= true)
        {
            this.renderBehind = true;
            this.vY = MathUtils.random(0f, 40f);
        }
        else
        {
            this.renderBehind = false;
            this.vY = MathUtils.random(0f, -40f);
        }
    }

    @Override
    protected void UpdateInternal(float deltaTime)
    {
        if (duration > 1f)
        {
            color.a = Interpolation.fade.apply(0.3f, 0f, this.duration - 1f);
        }
        else
        {
            color.a = Interpolation.fade.apply(0f, 0.3f, this.duration);
        }

        rotation += deltaTime * vY;

        super.UpdateInternal(deltaTime);
    }

    @Override
    public void render(SpriteBatch sb)
    {
        sb.setColor(color);
        sb.setBlendFunction(770, 1);
        sb.draw(img, x, y, img.packedWidth * 0.5f, img.packedHeight * 0.5f, img.packedWidth, img.packedHeight, scale, scale, rotation);
        sb.setBlendFunction(770, 771);
    }
}
