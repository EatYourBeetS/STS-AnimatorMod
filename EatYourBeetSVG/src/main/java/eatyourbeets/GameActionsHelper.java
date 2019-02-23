package eatyourbeets;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.actions.CycleCardAction;
import eatyourbeets.actions.ModifyMagicNumberAction;
import eatyourbeets.actions.OnCardDrawnAction;
import eatyourbeets.actions.OnRandomEnemyDamagedAction;

import java.util.ArrayList;
import java.util.function.BiConsumer;

@SuppressWarnings("UnusedReturnValue")
public class GameActionsHelper
{
    public static void AddToTop(AbstractGameAction action)
    {
        AbstractDungeon.actionManager.addToTop(action);
    }

    public static void AddToBottom(AbstractGameAction action)
    {
        AbstractDungeon.actionManager.addToBottom(action);
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

    public static ApplyPowerAction ApplyPower(AbstractCreature source, AbstractCreature target, AbstractPower power)
    {
        ApplyPowerAction action = new ApplyPowerAction(target, source, power, power.amount);
        AbstractDungeon.actionManager.addToBottom(action);
        return action;
    }

    public static ApplyPowerAction ApplyPower(AbstractCreature source, AbstractCreature target, AbstractPower power, int stacks)
    {
        ApplyPowerAction action = new ApplyPowerAction(target, source, power, stacks);
        AbstractDungeon.actionManager.addToBottom(action);
        return action;
    }
}
