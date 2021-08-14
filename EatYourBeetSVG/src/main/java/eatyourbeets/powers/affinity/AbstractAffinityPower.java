package eatyourbeets.powers.affinity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.EYBCardTooltip;
import eatyourbeets.powers.CommonPower;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.ColoredString;
import eatyourbeets.utilities.Colors;
import eatyourbeets.utilities.JUtils;
import eatyourbeets.utilities.RenderHelpers;

public abstract class AbstractAffinityPower extends CommonPower
{
    //@Formatter: off
    @Override public final void renderIcons(SpriteBatch sb, float x, float y, Color c) { }
    @Override public final void renderAmount(SpriteBatch sb, float x, float y, Color c) { }
    //@Formatter: on

    public final Affinity affinity;
    public int amountGainedThisTurn;
    public int retainedTurns;
    public EYBCardTooltip tooltip;
    public Hitbox hb;

    protected static final int[] DEFAULT_THRESHOLDS = new int[]{3, 6, 9, 12};
    protected int thresholdIndex;
    protected abstract void OnThresholdReached(int thresholdIndex);

    public AbstractAffinityPower(Affinity affinity, String powerID)
    {
        super(null, powerID);

        this.affinity = affinity;

        //TODO: Add tooltip to EYBPower base class
        this.tooltip = new EYBCardTooltip(name, description);
        this.tooltip.subText = new ColoredString();
        this.tooltip.icon = new TextureRegion(img);

        Initialize(null);
    }

    public void Initialize(AbstractCreature owner)
    {
        this.owner = owner;
        this.enabled = true;
        this.retainedTurns = 0;
        this.thresholdIndex = 0;

        Initialize(0, PowerType.BUFF, true);
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

        this.amountGainedThisTurn += amount;
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
        final Integer threshold = GetCurrentThreshold();
        if (threshold != null)
        {
            this.description = JUtils.Format(powerStrings.DESCRIPTIONS[0] + powerStrings.DESCRIPTIONS[1], name, threshold, 1);
        }
        else
        {
            this.description = JUtils.Format(powerStrings.DESCRIPTIONS[0], name);
        }

        this.tooltip.description = description;
    }

    @Override
    public void atStartOfTurn()
    {
        super.atStartOfTurn();

        this.amountGainedThisTurn = 0;

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

    public void Render(SpriteBatch sb)
    {
        final float scale = Settings.scale;
        final float w = hb.width;
        final float h = hb.height;
        final float x = hb.x + (5 * scale);
        final float y = hb.y + (9 * scale);
        final float cX = hb.cX + (5 * scale);
        final float cY = hb.cY;

        Color amountColor;
        if (retainedTurns != 0)
        {
            RenderHelpers.DrawCentered(sb, Colors.Gold(0.7f), GR.Common.Images.Panel_Elliptical_Half_H.Texture(), cX, cY, (w / scale) + 8, (h / scale) + 8, 1, 0);
            RenderHelpers.DrawCentered(sb, Colors.Black(0.9f), GR.Common.Images.Panel_Elliptical_Half_H.Texture(), cX, cY, w / scale, h / scale, 1, 0);
            amountColor = Colors.Green(1).cpy();
        }
        else
        {
            RenderHelpers.DrawCentered(sb, Colors.Black(0.6f), GR.Common.Images.Panel_Elliptical_Half_H.Texture(), cX, cY, w / scale, h / scale, 1, 0);
            amountColor = (amount > 0 ? Colors.Blue(1) : Colors.Cream(0.6f)).cpy();
        }

        final Color imgColor = Colors.White((enabled && (retainedTurns + amount) > 0) ? 1 : 0.5f);
        RenderHelpers.DrawCentered(sb, imgColor, img, x + 16 * scale, cY + (3f * scale), 32, 32, 1, 0);

        final Integer threshold = GetCurrentThreshold();
        if (threshold != null)
        {
            FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, "/" + threshold, x + (threshold < 10 ? 70 : 75) * scale, y, 1, amount > 0 ? Colors.White(1) : amountColor);
            FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, String.valueOf(amount), x + 44 * scale, y, fontScale, amountColor);
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

    @Override
    public void update(int slot)
    {
        super.update(slot);

        hb.update();

        if (hb.hovered)
        {
            EYBCardTooltip.QueueTooltip(tooltip, InputHelper.mX + hb.width, InputHelper.mY + (hb.height * 0.5f));
        }
    }
}