package eatyourbeets.powers.common;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnPostDrawSubscriber;
import eatyourbeets.interfaces.subscribers.OnStatsClearedSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.CommonPower;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

import java.util.HashSet;

public abstract class PlayerAttributePower extends CommonPower
{
    protected static final PermanentlyPreservedPowers permanentlyPreservedPowers = new PermanentlyPreservedPowers();
    protected static final PreservedPowers preservedPowers = new PreservedPowers();
    protected static final OverridePreservedPowers overrideDisabledPowers = new OverridePreservedPowers();
    protected static final PermanentlyDisabledPowers permanentlyDisabledPowers = new PermanentlyDisabledPowers();
    protected int threshold;

    public static int GetThreshold(int level)
    {
        switch (level)
        {
            case  1: return 10;
            case  2: return 20;
            case  3: return 30;
            case  4: return 40;
            default: return 0;
        }
    }

    public static int GetLevel(Class<? extends PlayerAttributePower> type)
    {
        if (AbstractDungeon.player != null)
        {
            PlayerAttributePower power = GameUtilities.GetPower(AbstractDungeon.player, type);
            if (power != null)
            {
                switch (power.threshold)
                {
                    case 0: return 0;
                    case 10: return 1;
                    case 20: return 2;
                    case 30: return 3;
                    case 40: return 4;
                    default: return 5;
                }
            }
        }

        return 0;
    }

    protected abstract float GetScaling(EYBCard card);
    protected abstract void OnThresholdReached();

    public PlayerAttributePower(String powerID, AbstractCreature owner, int amount)
    {
        super(owner, powerID);

        if (!disabled)
        {
            this.amount = amount;
        }
        else
        {
            this.amount = 0;
        }
        this.threshold = 10;

        updateDescription();
    }

    @Override
    public void updateDescription()
    {
        this.description = powerStrings.DESCRIPTIONS[0];

        if (threshold > 0)
        {
            this.description += JUtils.Format(powerStrings.DESCRIPTIONS[1], threshold, 1);
        }
    }

    @Override
    public void onInitialApplication()
    {
        updatePowerStatus();

        if (!isPowerGainDisabled())
        {
            super.onInitialApplication();

            UpdateThreshold();
        }
    }

    @Override
    public void stackPower(int stackAmount)
    {
        if (!isPowerGainDisabled())
        {
            super.stackPower(stackAmount);

            UpdateThreshold();
        }
    }

    @Override
    public void update(int slot)
    {
        super.update(slot);

        updatePowerStatus();
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

    public void updatePowerStatus()
    {
        this.enabled = (!preservedPowers.contains(ID) && !permanentlyPreservedPowers.contains(ID));

        isPowerGainDisabled();
    }

    public boolean isPowerGainDisabled()
    {
        this.disabled = (permanentlyDisabledPowers.contains(ID));

        if (this.disabled)
        {
            this.overrideDisabled = overrideDisabledPowers.contains(ID);
        }

        return disabled && !overrideDisabled;
    }

    @Override
    public void renderAmount(SpriteBatch sb, float x, float y, Color c)
    {
        if (threshold > 0)
        {
            final float offset_x = -32 * Settings.scale;
            final float offset_y = -5 * Settings.scale;
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

    protected void UpdateThreshold()
    {
        if (threshold == 10 && amount >= threshold)
        {
            OnThresholdReached();
            threshold = 20;
        }
        if (threshold == 20 && amount >= threshold)
        {
            OnThresholdReached();
            threshold = 30;
        }
        if (threshold == 30 && amount >= threshold)
        {
            OnThresholdReached();
            threshold = 40;
        }
        if (threshold == 40 && amount >= threshold)
        {
            OnThresholdReached();
            threshold = -1;
        }

        updateDescription();
    }

    public static class OverridePreservedPowers extends HashSet<String> implements OnStatsClearedSubscriber
    {
        @Override
        public void OnStatsCleared()
        {
            clear();
            CombatStats.onStatsCleared.Unsubscribe(this);
        }

        public void Subscribe(String powerID)
        {
            add(powerID);
            CombatStats.onStatsCleared.Subscribe(this);
        }

        public void Unsubscribe(String powerID)
        {
            remove(powerID);
            CombatStats.onStatsCleared.Unsubscribe(this);
        }
    }

    public static class PermanentlyPreservedPowers extends HashSet<String> implements OnStatsClearedSubscriber
    {
        @Override
        public void OnStatsCleared()
        {
            clear();
            CombatStats.onStatsCleared.Unsubscribe(this);
        }

        public void Subscribe(String powerID)
        {
            add(powerID);
            CombatStats.onStatsCleared.Subscribe(this);
        }

        public void Unsubscribe(String powerID)
        {
            remove(powerID);
            CombatStats.onStatsCleared.Unsubscribe(this);
        }
    }

    public static class PermanentlyDisabledPowers extends HashSet<String> implements OnStatsClearedSubscriber
    {
        @Override
        public void OnStatsCleared()
        {
            clear();
            CombatStats.onStatsCleared.Unsubscribe(this);
        }

        public void Subscribe(String powerID)
        {
            add(powerID);
            CombatStats.onStatsCleared.Subscribe(this);
        }

        public void Unsubscribe(String powerID)
        {
            remove(powerID);
            CombatStats.onStatsCleared.Unsubscribe(this);
        }
    }

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