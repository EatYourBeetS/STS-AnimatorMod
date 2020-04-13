package eatyourbeets.powers.common;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnPostDrawSubscriber;
import eatyourbeets.interfaces.subscribers.OnStatsClearedSubscriber;
import eatyourbeets.powers.CommonPower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.JavaUtilities;

import java.util.HashSet;

public abstract class PlayerAttributePower extends CommonPower
{
    protected static final PreservedPowers preservedPowers = new PreservedPowers();
    protected int threshold;

    public PlayerAttributePower(String powerID, AbstractCreature owner, int amount)
    {
        super(owner, powerID);

        this.amount = amount;
        this.threshold = 2;

        updateDescription();
    }

    @Override
    public void updateDescription()
    {
        this.description = powerStrings.DESCRIPTIONS[0];

        if (threshold > 0)
        {
            this.description += JavaUtilities.Format(powerStrings.DESCRIPTIONS[1], threshold, 1);
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

    public int GetCurrentLevel()
    {
        switch (threshold)
        {
            case 0: return 0;
            case 2: return 1;
            case 4: return 2;
            case 6: return 3;
            case 8: return 4;
            default: return 5;
        }
    }

    protected void UpdateThreshold()
    {
        int powerGain = 0;
        if (threshold == 2 && amount >= threshold)
        {
            OnThresholdReached();
            threshold = 4;
        }
        if (threshold == 4 && amount >= threshold)
        {
            OnThresholdReached();
            threshold = 6;
        }
        if (threshold == 6 && amount >= threshold)
        {
            OnThresholdReached();
            threshold = 8;
        }
        if (threshold == 8 && amount >= threshold)
        {
            OnThresholdReached();
            threshold = -1;
        }

        updateDescription();
    }

    @Override
    public void renderAmount(SpriteBatch sb, float x, float y, Color c)
    {
        if (threshold > 0)
        {
            final float offset_x  = -24 * Settings.scale;
            final float offset_y  = -5 * Settings.scale;
            final float offset_x2 = 0 * Settings.scale;
            final float offset_y2 = -5 * Settings.scale;
            final Color c1 = Color.GREEN.cpy();
            final Color c2 = Settings.CREAM_COLOR.cpy();
            c1.a = c2.a = c.a;
            FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, Integer.toString(this.amount), x + offset_x, y + offset_y, fontScale, c1);
            FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, "/" + this.threshold, x + offset_x2, y + offset_y2, 1, c2);
        }
        else
        {
            super.renderAmount(sb, x, y, c);
        }
    }

    protected abstract float GetScaling(EYBCard card);
    protected abstract void OnThresholdReached();

    public static class PreservedPowers extends HashSet<String> implements OnStatsClearedSubscriber, OnStartOfTurnPostDrawSubscriber
    {
        @Override
        public void OnStatsCleared()
        {
            clear();

            CombatStats.onStartOfTurnPostDraw.Unsubscribe(this);
            CombatStats.onStatsCleared.Unsubscribe(this);
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