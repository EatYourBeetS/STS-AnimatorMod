package eatyourbeets.powers.affinity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import eatyourbeets.cards.base.AffinityType;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.powers.CommonPower;
import eatyourbeets.utilities.JUtils;

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

    protected abstract float GetScaling(EYBCard card);

    protected abstract void OnThresholdReached(int threshold);

    public void Render(SpriteBatch sb, Hitbox hb)
    {
        final float w = hb.width;
        final float h = hb.height;
        final float x = hb.x + w * 2f;
        final float y = hb.y + h;

        sb.setColor(Color.WHITE);
        if (this.powerIcon != null)
        {
            sb.draw(this.powerIcon, x - 12f, y - 16f, 16f, 16f, w, h, Settings.scale, Settings.scale, 0f);
        }
        else
        {
            sb.draw(this.img, x - 12f, y - 12f, 16f, 16f, w, h, Settings.scale * 1.5f, Settings.scale * 1.5f, 0f, 0, 0, 32, 32, false, false);
        }

//        for (AbstractGameEffect e : effects)
//        {
//            e.render(sb, x, y);
//        }

        Integer threshold = GetCurrentThreshold();
        if (threshold != null)
        {
            FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, "/" + threshold, x + (w * 2f), hb.y, 1, Settings.CREAM_COLOR);
        }

        Color textColor = amount == 0 ? Settings.CREAM_COLOR : retained ? Settings.BLUE_TEXT_COLOR : Settings.GREEN_TEXT_COLOR;
        FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, String.valueOf(amount), x + (w * 0.9f), hb.y, fontScale, textColor);
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

    @Override
    public float modifyBlock(float blockAmount, AbstractCard card)
    {
        if (amount > 0 && card.baseBlock >= 0 && card.type != AbstractCard.CardType.ATTACK && card instanceof EYBCard)
        {
            return blockAmount + GetScaling((EYBCard) card);
        }

        return blockAmount;
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type, AbstractCard card)
    {
        if (amount > 0 && card.baseDamage >= 0 && card.type == AbstractCard.CardType.ATTACK && card instanceof EYBCard)
        {
            return damage + GetScaling((EYBCard) card);
        }

        return damage;
    }
}