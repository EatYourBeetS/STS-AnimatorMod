package eatyourbeets.powers.affinity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardTooltip;
import eatyourbeets.powers.EYBClickablePower;
import eatyourbeets.powers.PowerTriggerConditionType;
import eatyourbeets.resources.CardTooltips;
import eatyourbeets.resources.GR;
import eatyourbeets.ui.animator.combat.EYBCardAffinityRow;
import eatyourbeets.utilities.ColoredString;
import eatyourbeets.utilities.Colors;
import eatyourbeets.utilities.JUtils;
import eatyourbeets.utilities.RenderHelpers;

import java.text.DecimalFormat;
import java.util.ArrayList;

public abstract class AbstractAffinityPower extends EYBClickablePower
{
    public static String CreateFullID(Class<? extends AbstractAffinityPower> type)
    {
        return GR.Common.CreateID(type.getSimpleName());
    }

    public static final int BASE_THRESHOLD = 3;
    public static final Color ACTIVE_COLOR = new Color(0.5f, 1f, 0.5f, 1f);
    private static final DecimalFormat decimalFormat = new DecimalFormat("0.##");
    //@Formatter: off
    @Override public final void renderIcons(SpriteBatch sb, float x, float y, Color c) { }
    @Override public final void renderAmount(SpriteBatch sb, float x, float y, Color c) { }
    //@Formatter: on

    public final Affinity affinity;
    public final ArrayList<EYBCardTooltip> tooltips = new ArrayList<>();
    public int amountGainedThisTurn;
    public int effectMultiplier;
    public int gainMultiplier;
    public int scalingMultiplier;
    public int baseMaxAmount = BASE_THRESHOLD;
    public boolean forceEnableThisTurn;
    public boolean isActive;
    public Hitbox hb;

    private static final StringBuilder builder = new StringBuilder();

    public AbstractAffinityPower(Affinity affinity, String powerID)
    {
        super(null, powerID, PowerTriggerConditionType.Special, BASE_THRESHOLD, null, null);
        this.triggerCondition.checkCondition = (__) -> {return this.amount >= this.triggerCondition.requiredAmount;};
        this.triggerCondition.payCost = this::TrySpend;

        this.maxAmount = baseMaxAmount;
        this.affinity = affinity;

        //TODO: Add tooltip to EYBPower base class
        EYBCardTooltip tooltip = new EYBCardTooltip(name, description);
        tooltip.subText = new ColoredString();
        tooltip.icon = new TextureRegion(img);
        tooltips.add(tooltip);

        FindTooltipsFromText(powerStrings.DESCRIPTIONS[1]);

        Initialize(null);
    }

    public void Initialize(AbstractCreature owner)
    {
        this.owner = owner;
        this.enabled = true;
        this.effectMultiplier = 1;
        this.gainMultiplier = 1;
        this.scalingMultiplier = 1;
        this.maxAmount = baseMaxAmount;

        Initialize(0, PowerType.BUFF, true);
    }

    public void OnUsingCard(AbstractCard c, AbstractPlayer p, AbstractMonster m) {}

    public void OnUse(AbstractMonster m, int cost) {
        this.isActive = true;
        this.SetMaxAmount(maxAmount + 1);
    }

    public void SetMaxAmount(int threshold) {
        this.maxAmount = threshold;
    }

    public void SetPayCost(int threshold) {
        this.triggerCondition.requiredAmount = threshold;
        this.baseMaxAmount = this.maxAmount = threshold;
    }

    public void SetEffectMultiplier(int effectMultiplier) {
        this.effectMultiplier = effectMultiplier;
    }

    public void SetGainMultiplier(int gainMultiplier) {
        this.gainMultiplier = gainMultiplier;
    }

    public void SetScalingMultiplier(int scalingMultiplier) {
        this.scalingMultiplier = scalingMultiplier;
    }

    public void Stack(int amount, boolean maintain)
    {
        if (!IsEnabled())
        {
            return;
        }

        if (maintain)
        {
            Maintain();
        }

        amount *= this.gainMultiplier;
        super.stackPower(amount, false);
        this.amountGainedThisTurn += amount;
    }

    public Integer GetEffectiveScaling() {
        return GetEffectiveLevel() * this.scalingMultiplier;
    }

    protected float GetEffectiveIncrease() {
        return this.effectMultiplier;
    }

    public Integer GetEffectiveLevel() {
        return Math.max(0, maxAmount - baseMaxAmount);
    }

    protected float GetEffectiveMultiplier() {
        return 1f + GetEffectiveIncrease();
    }

    @Override
    public String GetUpdatedDescription()
    {
        String newDesc = FormatDescription(0, EYBCardAffinityRow.SYNERGY_MULTIPLIER, this.triggerCondition.requiredAmount, GetMultiplierForDescription(), !IsEnabled() ? powerStrings.DESCRIPTIONS[1] : "");
        this.tooltips.get(0).description = newDesc;
        return newDesc;
    }

    protected int GetMultiplierForDescription() {
        return (int) (GetEffectiveIncrease() * 100);
    }

    public boolean IsEnabled() {
        return enabled || forceEnableThisTurn;
    }

    public boolean CanSpend(int amount) {
        return IsEnabled() && this.amount >= amount;
    }

    public boolean TrySpend(int amount)
    {
        if (CanSpend(amount)) {
            super.stackPower(-amount, false);

            return true;
        }
        return false;
    }

    protected boolean TryUse(AbstractCard card) {
        if (CanSpend(this.triggerCondition.requiredAmount) && (!(card instanceof AnimatorCard) || ((AnimatorCard) card).cardData.CanTriggerSupercharge))
        {
            amount -= this.triggerCondition.requiredAmount;
            updateDescription();
            flash();
            return true;
        }
        return false;
    }

    public void Maintain() {
        this.forceEnableThisTurn = true;
    }

    @Override
    public void atStartOfTurn()
    {
        super.atStartOfTurn();

        this.amountGainedThisTurn = 0;
        this.forceEnableThisTurn = false;
        enabled = true;
        isActive = false;
    }

    public void Render(SpriteBatch sb)
    {
        final float scale = Settings.scale;
        final float w = hb.width;
        final float h = hb.height;
        final float x = hb.x + (5 * scale);
        final float y = hb.y + (9 * scale);
        final float cX = hb.cX + (15 * scale);
        final float cY = hb.cY;

        Integer level = GetEffectiveLevel();
        Color amountColor = !IsEnabled() ? Colors.Cream(0.6f) : amount >= this.triggerCondition.requiredAmount ? Colors.Gold(1).cpy() : Colors.White(1f);
        Color levelColor = level > 0 ? Colors.Green(1).cpy() : Colors.Cream(0.6f);
        if (effectMultiplier > 1)
        {
            RenderHelpers.DrawCentered(sb, Colors.Gold(0.7f), GR.Common.Images.Panel_Elliptical_Half_H.Texture(), cX, cY, (w / scale) + 8, (h / scale) + 8, 1, 0);
            RenderHelpers.DrawCentered(sb, Colors.Black(0.9f), GR.Common.Images.Panel_Elliptical_Half_H.Texture(), cX, cY, w / scale, h / scale, 1, 0);
        }
        else
        {
            RenderHelpers.DrawCentered(sb, Colors.Black(0.6f), GR.Common.Images.Panel_Elliptical_Half_H.Texture(), cX, cY, w / scale, h / scale, 1, 0);
        }

        final Color imgColor = Colors.White(IsEnabled() ? 1 : 0.5f);
        final Color borderColor = isActive ? ACTIVE_COLOR : (enabled && triggerCondition.CanUse()) ? imgColor : disabledColor;

        super.renderIconsImpl(sb, x + 16 * scale, cY + (3f * scale), borderColor, imgColor);
        FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, "L" + String.valueOf(level), x + 36 * scale, y - 8 * scale, fontScale, levelColor);

        if (maxAmount > 0)
        {
            FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, "/" + maxAmount, x + (maxAmount < 10 ? 90 : 95) * scale, y, 1, amountColor);
            FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, String.valueOf(amount), x + 64 * scale, y, fontScale, amountColor);
        }
        else
        {
            FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, String.valueOf(amount), x + 72 * scale, y, fontScale, amountColor);
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
                EYBCardTooltip tooltip = CardTooltips.FindByID(JUtils.InvokeBuilder(builder));
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