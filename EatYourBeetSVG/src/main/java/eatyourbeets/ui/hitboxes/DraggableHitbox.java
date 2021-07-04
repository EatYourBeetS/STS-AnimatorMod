package eatyourbeets.ui.hitboxes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.JUtils;

public class DraggableHitbox extends AdvancedHitbox
{
    protected Vector2 dragStart = null;

    public boolean canDrag;

    public DraggableHitbox(Hitbox hb)
    {
        this(hb.x, hb.y, hb.width, hb.height, true);
    }

    public DraggableHitbox(float width, float height)
    {
        this(-9999, -9999, width, height, true);
    }

    public DraggableHitbox(Hitbox hb, float width, float height)
    {
        this(hb.x, hb.y, width, height, true);
    }

    public DraggableHitbox(float x, float y, float width, float height)
    {
        this(x, y, width, height, true);
    }

    public DraggableHitbox(float x, float y, float width, float height, boolean canDrag)
    {
        super(x, y, width, height);

        this.canDrag = canDrag;
    }

    public DraggableHitbox SetPosition(float cX, float cY)
    {
        move(cX, cY);

        return this;
    }

    @Override
    public void update()
    {
        super.update();

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
}
