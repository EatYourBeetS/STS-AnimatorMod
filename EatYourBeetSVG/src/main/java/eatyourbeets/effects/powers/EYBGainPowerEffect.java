package eatyourbeets.effects.powers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import eatyourbeets.powers.BasePower;

public class EYBGainPowerEffect extends AbstractGameEffect
{
    private static final float EFFECT_DUR = 2.0F;
    private float scale;
    private Texture img;
    private AtlasRegion region48;

    public EYBGainPowerEffect(BasePower power, boolean playSfx)
    {
        this.img = power.img;

        if (power.powerIcon != null)
        {
            this.region48 = power.powerIcon;
        }
        else
        {
            this.region48 = power.region48;
        }

        if (playSfx)
        {
            power.playApplyPowerSfx();
        }

        this.duration = 2.0F;
        this.startingDuration = 2.0F;
        this.scale = Settings.scale;
        this.color = new Color(1.0F, 1.0F, 1.0F, 0.5F);
    }

    public void update()
    {
        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration > 0.5F)
        {
            this.scale = Interpolation.exp5Out.apply(3.0F * Settings.scale, Settings.scale, -(this.duration - 2.0F) / 1.5F);
        }
        else
        {
            this.color.a = Interpolation.fade.apply(0.5F, 0.0F, 1.0F - this.duration);
        }

    }

    public void render(SpriteBatch sb, float x, float y)
    {
        sb.setColor(this.color);
        sb.setBlendFunction(770, 1);
        if (this.region48 != null)
        {
            float half_w = region48.packedWidth / 2f;
            float half_h = region48.packedHeight / 2f;

            //sb.draw(this.region48, x - 12f, y - 16f, 16f, 16f, 32.0F, 32.0F, scale, scale, 0.0F);
            sb.draw(this.region48, x - half_w, y - half_h, half_w, half_h, region48.packedWidth , region48.packedHeight, scale * 0.1f, scale * 0.1f, 0.0F);
        }
        else
        {
            sb.draw(this.img, x - 16.0F, y - 16.0F, 16.0F, 16.0F, 32.0F, 32.0F, scale, scale, 0.0F, 0, 0, 32, 32, false, false);
        }

        sb.setBlendFunction(770, 771);
    }

    public void dispose()
    {
    }

    public void render(SpriteBatch sb)
    {
    }
}

