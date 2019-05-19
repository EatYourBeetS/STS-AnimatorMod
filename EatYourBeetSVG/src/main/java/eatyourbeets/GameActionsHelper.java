package eatyourbeets;

import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import eatyourbeets.actions.*;
import eatyourbeets.powers.PlayerStatistics;

import java.util.ArrayList;
import java.util.function.BiConsumer;
import java.util.function.Function;

@SuppressWarnings("UnusedReturnValue")
public class GameActionsHelper
{
    private static Order defaultOrder = Order.Bottom;

    public static void ResetOrder()
    {
        SetOrder(Order.Bottom);
    }

    public static void SetOrder(Order order)
    {
        defaultOrder = order;
    }

    public enum Order
    {
        Top,
        Bottom,
        TurnStart,
        NextCombat
    }

    public static void AddToDefault(AbstractGameAction action)
    {
        switch (defaultOrder)
        {
            case Top:
                AbstractDungeon.actionManager.addToTop(action); break;

            case Bottom:
                AbstractDungeon.actionManager.addToBottom(action); break;

            case TurnStart:
                AbstractDungeon.actionManager.addToTurnStart(action); break;

            case NextCombat:
                AbstractDungeon.actionManager.addToNextCombat(action); break;
        }
    }

    public static void AddToTurnStart(AbstractGameAction action)
    {
        AbstractDungeon.actionManager.addToTurnStart(action);
    }

    public static void AddToTop(AbstractGameAction action)
    {
        AbstractDungeon.actionManager.addToTop(action);
    }

    public static void AddToBottom(AbstractGameAction action)
    {
        AbstractDungeon.actionManager.addToBottom(action);
    }


    public static VFXAction VFX(AbstractGameEffect effect, float duration)
    {
        VFXAction action = new VFXAction(effect, duration);
        AddToDefault(action);
        return action;
    }

    public static ExhaustAnywhereAction ExhaustCard(AbstractCard card)
    {
        ExhaustAnywhereAction action = new ExhaustAnywhereAction(card);
        AddToDefault(action);
        return action;
    }

    public static ExhaustSpecificCardAction ExhaustCard(AbstractCard card, CardGroup group)
    {
        ExhaustSpecificCardAction action = new ExhaustSpecificCardAction(card, group);
        AddToDefault(action);
        return action;
    }

    public static ChannelAction ChannelOrb(AbstractOrb orb, boolean autoEvoke)
    {
        ChannelAction action = new ChannelAction(orb, autoEvoke);
        AddToDefault(action);
        return action;
    }

    public static ModifyMagicNumberAction ModifyMagicNumber(AbstractCard card, int amount, boolean relative)
    {
        if (!relative)
        {
            amount = amount - card.baseMagicNumber;
        }

        ModifyMagicNumberAction action = new ModifyMagicNumberAction(card.uuid, amount);
        AddToDefault(action);
        return action;
    }

    public static CycleCardAction CycleCardAction(int amount)
    {
        CycleCardAction action = new CycleCardAction(AbstractDungeon.player, amount);
        AddToDefault(action);
        return action;
    }

    public static GainBlockAction GainBlock(AbstractCreature source, int amount)
    {
        GainBlockAction action = new GainBlockAction(source, source, amount);
        AddToDefault(action);
        return action;
    }

    public static PiercingDamageAction DamageTargetPiercing(AbstractCreature source, AbstractCreature target, AbstractCard card, AbstractGameAction.AttackEffect effect)
    {
        PiercingDamageAction action = new PiercingDamageAction(target, new DamageInfo(source, card.damage, card.damageTypeForTurn), effect);
        AddToDefault(action);
        return action;
    }

    public static PiercingDamageAction DamageTargetPiercing(AbstractCreature source, AbstractCreature target, int amount, DamageInfo.DamageType damageType, AbstractGameAction.AttackEffect effect)
    {
        PiercingDamageAction action = new PiercingDamageAction(target, new DamageInfo(source, amount, damageType), effect);
        AddToDefault(action);
        return action;
    }

    public static DamageAction DamageTarget(AbstractCreature source, AbstractCreature target, AbstractCard card, AbstractGameAction.AttackEffect effect, boolean superFast)
    {
        DamageAction action = new DamageAction(target, new DamageInfo(source, card.damage, card.damageTypeForTurn), effect, superFast);
        AddToDefault(action);
        return action;
    }

    public static DamageAction DamageTarget(AbstractCreature source, AbstractCreature target, AbstractCard card, AbstractGameAction.AttackEffect effect)
    {
        DamageAction action = new DamageAction(target, new DamageInfo(source, card.damage, card.damageTypeForTurn), effect);
        AddToDefault(action);
        return action;
    }

    public static DamageAction DamageTarget(AbstractCreature source, AbstractCreature target, int amount, DamageInfo.DamageType damageType, AbstractGameAction.AttackEffect effect)
    {
        DamageAction action = new DamageAction(target, new DamageInfo(source, amount, damageType), effect);
        AddToDefault(action);
        return action;
    }

    public static DamageAllEnemiesAction DamageAllEnemies(AbstractCreature source, int[] amount, DamageInfo.DamageType damageType, AbstractGameAction.AttackEffect effect)
    {
        DamageAllEnemiesAction action = new DamageAllEnemiesAction(source, amount, damageType, effect);
        AddToDefault(action);
        return action;
    }

    public static DamageRandomEnemy2Action DamageRandomEnemyWhichActuallyWorks(AbstractCreature source, int amount, DamageInfo.DamageType damageType, AbstractGameAction.AttackEffect effect)
    {
        DamageRandomEnemy2Action action = new DamageRandomEnemy2Action(new DamageInfo(source, amount, damageType), effect);
        AddToDefault(action);
        return action;
    }

    public static DamageRandomEnemyAction DamageRandomEnemy(AbstractCreature source, int amount, DamageInfo.DamageType damageType, AbstractGameAction.AttackEffect effect)
    {
        DamageRandomEnemyAction action = new DamageRandomEnemyAction(new DamageInfo(source, amount, damageType), effect);
        AddToDefault(action);
        return action;
    }

    public static OnRandomEnemyDamagedAction DamageRandomEnemy(AbstractCreature source, int amount, DamageInfo.DamageType damageType, AbstractGameAction.AttackEffect effect, BiConsumer<Object, AbstractCreature> onDamage, Object state)
    {
        OnRandomEnemyDamagedAction action = new OnRandomEnemyDamagedAction(new DamageInfo(source, amount, damageType), effect, state, onDamage);
        AddToDefault(action);
        return action;
    }

    public static CallbackAction Callback(AbstractGameAction action, BiConsumer<Object, AbstractGameAction> onCompletion, Object state)
    {
        CallbackAction callbackAction = new CallbackAction(action, onCompletion, state);
        AddToDefault(callbackAction);
        return callbackAction;
    }

    public static OnCardDrawnAction DrawCard(AbstractCreature source, int amount, BiConsumer<Object, ArrayList<AbstractCard>> onDraw, Object context)
    {
        OnCardDrawnAction action = new OnCardDrawnAction(source, amount, onDraw, context);
        AddToDefault(action);
        return action;
    }

    public static DrawCardAction DrawCard(AbstractCreature source, int amount)
    {
        DrawCardAction action = new DrawCardAction(source, amount);
        AddToDefault(action);
        return action;
    }

    public static GainEnergyAction GainEnergy(int amount)
    {
        GainEnergyAction action = new GainEnergyAction(amount);
        AddToDefault(action);
        return action;
    }

    public static void ApplyPowerToAllEnemies(AbstractCreature source, Function<AbstractCreature, AbstractPower> createPower, int stacks)
    {
        for (AbstractMonster m : PlayerStatistics.GetCurrentEnemies(true))
        {
            AddToDefault(new ApplyPowerAction(m, source, createPower.apply(m), stacks));
        }
    }

    public static ApplyPowerAction ApplyPower(AbstractCreature source, AbstractCreature target, AbstractPower power)
    {
        ApplyPowerAction action = new ApplyPowerAction(target, source, power);
        AddToDefault(action);
        return action;
    }

    public static ApplyPowerAction ApplyPower(AbstractCreature source, AbstractCreature target, AbstractPower power, int stacks)
    {
        ApplyPowerAction action = new ApplyPowerAction(target, source, power, stacks);
        AddToDefault(action);
        return action;
    }

    public static ApplyPowerSilentlyAction ApplyPowerSilently(AbstractCreature source, AbstractCreature target, AbstractPower power, int stacks)
    {
        ApplyPowerSilentlyAction action = new ApplyPowerSilentlyAction(target, source, power, stacks);
        AddToDefault(action);
        return action;
    }

    public static AddTemporaryHPAction GainTemporaryHP(AbstractCreature source, AbstractCreature target, int amount)
    {
        AddTemporaryHPAction action = new AddTemporaryHPAction(target, source, amount);
        AddToDefault(action);
        return action;
    }

    public static DiscardAction ChooseAndDiscard(int amount, boolean random)
    {
        DiscardAction action = new DiscardAction(AbstractDungeon.player, AbstractDungeon.player, amount, random);
        AddToDefault(action);
        AddToBottom(new WaitAction(0.2f));
        return action;
    }
}
