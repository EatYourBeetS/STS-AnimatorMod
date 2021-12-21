package pinacolada.effects.vfx;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.Hitbox;
import eatyourbeets.effects.EYBEffectWithCallback;
import eatyourbeets.utilities.Colors;
import eatyourbeets.utilities.Mathf;
import eatyourbeets.utilities.Position2D;
import pinacolada.effects.Projectile;
import pinacolada.utilities.PCLJUtils;

import java.util.ArrayList;

public class ThrowProjectileEffect extends EYBEffectWithCallback<Hitbox>
{
    protected final ArrayList<Position2D> trailPositions = new ArrayList<>(); // z = scale
    protected boolean addTrailPosition;
    protected Projectile projectile;
    protected Hitbox target;
    protected float offset_x;
    protected float offset_y;
    protected Float speed;

    public ThrowProjectileEffect(Projectile projectile, Hitbox target)
    {
        super(Settings.ACTION_DUR_FAST, true);

        this.target = target;
        this.projectile = projectile;
        this.offset_x = this.offset_y = 0;
    }

    public ThrowProjectileEffect SetSpread(float spreadX, float spreadY)
    {
        this.offset_x = spreadX;
        this.offset_y = spreadY;

        return this;
    }

    public ThrowProjectileEffect SetTargetRotation(float degrees, float speed)
    {
        this.projectile.SetTargetRotation(degrees, speed);

        return this;
    }

    public ThrowProjectileEffect SetScale(float scale)
    {
        this.projectile.SetScale(scale);

        return this;
    }

    @Override
    protected void FirstUpdate()
    {
        super.FirstUpdate();

        final float target_x = target.cX + (Random(-offset_x, offset_x) * target.width);
        final float target_y = target.cY + (Random(-offset_y, offset_y) * target.height);
        projectile.SetTargetPosition(target_x, target_y);

        final float speed_x = (Mathf.Abs(projectile.target_pos.x - projectile.pos.x) / duration);
        final float speed_y = (Mathf.Abs(projectile.target_pos.y - projectile.pos.y) / duration);
        projectile.SetSpeed(0f, 0f, null, null)
                  .SetAcceleration(speed_x * 2, speed_y * 2, null, null, duration);
    }

    @Override
    protected void UpdateInternal(float deltaTime)
    {
        if (!Settings.DISABLE_EFFECTS)
        {
            int i = 0;
            while (i < trailPositions.size())
            {
                if ((trailPositions.get(i).scale -= deltaTime * i) <= 0)
                {
                    trailPositions.remove(i);
                }
                else
                {
                    i++;
                }
            }

            if (projectile.speed.x > (projectile.width * 2.5f) && (addTrailPosition ^= true))
            {
                final Position2D pos = projectile.GetCurrentPosition(true);
                pos.scale = 0.75f; // scale
                trailPositions.add(0, pos);
            }
        }

        if (PCLJUtils.ShowDebugInfo())
        {
            PCLJUtils.LogInfo(this, "Delta: " + deltaTime + ", P.D: " + projectile.acceleration_duration + ", A.D:" + duration);
            PCLJUtils.LogInfo(this, "Speed: " + projectile.speed);
            PCLJUtils.LogInfo(this, "Accel: " + projectile.acceleration);
        }

        this.projectile.Update(deltaTime);

        if (this.duration <= 0)
        {
            Complete(target);
        }
        else
        {
            this.duration = projectile.acceleration_duration;
        }
    }

    @Override
    public void render(SpriteBatch sb)
    {
        if (projectile != null)
        {
            for (Position2D trailPos : trailPositions)
            {
                projectile.Render(sb, Colors.Copy(projectile.color, trailPos.scale * 0.5f),
                        trailPos.x, trailPos.y, projectile.GetScale(true) * trailPos.scale);
            }

            projectile.Render(sb);
        }
    }
}
