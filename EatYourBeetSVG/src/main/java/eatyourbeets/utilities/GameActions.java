package eatyourbeets.utilities;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.defect.IncreaseMaxOrbAction;
import com.megacrit.cardcrawl.actions.unique.ArmamentsAction;
import com.megacrit.cardcrawl.actions.unique.RetainCardsAction;
import com.megacrit.cardcrawl.actions.utility.ShakeScreenAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.stances.AbstractStance;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.BorderLongFlashEffect;
import com.megacrit.cardcrawl.vfx.cardManip.CardFlashVfx;
import eatyourbeets.actions.EYBAction;
import eatyourbeets.actions.animator.CreateThrowingKnives;
import eatyourbeets.actions.autoTarget.ApplyPowerAuto;
import eatyourbeets.actions.basic.*;
import eatyourbeets.actions.cardManipulation.*;
import eatyourbeets.actions.damage.DealDamage;
import eatyourbeets.actions.damage.DealDamageToAll;
import eatyourbeets.actions.damage.DealDamageToRandomEnemy;
import eatyourbeets.actions.damage.LoseHP;
import eatyourbeets.actions.handSelection.*;
import eatyourbeets.actions.monsters.TalkAction;
import eatyourbeets.actions.orbs.ChannelOrb;
import eatyourbeets.actions.orbs.EvokeOrb;
import eatyourbeets.actions.orbs.TriggerOrbPassiveAbility;
import eatyourbeets.actions.pileSelection.*;
import eatyourbeets.actions.player.ChangeStance;
import eatyourbeets.actions.player.GainGold;
import eatyourbeets.actions.player.SpendEnergy;
import eatyourbeets.actions.powers.*;
import eatyourbeets.actions.special.DelayAllActions;
import eatyourbeets.actions.special.PlaySFX;
import eatyourbeets.actions.special.PlayVFX;
import eatyourbeets.actions.special.SelectCreature;
import eatyourbeets.actions.unnamed.ActivateDoll;
import eatyourbeets.actions.unnamed.HealDoll;
import eatyourbeets.actions.unnamed.ModifyDollHP;
import eatyourbeets.actions.unnamed.SummonDoll;
import eatyourbeets.actions.utility.CallbackAction;
import eatyourbeets.actions.utility.SequentialAction;
import eatyourbeets.actions.utility.WaitRealtimeAction;
import eatyourbeets.cards.animator.tokens.AffinityToken;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.SFX;
import eatyourbeets.interfaces.delegates.*;
import eatyourbeets.interfaces.subscribers.OnPhaseChangedSubscriber;
import eatyourbeets.monsters.PlayerMinions.UnnamedDoll;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.powers.affinity.animatorClassic.CorruptionPower;
import eatyourbeets.powers.affinity.animatorClassic.*;
import eatyourbeets.powers.animator.EarthenThornsPower;
import eatyourbeets.powers.common.EnergizedPower;
import eatyourbeets.powers.common.*;
import eatyourbeets.powers.replacement.*;
import eatyourbeets.powers.unnamed.AmplificationPower;
import eatyourbeets.powers.unnamed.WitheringPower;
import eatyourbeets.resources.GR;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.player;

@SuppressWarnings("UnusedReturnValue")
public final class GameActions
{
    public enum ActionOrder
    {
        TurnStart,
        NextCombat,

        Instant,
        Top,
        Bottom,
        Delayed,
        DelayedTop,
        Last
    }

    @Deprecated
    public static final GameActions NextCombat = new GameActions(ActionOrder.NextCombat);
    @Deprecated
    public static final GameActions TurnStart = new GameActions(ActionOrder.TurnStart);

    public static final GameActions Instant = new GameActions(ActionOrder.Instant);
    public static final GameActions Top = new GameActions(ActionOrder.Top);
    public static final GameActions Bottom = new GameActions(ActionOrder.Bottom);
    public static final GameActions Delayed = new GameActions(ActionOrder.Delayed);
    public static final GameActions DelayedTop = new GameActions(ActionOrder.DelayedTop);
    public static final GameActions Last = new GameActions(ActionOrder.Last);

    protected final ActionOrder actionOrder;

    protected GameActions(ActionOrder actionOrder)
    {
        this.actionOrder = actionOrder;
    }

    public static void ClearActions()
    {
        AbstractDungeon.actionManager.actions.clear();
    }

    public static DelayAllActions DelayCurrentActions()
    {
        return Top.Add(new DelayAllActions(true));
    }

    public static ArrayList<AbstractGameAction> GetActions()
    {
        return AbstractDungeon.actionManager.actions;
    }

    public static void InsertActions(ArrayList<AbstractGameAction> toAdd, int index)
    {
        final ArrayList<AbstractGameAction> actions = GetActions();
        for (int i = toAdd.size() - 1; i >= 0; i++)
        {
            actions.add(index, toAdd.get(i));
        }
    }

    public static ArrayList<AbstractGameAction> GetActionsCopy(boolean clearOriginal)
    {
        final ArrayList<AbstractGameAction> actions = GetActions();
        final ArrayList<AbstractGameAction> copy = new ArrayList<>(actions);
        if (clearOriginal)
        {
            actions.clear();
        }

        return copy;
    }

    public <T extends AbstractGameAction> T Add(T action)
    {
        if (action instanceof EYBAction)
        {
            ((EYBAction)action).SetOriginalOrder(actionOrder);
        }

        switch (actionOrder)
        {
            case Top:
            {
                AbstractDungeon.actionManager.addToTop(action);
                break;
            }

            case Bottom:
            {
                AbstractDungeon.actionManager.addToBottom(action);
                break;
            }

            case TurnStart:
            {
                AbstractDungeon.actionManager.addToTurnStart(action);
                break;
            }

            case NextCombat:
            {
                AbstractDungeon.actionManager.addToNextCombat(action);
                break;
            }

            case Instant:
            {
                AbstractGameAction current = AbstractDungeon.actionManager.currentAction;
                if (current != null)
                {
                    AbstractDungeon.actionManager.addToTop(current);
                }

                AbstractDungeon.actionManager.currentAction = action;
                AbstractDungeon.actionManager.phase = GameActionManager.Phase.EXECUTING_ACTIONS;

                break;
            }

            case Delayed:
            {
                Bottom.Callback(action, Bottom::Add);
                break;
            }

            case DelayedTop:
            {
                Bottom.Callback(action, Bottom::Add);
                break;
            }

            case Last:
            {
                ExecuteLast.Add(action);
                break;
            }
        }

        return action;
    }

    public ApplyPower ApplyBurning(AbstractCreature source, AbstractCreature target, int amount)
    {
        return StackPower(source, new BurningPower(target, source, amount));
    }

    public ApplyPowerAuto ApplyBurning(TargetHelper target, int amount)
    {
        return StackPower(target, PowerHelper.Burning, amount);
    }

    public ApplyPower ApplyConstricted(AbstractCreature source, AbstractCreature target, int amount)
    {
        return StackPower(source, new AnimatorConstrictedPower(target, source, amount));
    }

    public ApplyPowerAuto ApplyConstricted(TargetHelper target, int amount)
    {
        return StackPower(target, PowerHelper.Constricted, amount);
    }

    public ApplyPower ApplyFrail(AbstractCreature source, AbstractCreature target, int amount)
    {
        return StackPower(source, new FrailPower(target, amount, source == null || GameUtilities.IsMonster(source)));
    }

    public ApplyPower ApplyFreezing(AbstractCreature source, AbstractCreature target, int amount)
    {
        return StackPower(source, new FreezingPower(target, source, amount));
    }

    public ApplyPowerAuto ApplyFreezing(TargetHelper target, int amount)
    {
        return StackPower(target, PowerHelper.Freezing, amount);
    }

    public ApplyPower ApplyPoison(AbstractCreature source, AbstractCreature target, int amount)
    {
        return StackPower(source, target.isPlayer ? new PoisonPlayerPower(target, source, amount) : new PoisonPower(target, source, amount));
    }

    public ApplyPowerAuto ApplyPoison(TargetHelper target, int amount)
    {
        return StackPower(target, PowerHelper.Poison, amount);
    }

    public ApplyPower ApplyLockOn(AbstractCreature source, AbstractCreature target, int amount)
    {
        return StackPower(source, new LockOnPower(target, amount));
    }

    public ApplyPowerAuto ApplyLockOn(TargetHelper target, int amount)
    {
        return StackPower(target, PowerHelper.LockOn, amount);
    }

    public ApplyPower ApplyPower(AbstractPower power)
    {
        return ApplyPower(power.owner, power.owner, power);
    }

    public ApplyPowerAuto ApplyPower(TargetHelper target, PowerHelper power)
    {
        return Add(new ApplyPowerAuto(target, power, 1)).CanStack(false);
    }

    public ApplyPower ApplyPower(AbstractCreature source, AbstractPower power)
    {
        return ApplyPower(source, power.owner, power);
    }

    public ApplyPower ApplyPower(AbstractCreature source, AbstractCreature target, AbstractPower power)
    {
        return Add(new ApplyPower(source, target, power)).CanStack(false);
    }

    public ApplyPower ApplyVulnerable(AbstractCreature source, AbstractCreature target, int amount)
    {
        return StackPower(source, new AnimatorVulnerablePower(target, amount, source == null || GameUtilities.IsMonster(source)));
    }

    public ApplyPowerAuto ApplyVulnerable(TargetHelper target, int amount)
    {
        return StackPower(target, PowerHelper.Vulnerable, amount);
    }

    public ApplyPower ApplyWeak(AbstractCreature source, AbstractCreature target, int amount)
    {
        return StackPower(source, new WeakPower(target, amount, source == null || GameUtilities.IsMonster(source)));
    }

    public ApplyPowerAuto ApplyWeak(TargetHelper target, int amount)
    {
        return StackPower(target, PowerHelper.Weak, amount);
    }

    public PlayCard AutoPlay(AbstractCard card, CardGroup group, AbstractMonster target)
    {
        return (PlayCard) PlayCard(card, group, target).SpendEnergy(true, true).AddCondition(AbstractCard::hasEnoughEnergy);
    }

    public ApplyPower BlockNextTurn(int amount)
    {
        return StackPower(new NextTurnBlockPower(player, amount));
    }

    public PlayVFX BorderFlash(Color color)
    {
        return VFX(new BorderFlashEffect(color, true));
    }

    public PlayVFX BorderLongFlash(Color color)
    {
        return VFX(new BorderLongFlashEffect(color, true));
    }

    public <T> CallbackAction Callback(AbstractGameAction action, T state, ActionT2<T, AbstractGameAction> onCompletion)
    {
        return Add(new CallbackAction(action, state, onCompletion));
    }

    public CallbackAction Callback(AbstractGameAction action, ActionT1<AbstractGameAction> onCompletion)
    {
        return Add(new CallbackAction(action, onCompletion));
    }

    public CallbackAction Callback(AbstractGameAction action, ActionT0 onCompletion)
    {
        return Add(new CallbackAction(action, onCompletion));
    }

    public CallbackAction Callback(AbstractGameAction action)
    {
        return Add(new CallbackAction(action));
    }

    public CallbackAction Callback(ActionT0 onCompletion)
    {
        return Callback(new WaitAction(0.02f), onCompletion);
    }

    public CallbackAction Callback(ActionT1<AbstractGameAction> onCompletion)
    {
        return Callback(new WaitAction(0.02f), onCompletion);
    }

    public <T> CallbackAction Callback(T state, ActionT2<T, AbstractGameAction> onCompletion)
    {
        return Callback(new WaitAction(0.02f), state, onCompletion);
    }

    public ChangeStanceAction ChangeStance(AbstractStance stance)
    {
        return Add(new ChangeStanceAction(stance));
    }

    public ChangeStance ChangeStance(String stanceName)
    {
        return Add(new ChangeStance(stanceName));
    }

    public ChannelOrb ChannelOrb(AbstractOrb orb)
    {
        return Add(new ChannelOrb(orb));
    }

    public ChannelOrb ChannelOrbs(FuncT0<AbstractOrb> orbConstructor, int amount)
    {
        return Add(new ChannelOrb(orbConstructor, amount));
    }

    public ChannelOrb ChannelRandomOrb(int amount)
    {
        return Add(new ChannelOrb(GameUtilities::GetRandomOrb, amount));
    }

    public CreateThrowingKnives CreateThrowingKnives(int amount)
    {
        return Add(new CreateThrowingKnives(amount));
    }

    public CycleCards Cycle(String sourceName, int amount)
    {
        return (CycleCards) Add(new CycleCards(sourceName, amount, false)
        .SetOptions(true, true, true));
    }

    public DealDamage DealDamage(AbstractCreature source, AbstractCreature target, int amount, DamageInfo.DamageType damageType, AbstractGameAction.AttackEffect effect)
    {
        return Add(new DealDamage(target, new DamageInfo(source, amount, damageType), effect));
    }

    public DealDamage DealDamage(EYBCard card, AbstractCreature target, AbstractGameAction.AttackEffect effect)
    {
        return Add(new DealDamage(target, new DamageInfo(player, card.damage, card.damageTypeForTurn), effect))
        .SetPiercing(card.attackType.bypassThorns, card.attackType.bypassBlock);
    }

    public DealDamageToAll DealDamageToAll(EYBCard card, AbstractGameAction.AttackEffect effect)
    {
        return Add(new DealDamageToAll(player, card.multiDamage, card.damageTypeForTurn, effect, false))
        .SetPiercing(card.attackType.bypassThorns, card.attackType.bypassBlock);
    }

    public DealDamageToAll DealDamageToAll(int[] damageMatrix, DamageInfo.DamageType damageType, AbstractGameAction.AttackEffect effect)
    {
        return Add(new DealDamageToAll(player, damageMatrix, damageType, effect, false));
    }

    public DealDamageToRandomEnemy DealDamageToRandomEnemy(int baseDamage, DamageInfo.DamageType damageType, AbstractGameAction.AttackEffect effect)
    {
        return Add(new DealDamageToRandomEnemy(new DamageInfo(player, baseDamage, damageType), effect));
    }

    public DealDamageToRandomEnemy DealDamageToRandomEnemy(EYBCard card, AbstractGameAction.AttackEffect effect)
    {
        return Add(new DealDamageToRandomEnemy(card, effect))
        .SetPiercing(card.attackType.bypassThorns, card.attackType.bypassBlock);
    }

    public MoveCard Discard(AbstractCard card, CardGroup group)
    {
        return MoveCard(card, group, player.discardPile);
    }

    public DiscardFromHand DiscardFromHand(String sourceName, int amount, boolean isRandom)
    {
        return Add(new DiscardFromHand(sourceName, amount, isRandom));
    }

    public DiscardFromPile DiscardFromPile(String sourceName, int amount, CardGroup... groups)
    {
        return Add(new DiscardFromPile(sourceName, amount, groups));
    }

    public DrawCards Draw(int amount)
    {
        return Add(new DrawCards(amount));
    }

    public MoveCard Draw(AbstractCard card)
    {
        final float cardX = CardGroup.DRAW_PILE_X * 1.5f;
        final float cardY = CardGroup.DRAW_PILE_Y * 2f;

        return MoveCard(card, player.drawPile, player.hand)
        .SetCardPosition(cardX, cardY)
        .ShowEffect(true, false);
    }

    public ApplyPower DrawReduction(int amount)
    {
        return StackPower(new NextTurnDrawReductionPower(player, amount)).ShowEffect(false, false);
    }

    public ApplyPower DrawNextTurn(int amount)
    {
        return StackPower(new DrawCardNextTurnPower(player, amount));
    }

    public EvokeOrb EvokeOrb(int times)
    {
        return Add(new EvokeOrb(times, EvokeOrb.Mode.SameOrb));
    }

    public EvokeOrb EvokeOrb(int times, AbstractOrb orb)
    {
        return Add(new EvokeOrb(times, orb));
    }

    public EvokeOrb EvokeOrb(int times, EvokeOrb.Mode mode)
    {
        return Add(new EvokeOrb(times, mode));
    }

    public MoveCard Exhaust(AbstractCard card)
    {
        return MoveCard(card, player.exhaustPile);
    }

    public MoveCard Exhaust(AbstractCard card, CardGroup group)
    {
        return MoveCard(card, group, player.exhaustPile);
    }

    public ExhaustFromHand ExhaustFromHand(String sourceName, int amount, boolean isRandom)
    {
        return Add(new ExhaustFromHand(sourceName, amount, isRandom));
    }

    public ExhaustFromPile ExhaustFromPile(String sourceName, int amount, CardGroup... groups)
    {
        return Add(new ExhaustFromPile(sourceName, amount, groups));
    }

    public FetchFromPile FetchFromPile(String sourceName, int amount, CardGroup... groups)
    {
        return Add(new FetchFromPile(sourceName, amount, groups));
    }

    public PlayVFX Flash(AbstractCard card)
    {
        return Flash(card, Color.ORANGE.cpy(), false);
    }

    public PlayVFX SuperFlash(AbstractCard card)
    {
        return Flash(card, Color.ORANGE.cpy(), true);
    }

    public PlayVFX Flash(AbstractCard card, Color color, boolean superFlash)
    {
        return VFX(new CardFlashVfx(card, color.cpy(), superFlash));
    }

    public ApplyAffinityPower GainAgility(int amount)
    {
        return GainAgility(amount, false);
    }

    public ApplyAffinityPower GainAgility(int amount, boolean retain)
    {
        return GainAffinity(AgilityPower.AFFINITY_TYPE, amount, retain);
    }

    public ApplyPower GainArtifact(int amount)
    {
        return StackPower(new ArtifactPower(player, amount));
    }

    public ApplyAffinityPower GainBlessing(int amount)
    {
        return GainBlessing(amount, false);
    }

    public ApplyAffinityPower GainBlessing(int amount, boolean retain)
    {
        return GainAffinity(BlessingPower.AFFINITY_TYPE, amount, retain);
    }

    public GainBlock GainBlock(int amount)
    {
        return GainBlock(player, amount);
    }

    public GainBlock GainBlock(AbstractCreature target, int amount)
    {
        return Add(new GainBlock(target, target, amount));
    }

    public LoseBlock LoseBlock(int amount)
    {
        return LoseBlock(player, amount);
    }

    public LoseBlock LoseBlock(AbstractCreature target, int amount)
    {
        return Add(new LoseBlock(target, target, amount));
    }

    public ApplyPower GainBlur(int amount)
    {
        return StackPower(new AnimatorBlurPower(player, amount));
    }

    public ApplyAffinityPower GainCorruption(int amount)
    {
        return GainCorruption(amount, false);
    }
    
    public ApplyAffinityPower GainCorruption(int amount, boolean retain)
    {
        return GainAffinity(CorruptionPower.AFFINITY_TYPE, amount, retain);
    }

    public ApplyPower GainDexterity(int amount)
    {
        return StackPower(new DexterityPower(player, amount));
    }

    public ApplyPower GainDexterity(AbstractCreature source, AbstractCreature target, int amount)
    {
        return StackPower(source, new DexterityPower(target, amount));
    }

    public GainEnergyAction GainEnergy(int amount)
    {
        return Add(new GainEnergyAction(amount));
    }

    public ApplyPower GainEnergyNextTurn(int amount)
    {
        return StackPower(new EnergizedPower(player, amount));
    }

    public ApplyPower GainFocus(int amount)
    {
        return StackPower(new FocusPower(player, amount));
    }

    public ApplyAffinityPower GainForce(int amount)
    {
        return GainForce(amount, false);
    }

    public ApplyAffinityPower GainForce(int amount, boolean retain)
    {
        return GainAffinity(ForcePower.AFFINITY_TYPE, amount, retain);
    }

    public GainGold GainGold(int amount)
    {
        return Add(new GainGold(amount, true));
    }

    public ApplyPower GainInspiration(int amount)
    {
        return StackPower(new InspirationPower(player, amount));
    }

    public ApplyPower GainIntangible(int amount)
    {
        return StackPower(new AnimatorIntangiblePower(player, amount));
    }

    public ApplyAffinityPower GainIntellect(int amount)
    {
        return GainIntellect(amount, false);
    }

    public ApplyAffinityPower GainIntellect(int amount, boolean retain)
    {
        return GainAffinity(IntellectPower.AFFINITY_TYPE, amount, retain);
    }

    public ApplyPower GainMalleable(int amount)
    {
        return GainMalleable(player, player, amount);
    }

    public ApplyPower GainMalleable(AbstractCreature source, AbstractCreature target, int amount)
    {
        return StackPower(source, new MalleablePower(target, amount));
    }

    public ApplyPower GainMetallicize(int amount)
    {
        return StackPower(new AnimatorMetallicizePower(player, amount));
    }

    public IncreaseMaxOrbAction GainOrbSlots(int slots)
    {
        return Add(new IncreaseMaxOrbAction(slots));
    }

    public ApplyPower GainPlatedArmor(int amount)
    {
        return StackPower(new AnimatorPlatedArmorPower(player, amount));
    }

    public ApplyAffinityPower GainRandomAffinityPower(int amount, boolean retain)
    {
        if (GameUtilities.IsPlayerClass(GR.Animator.PlayerClass))
        {
            return GainAffinity(null, amount, retain);
        }
        
        return GainRandomAffinityPower(amount, retain, Affinity.Red, Affinity.Green, Affinity.Blue);
    }

    public ApplyAffinityPower GainRandomAffinityPower(int amount, boolean retain, Affinity... affinities)
    {
        return GainAffinity(GameUtilities.GetRandomElement(affinities), amount, retain);
    }

    public ApplyPower GainStrength(int amount)
    {
        return GainStrength(player, player, amount);
    }

    public ApplyPower GainStrength(AbstractCreature source, AbstractCreature target, int amount)
    {
        return StackPower(source, new StrengthPower(target, amount));
    }

    public ApplyPower GainTemporaryArtifact(int amount)
    {
        return StackPower(new TemporaryArtifactPower(player, amount));
    }

    public GainTemporaryHP GainTemporaryHP(int amount)
    {
        return GainTemporaryHP(player, player, amount);
    }

    public GainTemporaryHP GainTemporaryHP(AbstractCreature source, AbstractCreature target, int amount)
    {
        return Add(new GainTemporaryHP(source, target, amount));
    }

    public ApplyPower GainTemporaryThorns(int amount)
    {
        return StackPower(new EarthenThornsPower(player, amount));
    }

    public ApplyPower GainTemporaryStats(int strength, int dexterity, int focus)
    {
        final HashMap<PowerHelper, Integer> powers = new HashMap<>();
        if (strength != 0)
        {
            powers.put(PowerHelper.Strength, strength);
        }
        if (dexterity != 0)
        {
            powers.put(PowerHelper.Dexterity, dexterity);
        }
        if (focus != 0)
        {
            powers.put(PowerHelper.Focus, focus);
        }

        return StackPower(new TemporaryStatsPower(player, powers));
    }

    public ApplyPower GainTemporaryStats(int amount, PowerHelper... powerHelpers)
    {
        final HashMap<PowerHelper, Integer> powers = new HashMap<>();
        for (PowerHelper power : powerHelpers)
        {
            powers.put(power, amount);
        }

        return StackPower(new TemporaryStatsPower(player, powers));
    }

    public ApplyPower GainThorns(int amount)
    {
        return StackPower(new ThornsPower(player, amount));
    }

    public ApplyPower GainVitality(int amount)
    {
        return StackPower(new VitalityPower(player, amount));
    }

    public HealCreature Heal(AbstractCreature source, AbstractCreature target, int amount)
    {
        return Add(new HealCreature(target, source, amount));
    }

    public HealCreature Heal(int amount)
    {
        return Add(new HealCreature(player, player, amount));
    }

    public HealCreature RecoverHP(int amount)
    {
        return Add(new HealCreature(player, player, amount)).Recover(true);
    }

    public HealCreature HealPlayerLimited(AbstractCard card, int amount)
    {
        return Add(new HealCreature(player, player, amount).SetCard(card));
    }

    public ModifyAffinityScaling IncreaseExistingScaling(AbstractCard card, int amount)
    {
        return Add(new ModifyAffinityScaling(card, Affinity.General, amount, true));
    }

    public ModifyAffinityScaling IncreaseScaling(AbstractCard card, Affinity affinity, int amount)
    {
        return Add(new ModifyAffinityScaling(card, affinity, amount, true));
    }

    public ModifyAffinityScaling IncreaseScaling(CardGroup group, int cards, Affinity affinity, int amount)
    {
        return Add(new ModifyAffinityScaling(group, cards, affinity, amount, true));
    }

    public LoseHP LoseHP(AbstractCreature source, AbstractCreature target, int amount, AbstractGameAction.AttackEffect effect)
    {
        return Add(new LoseHP(target, source, amount, effect));
    }

    public LoseHP LoseHP(int amount, AbstractGameAction.AttackEffect effect)
    {
        return Add(new LoseHP(player, player, amount, effect));
    }

    public ApplyPower LoseHPUntilEndOfCombat(int amount, AbstractGameAction.AttackEffect effect)
    {
        return StackPower(new TemporaryHealthReductionPower(player, amount, effect));
    }

    public GenerateCard MakeCard(AbstractCard card, CardGroup group)
    {
        return Add(new GenerateCard(card, group));
    }

    public GenerateCard MakeCardInDiscardPile(AbstractCard card)
    {
        return MakeCard(card, player.discardPile);
    }

    public GenerateCard MakeCardInDrawPile(AbstractCard card)
    {
        return MakeCard(card, player.drawPile);
    }

    public GenerateCard MakeCardInHand(AbstractCard card)
    {
        return MakeCard(card, player.hand);
    }

    public GenerateCard ObtainAffinityToken(Affinity affinity, boolean upgraded)
    {
        return MakeCardInHand(AffinityToken.GetCard(affinity))
        .PlaySFX(SFX.TINGSHA, 2.25f, 2.5f, 0.75f)
        .SetUpgrade(upgraded, false);
    }

    public ModifyAffinityLevel SealAffinities(AbstractCard card, boolean reshuffle)
    {
        return (ModifyAffinityLevel)Add(new ModifyAffinityLevel(card, null, 0, false))
                .Seal(true, reshuffle).SetDuration(0.05f, false);
    }

    public ModifyAffinityLevel SealAffinities(CardGroup group, int cards, boolean reshuffle)
    {
        return (ModifyAffinityLevel)Add(new ModifyAffinityLevel(group, cards, null, 0, false))
                .Seal(true, reshuffle).SetDuration(0.05f, false);
    }

    public ModifyAffinityLevel ModifyAffinityLevel(AbstractCard card, Affinity affinity, int amount, boolean relative)
    {
        return Add(new ModifyAffinityLevel(card, affinity, amount, relative));
    }

    public ModifyAffinityLevel ModifyAffinityLevel(CardGroup group, int cards, Affinity affinity, int amount, boolean relative)
    {
        return Add(new ModifyAffinityLevel(group, cards, affinity, amount, relative));
    }

    public <S> ModifyAllCopies ModifyAllCopies(String cardID, S state, ActionT2<S, AbstractCard> onCompletion)
    {
        return Add(new ModifyAllCopies(cardID, state, onCompletion));
    }

    public ModifyAllCopies ModifyAllCopies(String cardID, ActionT1<AbstractCard> onCompletion)
    {
        return Add(new ModifyAllCopies(cardID, onCompletion));
    }

    public ModifyAllCopies ModifyAllCopies(String cardID)
    {
        return Add(new ModifyAllCopies(cardID));
    }

    public <S> ModifyAllInstances ModifyAllInstances(UUID uuid, S state, ActionT2<S, AbstractCard> onCompletion)
    {
        return Add(new ModifyAllInstances(uuid, state, onCompletion));
    }

    public ModifyAllInstances ModifyAllInstances(UUID uuid, ActionT1<AbstractCard> onCompletion)
    {
        return Add(new ModifyAllInstances(uuid, onCompletion));
    }

    public ModifyAllInstances ModifyAllInstances(UUID uuid)
    {
        return Add(new ModifyAllInstances(uuid));
    }

    public MotivateCard Motivate(AbstractCard card, int amount)
    {
        return Add(new MotivateCard(card, amount));
    }

    public MotivateCards Motivate(ArrayList<AbstractCard> cards, int amount)
    {
        return Add(new MotivateCards(cards, amount));
    }

    public MotivateCards Motivate(int cards)
    {
        return Motivate(player.hand, cards, 1);
    }

    public MotivateCards Motivate(CardGroup group, int cards)
    {
        return Motivate(group, cards, 1);
    }

    public MotivateCards Motivate(CardGroup group, int cards, int amount)
    {
        return Add(new MotivateCards(group, cards, amount));
    }

    public MoveCard MoveCard(AbstractCard card, CardGroup destination)
    {
        return Add(new MoveCard(card, destination));
    }

    public MoveCard MoveCard(AbstractCard card, CardGroup source, CardGroup destination)
    {
        return Add(new MoveCard(card, destination, source));
    }

    public MoveCards MoveCards(CardGroup source, CardGroup destination)
    {
        return Add(new MoveCards(destination, source));
    }

    public MoveCards MoveCards(CardGroup source, CardGroup destination, int amount)
    {
        return Add(new MoveCards(destination, source, amount));
    }

    public PlayCard PlayCard(CardGroup sourcePile, AbstractMonster target, FuncT1<AbstractCard, CardGroup> findCard)
    {
        return Add(new PlayCard(findCard, sourcePile, target));
    }

    public PlayCard PlayCard(AbstractCard card, CardGroup sourcePile, AbstractMonster target)
    {
        return Add(new PlayCard(card, target, false, actionOrder != ActionOrder.Top)).SetSourcePile(sourcePile);
    }

    public PlayCard PlayCard(AbstractCard card, AbstractMonster target)
    {
        return Add(new PlayCard(card, target, false, actionOrder != ActionOrder.Top));
    }

    public PlayCard PlayCopy(AbstractCard card, AbstractMonster target)
    {
        return Add(new PlayCard(card, target, true, actionOrder != ActionOrder.Top))
        .SetCurrentPosition(card.current_x, card.current_y)
        .SpendEnergy(false, true)
        .SetPurge(true);
    }

    public PlayFromPile PlayFromPile(String sourceName, int amount, AbstractMonster target, CardGroup... groups)
    {
        return Add(new PlayFromPile(sourceName, target, amount, groups));
    }

    public ApplyPower PlayNextTurn(AbstractCard card, AbstractMonster target)
    {
        return StackPower(new NextTurnPlayCardPower(player, card, target));
    }

    public ApplyPower PlayNextTurn(ArrayList<AbstractCard> cards, AbstractMonster target)
    {
        return StackPower(new NextTurnPlayCardPower(player, cards, target));
    }

    public PurgeAnywhere Purge(UUID uuid)
    {
        return Add(new PurgeAnywhere(uuid));
    }

    public PurgeAnywhere Purge(AbstractCard card)
    {
        return Add(new PurgeAnywhere(card));
    }

    public PurgeFromPile PurgeFromPile(String sourceName, int amount, CardGroup... groups)
    {
        return Add(new PurgeFromPile(sourceName, amount, groups));
    }

    public ReducePower ReducePower(AbstractCreature source, String powerID, int amount)
    {
        return Add(new ReducePower(source, source, powerID, amount));
    }

    public ReducePower ReducePower(AbstractPower power, int amount)
    {
        return Add(new ReducePower(power.owner, power.owner, power, amount));
    }

    public ReducePower ReducePower(Affinity affinity, int amount)
    {
        return ReducePower(GameUtilities.GetPower(affinity), amount);
    }

    public IncreasePower IncreasePower(AbstractPower power, int amount)
    {
        return Add(new IncreasePower(power.owner, power.owner, power, amount));
    }

    public ApplyPower ReboundCards(int amount)
    {
        final ReboundPower power = new ReboundPower(player);
        power.amount = amount;
        return StackPower(power);
    }

    public ReduceStrength ReduceStrength(AbstractCreature target, int amount, boolean temporary)
    {
        return ReduceStrength(player, target, amount, temporary);
    }

    public ReduceStrength ReduceStrength(AbstractCreature source, AbstractCreature target, int amount, boolean temporary)
    {
        return Add(new ReduceStrength(source, target, amount, temporary));
    }

    public <S> DiscardFromHand Reload(String sourceName, S state, ActionT2<S, ArrayList<AbstractCard>> onReload)
    {
        return (DiscardFromHand) Add(new DiscardFromHand(sourceName, 999, false)
        .SetOptions(true, true, true)
        .AddCallback(state, onReload));
    }

    public DiscardFromHand Reload(String sourceName, ActionT1<ArrayList<AbstractCard>> onReload)
    {
        return (DiscardFromHand) Add(new DiscardFromHand(sourceName, 999, false)
        .SetOptions(true, true, true)
        .AddCallback(onReload));
    }

    public ModifyPowers RemoveDebuffs(AbstractCreature target, ListSelection<AbstractPower> selection, int count)
    {
        return Add(new ModifyPowers(target, target, 0, false))
        .SetFilter(GameUtilities::IsDebuff)
        .SetSelection(selection, count)
        .IsDebuffInteraction(true);
    }

    public ModifyPowers ModifyDebuffs(AbstractCreature target, FuncT1<Integer, AbstractPower> calculateNewAmount, ListSelection<AbstractPower> selection, int count)
    {
        return Add(new ModifyPowers(target, target, calculateNewAmount))
        .SetFilter(GameUtilities::IsDebuff)
        .SetSelection(selection, count)
        .IsDebuffInteraction(true);
    }

    public ModifyPowers ReduceDebuffs(AbstractCreature target, int amount, ListSelection<AbstractPower> selection, int count)
    {
        return Add(new ModifyPowers(target, target, -amount, true))
        .SetFilter(GameUtilities::IsDebuff)
        .SetSelection(selection, count)
        .IsDebuffInteraction(true);
    }

    public RemovePower RemovePower(AbstractCreature source, AbstractPower power)
    {
        return Add(new RemovePower(power.owner, source, power));
    }

    public RemovePower RemovePower(AbstractCreature source, AbstractCreature target, AbstractPower power)
    {
        return Add(new RemovePower(target, source, power));
    }

    public RemovePower RemovePower(AbstractCreature source, AbstractCreature target, String powerID)
    {
        return Add(new RemovePower(target, source, powerID));
    }

    public ReplaceCard ReplaceCard(UUID uuid, AbstractCard replacement)
    {
        return Add(new ReplaceCard(uuid, replacement));
    }

    public ReshuffleDiscardPile ReshuffleDiscardPile(boolean onlyIfEmpty)
    {
        return Add(new ReshuffleDiscardPile(onlyIfEmpty));
    }

    public ReshuffleFromHand ReshuffleFromHand(String sourceName, int amount, boolean isRandom)
    {
        return Add(new ReshuffleFromHand(sourceName, amount, isRandom));
    }

    public MoveCard Reshuffle(AbstractCard card, CardGroup sourcePile)
    {
        return MoveCard(card, sourcePile, player.drawPile).SetDestination(CardSelection.Bottom(GameUtilities.GetRNG().random((int)(player.drawPile.size() * 0.8f))));
    }

    public WaitAction ShowCopy(AbstractCard card)
    {
        return GameActions.Bottom.Wait(GameEffects.List.ShowCopy(card, Settings.WIDTH * 0.75f, Settings.HEIGHT * 0.5f).duration * 0.35f);
    }

    public PlaySFX SFX(String key)
    {
        return SFX(key, 1, 1, 1);
    }

    public PlaySFX SFX(String key, float pitchMin, float pitchMax)
    {
        return Add(new PlaySFX(key, pitchMin, pitchMax, 1));
    }

    public PlaySFX SFX(String key, float pitchMin, float pitchMax, float volume)
    {
        return Add(new PlaySFX(key, pitchMin, pitchMax, volume));
    }

    public ScryWhichActuallyTriggersDiscard Scry(int amount)
    {
        return Add(new ScryWhichActuallyTriggersDiscard(amount));
    }

    public SelectCreature SelectCreature(SelectCreature.Targeting target, String source)
    {
        return Add(new SelectCreature(target, source));
    }

    public SelectCreature SelectCreature(AbstractCard card)
    {
        return Add(new SelectCreature(card));
    }

    public SelectCreature SelectCreature(AbstractCreature target)
    {
        return Add(new SelectCreature(target));
    }

    public SelectFromHand SelectFromHand(String sourceName, int amount, boolean isRandom)
    {
        return Add(new SelectFromHand(sourceName, amount, isRandom));
    }

    public SelectFromPile SelectFromPile(String sourceName, int amount, CardGroup... groups)
    {
        return Add(new SelectFromPile(sourceName, amount, groups));
    }

    public SelectFromHand Retain(String sourceName, int amount, boolean isRandom)
    {
        return (SelectFromHand) SelectFromHand(sourceName, 1, false)
        .SetOptions(true, true, true)
        .SetMessage(RetainCardsAction.TEXT[0])
        .SetFilter(GameUtilities::CanRetain)
        .AddCallback(cards ->
        {
            for (AbstractCard c : cards)
            {
                GameUtilities.Retain(c);
            }
        });
    }

    public SequentialAction Sequential(AbstractGameAction action, AbstractGameAction action2)
    {
        return Add(new SequentialAction(action, action2));
    }

    public ShakeScreenAction ShakeScreen(float actionDuration, ScreenShake.ShakeDur shakeDuration, ScreenShake.ShakeIntensity intensity)
    {
        return Add(new ShakeScreenAction(actionDuration, shakeDuration, intensity));
    }

    public SpendEnergy SpendEnergy(AbstractCard card)
    {
        return Add(new SpendEnergy(card.freeToPlay() ? 0 : card.costForTurn, false));
    }

    public SpendEnergy SpendEnergy(int amount, boolean canSpendLess)
    {
        return Add(new SpendEnergy(amount, canSpendLess));
    }

    public ApplyAffinityPower BoostAffinity(Affinity affinity)
    {
        return GainAffinity(affinity, 1, true);
    }

    public ApplyAffinityPower GainAffinity(Affinity affinity)
    {
        return GainAffinity(affinity, 1, false);
    }

    public ApplyAffinityPower GainAffinity(Affinity affinity, int amount)
    {
        return GainAffinity(affinity, amount, false);
    }

    public ApplyAffinityPower GainAffinity(Affinity affinity, int amount, boolean retain)
    {
        return Add(new ApplyAffinityPower(player, affinity, amount, retain));
    }

    public ApplyPower StackPower(AbstractPower power)
    {
        return StackPower(power.owner, power);
    }

    public ApplyPower StackPower(AbstractCreature source, AbstractPower power)
    {
        return Add(new ApplyPower(source, power.owner, power, power.amount));
    }

    public ApplyPowerAuto StackPower(TargetHelper target, PowerHelper power, int stacks)
    {
        return Add(new ApplyPowerAuto(target, power, stacks));
    }

    public ApplyPower StackAmplification(AbstractCreature source, AbstractCreature target, int amount)
    {
        return StackPower(source, new AmplificationPower(target, amount));
    }

    public ApplyPowerAuto StackAmplification(TargetHelper target, int amount)
    {
        return StackPower(target, PowerHelper.Amplification, amount);
    }

    public ApplyPower StackWithering(AbstractCreature source, AbstractCreature target, int amount)
    {
        return StackPower(source, new WitheringPower(target, amount));
    }

    public ApplyPowerAuto StackWithering(TargetHelper target, int amount)
    {
        return StackPower(target, PowerHelper.Withering, amount);
    }

    public SelectCreature SelectDoll(String source)
    {
        return SelectCreature(SelectCreature.Targeting.PlayerMinion, source);
    }

    public SelectCreature SacrificeDoll(String source)
    {
        return (SelectCreature) SelectCreature(SelectCreature.Targeting.PlayerMinion, source)
        .AddCallback(c ->
        {
            if (c != null)
            {
                CombatStats.Dolls.Sacrifice((UnnamedDoll) c);
            }
        });
    }

    public SelectCreature SacrificeDoll(AbstractMonster doll)
    {
        return (SelectCreature) SelectCreature(doll)
        .AddCallback(c ->
        {
            if (c instanceof UnnamedDoll)
            {
                CombatStats.Dolls.Sacrifice((UnnamedDoll) c);
            }
        });
    }


    public ActivateDoll ActivateDoll(int times)
    {
        return Add(new ActivateDoll(times));
    }

    public ActivateDoll ActivateDoll(AbstractCreature doll, int times)
    {
        return Add(new ActivateDoll((UnnamedDoll) doll, times));
    }

    public HealDoll HealDolls(int amount)
    {
        return Add(new HealDoll(amount));
    }

    public HealDoll HealDoll(AbstractCreature doll, int amount)
    {
        return Add(new HealDoll((UnnamedDoll)doll, amount));
    }

    public SummonDoll SummonDoll(int times)
    {
        return Add(new SummonDoll(times));
    }

    public ModifyDollHP ModifyDollMaxHP(AbstractCreature doll, int amount)
    {
        return Add(new ModifyDollHP((UnnamedDoll) doll, amount));
    }

    public ModifyDollHP ModifyDollsMaxHP(int amount)
    {
        return Add(new ModifyDollHP(amount));
    }

    public ApplyPower DealDamageAtEndOfTurn(AbstractCreature source, AbstractCreature target, int amount)
    {
        return DealDamageAtEndOfTurn(source, target, amount, AttackEffects.SLASH_VERTICAL);
    }

    public ApplyPower DealDamageAtEndOfTurn(AbstractCreature source, AbstractCreature target, int amount, AbstractGameAction.AttackEffect effect)
    {
        return StackPower(source, new DelayedDamagePower(target, amount, effect)).IgnoreArtifact(amount < 4);
    }

    public ApplyPower TakeDamageAtEndOfTurn(int amount, AbstractGameAction.AttackEffect effect)
    {
        return DealDamageAtEndOfTurn(player, player, amount, effect);
    }

    public ApplyPower TakeDamageAtEndOfTurn(int amount)
    {
        return DealDamageAtEndOfTurn(player, player, amount);
    }

    public DealDamage TakeDamage(int amount, AbstractGameAction.AttackEffect effect)
    {
        return TakeDamage(player, amount, effect);
    }

    public DealDamage TakeDamage(AbstractCreature target, int amount, AbstractGameAction.AttackEffect effect)
    {
        return DealDamage(null, target, amount, DamageInfo.DamageType.THORNS, effect);
    }

    public TalkAction Talk(AbstractCreature source, String text)
    {
        return Add(new TalkAction(source, text));
    }

    public TalkAction Talk(AbstractCreature source, String text, float duration, float bubbleDuration)
    {
        return Add(new TalkAction(source, text, duration, bubbleDuration));
    }

    public TriggerOrbPassiveAbility TriggerOrbPassive(int times)
    {
        return Add(new TriggerOrbPassiveAbility(times));
    }

    public TriggerOrbPassiveAbility TriggerOrbPassive(AbstractOrb orb, int times)
    {
        return Add(new TriggerOrbPassiveAbility(orb, times));
    }

    public SelectFromHand UpgradeFromHand(String sourceName, int amount, boolean isRandom)
    {
        return (SelectFromHand) SelectFromHand(sourceName, amount, isRandom)
        .SetOptions(true, true, true, false, true, false)
        .SetMessage(ArmamentsAction.TEXT[0])
        .SetFilter(AbstractCard::canUpgrade)
        .AddCallback(cards ->
        {
            for (AbstractCard c : cards)
            {
                if (c.canUpgrade())
                {
                    c.upgrade();
                    c.flash();
                }
            }
        });
    }

    public UpgradeFromPile UpgradeFromPile(CardGroup group, int amount, boolean permanent)
    {
        return Add(new UpgradeFromPile(group, amount).UpgradePermanently(permanent));
    }

    public PlayVFX VFX(AbstractGameEffect effect)
    {
        return Add(new PlayVFX(effect, 0));
    }

    public PlayVFX VFX(AbstractGameEffect effect, float wait)
    {
        return Add(new PlayVFX(effect, wait));
    }

    public PlayVFX VFX(AbstractGameEffect effect, float wait, boolean isPercentage)
    {
        return Add(new PlayVFX(effect, isPercentage ? effect.duration * wait : wait));
    }

    public WaitAction Wait(float duration)
    {
        return Add(new WaitAction(duration));
    }

    public WaitRealtimeAction WaitRealtime(float duration)
    {
        return Add(new WaitRealtimeAction(duration));
    }

    protected static class ExecuteLast implements OnPhaseChangedSubscriber
    {
        private final AbstractGameAction action;

        private ExecuteLast(AbstractGameAction action)
        {
            this.action = action;
        }

        public static void Add(AbstractGameAction action)
        {
            CombatStats.onPhaseChanged.Subscribe(new ExecuteLast(action));
        }

        @Override
        public void OnPhaseChanged(GameActionManager.Phase phase)
        {
            if (phase == GameActionManager.Phase.WAITING_ON_USER)
            {
                GameActions.Bottom.Add(action);
                CombatStats.onPhaseChanged.Unsubscribe(this);
            }
        }
    }
}