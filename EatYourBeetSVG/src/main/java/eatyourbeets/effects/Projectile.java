package eatyourbeets.effects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import eatyourbeets.utilities.AdvancedTexture;
import eatyourbeets.utilities.Mathf;
import eatyourbeets.utilities.Position2D;
import eatyourbeets.utilities.RenderHelpers;

public class Projectile extends AdvancedTexture
{
    public boolean flipX;
    public boolean flipY;
    public float width;
    public float height;

    public Position2D target_pos = new Position2D();
    public Position2D offset = new Position2D();
    public Position2D target_offset = new Position2D();
    public Position2D speed = new Position2D(10f, 10f, 24f, 2f);
    public Position2D acceleration = new Position2D();
    public float acceleration_duration = 0;
    public RenderHelpers.BlendingMode blendingMode = RenderHelpers.BlendingMode.Normal;

    public Projectile(Texture texture, float width, float height)
    {
        super(texture);

        this.target_pos.Import(pos);
        this.width = width;
        this.height = height;
    }

    public Projectile SetSpeedMulti(Float x, Float y, Float rotation, Float scale)
    {
        if (x != null)
        {
            this.speed.x *= x;
        }
        if (y != null)
        {
            this.speed.y *= y;
        }
        if (rotation != null)
        {
            this.speed.rotation *= rotation;
        }
        if (scale != null)
        {
            this.speed.scale *= scale;
        }

        return this;
    }

    public Projectile SetAcceleration(Float x, Float y, Float rotation, Float scale, Float duration)
    {
        if (x != null)
        {
            this.acceleration.x = x;
        }
        if (y != null)
        {
            this.acceleration.y = y;
        }
        if (rotation != null)
        {
            this.acceleration.rotation = rotation;
        }
        if (scale != null)
        {
            this.acceleration.scale = scale;
        }
        if (duration != null)
        {
            this.acceleration_duration = duration;
        }

        return this;
    }

    public Projectile SetSpeed(Float x, Float y, Float rotation, Float scale)
    {
        if (x != null)
        {
            this.speed.x = x;
        }
        if (y != null)
        {
            this.speed.y = y;
        }
        if (rotation != null)
        {
            this.speed.rotation = rotation;
        }
        if (scale != null)
        {
            this.speed.scale = scale;
        }

        return this;
    }

    public Projectile SetPosition(Float cX, Float cY)
    {
        if (cX != null)
        {
            this.pos.x = this.target_pos.x = cX;
        }
        if (cY != null)
        {
            this.pos.y = this.target_pos.y = cY;
        }

        return this;
    }

    public Projectile SetTargetPosition(Float cX, Float cY)
    {
        if (cX != null)
        {
            this.target_pos.x = cX;
        }
        if (cY != null)
        {
            this.target_pos.y = cY;
        }

        return this;
    }

    public Projectile SetScale(float scale)
    {
        this.pos.scale = this.target_pos.scale = scale;

        return this;
    }

    public Projectile SetTargetScale(float scale, Float speed)
    {
        this.target_pos.scale = scale;

        if (speed != null)
        {
            this.speed.scale = speed;
        }

        return this;
    }

    public Projectile SetRotation(float degrees)
    {
        this.pos.rotation = this.target_pos.rotation = degrees;

        return this;
    }

    public Projectile SetTargetRotation(float degrees, Float speed)
    {
        this.target_pos.rotation = degrees;

        if (speed != null)
        {
            this.speed.rotation = speed;
        }

        return this;
    }

    public Projectile SetOffset(Float x, Float y, Float rotation, Float scale)
    {
        if (x != null)
        {
            offset.x = target_offset.x = x;
        }
        if (y != null)
        {
            offset.y = target_offset.y = y;
        }
        if (rotation != null)
        {
            offset.rotation = target_offset.rotation = rotation;
        }
        if (scale != null)
        {
            offset.scale = target_offset.scale = scale;
        }

        return this;
    }

    public Projectile SetTargetOffset(Float x, Float y, Float rotation, Float scale)
    {
        if (x != null)
        {
            target_offset.x = x;
        }
        if (y != null)
        {
            target_offset.y = y;
        }
        if (rotation != null)
        {
            target_offset.rotation = rotation;
        }
        if (scale != null)
        {
            target_offset.scale = scale;
        }

        return this;
    }

    public Projectile SetFlip(Boolean flipX, Boolean flipY)
    {
        if (flipX != null)
        {
            this.flipX = flipX;
        }
        if (flipY != null)
        {
            this.flipY = flipY;
        }

        return this;
    }

    public Projectile SetColor(Color color)
    {
        return (Projectile) super.SetColor(color);
    }

    public Projectile SetColor(Float r, Float g, Float b, Float a)
    {
        return (Projectile) super.SetColor(r, g, b, a);
    }

    public Projectile SetBlendingMode(RenderHelpers.BlendingMode blendingMode) {
        this.blendingMode = blendingMode;
        return this;
    }

    public void Update(float delta)
    {
        pos.ApplyMovement(target_pos, speed, delta);
        offset.ApplyMovement(target_offset, speed, delta);
        speed.ApplyAcceleration(acceleration, acceleration_duration, delta, Interpolation.linear);
        acceleration_duration = Mathf.Max(0, acceleration_duration - delta);
    }

    public void Render(SpriteBatch sb)
    {
        Render(sb, color == null ? sb.getColor() : color);
    }

    public void Render(SpriteBatch sb, Color color)
    {
        Render(sb, color, GetX(true), GetY(true), GetScale(true));
    }

    public void Render(SpriteBatch sb, Color color, float cX, float cY, float scale)
    {
        if (blendingMode != RenderHelpers.BlendingMode.Normal) {
            sb.setBlendFunction(blendingMode.srcFunc, blendingMode.dstFunc);
            RenderHelpers.DrawCentered(sb, color, texture, cX, cY, width, height, scale, GetRotation(true), flipX, flipY);
            sb.setBlendFunction(770, 771);
        }
        else {
            RenderHelpers.DrawCentered(sb, color, texture, cX, cY, width, height, scale, GetRotation(true), flipX, flipY);
        }
    }

    public Position2D GetCurrentPosition(boolean addOffset)
    {
        return new Position2D(GetX(addOffset), GetY(addOffset), GetRotation(addOffset), GetScale(addOffset));
    }

    public float GetX(boolean addOffset)
    {
        return addOffset ? (GetX() + offset.x) : GetX();
    }

    public float GetY(boolean addOffset)
    {
        return addOffset ? (GetY() + offset.y) : GetY();
    }

    public float GetRotation(boolean addOffset)
    {
        return addOffset ? (GetRotation() + offset.rotation) : GetRotation();
    }

    public float GetScale(boolean addOffset)
    {
        return addOffset ? (GetScale() + offset.scale) : GetScale();
    }
}
