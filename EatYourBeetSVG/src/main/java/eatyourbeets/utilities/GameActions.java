package eatyourbeets.utilities;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.actions.defect.IncreaseMaxOrbAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.cardManip.CardFlashVfx;
import eatyourbeets.actions.animator.CreateThrowingKnives;
import eatyourbeets.actions.basic.*;
import eatyourbeets.actions.cardManipulation.*;
import eatyourbeets.actions.damage.DealDamage;
import eatyourbeets.actions.damage.DealDamageToAll;
import eatyourbeets.actions.damage.DealDamageToRandomEnemy;
import eatyourbeets.actions.handSelection.CycleCards;
import eatyourbeets.actions.handSelection.DiscardFromHand;
import eatyourbeets.actions.handSelection.ExhaustFromHand;
import eatyourbeets.actions.handSelection.SelectFromHand;
import eatyourbeets.actions.monsters.TalkAction;
import eatyourbeets.actions.pileSelection.DiscardFromPile;
import eatyourbeets.actions.pileSelection.ExhaustFromPile;
import eatyourbeets.actions.pileSelection.FetchFromPile;
import eatyourbeets.actions.pileSelection.SelectFromPile;
import eatyourbeets.actions.powers.ApplyPower;
import eatyourbeets.actions.powers.ReduceStrength;
import eatyourbeets.actions.special.GainGold;
import eatyourbeets.actions.special.ReplaceCard;
import eatyourbeets.actions.special.ReshuffleDiscardPile;
import eatyourbeets.actions.special.SpendEnergy;
import eatyourbeets.actions.utility.CallbackAction;
import eatyourbeets.actions.utility.SequentialAction;
import eatyourbeets.actions.utility.WaitRealtimeAction;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.interfaces.delegates.ActionT1;
import eatyourbeets.interfaces.delegates.ActionT2;
import eatyourbeets.interfaces.delegates.FuncT1;
import eatyourbeets.interfaces.subscribers.OnPhaseChangedSubscriber;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.powers.animator.BurningPower;
import eatyourbeets.powers.animator.EarthenThornsPower;
import eatyourbeets.powers.common.AgilityPower;
import eatyourbeets.powers.common.ForcePower;
import eatyourbeets.powers.common.IntellectPower;
import eatyourbeets.powers.common.TemporaryArtifactPower;

import java.util.ArrayList;
import java.util.UUID;

@SuppressWarnings("UnusedReturnValue")
public final class GameActions
{
    @Deprecated
    public static final GameActions NextCombat = new GameActions(ActionOrder.NextCombat);

    public static final GameActions Top = new GameActions(ActionOrder.Top);
    public static final GameActions Bottom = new GameActions(ActionOrder.Bottom);
    public static final GameActions TurnStart = new GameActions(ActionOrder.TurnStart);
    public static final GameActions Instant = new GameActions(ActionOrder.Instant);
    public static final GameActions Last = new GameActions(ActionOrder.Last);

    protected final ActionOrder actionOrder;

    protected GameActions(ActionOrder actionOrder)
    {
        this.actionOrder = actionOrder;
    }

    public <T extends AbstractGameAction> T Add(T action)
    {
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

            case Last:
            {
                DelayedAction.Add(action);
                break;
            }
        }

        return action;
    }

    public ApplyPower ApplyBurning(AbstractCreature source, AbstractCreature target, int amount)
    {
        return StackPower(source, new BurningPower(source, target, amount));
    }

    public ApplyPower ApplyConstricted(AbstractCreature source, AbstractCreature target, int amount)
    {
        return StackPower(source, new ConstrictedPower(target, source, amount));
    }

    public ApplyPower ApplyFrail(AbstractCreature source, AbstractCreature target, int amount)
    {
        return StackPower(source, new FrailPower(target, amount, !source.isPlayer));
    }

    public ApplyPower ApplyPoison(AbstractCreature source, AbstractCreature target, int amount)
    {
        return StackPower(source, new PoisonPower(target, source, amount));
    }

    public ApplyPower ApplyPower(AbstractCreature source, AbstractCreature target, AbstractPower power)
    {
        return Add(new ApplyPower(source, target, power));
    }

    public ApplyPower ApplyPower(AbstractCreature source, AbstractCreature target, AbstractPower power, int stacks)
    {
        return Add(new ApplyPower(source, target, power, stacks));
    }

    public ApplyPower ApplyPowerSilently(AbstractCreature source, AbstractCreature target, AbstractPower power, int stacks)
    {
        return Add(new ApplyPower(source, target, power, stacks)).IgnoreArtifact(true).ShowEffect(false, true);
    }

    public ApplyPower ApplyVulnerable(AbstractCreature source, AbstractCreature target, int amount)
    {
        return StackPower(source, new VulnerablePower(target, amount, !source.isPlayer));
    }

    public ApplyPower ApplyWeak(AbstractCreature source, AbstractCreature target, int amount)
    {
        return StackPower(source, new WeakPower(target, amount, !source.isPlayer));
    }

    public CallbackAction Callback(AbstractGameAction action, Object state, ActionT2<Object, AbstractGameAction> onCompletion)
    {
        return Add(new CallbackAction(action, state, onCompletion));
    }

    public CallbackAction Callback(AbstractGameAction action, ActionT1<AbstractGameAction> onCompletion)
    {
        return Add(new CallbackAction(action, onCompletion));
    }

    public CallbackAction Callback(AbstractGameAction action)
    {
        return Add(new CallbackAction(action));
    }

    public CallbackAction Callback(ActionT1<AbstractGameAction> onCompletion)
    {
        return Callback(new WaitAction(0.05f), onCompletion);
    }

    public CallbackAction Callback(Object state, ActionT2<Object, AbstractGameAction> onCompletion)
    {
        return Callback(new WaitAction(0.05f), state, onCompletion);
    }

    public ChannelAction ChannelOrb(AbstractOrb orb, boolean autoEvoke)
    {
        return Add(new ChannelAction(orb, autoEvoke));
    }

    public ChannelAction ChannelRandomOrb(boolean autoEvoke)
    {
        return Add(new ChannelAction(GameUtilities.GetRandomOrb(), autoEvoke));
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
        return Add(new DealDamage(target, new DamageInfo(AbstractDungeon.player, card.damage, card.damageTypeForTurn), effect))
        .SetPiercing(card.attackType.bypassThorns, card.attackType.bypassBlock);
    }

    public DealDamageToAll DealDamageToAll(EYBCard card, AbstractGameAction.AttackEffect effect)
    {
        return Add(new DealDamageToAll(AbstractDungeon.player, card.multiDamage, card.damageTypeForTurn, effect))
        .SetPiercing(card.attackType.bypassThorns, card.attackType.bypassBlock);
    }

    public DealDamageToAll DealDamageToAll(int[] damageMatrix, DamageInfo.DamageType damageType, AbstractGameAction.AttackEffect effect)
    {
        return Add(new DealDamageToAll(AbstractDungeon.player, damageMatrix, damageType, effect));
    }

    public DealDamageToRandomEnemy DealDamageToRandomEnemy(int baseDamage, DamageInfo.DamageType damageType, AbstractGameAction.AttackEffect effect)
    {
        return Add(new DealDamageToRandomEnemy(new DamageInfo(AbstractDungeon.player, baseDamage, damageType), effect));
    }

    public DealDamageToRandomEnemy DealDamageToRandomEnemy(EYBCard card, AbstractGameAction.AttackEffect effect)
    {
        return Add(new DealDamageToRandomEnemy(card, effect))
        .SetPiercing(card.attackType.bypassThorns, card.attackType.bypassBlock);
    }

    public MoveCard Discard(AbstractCard card, CardGroup group)
    {
        return MoveCard(card, group, AbstractDungeon.player.discardPile);
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

        return MoveCard(card, AbstractDungeon.player.drawPile, AbstractDungeon.player.hand)
        .SetCardPosition(cardX, cardY)
        .ShowEffect(true, false);
    }

    public MoveCard Exhaust(AbstractCard card)
    {
        return MoveCard(card, AbstractDungeon.player.exhaustPile);
    }

    public MoveCard Exhaust(AbstractCard card, CardGroup group)
    {
        return MoveCard(card, group, AbstractDungeon.player.exhaustPile);
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

    public VFXAction Flash(AbstractCard card)
    {
        return Add(new VFXAction(new CardFlashVfx(card, Color.ORANGE.cpy())));
    }

    public ApplyPower GainAgility(int amount)
    {
        return GainAgility(amount, false);
    }

    public ApplyPower GainAgility(int amount, boolean preserveOnce)
    {
        if (preserveOnce)
        {
            AgilityPower.PreserveOnce();
        }

        return StackPower(new AgilityPower(AbstractDungeon.player, amount));
    }

    public ApplyPower GainArtifact(int amount)
    {
        return StackPower(new ArtifactPower(AbstractDungeon.player, amount));
    }

    public GainBlock GainBlock(AbstractCreature target, int amount)
    {
        return Add(new GainBlock(target, target, amount));
    }

    public GainBlock GainBlock(int amount)
    {
        return Add(new GainBlock(AbstractDungeon.player, AbstractDungeon.player, amount));
    }

    public ApplyPower GainBlur(int amount)
    {
        return StackPower(new BlurPower(AbstractDungeon.player, amount));
    }

    public ApplyPower GainDexterity(int amount)
    {
        return StackPower(new DexterityPower(AbstractDungeon.player, amount));
    }

    public GainEnergyAction GainEnergy(int amount)
    {
        return Add(new GainEnergyAction(amount));
    }

    public ApplyPower GainFocus(int amount)
    {
        return StackPower(new FocusPower(AbstractDungeon.player, amount));
    }

    public ApplyPower GainForce(int amount)
    {
        return GainForce(amount, false);
    }

    public ApplyPower GainForce(int amount, boolean preserveOnce)
    {
        if (preserveOnce)
        {
            ForcePower.PreserveOnce();
        }

        return StackPower(new ForcePower(AbstractDungeon.player, amount));
    }

    public GainGold GainGold(int amount)
    {
        return Add(new GainGold(amount, true));
    }

    public ApplyPower GainIntellect(int amount)
    {
        return GainIntellect(amount, false);
    }

    public ApplyPower GainIntellect(int amount, boolean preserveOnce)
    {
        if (preserveOnce)
        {
            IntellectPower.PreserveOnce();
        }

        return StackPower(new IntellectPower(AbstractDungeon.player, amount));
    }

    public ApplyPower GainMetallicize(int amount)
    {
        return StackPower(new MetallicizePower(AbstractDungeon.player, amount));
    }

    public IncreaseMaxOrbAction GainOrbSlots(int slots)
    {
        return Add(new IncreaseMaxOrbAction(slots));
    }

    public ApplyPower GainPlatedArmor(int amount)
    {
        return StackPower(new PlatedArmorPower(AbstractDungeon.player, amount));
    }

    public ApplyPower GainRandomStat(int amount)
    {
        int roll = AbstractDungeon.cardRandomRng.random(2);
        switch (roll)
        {
            case 0:
            {
                return GainIntellect(amount);
            }
            case 1:
            {
                return GainAgility(amount);
            }
            case 2:
            default:
            {
                return GainForce(amount);
            }
        }
    }

    public ApplyPower GainStrength(int amount)
    {
        return StackPower(new StrengthPower(AbstractDungeon.player, amount));
    }

    public ApplyPower GainTemporaryArtifact(int amount)
    {
        return StackPower(new TemporaryArtifactPower(AbstractDungeon.player, amount));
    }

    public GainTemporaryHP GainTemporaryHP(int amount)
    {
        return Add(new GainTemporaryHP(AbstractDungeon.player, AbstractDungeon.player, amount));
    }

    public ApplyPower GainTemporaryThorns(int amount)
    {
        return StackPower(new EarthenThornsPower(AbstractDungeon.player, amount));
    }

    public ApplyPower GainThorns(int amount)
    {
        return StackPower(new ThornsPower(AbstractDungeon.player, amount));
    }

    public HealFaster Heal(AbstractCreature source, AbstractCreature target, int amount)
    {
        return Add(new HealFaster(target, source, amount));
    }

    public HealFaster Heal(int amount)
    {
        return Add(new HealFaster(AbstractDungeon.player, AbstractDungeon.player, amount));
    }

    public LoseHPAction LoseHP(int amount, AbstractGameAction.AttackEffect effect)
    {
        return Add(new LoseHPAction(AbstractDungeon.player, AbstractDungeon.player, amount, effect));
    }

    public MakeTempCard MakeCard(AbstractCard card, CardGroup group)
    {
        return Add(new MakeTempCard(card, group));
    }

    public MakeTempCard MakeCardInDiscardPile(AbstractCard card)
    {
        return MakeCard(card, AbstractDungeon.player.discardPile);
    }

    public MakeTempCard MakeCardInDrawPile(AbstractCard card)
    {
        return MakeCard(card, AbstractDungeon.player.drawPile);
    }

    public MakeTempCard MakeCardInHand(AbstractCard card)
    {
        return MakeCard(card, AbstractDungeon.player.hand);
    }

    public ModifyAllCombatInstances ModifyAllCombatInstances(UUID uuid, Object state, ActionT2<Object, AbstractCard> onCompletion)
    {
        return Add(new ModifyAllCombatInstances(uuid, state, onCompletion));
    }

    public ModifyAllCombatInstances ModifyAllCombatInstances(UUID uuid, ActionT1<AbstractCard> onCompletion)
    {
        return Add(new ModifyAllCombatInstances(uuid, onCompletion));
    }

    public ModifyAllCombatInstances ModifyAllCombatInstances(UUID uuid)
    {
        return Add(new ModifyAllCombatInstances(uuid));
    }

    public MotivateAction Motivate()
    {
        return Add(new MotivateAction(1));
    }

    public ArrayList<MotivateAction> Motivate(int times)
    {
        ArrayList<MotivateAction> actions = new ArrayList<>();

        for (int i = 0; i < times; i++)
        {
            actions.add(Motivate());
        }

        return actions;
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
        return Add(new PlayCard(card, target, false)).SetSourcePile(sourcePile);
    }

    public PlayCard PlayCard(AbstractCard card, AbstractMonster target)
    {
        return Add(new PlayCard(card, target, false));
    }

    public PlayCard PlayCopy(AbstractCard card, AbstractMonster target)
    {
        return Add(new PlayCard(card, target, true))
        .SetCurrentPosition(card.current_x, card.current_y)
        .SetPurge(true);
    }

    public PurgeAnywhere Purge(UUID uuid)
    {
        return Add(new PurgeAnywhere(uuid));
    }

    public PurgeAnywhere Purge(AbstractCard card)
    {
        return Add(new PurgeAnywhere(card));
    }

    public ReducePowerAction ReducePower(AbstractCreature source, String powerID, int amount)
    {
        return Add(new ReducePowerAction(source, source, powerID, amount));
    }

    public ReducePowerAction ReducePower(AbstractPower power, int amount)
    {
        return Add(new ReducePowerAction(power.owner, power.owner, power, amount));
    }

    public ReduceStrength ReduceStrength(AbstractCreature target, int amount, boolean temporary)
    {
        return Add(new ReduceStrength(AbstractDungeon.player, target, amount, temporary));
    }

    public DiscardFromHand Reload(String sourceName, Object state, ActionT2<Object, ArrayList<AbstractCard>> onReload)
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

    public RemoveSpecificPowerAction RemovePower(AbstractCreature source, AbstractCreature target, AbstractPower power)
    {
        return Add(new RemoveSpecificPowerAction(target, source, power));
    }

    public RemoveSpecificPowerAction RemovePower(AbstractCreature source, AbstractCreature target, String powerID)
    {
        return Add(new RemoveSpecificPowerAction(target, source, powerID));
    }

    public ReplaceCard ReplaceCard(UUID uuid, AbstractCard replacement)
    {
        return Add(new ReplaceCard(uuid, replacement));
    }

    public ReshuffleDiscardPile ReshuffleDiscardPile(boolean onlyIfEmpty)
    {
        return Add(new ReshuffleDiscardPile(onlyIfEmpty));
    }

    public ScryWhichActuallyTriggersDiscard Scry(int amount)
    {
        return Add(new ScryWhichActuallyTriggersDiscard(amount));
    }

    public SFXAction SFX(String key)
    {
        return Add(new SFXAction(key));
    }

    public SFXAction SFX(String key, float pitchVar)
    {
        return Add(new SFXAction(key, pitchVar));
    }

    public SelectFromHand SelectFromHand(String sourceName, int amount, boolean isRandom)
    {
        return Add(new SelectFromHand(sourceName, amount, isRandom));
    }

    public SelectFromPile SelectFromPile(String sourceName, int amount, CardGroup... groups)
    {
        return Add(new SelectFromPile(sourceName, amount, groups));
    }

    public SequentialAction Sequential(AbstractGameAction action, AbstractGameAction action2)
    {
        return Add(new SequentialAction(action, action2));
    }

    public SpendEnergy SpendEnergy(int amount, boolean canSpendLess)
    {
        return Add(new SpendEnergy(amount, canSpendLess));
    }

    public ApplyPower StackPower(AbstractCreature source, AbstractPower power)
    {
        return Add(new ApplyPower(source, power.owner, power, power.amount));
    }

    public ApplyPower StackPower(AbstractPower power)
    {
        return StackPower(power.owner, power);
    }

    public TalkAction Talk(AbstractCreature source, String text)
    {
        return Add(new TalkAction(source, text));
    }

    public TalkAction Talk(AbstractCreature source, String text, float duration, float bubbleDuration)
    {
        return Add(new TalkAction(source, text, duration, bubbleDuration));
    }

    public VFXAction VFX(AbstractGameEffect effect)
    {
        return Add(new VFXAction(effect));
    }

    public VFXAction VFX(AbstractGameEffect effect, float duration)
    {
        return Add(new VFXAction(effect, duration));
    }

    public WaitAction Wait(float duration)
    {
        return Add(new WaitAction(duration));
    }

    public WaitRealtimeAction WaitRealtime(float duration)
    {
        return Add(new WaitRealtimeAction(duration));
    }

    public enum ActionOrder
    {
        Top,
        Bottom,
        TurnStart,
        NextCombat,
        Instant,
        Last
    }

    protected static class DelayedAction implements OnPhaseChangedSubscriber
    {
        private final AbstractGameAction action;

        private DelayedAction(AbstractGameAction action)
        {
            this.action = action;
        }

        public static void Add(AbstractGameAction action)
        {
            PlayerStatistics.onPhaseChanged.Subscribe(new DelayedAction(action));
        }

        @Override
        public void OnPhaseChanged(GameActionManager.Phase phase)
        {
            if (phase == GameActionManager.Phase.WAITING_ON_USER)
            {
                GameActions.Bottom.Add(action);

                PlayerStatistics.onPhaseChanged.Unsubscribe(this);
            }
        }
    }
}