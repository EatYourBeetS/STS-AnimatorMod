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
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.CommonPower;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.*;

public abstract class AbstractAffinityPower extends CommonPower
{
    public final Affinity affinity;
    public final String symbol;
    public int amountGainedThisTurn;
    public int retainedTurns;
    public int minimumAmount;
    public EYBCardTooltip tooltip;
    public Hitbox hb;

    protected static final int[] DEFAULT_THRESHOLDS = new int[]{ 3, 6, 9, 12 };
    protected static final int MAX_AMOUNT = 20;
    protected int thresholdIndex;
    protected int thresholdBonusAmount;

    public AbstractAffinityPower(Affinity affinity, String powerID, String symbol)
    {
        super(null, powerID);

        this.minimumAmount = 0;
        this.maxAmount = MAX_AMOUNT;
        this.affinity = affinity;
        this.symbol = symbol;
        this.canBeZero = true;

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
        this.minimumAmount = 0;
        this.maxAmount = MAX_AMOUNT;

        Initialize(0, PowerType.BUFF, true);
    }

    public void RetainOnce()
    {
        if (this.retainedTurns == 0)
        {
            this.retainedTurns = 1;
        }
    }

    public void IncreaseMinimumAmount(int amount)
    {
        this.minimumAmount = Mathf.Clamp(minimumAmount + amount, 0, maxAmount);
        updateDescription();
//        if (this.amount < minimumAmount)
//        {
//            Stack(minimumAmount - this.amount, false);
//        }
    }

    public void DecreaseMaximumAmount(int amount)
    {
        SetMaximumAmount(maxAmount - amount);
    }

    public void SetMaximumAmount(int amount)
    {
        final int[] thresholds = GetThresholds();
        this.maxAmount = Mathf.Clamp(amount, thresholds[thresholds.length - 2], MAX_AMOUNT);
        if (minimumAmount > maxAmount)
        {
            minimumAmount = maxAmount;
        }
        if (this.amount > maxAmount)
        {
            reducePower(this.amount - maxAmount);
        }
    }

    public void Retain(int turns, boolean relative)
    {
        this.retainedTurns = (relative ? (retainedTurns + turns) : turns);
    }

    public void Stack(int amount, boolean retain)
    {
        if (!enabled)
        {
            return;
        }

        if (this.amount >= maxAmount || retain)
        {
            RetainOnce();
        }

        if (amount > 0)
        {
            stackPower(amount, true);
        }
    }

    public void stackPower(int stackAmount, boolean updateBaseAmount)
    {
        super.stackPower(stackAmount, updateBaseAmount);
        this.amountGainedThisTurn += stackAmount;
        UpdateThreshold();
    }

    public void Reduce(int reduceAmount, boolean ignoreMinimumAmount)
    {
        if (!ignoreMinimumAmount)
        {
            final int newAmount = amount - reduceAmount;
            if (newAmount < minimumAmount)
            {
                reduceAmount = Mathf.Max(0, reduceAmount - (minimumAmount - newAmount));
            }
        }

        super.reducePower(reduceAmount);
    }

    public Integer GetCurrentThreshold()
    {
        final int[] thresholds = GetThresholds();
        return (thresholdIndex < thresholds.length) ? thresholds[thresholdIndex] : null;
    }

    public int GetThresholdLevel()
    {
        return thresholdIndex;
    }

    public int[] GetThresholds()
    {
        return DEFAULT_THRESHOLDS;
    }

    protected void UpdateThreshold()
    {
        final int[] thresholds = GetThresholds();
        for (int i = thresholdIndex; i < thresholds.length; i++)
        {
            if (amount >= thresholds[i])
            {
                if (minimumAmount < thresholds[i])
                {
                    minimumAmount = thresholds[i];
                }

                thresholdIndex += 1;
                OnThresholdReached();
                CombatStats.OnAffinityThresholdReached(this, thresholdIndex);
            }
        }

        updateDescription();
    }

    @Override
    public final void updateDescription()
    {
        this.description = GetUpdatedDescription() + " NL (Min #b" + minimumAmount + "^, Max #b" + maxAmount + "^)"; // TODO: localization
        this.tooltip.description = this.description;
    }

    protected String GetUpdatedDescription()
    {
        String description = powerStrings.DESCRIPTIONS[0];

        final Integer threshold = GetCurrentThreshold();
        if (threshold != null)
        {
            description += FormatDescription(1, threshold, 1);
        }

        return description;
    }

    @Override
    public void atStartOfTurn()
    {
        super.atStartOfTurn();

        this.amountGainedThisTurn = 0;

        if (CanDecrease())
        {
            reducePower(1);
        }
        else if (this.retainedTurns > 0)
        {
            this.retainedTurns -= 1;
        }
    }

    @Override
    public final void renderIcons(SpriteBatch sb, float x, float y, Color c)
    {
        if (!CombatStats.Affinities.isActive)
        {
            super.renderIcons(sb, x, y, c);
        }
    }

    @Override
    public final void renderAmount(SpriteBatch sb, float x, float y, Color c)
    {
        if (!CombatStats.Affinities.isActive)
        {
            final Integer threshold = GetCurrentThreshold();
            final Color c1 = (retainedTurns != 0 ? Colors.Green(c.a) : Colors.Blue(c.a)).cpy();
            if (threshold != null)
            {
                final float offset_x = -24 * Settings.scale;
                final float offset_y = -5 * Settings.scale;
                final float offset_x2 = 0 * Settings.scale;
                final float offset_y2 = -5 * Settings.scale;
                final Color c2 = Colors.Cream(c.a).cpy();
                FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, " /" + threshold, x + offset_x2, y + offset_y2, 1, c2);
                FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, Integer.toString(this.amount), x + offset_x, y + offset_y, fontScale, c1);
            }
            else
            {
                super.renderAmount(sb, x, y, c1);
            }
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
        if (!CanDecrease())
        {
            RenderHelpers.DrawCentered(sb, Colors.Gold(0.7f), GR.Common.Images.Panel_Elliptical_Half_H.Texture(), cX, cY, (w / scale) + 8, (h / scale) + 8, 1, 0);
            RenderHelpers.DrawCentered(sb, Colors.Black(0.9f), GR.Common.Images.Panel_Elliptical_Half_H.Texture(), cX, cY, w / scale, h / scale, 1, 0);
            amountColor = Colors.Green(1).cpy();
        }
        else
        {
            RenderHelpers.DrawCentered(sb, Colors.Black(0.6f), GR.Common.Images.Panel_Elliptical_Half_H.Texture(), cX, cY, w / scale, h / scale, 1, 0);
            amountColor = (amount > minimumAmount ? Colors.Blue(1) : Colors.Cream(minimumAmount > 0 ? 1 : 0.6f)).cpy();
        }

        final Color imgColor = Colors.White((enabled && (retainedTurns + amount) > 0) ? 1 : 0.5f);
        RenderHelpers.DrawCentered(sb, imgColor, img, x + 16 * scale, cY + (3f * scale), 32, 32, 1, 0);

        final Integer threshold = GetCurrentThreshold();
        if (threshold != null && maxAmount >= threshold)
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

        if (hb != null)
        {
            hb.update();

            if (hb.hovered)
            {
                EYBCardTooltip.QueueTooltip(tooltip, InputHelper.mX + hb.width, InputHelper.mY + (hb.height * 0.5f), false);
            }
        }
    }

    protected PowerHelper GetThresholdBonusPower()
    {
        return null;
    }

    protected void OnThresholdReached()
    {
        final PowerHelper powerHelper = GetThresholdBonusPower();
        if (powerHelper == null)
        {
            throw new RuntimeException(name + " must override either GetThresholdBonusPower or RefreshThresholdBonus.");
        }

        GameActions.Top.StackPower(TargetHelper.Source(owner), powerHelper, 1)
        .ShowEffect(true, true);
    }

    protected boolean CanDecrease()
    {
        return amount > minimumAmount && retainedTurns != 0;
    }
}