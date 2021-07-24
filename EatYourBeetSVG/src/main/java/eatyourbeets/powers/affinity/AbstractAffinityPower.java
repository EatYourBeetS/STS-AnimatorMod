package eatyourbeets.powers.affinity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import eatyourbeets.cards.base.AffinityType;
import eatyourbeets.powers.CommonPower;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.JUtils;
import eatyourbeets.utilities.RenderHelpers;

public abstract class AbstractAffinityPower extends CommonPower
{
    //@Formatter: off
    @Override public final void renderIcons(SpriteBatch sb, float x, float y, Color c) { }
    @Override public final void renderAmount(SpriteBatch sb, float x, float y, Color c) { }
    @Override public void update(int slot) { super.update(slot); }
    //@Formatter: on

    public final AffinityType affinityType;
    public int retainedTurns;

    protected static final int[] DEFAULT_THRESHOLDS = new int[]{3, 6, 9, 12};
    protected int thresholdIndex;
    protected abstract void OnThresholdReached(int threshold);

    public AbstractAffinityPower(AffinityType type, String powerID)
    {
        super(null, powerID);

        this.affinityType = type;

        Initialize(null);
    }

    public void Initialize(AbstractCreature owner)
    {
        this.owner = owner;
        this.amount = 0;
        this.enabled = true;
        this.retainedTurns = 0;
        this.thresholdIndex = 0;
        this.updateDescription();
    }

    public void RetainOnce()
    {
        if (this.retainedTurns == 0)
        {
            this.retainedTurns = 1;
        }
    }

    public void Retain(int turns, boolean relative)
    {
        this.retainedTurns = (relative ? (retainedTurns + turns) : turns);
    }

    public void Stack(int amount, boolean retain)
    {
        if (!enabled && amount > 0)
        {
            return;
        }

        this.amount += amount;
        this.fontScale = 8f;

        if (retain)
        {
            RetainOnce();
        }

        UpdateThreshold();
    }

    public Integer GetCurrentThreshold()
    {
        int[] thresholds = GetThresholds();
        return (thresholdIndex < thresholds.length) ? thresholds[thresholdIndex] : null;
    }

    public int[] GetThresholds()
    {
        return DEFAULT_THRESHOLDS;
    }

    protected void UpdateThreshold()
    {
        int[] thresholds = GetThresholds();
        for (int i = thresholdIndex; i < thresholds.length; i++)
        {
            if (amount >= thresholds[i])
            {
                OnThresholdReached(i);
                thresholdIndex += 1;
            }
        }

        updateDescription();
    }

    @Override
    public void updateDescription()
    {
        this.description = powerStrings.DESCRIPTIONS[0];

        final Integer threshold = GetCurrentThreshold();
        if (threshold != null)
        {
            this.description = JUtils.Format(description + powerStrings.DESCRIPTIONS[1], name, threshold, 1);
        }
        else
        {
            this.description = JUtils.Format(description, name);
        }
    }

    @Override
    public void atStartOfTurn()
    {
        super.atStartOfTurn();

        if (this.retainedTurns == 0)
        {
            if (amount > 0)
            {
                reducePower(1);
            }
        }
        else if (this.retainedTurns > 0)
        {
            this.retainedTurns -= 1;
        }
    }

    public void Render(SpriteBatch sb, Hitbox hb)
    {
        final float scale = Settings.scale;
        final float w = hb.width;
        final float h = hb.height;
        final float x = hb.x + w + (5 * scale);
        final float y = hb.y + (9 * scale);
        final float cX = hb.cX + w + (5 * scale);
        final float cY = hb.cY;

        Color amountColor = Settings.BLUE_TEXT_COLOR;
        if (retainedTurns != 0)
        {
            RenderHelpers.DrawCentered(sb, RenderHelpers.CopyColor(Settings.GOLD_COLOR, 0.7f), GR.Common.Images.Panel_Elliptical_Half_H.Texture(), cX, cY, (w / scale) + 8, (h / scale) + 8, 1, 0);
            RenderHelpers.DrawCentered(sb, RenderHelpers.BlackColor(0.9f), GR.Common.Images.Panel_Elliptical_Half_H.Texture(), cX, cY, w / scale, h / scale, 1, 0);
            amountColor = Settings.GREEN_TEXT_COLOR;
        }
        else
        {
            RenderHelpers.DrawCentered(sb, RenderHelpers.BlackColor(0.6f), GR.Common.Images.Panel_Elliptical_Half_H.Texture(), cX, cY, w / scale, h / scale, 1, 0);
        }

        RenderHelpers.DrawCentered(sb, new Color(1, 1, 1, enabled ? 1 : 0.5f), img, x + 16 * scale, cY + (3f * scale), 32, 32, 1, 0);

        final Integer threshold = GetCurrentThreshold();
        if (threshold != null)
        {
            FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, "/" + threshold, x + (threshold < 10 ? 70 : 75) * scale, y, 1, Settings.CREAM_COLOR);
            FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, String.valueOf(amount), x + 44 * scale, y, fontScale, amount == 0 ? Settings.CREAM_COLOR : amountColor);
        }
        else
        {
            FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, String.valueOf(amount), x + 52 * scale, y, fontScale, amountColor);
        }

        for (AbstractGameEffect e : effects)
        {
            e.render(sb, x + w + (5 * scale), cY + (5f * scale));
        }
    }
}