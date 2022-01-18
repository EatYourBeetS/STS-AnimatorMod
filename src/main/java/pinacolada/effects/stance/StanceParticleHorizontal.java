package pinacolada.effects.stance;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import eatyourbeets.effects.EYBEffect;

public class StanceParticleHorizontal extends EYBEffect
{
    private float x;
    private float y;
    private float vX;
    private float vY;
    private float dvy;
    private float dvx;
    private Texture img;

    public StanceParticleHorizontal(Color particleColor)
    {
        super(Random(0.6f, 1f), true);

        final float multi = Settings.scale;
        img = ImageMaster.FROST_ACTIVATE_VFX_1;
        scale = Random(0.6f, 1f) * multi;
        color = particleColor.cpy();
        vX = Random(-300f, -50f) * multi;
        vY = Random(-200f, -100f) * multi;
        x = player.hb.cX + Random(100f, 160f) * multi - 32f;
        y = player.hb.cY + Random(-50f, 220f) * multi - 32f;
        renderBehind = RandomBoolean(0.2f + (scale - 0.5f));
        dvx = 400f * multi * scale;
        dvy = 100f * multi;
    }

    @Override
    protected void UpdateInternal(float deltaTime)
    {
        x += vX * deltaTime;
        y += vY * deltaTime;
        vY += dvy * deltaTime;
        vX -= dvx * deltaTime;

        //noinspection SuspiciousNameCombination
        rotation = -(57.295776f * MathUtils.atan2(vX, vY));

        final float halfDuration = startingDuration * 0.5f;
        if (duration > halfDuration)
        {
            color.a = Interpolation.fade.apply(1f, 0f, (duration - halfDuration) / halfDuration);
        }
        else
        {
            color.a = Interpolation.fade.apply(0f, 1f, duration / halfDuration);
        }

        vY += deltaTime * 40f * Settings.scale;

        super.UpdateInternal(deltaTime);
    }

    @Override
    public void render(SpriteBatch sb)
    {
        sb.setColor(color);
        sb.setBlendFunction(770, 1);
        sb.draw(img, x, y, 32f, 32f, 25f,
        128f, scale, scale + (startingDuration * 0.2f - duration) * Settings.scale, rotation,
        0, 0, 64, 64, false, false);
        sb.setBlendFunction(770, 771);
    }
}
