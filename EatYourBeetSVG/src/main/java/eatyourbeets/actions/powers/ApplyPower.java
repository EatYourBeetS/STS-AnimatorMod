package eatyourbeets.actions.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.TextAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.relics.*;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import com.megacrit.cardcrawl.vfx.combat.PowerBuffEffect;
import com.megacrit.cardcrawl.vfx.combat.PowerDebuffEffect;
import eatyourbeets.actions.EYBActionWithCallback;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

import java.util.Collections;

public class ApplyPower extends EYBActionWithCallback<AbstractPower>
{
    public static final String[] TEXT = ApplyPowerAction.TEXT;

    public AbstractPower callbackResult;
    public AbstractPower powerToApply;
    public boolean chooseRandomTarget;
    public boolean ignoreArtifact;
    public boolean showEffect = true;
    public boolean skipIfZero = false;
    public boolean skipIfNull = true;
    public boolean canStack = true;
    public boolean faster;

    public ApplyPower(AbstractCreature source, AbstractCreature target, AbstractPower power)
    {
        this(source, target, power, power.amount);
    }

    public ApplyPower(AbstractCreature source, AbstractCreature target, AbstractPower power, int amount)
    {
        this(source, target, power, amount, false, AttackEffect.NONE);
    }

    public ApplyPower(AbstractCreature source, AbstractCreature target, AbstractPower power, int amount, AttackEffect effect)
    {
        this(source, target, power, amount, false, effect);
    }

    public ApplyPower(AbstractCreature source, AbstractCreature target, AbstractPower power, int amount, boolean isFast)
    {
        this(source, target, power, amount, isFast, AttackEffect.NONE);
    }

    public ApplyPower(AbstractCreature source, AbstractCreature target, AbstractPower power, int amount, boolean isFast, AttackEffect effect)
    {
        super(ActionType.POWER, Settings.FAST_MODE ? Settings.ACTION_DUR_XFAST : Settings.ACTION_DUR_FASTER);

        powerToApply = power;
        attackEffect = effect;
        faster = isFast;

        Initialize(source, target, amount);

        HardCodedStuff_SneckoSkull();

        if (GameUtilities.AreMonstersBasicallyDead())
        {
            Complete();
            return;
        }

        if (powerToApply.ID.equals(CorruptionPower.POWER_ID))
        {
            HardCodedStuff_Corruption(player.hand);
            HardCodedStuff_Corruption(player.drawPile);
            HardCodedStuff_Corruption(player.discardPile);
            HardCodedStuff_Corruption(player.exhaustPile);
        }
    }

    public ApplyPower CanStack(boolean canStack)
    {
        this.canStack = canStack;

        return this;
    }

    public ApplyPower SkipIfZero(boolean skipIfZero)
    {
        this.skipIfZero = skipIfZero;

        return this;
    }

    public ApplyPower SkipIfNull(boolean skipIfNull)
    {
        this.skipIfNull = skipIfNull;

        return this;
    }

    public ApplyPower IgnoreArtifact(boolean ignoreArtifact)
    {
        this.ignoreArtifact = ignoreArtifact;

        return this;
    }

    public ApplyPower ShowEffect(boolean showEffect, boolean isFast)
    {
        this.showEffect = showEffect;
        this.faster = isFast;

        return this;
    }

    public ApplyPower ChooseRandomTarget(boolean value)
    {
        this.chooseRandomTarget = value;

        return this;
    }

    @Override
    protected void FirstUpdate()
    {
        if (chooseRandomTarget)
        {
            target = GameUtilities.GetRandomEnemy(true);
            powerToApply.owner = target;
        }

        if (amount == 0 && skipIfZero)
        {
            Complete();
            return;
        }

        if (shouldCancelAction() || HardCodedStuff_NoDraw() || !GameUtilities.CanApplyPower(source, target, powerToApply, this))
        {
            Complete(callbackResult);
            return;
        }

        if (source != null)
        {
            for (AbstractPower power : source.powers)
            {
                power.onApplyPower(powerToApply, target, source);
            }

            HardCodedStuff_ChampionBelt();
        }

        if (target.isPlayer && HardCodedStuff_TurnipAndGingerCheck())
        {
            tickDuration();
            return;
        }

        AbstractPower existingPower = null;
        for (AbstractPower power : target.powers)
        {
            if (power.ID.equals(powerToApply.ID))
            {
                if (canStack)
                {
                    existingPower = power;
                    break;
                }
                else
                {
                    return;
                }
            }
        }

        if (powerToApply.type == AbstractPower.PowerType.DEBUFF)
        {
            if (HardCodedStuff_Artifact())
            {
                tickDuration();
                return;
            }
            else if (showEffect)
            {
                target.useFastShakeAnimation(0.5f);
            }
        }

        CombatStats.OnApplyPower(source, target, powerToApply);

        if (showEffect)
        {
            GameEffects.List.Add(new FlashAtkImgEffect(target.hb.cX, target.hb.cY, attackEffect));
        }

        if (existingPower != null)
        {
            StackPower(existingPower);
        }
        else
        {
            AddPower();
        }

        AbstractDungeon.onModifyPower();
    }

    @Override
    protected void UpdateInternal(float deltaTime)
    {
        if (TickDuration(deltaTime))
        {
            Complete(callbackResult);
        }
    }

    @Override
    protected boolean shouldCancelAction()
    {
        return GameUtilities.AreMonstersBasicallyDead() || super.shouldCancelAction();
    }

    private void StackPower(AbstractPower power)
    {
        callbackResult = power;
        power.stackPower(amount);

        if (showEffect)
        {
            power.flash();

            if (amount <= 0 && IsStrengthDexterityOrFocus(power))
            {
                GameEffects.List.Add(new PowerDebuffEffect(target.hb.cX - target.animX,
                target.hb.cY + target.hb.height / 2f, powerToApply.name + TEXT[3]));
            }
            else if (amount > 0)
            {
                if (power.type != AbstractPower.PowerType.BUFF && !(power instanceof StrengthPower) && !(power instanceof DexterityPower))
                {
                    GameEffects.List.Add(new PowerDebuffEffect(target.hb.cX - target.animX,
                    target.hb.cY + target.hb.height / 2f, "+" + amount + " " + powerToApply.name));
                }
                else
                {
                    GameEffects.List.Add(new PowerBuffEffect(target.hb.cX - target.animX,
                    target.hb.cY + target.hb.height / 2f, "+" + amount + " " + powerToApply.name));
                }
            }
            else if (power.type == AbstractPower.PowerType.BUFF)
            {
                GameEffects.List.Add(new PowerBuffEffect(target.hb.cX - target.animX,
                target.hb.cY + target.hb.height / 2f, powerToApply.name + TEXT[3]));
            }
            else
            {
                GameEffects.List.Add(new PowerDebuffEffect(target.hb.cX - target.animX,
                target.hb.cY + target.hb.height / 2f, powerToApply.name + TEXT[3]));
            }
        }

        power.updateDescription();
    }

    private void AddPower()
    {
        callbackResult = powerToApply;
        target.addPower(powerToApply);

        Collections.sort(target.powers);

        powerToApply.onInitialApplication();

        if (showEffect)
        {
            powerToApply.flash();

            if (amount <= 0 && IsStrengthDexterityOrFocus(powerToApply))
            {
                GameEffects.List.Add(new PowerDebuffEffect(target.hb.cX - target.animX,
                target.hb.cY + target.hb.height / 2f, powerToApply.name + TEXT[3]));
            }
            else if (powerToApply.type == AbstractPower.PowerType.BUFF)
            {
                GameEffects.List.Add(new PowerBuffEffect(target.hb.cX - target.animX, target.hb.cY + target.hb.height / 2f, powerToApply.name));
            }
            else
            {
                GameEffects.List.Add(new PowerDebuffEffect(target.hb.cX - target.animX, target.hb.cY + target.hb.height / 2f, powerToApply.name));
            }
        }
    }

    @Override
    protected void Complete(AbstractPower result)
    {
        if (!skipIfNull || (result != null && result.owner != null))
        {
            super.Complete(result);
        }
    }

    private boolean HardCodedStuff_Artifact()
    {
        if (ignoreArtifact)
        {
            return false;
        }

        final AbstractPower artifact = target.getPower(ArtifactPower.POWER_ID);
        if (artifact != null)
        {
            GameActions.Top.Add(new TextAboveCreatureAction(target, TEXT[0]));

            CardCrawlGame.sound.play("NULLIFY_SFX");
            artifact.flashWithoutSound();
            artifact.onSpecificTrigger();

            return true;
        }

        return false;
    }

    private boolean HardCodedStuff_NoDraw()
    {
        return powerToApply instanceof NoDrawPower && target.hasPower(powerToApply.ID);
    }

    private void HardCodedStuff_SneckoSkull()
    {
        if (source != null && source.isPlayer && target != source && player.hasRelic(SneckoSkull.ID) && powerToApply.ID.equals(PoisonPower.POWER_ID))
        {
            player.getRelic(SneckoSkull.ID).flash();
            powerToApply.amount += 1;
            amount += 1;
        }
    }

    private void HardCodedStuff_Corruption(CardGroup group)
    {
        for (AbstractCard c : group.group)
        {
            if (c.type == AbstractCard.CardType.SKILL)
            {
                c.modifyCostForCombat(-9);
            }
        }
    }

    private void HardCodedStuff_ChampionBelt()
    {
        if (source.isPlayer && target != source && powerToApply.ID.equals(VulnerablePower.POWER_ID) && !target.hasPower(ArtifactPower.POWER_ID))
        {
            AbstractRelic belt = player.getRelic(ChampionsBelt.ID);
            if (belt != null)
            {
                belt.onTrigger(target);
            }
        }
    }

    private boolean HardCodedStuff_TurnipAndGingerCheck()
    {
        if (powerToApply.ID.equals(WeakPower.POWER_ID))
        {
            AbstractRelic relic = player.getRelic(Ginger.ID);
            if (relic != null)
            {
                GameActions.Top.Add(new TextAboveCreatureAction(target, TEXT[1]));
                relic.flash();

                return true;
            }
        }
        else if (powerToApply.ID.equals(FrailPower.POWER_ID))
        {
            AbstractRelic relic = player.getRelic(Turnip.ID);
            if (relic != null)
            {
                GameActions.Top.Add(new TextAboveCreatureAction(target, TEXT[1]));
                relic.flash();

                return true;
            }
        }

        return false;
    }

    private static boolean IsStrengthDexterityOrFocus(AbstractPower power)
    {
        return StrengthPower.POWER_ID.equals(power.ID) || DexterityPower.POWER_ID.equals(power.ID) || FocusPower.POWER_ID.equals(power.ID);
    }
}