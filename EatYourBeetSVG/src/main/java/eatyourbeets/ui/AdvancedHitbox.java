package eatyourbeets.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.JUtils;

public class AdvancedHitbox extends Hitbox
{
    protected Vector2 dragStart = null;

    public float target_cX;
    public float target_cY;
    public boolean canDrag;

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
        this(x, y, width, height, false);
    }

    public AdvancedHitbox(float x, float y, float width, float height, boolean canDrag)
    {
        super(x, y, width, height);

        this.target_cX = cX;
        this.target_cY = cY;
        this.canDrag = canDrag;
    }

    public AdvancedHitbox SetPosition(float cX, float cY)
    {
        move(cX, cY);

        return this;
    }

    public AdvancedHitbox SetDraggable(boolean draggable)
    {
        this.canDrag = draggable;

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

        if (canDrag)
        {
            float mX = Gdx.input.getX();
            float mY = Settings.HEIGHT - Gdx.input.getY();

            if (hovered || dragStart != null)
            {
                if (InputHelper.justClickedLeft)
                {
                    if (GR.UI.TryDragging())
                    {
                        dragStart = new Vector2(mX, mY);
                        return;
                    }
                }
                else if (!InputHelper.justReleasedClickLeft && dragStart != null)
                {
                    final float max_X = Settings.WIDTH + (width * 0.25f);
                    final float min_X = -width * 0.25f;
                    final float max_Y = Settings.HEIGHT + (height * 0.25f);
                    final float min_Y = -height * 0.25f;

                    target_cX = Math.min(max_X, Math.max(min_X, target_cX + (mX - dragStart.x)));
                    target_cY = Math.min(max_Y, Math.max(min_Y, target_cY + (mY - dragStart.y)));

                    if (GR.UI.TryDragging())
                    {
                        dragStart.set(mX, mY);
                        return;
                    }
                }
            }
        }

        if (dragStart != null)
        {
            if (Settings.isDebug)
            {
                float xPercentage  = x  * 100f / Settings.WIDTH;
                float yPercentage  = y  * 100f / Settings.HEIGHT;
                float cxPercentage = cX * 100f / Settings.WIDTH;
                float cyPercentage = cY * 100f / Settings.HEIGHT;

                JUtils.LogInfo(this, "x  = {0}({1}%) , y  = {2}({3}%)", x, xPercentage, y, yPercentage);
                JUtils.LogInfo(this, "cX = {0}({1}%) , cY = {2}({3}%)", cX, cxPercentage, cY, cyPercentage);
            }

            dragStart = null;
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

    private float Lerp(float current, float target)
    {
        float lerp = MathUtils.lerp(current, target, Gdx.graphics.getDeltaTime() * 9f);
        if (Math.abs(current - target) < Settings.UI_SNAP_THRESHOLD)
        {
            return target;
        }
        else
        {
            return lerp;
        }
    }
}
