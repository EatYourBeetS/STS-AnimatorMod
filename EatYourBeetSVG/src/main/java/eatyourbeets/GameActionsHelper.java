package eatyourbeets;

import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
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
import eatyourbeets.actions.*;
import eatyourbeets.powers.PlayerStatistics;

import java.util.ArrayList;
import java.util.function.BiConsumer;
import java.util.function.Function;

@SuppressWarnings("UnusedReturnValue")
public class GameActionsHelper
{
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

    public static ExhaustAnywhereAction ExhaustCard(AbstractCard card)
    {
        ExhaustAnywhereAction action = new ExhaustAnywhereAction(card);
        AbstractDungeon.actionManager.addToBottom(action);
        return action;
    }

    public static ExhaustSpecificCardAction ExhaustCard(AbstractCard card, CardGroup group)
    {
        ExhaustSpecificCardAction action = new ExhaustSpecificCardAction(card, group);
        AbstractDungeon.actionManager.addToBottom(action);
        return action;
    }

    public static ChannelAction ChannelOrb(AbstractOrb orb, boolean autoEvoke)
    {
        ChannelAction action = new ChannelAction(orb, autoEvoke);
        AbstractDungeon.actionManager.addToBottom(action);
        return action;
    }

    public static ModifyMagicNumberAction ModifyMagicNumber(AbstractCard card, int amount, boolean relative)
    {
        if (!relative)
        {
            amount = amount - card.baseMagicNumber;
        }

        ModifyMagicNumberAction action = new ModifyMagicNumberAction(card.uuid, amount);
        AbstractDungeon.actionManager.addToBottom(action);
        return action;
    }

    public static CycleCardAction CycleCardAction(int amount)
    {
        CycleCardAction action = new CycleCardAction(AbstractDungeon.player, amount);
        AbstractDungeon.actionManager.addToBottom(action);
        return action;
    }

    public static GainBlockAction GainBlock(AbstractCreature source, int amount)
    {
        GainBlockAction action = new GainBlockAction(source, source, amount);
        AbstractDungeon.actionManager.addToBottom(action);
        return action;
    }

    public static PiercingDamageAction DamageTargetPiercing(AbstractCreature source, AbstractCreature target, AbstractCard card, AbstractGameAction.AttackEffect effect)
    {
        PiercingDamageAction action = new PiercingDamageAction(target, new DamageInfo(source, card.damage, card.damageTypeForTurn), effect);
        AbstractDungeon.actionManager.addToBottom(action);
        return action;
    }

    public static DamageAction DamageTarget(AbstractCreature source, AbstractCreature target, AbstractCard card, AbstractGameAction.AttackEffect effect)
    {
        DamageAction action = new DamageAction(target, new DamageInfo(source, card.damage, card.damageTypeForTurn), effect);
        AbstractDungeon.actionManager.addToBottom(action);
        return action;
    }

    public static DamageAction DamageTarget(AbstractCreature source, AbstractCreature target, int amount, DamageInfo.DamageType damageType, AbstractGameAction.AttackEffect effect)
    {
        DamageAction action = new DamageAction(target, new DamageInfo(source, amount, damageType), effect);
        AbstractDungeon.actionManager.addToBottom(action);
        return action;
    }

    public static DamageAllEnemiesAction DamageAllEnemies(AbstractCreature source, int[] amount, DamageInfo.DamageType damageType, AbstractGameAction.AttackEffect effect)
    {
        DamageAllEnemiesAction action = new DamageAllEnemiesAction(source, amount, damageType, effect);
        AbstractDungeon.actionManager.addToBottom(action);
        return action;
    }

    public static DamageRandomEnemyAction DamageRandomEnemy(AbstractCreature source, int amount, DamageInfo.DamageType damageType, AbstractGameAction.AttackEffect effect)
    {
        DamageRandomEnemyAction action = new DamageRandomEnemyAction(new DamageInfo(source, amount, damageType), effect);
        AbstractDungeon.actionManager.addToBottom(action);
        return action;
    }

    public static OnRandomEnemyDamagedAction DamageRandomEnemy(AbstractCreature source, int amount, DamageInfo.DamageType damageType, AbstractGameAction.AttackEffect effect, BiConsumer<Object, AbstractCreature> onDamage, Object state)
    {
        OnRandomEnemyDamagedAction action = new OnRandomEnemyDamagedAction(new DamageInfo(source, amount, damageType), effect, state, onDamage);
        AbstractDungeon.actionManager.addToBottom(action);
        return action;
    }

    public static CallbackAction Callback(AbstractGameAction action, BiConsumer<Object, AbstractGameAction> onCompletion, Object state)
    {
        CallbackAction callbackAction = new CallbackAction(action, onCompletion, state);
        AbstractDungeon.actionManager.addToBottom(callbackAction);
        return callbackAction;
    }

    public static OnCardDrawnAction DrawCard(AbstractCreature source, int amount, BiConsumer<Object, ArrayList<AbstractCard>> onDraw, Object context)
    {
        OnCardDrawnAction action = new OnCardDrawnAction(source, amount, onDraw, context);
        AbstractDungeon.actionManager.addToBottom(action);
        return action;
    }

    public static DrawCardAction DrawCard(AbstractCreature source, int amount)
    {
        DrawCardAction action = new DrawCardAction(source, amount);
        AbstractDungeon.actionManager.addToBottom(action);
        return action;
    }

    public static GainEnergyAction GainEnergy(int amount)
    {
        GainEnergyAction action = new GainEnergyAction(amount);
        AbstractDungeon.actionManager.addToBottom(action);
        return action;
    }

    public static void ApplyPowerToAllEnemies(AbstractCreature source, Function<AbstractCreature, AbstractPower> createPower, int stacks)
    {
        for (AbstractMonster m : PlayerStatistics.GetCurrentEnemies(true))
        {
            AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(m, source, createPower.apply(m), stacks));
        }
    }

    public static ApplyPowerAction ApplyPower(AbstractCreature source, AbstractCreature target, AbstractPower power)
    {
        ApplyPowerAction action = new ApplyPowerAction(target, source, power);
        AbstractDungeon.actionManager.addToBottom(action);
        return action;
    }

    public static ApplyPowerAction ApplyPower(AbstractCreature source, AbstractCreature target, AbstractPower power, int stacks)
    {
        ApplyPowerAction action = new ApplyPowerAction(target, source, power, stacks);
        AbstractDungeon.actionManager.addToBottom(action);
        return action;
    }

    public static AddTemporaryHPAction GainTemporaryHP(AbstractCreature source, AbstractCreature target, int amount)
    {
        AddTemporaryHPAction action = new AddTemporaryHPAction(target, source, amount);
        AbstractDungeon.actionManager.addToBottom(action);
        return action;
    }

    public static DiscardAction ChooseAndDiscard(int amount, boolean random)
    {
        DiscardAction action = new DiscardAction(AbstractDungeon.player, AbstractDungeon.player, amount, random);
        AbstractDungeon.actionManager.addToBottom(action);
        AbstractDungeon.actionManager.addToBottom(new WaitAction(0.2f));
        return action;
    }
}
