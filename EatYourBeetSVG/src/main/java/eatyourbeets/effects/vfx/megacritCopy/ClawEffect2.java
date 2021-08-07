package eatyourbeets.effects.vfx.megacritCopy;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.combat.AnimatedSlashEffect;
import eatyourbeets.effects.EYBEffect;
import eatyourbeets.effects.SFX;
import eatyourbeets.utilities.GameEffects;

public class ClawEffect2 extends EYBEffect
{
    private float x;
    private float y;
    private Color color2;
    private boolean flipX;

    public ClawEffect2(float x, float y, Color color1, Color color2)
    {
        super(0.1f, false);

        this.x = x;
        this.y = y;

        SetColors(color1, color2);
    }

    public ClawEffect2 SetColors(Color color1, Color color2)
    {
        this.color = color1.cpy();
        this.color2 = color2.cpy();

        return this;
    }

    public ClawEffect2 SetScale(float scale)
    {
        this.scale = scale;

        return this;
    }

    public ClawEffect2 FlipX(boolean flipX)
    {
        this.flipX = flipX;

        return this;
    }

    @Override
    protected void FirstUpdate()
    {
        SFX.Play(RandomBoolean() ? SFX.ATTACK_DAGGER_5 : SFX.ATTACK_DAGGER_6, 0.7f / scale, 1f / scale);

        float angle = Random(115f, 155f);
        float offset = 35 * scale * Settings.scale;
        if (flipX)
        {
            Slash(this.x - offset, this.y + offset, -150.0F, -150.0F, angle);
            Slash(this.x, this.y, 150.0F, -150.0F, angle);
            Slash(this.x + offset, this.y - offset, -150.0F, -150.0F, angle);
        }
        else
        {
            Slash(this.x + offset, this.y + offset, 150.0F, -150.0F, -angle);
            Slash(this.x, this.y, 150.0F, -150.0F, -angle);
            Slash(this.x - offset, this.y - offset, 150.0F, -150.0F, -angle);
        }

        Complete();
    }

    @Override
    public void render(SpriteBatch sb)
    {

    }

    @Override
    public void dispose()
    {

    }

    protected void Slash(float x, float y, float dX, float dY, float angle)
    {
        GameEffects.Queue.Add(new AnimatedSlashEffect(x, y, dX, dY, angle, scale * 2f, this.color, this.color2));
    }
}
