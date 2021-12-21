package pinacolada.effects.stance;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import eatyourbeets.effects.EYBEffect;

public class StanceParticleVertical extends EYBEffect
{
    private float x;
    private float y;
    private float vX;
    private float vY;
    private float dvy;
    private float dvx;
    private TextureAtlas.AtlasRegion img;

    public StanceParticleVertical(Color particleColor)
    {
        super(Random(1.3f, 1.8f), true);

        final float multi = Settings.scale;
        img = ImageMaster.GLOW_SPARK;
        scale = Random(0.6f, 1f) * multi;
        color = particleColor.cpy();
        x = player.hb.cX + Random(-player.hb.width / 2f - 30f * multi, player.hb.width / 2f + 30f * multi);
        y = player.hb.cY + Random(-player.hb.height / 2f - -10f * multi, player.hb.height / 2f - 10f * multi);
        x -= (float) img.packedWidth / 2f;
        y -= (float) img.packedHeight / 2f;
        renderBehind = RandomBoolean(0.2f + (scale - 0.5f));
        rotation = Random(-8f, 8f);
    }

    @Override
    protected void UpdateInternal(float deltaTime)
    {
        final float halfDuration = startingDuration * 0.5f;
        if (duration > halfDuration)
        {
            color.a = Interpolation.fade.apply(1f, 0f, (duration - halfDuration) / halfDuration);
        }
        else
        {
            color.a = Interpolation.fade.apply(0f, 1f, duration / halfDuration);
        }

        vY += GetDeltaTime() * 40f * Settings.scale;

        super.UpdateInternal(deltaTime);
    }

    @Override
    public void render(SpriteBatch sb)
    {
        sb.setColor(color);
        sb.setBlendFunction(770, 1);
        sb.draw(img, x, y + vY, (float) img.packedWidth / 2f,
        (float) img.packedHeight / 2f, (float) img.packedWidth, (float) img.packedHeight,
        scale * 0.8f, (0.1f + (startingDuration - duration) * 2f * scale) * Settings.scale, rotation);
        sb.setBlendFunction(770, 771);
    }
}
