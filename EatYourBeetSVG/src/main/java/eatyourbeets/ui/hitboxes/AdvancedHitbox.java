package eatyourbeets.ui.hitboxes;

import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.Hitbox;

public class AdvancedHitbox extends Hitbox
{
    public float lerpSpeed;
    public float target_cX;
    public float target_cY;

    public AdvancedHitbox(Hitbox hb)
    {
        this(hb.x, hb.y, hb.width, hb.height);
    }

    public AdvancedHitbox(float width, float height)
    {
        this(-9999, -9999, width, height);
    }

    public AdvancedHitbox(Hitbox hb, float width, float height)
    {
        this(hb.x, hb.y, width, height);
    }

    public AdvancedHitbox(float x, float y, float width, float height)
    {
        super(x, y, width, height);

        this.target_cX = cX;
        this.target_cY = cY;
        this.lerpSpeed = 9f;
    }

    public AdvancedHitbox SetPosition(float cX, float cY)
    {
        move(cX, cY);

        return this;
    }

    public AdvancedHitbox SetTargetPosition(float cX, float cY)
    {
        this.target_cX = cX;
        this.target_cY = cY;

        return this;
    }

    @Override
    public void update()
    {
        super.update();

        if (cX != target_cX || cY != target_cY)
        {
            moveInternal(Lerp(cX, target_cX), Lerp(cY, target_cY));
        }
    }

    @Override
    public void translate(float x, float y)
    {
        this.x = x;
        this.y = y;
        this.target_cX = this.cX = x + this.width / 2f;
        this.target_cY = this.cY = y + this.height / 2f;
    }

    @Override
    public void resize(float w, float h)
    {
        this.width = w;
        this.height = h;
        this.target_cX = this.cX = x + this.width / 2f;
        this.target_cY = this.cY = y + this.height / 2f;
    }

    @Override
    public void move(float cX, float cY)
    {
        this.target_cX = this.cX = cX;
        this.target_cY = this.cY = cY;
        this.x = cX - this.width / 2f;
        this.y = cY - this.height / 2f;
    }

    protected void moveInternal(float cX, float cY)
    {
        this.cX = cX;
        this.cY = cY;
        this.x = cX - this.width / 2f;
        this.y = cY - this.height / 2f;
    }

    protected float Lerp(float current, float target)
    {
        if (lerpSpeed < 0 || Math.abs(current - target) < Settings.UI_SNAP_THRESHOLD)
        {
            return target;
        }

        float lerp = (target - current) * Gdx.graphics.getDeltaTime() * lerpSpeed;
        float result = current + lerp;
        if (current < target)
        {
            return result > target ? target : result;
        }

        return result < target ? target : result;
    }
}