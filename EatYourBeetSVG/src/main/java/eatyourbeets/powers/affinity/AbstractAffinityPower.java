package eatyourbeets.powers.affinity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import eatyourbeets.cards.base.AffinityType;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnPostDrawSubscriber;
import eatyourbeets.interfaces.subscribers.OnStatsClearedSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.CommonPower;
import eatyourbeets.utilities.JUtils;

import java.util.HashSet;

public abstract class AbstractAffinityPower extends CommonPower
{
    protected static final int[] DEFAULT_THRESHOLDS = new int[] { 3, 6, 9, 12 };
    protected static final PreservedPowers preservedPowers = new PreservedPowers();
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

    public void Stack(int amount, boolean retain)
    {
        this.amount += amount;
        if (this.enabled && retain)
        {
            this.enabled = false;
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
    public void onInitialApplication()
    {
        super.onInitialApplication();

        UpdateThreshold();
    }

    @Override
    public void stackPower(int stackAmount)
    {
        super.stackPower(stackAmount);

        UpdateThreshold();
    }

    @Override
    public void atStartOfTurn()
    {
        super.atStartOfTurn();

        if (enabled && amount > 0)
        {
            reducePower(1);
        }
    }

    @Override
    public void update(int slot)
    {
        super.update(slot);

        this.enabled = (!preservedPowers.contains(ID));
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

    @Override
    public void renderAmount(SpriteBatch sb, float x, float y, Color c)
    {
        Integer threshold = GetCurrentThreshold();
        if (threshold != null)
        {
            final float offset1_x = -24 * Settings.scale;
            final float offset2_x = 3 * Settings.scale;
            final float offset1_y = -5 * Settings.scale;
            final float offset2_y = -5 * Settings.scale;
            final Color c1 = Settings.GREEN_TEXT_COLOR.cpy();
            final Color c2 = Settings.CREAM_COLOR.cpy();
            c1.a = c2.a = c.a;
            FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, "/" + threshold, x + offset2_x, y + offset2_y, 1, c2);
            FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, Integer.toString(this.amount), x + offset1_x, y + offset1_y, fontScale, c1);
        }
        else
        {
            super.renderAmount(sb, x, y, c);
        }
    }

    public static class PreservedPowers extends HashSet<String> implements OnStatsClearedSubscriber, OnStartOfTurnPostDrawSubscriber
    {
        @Override
        public boolean OnStatsCleared()
        {
            clear();
            CombatStats.onStartOfTurnPostDraw.Unsubscribe(this);
            CombatStats.onStatsCleared.Unsubscribe(this);
            return true;
        }

        @Override
        public void OnStartOfTurnPostDraw()
        {
            clear();
            CombatStats.onStartOfTurnPostDraw.Unsubscribe(this);
            CombatStats.onStatsCleared.Unsubscribe(this);
        }

        public void Subscribe(String powerID)
        {
            add(powerID);
            CombatStats.onStatsCleared.Subscribe(this);
            CombatStats.onStartOfTurnPostDraw.Subscribe(this);
        }
    }
}