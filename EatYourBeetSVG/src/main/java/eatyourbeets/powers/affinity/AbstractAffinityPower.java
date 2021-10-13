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
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.resources.CardTooltips;
import eatyourbeets.utilities.*;

import java.util.ArrayList;

public abstract class AbstractAffinityPower extends CommonPower
{
    //@Formatter: off
    @Override public final void renderIcons(SpriteBatch sb, float x, float y, Color c) { }
    @Override public final void renderAmount(SpriteBatch sb, float x, float y, Color c) { }
    //@Formatter: on

    public static final Color COLOR_HIGHLIGHT_WEAK = new Color(0.5f, 0.5f, 0.5f, 0.75f);
    public static final Color COLOR_HIGHLIGHT_STRONG = new Color(0.75f, 0.75f, 0.35f, 0.75f);

    public final Affinity affinity;
    public final ArrayList<EYBCardTooltip> tooltips = new ArrayList<>();
    public int amountGainedThisTurn;
    public int retainedTurns;
    public boolean forceEnableThisTurn;
    public Hitbox hb;

    private static final StringBuilder builder = new StringBuilder();
    protected static final int THRESHOLD_MULTIPLIER = 15;
    protected static final int MAX_STAT_AMOUNT = 999;
    protected int thresholdIndex;
    protected int thresholdBonusAmount;
    protected int thresholdBonusModifier;

    public AbstractAffinityPower(Affinity affinity, String powerID)
    {
        super(null, powerID);

        this.affinity = affinity;

        //TODO: Add tooltip to EYBPower base class
        EYBCardTooltip tooltip = new EYBCardTooltip(name, description);
        tooltip.subText = new ColoredString();
        tooltip.icon = new TextureRegion(img);
        tooltips.add(tooltip);

        FindTooltipsFromText(powerStrings.DESCRIPTIONS[0]);

        Initialize(null);
    }

    public void Initialize(AbstractCreature owner)
    {
        this.owner = owner;
        this.enabled = true;
        this.retainedTurns = 0;
        this.thresholdIndex = 0;
        this.thresholdBonusAmount = 0;
        this.thresholdBonusModifier = 0;

        Initialize(0, PowerType.BUFF, true);
    }

    public void Maintain() {
        RetainOnce();
        this.forceEnableThisTurn = true;
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
        if (!enabled && !forceEnableThisTurn)
        {
            return;
        }

        if (this.amount == maxAmount || retain)
        {
            RetainOnce();
        }

        if (amount > 0)
        {
            super.stackPower(amount, true);
            this.amountGainedThisTurn += amount;
        }

        UpdateThreshold();
    }

    public Integer GetCurrentThreshold()
    {
        final int threshold = GetThresholdMultiplier();
        return (amount < MAX_STAT_AMOUNT) ? (thresholdIndex + 1) * threshold : null;
    }

    public int GetThresholdMultiplier()
    {
        return THRESHOLD_MULTIPLIER;
    }

    public void AddThresholdBonusModifier(int amount)
    {
        RefreshThresholdBonus(false, amount);
    }

    protected void UpdateThreshold()
    {
        Integer threshold = GetCurrentThreshold();
        while (threshold != null && threshold <= amount)
        {
                thresholdIndex += 1;
                RefreshThresholdBonus(true, 0);
                threshold = GetCurrentThreshold();
        }

        updateDescription();
    }

    @Override
    public final void updateDescription()
    {
        this.tooltips.get(0).description = this.description = GetUpdatedDescription();
    }

    protected String GetUpdatedDescription()
    {
        String description = powerStrings.DESCRIPTIONS[0];

        final Integer threshold = GetCurrentThreshold();
        if (threshold != null)
        {
            return FormatDescription(0, threshold, 1);
        }

        return description;
    }

    @Override
    public void atStartOfTurn()
    {
        super.atStartOfTurn();

        this.amountGainedThisTurn = 0;
        this.forceEnableThisTurn = false;

        //Should not be used
        if (this.retainedTurns > 0)
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
            //RenderHelpers.DrawCentered(sb, Colors.Gold(0.7f), GR.Common.Images.Panel_Elliptical_Half_H.Texture(), cX, cY, (w / scale) + 8, (h / scale) + 8, 1, 0);
            //RenderHelpers.DrawCentered(sb, Colors.Black(0.9f), GR.Common.Images.Panel_Elliptical_Half_H.Texture(), cX, cY, w / scale, h / scale, 1, 0);
            amountColor = Colors.Green(1).cpy();
        }
        else
        {
            //RenderHelpers.DrawCentered(sb, Colors.Black(0.6f), GR.Common.Images.Panel_Elliptical_Half_H.Texture(), cX, cY, w / scale, h / scale, 1, 0);
            amountColor = (amount > 0 ? Colors.Blue(1) : Colors.Cream(0.6f)).cpy();
        }

        final Color imgColor = Colors.White(((enabled || forceEnableThisTurn) && (retainedTurns + amount) > 0) ? 1 : 0.5f);
        RenderHelpers.DrawCentered(sb, imgColor, img, x + 16 * scale, cY + (3f * scale), 32, 32, 1, 0);

        final Integer threshold = GetCurrentThreshold();
        if (threshold != null)
        {
            FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, "/" + threshold, x + 90 * scale, y, 1, amount > 0 ? Colors.White(1) : amountColor);
            FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, String.valueOf(amount), x + 55 * scale, y, fontScale, amountColor);
        }
        else
        {
            FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, String.valueOf(amount), x + 60 * scale, y, fontScale, amountColor);
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
            EYBCardTooltip.QueueTooltips(tooltips, InputHelper.mX + hb.width, InputHelper.mY + (hb.height * 0.5f));
        }
    }

    public PowerHelper GetThresholdBonusPower()
    {
        return null;
    }

    public void RefreshThresholdBonus(boolean thresholdReached, int addModifier)
    {
        final PowerHelper powerHelper = GetThresholdBonusPower();
        if (powerHelper == null)
        {
            throw new RuntimeException(name + " must override either GetThresholdBonusPower or RefreshThresholdBonus.");
        }

        int oldAmount = thresholdBonusAmount + thresholdBonusModifier;

        if (thresholdReached)
        {
            thresholdBonusAmount += 1;
        }

        thresholdBonusModifier += addModifier;

        final int newAmount = Math.max(0, thresholdBonusAmount + thresholdBonusModifier);
        if (newAmount > 0)
        {
            GameActions.Top.StackPower(TargetHelper.Source(owner), powerHelper, newAmount - oldAmount)
            .ShowEffect(newAmount > thresholdBonusAmount, true);
        }
    }

    protected void FindTooltipsFromText(String text) {

        boolean foundIcon = false;
        for (int i = 0; i < text.length(); i++)
        {
            char c = text.charAt(i);

            if (foundIcon) {
                if (']' != c)
                {
                    builder.append(c);
                    continue;
                }
                foundIcon = false;
                EYBCardTooltip tooltip = CardTooltips.FindByName("["+JUtils.InvokeBuilder(builder)+"]");
                if (tooltip != null) {
                    tooltips.add(tooltip);
                }
            }
            else if ('[' == c)
            {
                foundIcon = true;
            }
        }

    }
}