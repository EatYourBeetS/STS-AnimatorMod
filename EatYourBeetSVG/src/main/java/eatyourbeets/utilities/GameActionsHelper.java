package eatyourbeets.utilities;

import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.actions.utility.QueueCardAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.utility.UnlimboAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.*;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import eatyourbeets.actions.animator.MotivateAction;
import eatyourbeets.actions.common.*;
import eatyourbeets.actions.unnamed.MoveToVoidAction;
import eatyourbeets.interfaces.OnCallbackSubscriber;
import eatyourbeets.orbs.Aether;
import eatyourbeets.orbs.Earth;
import eatyourbeets.orbs.Fire;
import eatyourbeets.powers.common.AgilityPower;
import eatyourbeets.powers.common.ForcePower;
import eatyourbeets.powers.common.IntellectPower;

import java.util.ArrayList;
import java.util.UUID;
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
                AbstractDungeon.actionManager.addToTop(action);
                break;

            case Bottom:
                AbstractDungeon.actionManager.addToBottom(action);
                break;

            case TurnStart:
                AbstractDungeon.actionManager.addToTurnStart(action);
                break;

            case NextCombat:
                AbstractDungeon.actionManager.addToNextCombat(action);
                break;
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

    public static WaitAction Wait(float duration)
    {
        WaitAction action = new WaitAction(duration);
        AddToDefault(action);
        return action;
    }

    public static WaitRealtimeAction WaitRealtime(float duration)
    {
        WaitRealtimeAction action = new WaitRealtimeAction(duration);
        AddToDefault(action);
        return action;
    }

    public static SFXAction SFX(String key)
    {
        SFXAction action = new SFXAction(key);
        AddToDefault(action);
        return action;
    }

    public static SFXAction SFX(String key, float pitchVar)
    {
        SFXAction action = new SFXAction(key, pitchVar);
        AddToDefault(action);
        return action;
    }

    public static VFXAction VFX(AbstractGameEffect effect)
    {
        VFXAction action = new VFXAction(effect);
        AddToDefault(action);
        return action;
    }

    public static VFXAction VFX(AbstractGameEffect effect, float duration)
    {
        VFXAction action = new VFXAction(effect, duration);
        AddToDefault(action);
        return action;
    }

    public static PurgeAnywhereAction PurgeCard(UUID uuid)
    {
        PurgeAnywhereAction action = new PurgeAnywhereAction(uuid);
        AddToDefault(action);
        return action;
    }

    public static PurgeAnywhereAction PurgeCard(AbstractCard card)
    {
        PurgeAnywhereAction action = new PurgeAnywhereAction(card);
        AddToDefault(action);
        return action;
    }

    public static MoveSpecificCardAction MoveCard(AbstractCard card, CardGroup destination, CardGroup source, boolean showEffect)
    {
        MoveSpecificCardAction action = new MoveSpecificCardAction(card, destination, source, showEffect);
        AddToDefault(action);
        return action;
    }

    public static MoveToVoidAction MoveToVoid(AbstractCard card)
    {
        MoveToVoidAction action = new MoveToVoidAction(card);
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

    public static ChannelAction ChannelRandomOrb(boolean autoEvoke)
    {
        ChannelAction action = new ChannelAction(GetRandomOrb(), autoEvoke);
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

    public static CycleCardAction CycleCardAction(int amount, String sourceName)
    {
        CycleCardAction action = new CycleCardAction(AbstractDungeon.player, amount, sourceName);
        AddToDefault(action);
        return action;
    }

    public static GainBlockAction GainBlock(AbstractCreature source, int amount, boolean superfast)
    {
        GainBlockAction action = new GainBlockAction(source, source, amount, superfast);
        AddToDefault(action);
        return action;
    }

    public static void RandomCostReduction(int times, int amount, boolean permanent)
    {
        for (int i = 0; i < times; i++)
        {
            AddToDefault(new RandomCostReductionAction(amount, permanent));
        }
    }

    public static void Motivate(int times, int costReduction)
    {
        for (int i = 0; i < times; i++)
        {
            AddToDefault(new MotivateAction(costReduction));
        }
    }

    public static MotivateAction Motivate(int costReduction)
    {
        MotivateAction action = new MotivateAction(costReduction);
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

    public static DamageAction DamageTarget(AbstractCreature source, AbstractCreature target, int amount, DamageInfo.DamageType damageType, AbstractGameAction.AttackEffect effect, boolean superFast)
    {
        DamageAction action = new DamageAction(target, new DamageInfo(source, amount, damageType), effect, superFast);
        AddToDefault(action);
        return action;
    }

    public static DamageAction DamageTarget(AbstractCreature source, AbstractCreature target, int amount, DamageInfo.DamageType damageType, AbstractGameAction.AttackEffect effect)
    {
        DamageAction action = new DamageAction(target, new DamageInfo(source, amount, damageType), effect);
        AddToDefault(action);
        return action;
    }

    public static DamageAllEnemiesPiercingAction DamageAllEnemiesPiercing(AbstractCreature source, int[] amount, DamageInfo.DamageType damageType, AbstractGameAction.AttackEffect effect, boolean isFast)
    {
        DamageAllEnemiesPiercingAction action = new DamageAllEnemiesPiercingAction(source, amount, damageType, effect, isFast);
        AddToDefault(action);
        return action;
    }

    public static DamageAllEnemiesAction DamageAllEnemies(AbstractCreature source, int[] amount, DamageInfo.DamageType damageType, AbstractGameAction.AttackEffect effect, boolean isFast)
    {
        DamageAllEnemiesAction action = new DamageAllEnemiesAction(source, amount, damageType, effect, isFast);
        AddToDefault(action);
        return action;
    }

    public static DamageAllEnemiesAction DamageAllEnemies(AbstractCreature source, int[] amount, DamageInfo.DamageType damageType, AbstractGameAction.AttackEffect effect)
    {
        DamageAllEnemiesAction action = new DamageAllEnemiesAction(source, amount, damageType, effect);
        AddToDefault(action);
        return action;
    }

    public static DamageRandomEnemy2Action DamageRandomEnemyWhichActuallyWorks(AbstractCreature source, AbstractCard card, AbstractGameAction.AttackEffect effect)
    {
        DamageRandomEnemy2Action action = new DamageRandomEnemy2Action(new DamageInfo(source, card.baseDamage, card.damageTypeForTurn), effect);
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

    public static CallbackAction DelayedAction(OnCallbackSubscriber subscriber)
    {
        return DelayedAction(subscriber, subscriber);
    }

    public static CallbackAction DelayedAction(OnCallbackSubscriber subscriber, Object state)
    {
        CallbackAction callbackAction = new CallbackAction(new WaitAction(0.1f), subscriber::OnCallback, state);
        AddToDefault(callbackAction);
        return callbackAction;
    }

    public static CallbackAction Callback(AbstractGameAction action, BiConsumer<Object, AbstractGameAction> onCompletion, Object state)
    {
        CallbackAction callbackAction = new CallbackAction(action, onCompletion, state);
        AddToDefault(callbackAction);
        return callbackAction;
    }

    public static SequentialAction Sequential(AbstractGameAction action, AbstractGameAction action2)
    {
        SequentialAction sequentialAction = new SequentialAction(action, action2);
        AddToDefault(sequentialAction);
        return sequentialAction;
    }

    public static ChooseFromPileAction ChooseFromPile(int amount, boolean random, CardGroup source, BiConsumer<Object, ArrayList<AbstractCard>> onCompletion, Object state, String message, boolean useSource)
    {
        ChooseFromPileAction action = new ChooseFromPileAction(amount, random, source, onCompletion, state, message, useSource);
        AddToDefault(action);
        return action;
    }

    public static ChooseFromHandAction ChooseFromHand(int amount, boolean random, BiConsumer<Object, ArrayList<AbstractCard>> onCompletion, Object state, String message)
    {
        ChooseFromHandAction action = new ChooseFromHandAction(amount, random, onCompletion, state, message);
        AddToDefault(action);
        return action;
    }

    public static OnCardDrawnAction DrawCard(AbstractCreature source, int amount, BiConsumer<Object, ArrayList<AbstractCard>> onDraw, Object context)
    {
        OnCardDrawnAction action = new OnCardDrawnAction(source, amount, onDraw, context);
        AddToDefault(action);
        return action;
    }

    public static MakeTempCardInDrawPileAction MakeCardInDrawPile(AbstractCard card, int amount, boolean upgraded)
    {
        if (upgraded)
        {
            card.upgrade();
        }

        MakeTempCardInDrawPileAction action = new MakeTempCardInDrawPileAction(card, amount, true, true);
        AddToDefault(action);
        return action;
    }

    public static MakeTempCardInDiscardAction MakeCardInDiscardPile(AbstractCard card, int amount, boolean upgraded)
    {
        if (upgraded)
        {
            card.upgrade();
        }

        MakeTempCardInDiscardAction action = new MakeTempCardInDiscardAction(card, amount);
        AddToDefault(action);
        return action;
    }

    public static MakeTempCardInHandAction MakeCardInHand(AbstractCard card, int amount, boolean upgraded)
    {
        if (upgraded)
        {
            card.upgrade();
        }

        MakeTempCardInHandAction action = new MakeTempCardInHandAction(card, amount);
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
        for (AbstractMonster m : GameUtilities.GetCurrentEnemies(true))
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

    public static ApplyPowerAction ApplyPower(AbstractCreature source, AbstractCreature target, AbstractPower power, int stacks, boolean isFast)
    {
        ApplyPowerAction action = new ApplyPowerAction(target, source, power, stacks, isFast);
        AddToDefault(action);
        return action;
    }

    public static ApplyPowerSilentlyAction ApplyPowerSilently(AbstractCreature source, AbstractCreature target, AbstractPower power, int stacks)
    {
        ApplyPowerSilentlyAction action = new ApplyPowerSilentlyAction(target, source, power, stacks);
        AddToDefault(action);
        return action;
    }

    public static ApplyPowerAction GainForce(int amount)
    {
        AbstractPlayer p = AbstractDungeon.player;
        return ApplyPower(p, p, new ForcePower(p, amount), amount);
    }

    public static ApplyPowerAction GainIntellect(int amount)
    {
        AbstractPlayer p = AbstractDungeon.player;
        return ApplyPower(p, p, new IntellectPower(p, amount), amount);
    }

    public static ApplyPowerAction GainAgility(int amount)
    {
        AbstractPlayer p = AbstractDungeon.player;
        return ApplyPower(p, p, new AgilityPower(p, amount), amount);
    }

    public static ApplyPowerAction GainRandomStat(int amount)
    {
        int roll = AbstractDungeon.cardRandomRng.random(2);
        switch (roll)
        {
            case 0:
            {
                return GameActionsHelper.GainIntellect(amount);
            }
            case 1:
            {
                return GameActionsHelper.GainAgility(amount);
            }
            case 2:
            default:
            {
                return GameActionsHelper.GainForce(amount);
            }
        }
    }

    public static AddTemporaryHPAction GainTemporaryHP(AbstractCreature target, int amount)
    {
        AddTemporaryHPAction action = new AddTemporaryHPAction(target, target, amount);
        AddToDefault(action);
        return action;
    }

    public static AddTemporaryHPAction GainTemporaryHP(AbstractCreature source, AbstractCreature target, int amount)
    {
        AddTemporaryHPAction action = new AddTemporaryHPAction(target, source, amount);
        AddToDefault(action);
        return action;
    }

    public static DiscardAction Discard(int amount, boolean random)
    {
        DiscardAction action = new DiscardAction(AbstractDungeon.player, AbstractDungeon.player, amount, random);
        AddToDefault(action);
        AddToBottom(new WaitAction(0.2f));
        return action;
    }

    public static void PlayCard(AbstractCard card, AbstractMonster m)
    {
        if (card.cost > 0)
        {
            card.freeToPlayOnce = true;
        }

        AbstractDungeon.player.limbo.group.add(card);
        card.current_y = -200.0F * Settings.scale;
        card.target_x = (float) Settings.WIDTH / 2.0F + 200.0F * Settings.scale;
        card.target_y = (float) Settings.HEIGHT / 2.0F;
        card.targetAngle = 0.0F;
        card.lighten(false);
        card.drawScale = 0.12F;
        card.targetDrawScale = 0.75F;
        if (!card.canUse(AbstractDungeon.player, m))
        {
            AbstractDungeon.actionManager.addToTop(new UnlimboAction(card));
            AbstractDungeon.actionManager.addToTop(new DiscardSpecificCardAction(card, AbstractDungeon.player.limbo));
            AbstractDungeon.actionManager.addToTop(new WaitAction(0.4F));
        }
        else
        {
            card.applyPowers();
            card.freeToPlayOnce = true;
            AbstractDungeon.actionManager.addToTop(new QueueCardAction(card, m));
            AbstractDungeon.actionManager.addToTop(new UnlimboAction(card));
            if (!Settings.FAST_MODE)
            {
                AbstractDungeon.actionManager.addToTop(new WaitAction(Settings.ACTION_DUR_MED));
            }
            else
            {
                AbstractDungeon.actionManager.addToTop(new WaitAction(Settings.ACTION_DUR_FASTER));
            }
        }
    }

    public static CardQueueItem PlayCopy(AbstractCard source, AbstractMonster m)
    {
        AbstractCard temp = source.makeSameInstanceOf();
        AbstractDungeon.player.limbo.addToBottom(temp);
        temp.current_x = source.current_x;
        temp.current_y = source.current_y;
        temp.target_x = (float) Settings.WIDTH / 2.0F - 300.0F * Settings.scale;
        temp.target_y = (float) Settings.HEIGHT / 2.0F;

        if (temp.cost > 0)
        {
            temp.freeToPlayOnce = true;
        }

        temp.calculateCardDamage(m);
        temp.purgeOnUse = true;

        CardQueueItem item = new CardQueueItem(temp, m, source.energyOnUse, true);
        AbstractDungeon.actionManager.cardQueue.add(item);

        return item;
    }

    private static final WeightedList<AbstractOrb> orbs = new WeightedList<>();
    private static AbstractOrb GetRandomOrb()
    {
        if (orbs.Count() == 0)
        {
            orbs.Add(new Lightning(), 7);
            orbs.Add(new Frost(), 7);
            orbs.Add(new Earth(), 6);
            orbs.Add(new Fire(), 6);
            orbs.Add(new Plasma(), 4);
            orbs.Add(new Dark(), 4);
            orbs.Add(new Aether(), 4);
        }

        return orbs.Retrieve(AbstractDungeon.cardRandomRng, false).makeCopy();
    }
}
