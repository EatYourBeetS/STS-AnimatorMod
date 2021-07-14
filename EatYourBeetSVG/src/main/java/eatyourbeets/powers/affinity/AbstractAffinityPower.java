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

    protected static final int[] DEFAULT_THRESHOLDS = new int[]{3, 6, 9, 12};

    protected int thresholdIndex;

    public AffinityType affinityType;
    public boolean retained;

    public AbstractAffinityPower(AffinityType type, String powerID, AbstractCreature owner, int amount)
    {
        super(owner, powerID);

        this.affinityType = type;
        this.amount = amount;
        this.thresholdIndex = 0;

        updateDescription();
    }

    protected abstract void OnThresholdReached(int threshold);

    public void Render(SpriteBatch sb, Hitbox hb)
    {
        final float w = hb.width;
        final float h = hb.height;
        final float x = hb.x + w;
        final float y = hb.y + (9 * Settings.scale);

        if (retained)
        {
            RenderHelpers.DrawCentered(sb, Settings.HALF_TRANSPARENT_WHITE_COLOR, GR.Common.Images.Panel_Rounded_Half_H.Texture(), hb.cX + w, hb.cY, w + 9, h + 9, 1, 0);
        }
        RenderHelpers.DrawCentered(sb, Color.BLACK, GR.Common.Images.Panel_Rounded_Half_H.Texture(), hb.cX + w, hb.cY, w, h, 1, 0);
        RenderHelpers.DrawCentered(sb, Color.WHITE, img, x + 16 * Settings.scale, hb.cY + (3f * Settings.scale), 32, 32, 1, 0);

        final Integer threshold = GetCurrentThreshold();
        final Color textColor = amount == 0 ? Settings.CREAM_COLOR : retained ? Settings.BLUE_TEXT_COLOR : Settings.GREEN_TEXT_COLOR;
        if (threshold != null)
        {
            FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, "/" + threshold, x + (threshold < 10 ? 70 : 75) * Settings.scale, y, 1, Settings.CREAM_COLOR);
            FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, String.valueOf(amount), x + 44 * Settings.scale, y, fontScale, textColor);
        }
        else
        {
            FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, String.valueOf(amount), x + 52 * Settings.scale, y, fontScale, textColor);
        }

        for (AbstractGameEffect e : effects)
        {
            e.render(sb, x, hb.y);
        }
    }

    public void Retain()
    {
        this.retained = true;
    }

    public void Stack(int amount, boolean retain)
    {
        this.amount += amount;
        this.fontScale = 8f;

        if (retain)
        {
            Retain();
        }

        UpdateThreshold();
    }

    public Integer GetCurrentThreshold()
    {
        int[] thresholds = GetThresholds();
        if (thresholdIndex < thresholds.length)
        {
            return thresholds[thresholdIndex];
        }

        return null;
    }

    public int[] GetThresholds()
    {
        return DEFAULT_THRESHOLDS;
    }

    public void Initialize(AbstractCreature owner)
    {
        this.owner = owner;
        this.amount = 0;
        this.enabled = true;
        this.retained = false;
        this.thresholdIndex = 0;
        this.updateDescription();
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

        int[] thresholds = GetThresholds();
        if (thresholdIndex < thresholds.length)
        {
            this.description = JUtils.Format(description + powerStrings.DESCRIPTIONS[1], name, thresholds[thresholdIndex], 1);
        }
        else
        {
            this.description = JUtils.Format(description, name, thresholdIndex, 1);
        }
    }

    @Override
    public void atStartOfTurn()
    {
        super.atStartOfTurn();

        if (!retained && amount > 0)
        {
            reducePower(1);
        }
    }
}