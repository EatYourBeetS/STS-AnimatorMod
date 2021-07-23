package eatyourbeets.effects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import eatyourbeets.utilities.ColoredTexture;
import eatyourbeets.utilities.Mathf;
import eatyourbeets.utilities.RenderHelpers;
import org.lwjgl.util.vector.Vector3f;

public class Projectile extends ColoredTexture
{
    public boolean flipX;
    public boolean flipY;
    public float width;
    public float height;
    public float target_scale = 1;
    public Vector3f current_pos = new Vector3f();
    public Vector3f target_pos = new Vector3f();
    public Vector3f current_offset = new Vector3f();
    public Vector3f target_offset = new Vector3f();
    public Vector3f acceleration = new Vector3f(0f, 0f, 0f);
    public Vector3f speed = new Vector3f(10f, 10f, 24f);

    public Projectile(Texture texture, float width, float height)
    {
        super(texture);

        this.width = width;
        this.height = height;
    }

    public Projectile SetSpeedMulti(Float x, Float y, Float rotation)
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
            this.speed.z *= rotation;
        }

        return this;
    }

    public Projectile SetAcceleration(Float x, Float y, Float rotation)
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
            this.acceleration.z = rotation;
        }

        return this;
    }

    public Projectile SetSpeed(Float x, Float y, Float rotation)
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
            this.speed.z = rotation;
        }

        return this;
    }

    public Projectile SetPosition(Float cX, Float cY)
    {
        if (cX != null)
        {
            this.current_pos.x = this.target_pos.x = cX;
        }
        if (cY != null)
        {
            this.current_pos.y = this.target_pos.y = cY;
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
        this.scale = this.target_scale = scale;

        return this;
    }

    public Projectile SetTargetScale(float scale)
    {
        this.target_scale = scale;

        return this;
    }

    public Projectile SetRotation(float degrees)
    {
        this.current_pos.z = this.target_pos.z = degrees;

        return this;
    }

    public Projectile SetTargetRotation(float degrees)
    {
        this.target_pos.z = degrees;

        return this;
    }

    public Projectile SetOffset(Float x, Float y, Float rotation)
    {
        if (x != null)
        {
            current_offset.x = target_offset.x = x;
        }
        if (y != null)
        {
            current_offset.y = target_offset.y = y;
        }
        if (rotation != null)
        {
            current_offset.z = target_offset.z = rotation;
        }

        return this;
    }

    public Projectile SetTargetOffset(Float x, Float y, Float rotation)
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
            target_offset.z = rotation;
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

    public void Update(float delta)
    {
        if (scale != target_scale)
        {
            scale = Mathf.MoveTowards(scale, target_scale, delta * 2f);
        }

        Mathf.MoveTowards(current_pos, target_pos, speed, delta);
        Mathf.MoveTowards(current_offset, target_offset, speed, delta);
        Mathf.AddVector(speed, acceleration, delta);
    }

    public void Render(SpriteBatch sb)
    {
        Render(sb, color == null ? sb.getColor() : color);
    }

    public void Render(SpriteBatch sb, Color color)
    {
        RenderHelpers.DrawCentered(sb, color, texture, current_pos.x + current_offset.x, current_pos.y + current_offset.y,
                width, height, scale, current_pos.z + current_offset.z, flipX, flipY);
    }
}
