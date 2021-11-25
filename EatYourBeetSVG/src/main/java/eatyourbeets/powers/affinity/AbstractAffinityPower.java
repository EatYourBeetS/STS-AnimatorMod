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
import eatyourbeets.interfaces.subscribers.OnGainAffinitySubscriber;
import eatyourbeets.powers.CommonPower;
import eatyourbeets.resources.CardTooltips;
import eatyourbeets.resources.GR;
import eatyourbeets.ui.animator.combat.EYBCardAffinityRow;
import eatyourbeets.utilities.*;

import java.text.DecimalFormat;
import java.util.ArrayList;

public abstract class AbstractAffinityPower extends CommonPower implements OnGainAffinitySubscriber
{
    public static final int BASE_CHARGE_THRESHOLD = 5;
    public static final int BASE_EXPERIENCE_PER_LEVEL = 10;
    public static final int MULTIPLIER_PER_LEVEL = 50;
    private static final DecimalFormat decimalFormat = new DecimalFormat("0.##");
    //@Formatter: off
    @Override public final void renderIcons(SpriteBatch sb, float x, float y, Color c) { }
    @Override public final void renderAmount(SpriteBatch sb, float x, float y, Color c) { }
    //@Formatter: on

    public final Affinity affinity;
    public final ArrayList<EYBCardTooltip> tooltips = new ArrayList<>();
    public int chargeThreshold = BASE_CHARGE_THRESHOLD;
    public int amountGainedThisTurn;
    public int experience;
    public int gainMultiplier;
    public int scalingMultiplier;
    public boolean forceEnableThisTurn;
    public Hitbox hb;

    private static final StringBuilder builder = new StringBuilder();
    protected int experiencePerLevel = BASE_EXPERIENCE_PER_LEVEL;
    protected int powerLevel;
    protected int powerLevelModifier;

    public AbstractAffinityPower(Affinity affinity, String powerID)
    {
        super(null, powerID);

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
        this.experience = 0;
        this.gainMultiplier = 1;
        this.scalingMultiplier = 1;
        this.powerLevel = 0;
        this.powerLevelModifier = 0;

        Initialize(0, PowerType.BUFF, true);
    }

    public void OnUsingCard(AbstractCard c, AbstractPlayer p, AbstractMonster m) {}
    public void StartOtherSubscriptions() {}

    public void SetChargeThreshold(int chargeThreshold) {
        this.chargeThreshold = chargeThreshold;
    }

    public void SetGainMultiplier(int gainMultiplier) {
        this.gainMultiplier = gainMultiplier;
    }

    public void SetScalingMultiplier(int scalingMultiplier) {
        this.scalingMultiplier = scalingMultiplier;
    }

    public void Stack(int amount, boolean maintain)
    {
        if (!enabled && !forceEnableThisTurn)
        {
            return;
        }

        if (maintain)
        {
            Maintain();
        }

        amount *= GetEffectiveAmountMultiplier();
        super.stackPower(amount, false);
        this.amountGainedThisTurn += amount;
    }

    public void IncreaseLevel(int amount)
    {
        powerLevel = Math.max(0, powerLevel + amount);
        flash();
        UpdateThreshold();
    }

    public Integer GetActualLevel() {
        return powerLevel;
    }

    public Integer GetEffectiveLevel() {
        return Math.max(0, powerLevel + powerLevelModifier);
    }

    public Integer GetEffectiveAmountMultiplier() {
        return gainMultiplier * (100 + MULTIPLIER_PER_LEVEL * GetEffectiveLevel()) / 100;
    }

    public Integer GetCurrentThreshold()
    {
        int threshold = (powerLevel + 1) * experiencePerLevel;
        return Math.max(threshold, experiencePerLevel);
    }

    public Integer GetEffectiveScaling() {
        return GetEffectiveLevel() * this.scalingMultiplier;
    }

    public void IncreasePowerLevelModifier(int amount)
    {
        powerLevelModifier += amount;
        updateDescription();
    }

    protected void UpdateThreshold()
    {
        while(experience >= GetCurrentThreshold()) {
            IncreaseLevel(1);
        }
        updateDescription();
    }

    @Override
    public int OnGainAffinity(Affinity affinity, int amount, boolean isActuallyGaining) {
        if (isActuallyGaining && (affinity.equals(this.affinity) || affinity.equals(Affinity.Star))) {
            experience += amount;
            UpdateThreshold();
        }
        return amount;
    }

    @Override
    public final void updateDescription()
    {
        this.tooltips.get(0).description = this.description = GetUpdatedDescription();
    }

    protected float GetChargeIncrease(int charge) {
        return Math.floorDiv(charge, BASE_CHARGE_THRESHOLD);
    }

    protected float GetChargeMultiplier() {
        return 1f + GetChargeIncrease(amount);
    }

    protected int GetCurrentChargeCost() {return Math.max(chargeThreshold, Math.floorDiv(amount, chargeThreshold) * chargeThreshold);}

    protected String GetUpdatedDescription()
    {
        return FormatDescription(0, EYBCardAffinityRow.SYNERGY_MULTIPLIER, (GetEffectiveAmountMultiplier() * 100) - 100, GetCurrentChargeCost(), GetMultiplierForDescription(), !enabled ? powerStrings.DESCRIPTIONS[1] : "");
    }

    protected int GetMultiplierForDescription() {
        return (int) (GetChargeIncrease(Math.max(amount,chargeThreshold)) * 100);
    }

    public boolean CanSpend(int amount) {
        return enabled && this.amount >= amount;
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
        if (CanSpend(chargeThreshold) && (!(card instanceof AnimatorCard) || ((AnimatorCard) card).cardData.CanTriggerSupercharge))
        {
            amount -= GetCurrentChargeCost();
            updateDescription();
            flash();
            return true;
        }
        return false;
    }

    public void Maintain() {
        this.forceEnableThisTurn = true;
        this.enabled = false;
    }

    @Override
    public void atStartOfTurn()
    {
        super.atStartOfTurn();

        this.amountGainedThisTurn = 0;
        this.forceEnableThisTurn = false;
        enabled = true;
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

        Integer level = GetEffectiveLevel();
        Color amountColor = !enabled && !forceEnableThisTurn ? Colors.Cream(0.6f) : amount >= GetCurrentChargeCost() ? Colors.Gold(1).cpy() : Colors.White(1f);
        Color levelColor = GetEffectiveLevel() > 0 ? Colors.Green(1).cpy() : Colors.Cream(0.6f);
        if (gainMultiplier > 1)
        {
            RenderHelpers.DrawCentered(sb, Colors.Gold(0.7f), GR.Common.Images.Panel_Elliptical_Half_H.Texture(), cX, cY, (w / scale) + 8, (h / scale) + 8, 1, 0);
            RenderHelpers.DrawCentered(sb, Colors.Black(0.9f), GR.Common.Images.Panel_Elliptical_Half_H.Texture(), cX, cY, w / scale, h / scale, 1, 0);
        }
        else
        {
            RenderHelpers.DrawCentered(sb, Colors.Black(0.6f), GR.Common.Images.Panel_Elliptical_Half_H.Texture(), cX, cY, w / scale, h / scale, 1, 0);
        }

        final Color imgColor = Colors.White((enabled || forceEnableThisTurn) ? 1 : 0.5f);
        RenderHelpers.DrawCentered(sb, imgColor, img, x + 16 * scale, cY + (3f * scale), 32, 32, 1, 0);

        final Integer threshold = GetCurrentThreshold();
        if (threshold != null)
        {
            FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, "L" + String.valueOf(level), x + 36 * scale, y - 8 * scale, fontScale, levelColor);
        }

        FontHelper.renderFontRightTopAligned(sb, EYBFontHelper.CardIconFont_Small, String.valueOf(amount), x + 64 * scale, y, fontScale, amountColor);

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