package eatyourbeets.effects.vfx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import eatyourbeets.effects.EYBEffect;
import eatyourbeets.utilities.Colors;

@SuppressWarnings("FieldCanBeLocal")
public class ColoredSweepingBeamEffect extends EYBEffect
{
    protected static final Color SECONDARY_COLOR = new Color(0.3f, 0.3f, 1f, 1f);

    private final float sX;
    private final float sY;
    private float dX;
    private float dY;
    private float dst;
    private final boolean isFlipped;
    private final float DUR = 0.3f;
    private static AtlasRegion img;

    public ColoredSweepingBeamEffect(float sX, float sY, boolean isFlipped, Color color)
    {
        if (img == null)
        {
            img = ImageMaster.vfxAtlas.findRegion("combat/laserThin");
        }

        this.isFlipped = isFlipped;
        if (isFlipped)
        {
            this.sX = sX - 32f * Settings.scale;
            this.sY = sY + 20f * Settings.scale;
        }
        else
        {
            this.sX = sX + 40f * Settings.scale;
            this.sY = sY + 50f * Settings.scale;
        }

        this.color = color.cpy();
        this.duration = DUR;
        this.startingDuration = DUR;
    }

    @Override
    protected void UpdateInternal(float deltaTime)
    {
        if (this.isFlipped)
        {
            this.dX = (float) Settings.WIDTH / 2f * Interpolation.pow3Out.apply(this.duration);
            this.dY = AbstractDungeon.floorY + 10f * Settings.scale;
        }
        else
        {
            this.dX = (float) Settings.WIDTH + (float) (-Settings.WIDTH) / 2f * Interpolation.pow3Out.apply(this.duration);
            this.dY = AbstractDungeon.floorY + 30f * Settings.scale;
        }

        this.dst = Vector2.dst(this.sX, this.sY, this.dX, this.dY) / Settings.scale;
        this.rotation = MathUtils.atan2(this.dX - this.sX, this.dY - this.sY);
        this.rotation *= 57.295776f;
        this.rotation = -this.rotation + 90f;
        if (this.duration > this.startingDuration / 2f)
        {
            this.color.a = Interpolation.pow2In.apply(1f, 0f, (this.duration - 0.25f) * 4f);
        }
        else
        {
            this.color.a = Interpolation.pow2Out.apply(0f, 1f, this.duration * 4f);
        }

        super.UpdateInternal(deltaTime);
    }

    public void render(SpriteBatch sb)
    {
        sb.setBlendFunction(770, 1);
        sb.setColor(this.color);
        sb.draw(img, this.sX, this.sY - (float) img.packedHeight / 2f + 10f * Settings.scale, 0f, (float) img.packedHeight / 2f, this.dst, 50f, this.scale + Random(-0.01f, 0.01f), this.scale, this.rotation);
        sb.setColor(Colors.Lerp(color, SECONDARY_COLOR, 0.5f, color.a));//new Color(0.3f, 0.3f, 1f, this.color.a));
        sb.draw(img, this.sX, this.sY - (float) img.packedHeight / 2f, 0f, (float) img.packedHeight / 2f, this.dst, Random(50f, 90f), this.scale + Random(-0.02f, 0.02f), this.scale, this.rotation);
        sb.setBlendFunction(770, 771);
    }
}