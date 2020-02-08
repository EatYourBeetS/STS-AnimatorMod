package eatyourbeets.ui.controls;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import eatyourbeets.interfaces.csharp.ActionT1;
import eatyourbeets.ui.GUIElement;

public class GUI_VerticalScrollBar extends GUIElement
{
    protected final float cursorWidth;
    protected final float cursorHeight;
    protected final float borderHeight;
    protected final Hitbox hb;

    public final GUI_Image topImage = new GUI_Image(ImageMaster.SCROLL_BAR_TOP);
    public final GUI_Image centerImage = new GUI_Image(ImageMaster.SCROLL_BAR_MIDDLE);
    public final GUI_Image bottomImage = new GUI_Image(ImageMaster.SCROLL_BAR_BOTTOM);
    public final GUI_Image cursorImage = new GUI_Image(ImageMaster.SCROLL_BAR_TRAIN);

    public ActionT1<Float> onScroll;
    public boolean isBackgroundVisible;
    public boolean isDragging;
    public float currentScrollPercent;
    public float cursorDrawPosition;

    public GUI_VerticalScrollBar(Hitbox hb)
    {
        this.isBackgroundVisible = true;
        this.cursorWidth = hb.width * 0.7f;
        this.cursorHeight = hb.width * 1.1f;
        this.borderHeight = cursorHeight * 0.25f;
        this.hb = hb;
    }

    public GUI_VerticalScrollBar SetPosition(float x, float y)
    {
        this.hb.move(x, y);

        return this;
    }

    public GUI_VerticalScrollBar SetOnScroll(ActionT1<Float> onScroll)
    {
        this.onScroll = onScroll;

        return this;
    }

    public void Update()
    {
        cursorDrawPosition = MathHelper.scrollSnapLerpSpeed(cursorDrawPosition, FromPercentage(currentScrollPercent));

        hb.update();

        if (isDragging)
        {
            if (InputHelper.justReleasedClickLeft)
            {
                isDragging = false;
            }
            else
            {
                Scroll(ToPercentage(InputHelper.mY), true);
            }
        }
        else if (hb.hovered && InputHelper.isMouseDown)
        {
            isDragging = true;
        }
    }

    public void Scroll(float percent, boolean triggerEvent)
    {
        currentScrollPercent = Clamp(percent);

        if (triggerEvent && onScroll != null)
        {
            onScroll.Invoke(currentScrollPercent);
        }
    }

    public void Render(SpriteBatch sb)
    {
        if (isBackgroundVisible)
        {
            centerImage.Render(sb, hb);
            topImage.Render(sb, hb.x, hb.y + hb.height, hb.width, topImage.srcHeight);
            bottomImage.Render(sb, hb.x, hb.y - bottomImage.srcHeight, hb.width, bottomImage.srcHeight);
        }

        cursorImage.Render(sb,hb.cX - (cursorWidth / 2f), cursorDrawPosition, cursorWidth, cursorHeight);

        hb.render(sb);
    }

    private float ToPercentage(float position)
    {
        float minY = this.hb.y + this.hb.height - borderHeight;
        float maxY = this.hb.y + borderHeight;

        return Clamp(MathHelper.percentFromValueBetween(minY, maxY, position));
    }

    private float FromPercentage(float percent)
    {
        float topY = this.hb.y + this.hb.height - cursorHeight + borderHeight;
        float bottomY = this.hb.y - borderHeight;

        return MathHelper.valueFromPercentBetween(topY, bottomY, Clamp(percent));
    }

    private static float Clamp(float percent)
    {
        return Math.max(0.0F, Math.min(percent, 1.0F));
    }
}
