package pinacolada.utilities;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.defect.IncreaseMaxOrbAction;
import com.megacrit.cardcrawl.actions.unique.ArmamentsAction;
import com.megacrit.cardcrawl.actions.utility.ShakeScreenAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;
import com.megacrit.cardcrawl.stances.AbstractStance;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.BorderLongFlashEffect;
import com.megacrit.cardcrawl.vfx.cardManip.CardFlashVfx;
import eatyourbeets.actions.monsters.TalkAction;
import eatyourbeets.actions.special.PlayVFX;
import eatyourbeets.actions.utility.CallbackAction;
import eatyourbeets.actions.utility.SequentialAction;
import eatyourbeets.actions.utility.WaitRealtimeAction;
import eatyourbeets.interfaces.delegates.*;
import eatyourbeets.interfaces.subscribers.OnPhaseChangedSubscriber;
import eatyourbeets.utilities.CardSelection;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.ListSelection;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.actions.affinity.*;
import pinacolada.actions.autoTarget.ApplyPowerAuto;
import pinacolada.actions.basic.*;
import pinacolada.actions.cardManipulation.*;
import pinacolada.actions.damage.DealDamage;
import pinacolada.actions.damage.DealDamageToAll;
import pinacolada.actions.damage.DealDamageToRandomEnemy;
import pinacolada.actions.damage.LoseHP;
import pinacolada.actions.handSelection.CycleCards;
import pinacolada.actions.handSelection.DiscardFromHand;
import pinacolada.actions.handSelection.ExhaustFromHand;
import pinacolada.actions.handSelection.SelectFromHand;
import pinacolada.actions.orbs.ChannelOrb;
import pinacolada.actions.orbs.EvokeOrb;
import pinacolada.actions.orbs.InduceOrb;
import pinacolada.actions.orbs.TriggerOrbPassiveAbility;
import pinacolada.actions.pileSelection.*;
import pinacolada.actions.player.ChangeStance;
import pinacolada.actions.player.GainGold;
import pinacolada.actions.player.SpendEnergy;
import pinacolada.actions.powers.*;
import pinacolada.actions.special.*;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.pcl.tokens.AffinityToken;
import pinacolada.powers.PCLCombatStats;
import pinacolada.powers.PCLPowerHelper;
import pinacolada.powers.affinity.*;
import pinacolada.powers.common.EnergizedPower;
import pinacolada.powers.common.*;
import pinacolada.powers.replacement.*;
import pinacolada.resources.GR;
import pinacolada.ui.combat.PCLAffinityMeter;

import java.util.ArrayList;
import java.util.UUID;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.player;

@SuppressWarnings("UnusedReturnValue")
public final class PCLActions
{

    @Deprecated
    public static final PCLActions NextCombat = new PCLActions(GameActions.ActionOrder.NextCombat, GameActions.NextCombat);
    @Deprecated
    public static final PCLActions TurnStart = new PCLActions(GameActions.ActionOrder.TurnStart, GameActions.TurnStart);

    public static final PCLActions Instant = new PCLActions(GameActions.ActionOrder.Instant, GameActions.Instant);
    public static final PCLActions Top = new PCLActions(GameActions.ActionOrder.Top, GameActions.Top);
    public static final PCLActions Bottom = new PCLActions(GameActions.ActionOrder.Bottom, GameActions.Bottom);
    public static final PCLActions Delayed = new PCLActions(GameActions.ActionOrder.Delayed, GameActions.Delayed);
    public static final PCLActions Last = new PCLActions(GameActions.ActionOrder.Last, GameActions.Last);

    protected  final GameActions.ActionOrder actionOrder;
    protected final GameActions baseActions;

    protected PCLActions(GameActions.ActionOrder actionOrder, GameActions baseActions)
    {
        this.actionOrder = actionOrder;
        this.baseActions = baseActions;
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

    public <T extends AbstractGameAction> T Add(T action)
    {
        return baseActions.Add(action);
    }

    public ChangeAffinityCount AddAffinity(PCLAffinity affinity, int amount)
    {
        return Add(new ChangeAffinityCount(affinity, amount).ShowEffect(true));
    }

    public ApplyPower ApplyBlinded(AbstractCreature source, AbstractCreature target, int amount)
    {
        return StackPower(source, new BlindedPower(target, source, amount));
    }

    public ApplyPowerAuto ApplyBlinded(TargetHelper target, int amount)
    {
        return StackPower(target, PCLPowerHelper.Blinded, amount);
    }

    public ApplyPower ApplyBurning(AbstractCreature source, AbstractCreature target, int amount)
    {
        return StackPower(source, new BurningPower(target, source, amount));
    }

    public ApplyPowerAuto ApplyBurning(TargetHelper target, int amount)
    {
        return StackPower(target, PCLPowerHelper.Burning, amount);
    }

    public ApplyPower ApplyConstricted(AbstractCreature source, AbstractCreature target, int amount)
    {
        return StackPower(source, new PCLConstrictedPower(target, source, amount));
    }

    public ApplyPowerAuto ApplyConstricted(TargetHelper target, int amount)
    {
        return StackPower(target, PCLPowerHelper.Constricted, amount);
    }

    public ApplyPowerAuto ApplyElectrified(TargetHelper target, int amount)
    {
        return StackPower(target, PCLPowerHelper.Electrified, amount);
    }


    public ApplyPower ApplyElectrified(AbstractCreature source, AbstractCreature target, int amount)
    {
        return StackPower(source, new ElectrifiedPower(target, source, amount));
    }

    public ApplyPower ApplyFrail(AbstractCreature source, AbstractCreature target, int amount)
    {
        return StackPower(source, new PCLFrailPower(target, amount, source == null || PCLGameUtilities.IsMonster(source)));
    }

    public ApplyPowerAuto ApplyFrail(TargetHelper target, int amount)
    {
        return StackPower(target, PCLPowerHelper.Frail, amount);
    }

    public ApplyPower ApplyFreezing(AbstractCreature source, AbstractCreature target, int amount)
    {
        return StackPower(source, new FreezingPower(target, source, amount));
    }

    public ApplyPowerAuto ApplyFreezing(TargetHelper target, int amount)
    {
        return StackPower(target, PCLPowerHelper.Freezing, amount);
    }

    public ApplyPower ApplyPoison(AbstractCreature source, AbstractCreature target, int amount)
    {
        return StackPower(source, new PoisonPower(target, source, amount));
    }

    public ApplyPowerAuto ApplyPoison(TargetHelper target, int amount)
    {
        return StackPower(target, PCLPowerHelper.Poison, amount);
    }

    public ApplyPower ApplyLockOn(AbstractCreature source, AbstractCreature target, int amount)
    {
        return StackPower(source, new PCLLockOnPower(target, amount));
    }

    public ApplyPowerAuto ApplyLockOn(TargetHelper target, int amount)
    {
        return StackPower(target, PCLPowerHelper.LockOn, amount);
    }

    public ApplyPower ApplyPower(AbstractPower power)
    {
        return ApplyPower(power.owner, power.owner, power);
    }

    public ApplyPowerAuto ApplyPower(TargetHelper target, PCLPowerHelper power)
    {
        return Add(new ApplyPowerAuto(target, power, 1)).CanStack(false);
    }

    public ApplyPowerAuto ApplyPower(TargetHelper target, PCLPowerHelper power, int amount)
    {
        return Add(new ApplyPowerAuto(target, power, amount));
    }


    public ApplyPower ApplyPower(AbstractCreature source, AbstractPower power)
    {
        return ApplyPower(source, power.owner, power);
    }

    public ApplyPower ApplyPower(AbstractCreature source, AbstractCreature target, AbstractPower power)
    {
        return Add(new ApplyPower(source, target, power)).CanStack(false);
    }

    public ApplyPowerAuto ApplyShackles(TargetHelper target, int amount)
    {
        return StackPower(target, PCLPowerHelper.Shackles, amount);
    }

    public ApplyPower ApplyVulnerable(AbstractCreature source, AbstractCreature target, int amount)
    {
        return StackPower(source, new PCLVulnerablePower(target, amount, source == null || PCLGameUtilities.IsMonster(source)));
    }

    public ApplyPowerAuto ApplyVulnerable(TargetHelper target, int amount)
    {
        return StackPower(target, PCLPowerHelper.Vulnerable, amount);
    }

    public ApplyPower ApplyWeak(AbstractCreature source, AbstractCreature target, int amount)
    {
        return StackPower(source, new PCLWeakPower(target, amount, source == null || PCLGameUtilities.IsMonster(source)));
    }

    public ApplyPowerAuto ApplyWeak(TargetHelper target, int amount)
    {
        return StackPower(target, PCLPowerHelper.Weak, amount);
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
        return Callback(new WaitAction(0.05f), onCompletion);
    }

    public CallbackAction Callback(ActionT1<AbstractGameAction> onCompletion)
    {
        return Callback(new WaitAction(0.05f), onCompletion);
    }

    public <T> CallbackAction Callback(T state, ActionT2<T, AbstractGameAction> onCompletion)
    {
        return Callback(new WaitAction(0.05f), state, onCompletion);
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

    public ChannelOrb ChannelRandomOrbs(int amount)
    {
        return Add(new ChannelOrb(PCLGameUtilities::GetRandomOrb, amount));
    }

    public CreateGriefSeeds CreateGriefSeeds(int amount)
    {
        return Add(new CreateGriefSeeds(amount));
    }

    public CreateThrowingKnives CreateThrowingKnives(int amount)
    {
        return Add(new CreateThrowingKnives(amount));
    }

    public CreateThrowingKnives CreateThrowingKnives(int amount, CardGroup pile)
    {
        return Add(new CreateThrowingKnives(amount, pile));
    }

    public CycleCards Cycle(String sourceName, int amount)
    {
        return (CycleCards) Add(new CycleCards(sourceName, amount, false)
        .SetOptions(true, true, true));
    }

    public ArrayList<DealDamage> DealCardDamage(PCLCard card, AbstractCreature target, AbstractGameAction.AttackEffect effect)
    {
        ArrayList<DealDamage> actions = new ArrayList<>();
        for (int i = 0; i < card.hitCount; i++)
        {
            actions.add(Add(new DealDamage(target, new DamageInfo(player, card.damage, card.damageTypeForTurn), effect))
                    .SetPiercing(card.attackType.bypassThorns, card.attackType.bypassBlock));
        }

        return actions;
    }

    public DealDamage DealDamage(AbstractCreature source, AbstractCreature target, int amount, DamageInfo.DamageType damageType, AbstractGameAction.AttackEffect effect)
    {
        return Add(new DealDamage(target, new DamageInfo(source, amount, damageType), effect));
    }

    public ArrayList<DealDamageToAll> DealCardDamageToAll(PCLCard card, AbstractGameAction.AttackEffect effect)
    {
        ArrayList<DealDamageToAll> actions = new ArrayList<>();
        for (int i = 0; i < card.hitCount; i++)
        {
            actions.add(Add(new DealDamageToAll(player, card.multiDamage, card.damageTypeForTurn, effect, false))
                    .SetPiercing(card.attackType.bypassThorns, card.attackType.bypassBlock));
        }

        return actions;
    }

    public DealDamageToAll DealDamageToAll(int[] damageMatrix, DamageInfo.DamageType damageType, AbstractGameAction.AttackEffect effect)
    {
        return Add(new DealDamageToAll(player, damageMatrix, damageType, effect, false));
    }

    public ArrayList<DealDamageToRandomEnemy> DealCardDamageToRandomEnemy(PCLCard card, AbstractGameAction.AttackEffect effect)
    {
        ArrayList<DealDamageToRandomEnemy> actions = new ArrayList<>();
        for (int i = 0; i < card.hitCount; i++)
        {
            actions.add(Add(new DealDamageToRandomEnemy(card, effect))
                    .SetPiercing(card.attackType.bypassThorns, card.attackType.bypassBlock));
        }
        return actions;
    }

    public DealDamageToRandomEnemy DealDamageToRandomEnemy(int baseDamage, DamageInfo.DamageType damageType, AbstractGameAction.AttackEffect effect)
    {
        return Add(new DealDamageToRandomEnemy(new DamageInfo(player, baseDamage, damageType), effect));
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

    public SelectFromHand Exchange(String sourceName, int amount)
    {
        SelectFromHand select = (SelectFromHand) new SelectFromHand(sourceName, amount, false)
                .SetMessage(GR.PCL.Strings.HandSelection.MoveToDrawPile)
                .AddCallback(selected ->
                {
                    for (AbstractCard c : selected) {
                        PCLActions.Top.MoveCard(c, player.hand, player.drawPile).SetDestination(CardSelection.Top);
                    }
                });
        Add(new DrawCards(amount).AddCallback(() -> PCLActions.Top.Add(select)));
        return select;
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
        return VFX(new CardFlashVfx(card, Color.ORANGE.cpy()));
    }

    public PlayVFX SuperFlash(AbstractCard card)
    {
        return VFX(new CardFlashVfx(card, Color.ORANGE.cpy(), true));
    }

    public ApplyAffinityPower GainVelocity(int amount)
    {
        return GainVelocity(amount, false);
    }

    public ApplyAffinityPower GainVelocity(int amount, boolean maintain)
    {
        return StackAffinityPower(VelocityPower.AFFINITY_TYPE, amount, maintain);
    }

    public ApplyPower GainArtifact(int amount)
    {
        return StackPower(new ArtifactPower(player, amount));
    }

    public ApplyAffinityPower GainInvocation(int amount)
    {
        return GainInvocation(amount, false);
    }

    public ApplyAffinityPower GainInvocation(int amount, boolean maintain)
    {
        return StackAffinityPower(InvocationPower.AFFINITY_TYPE, amount, maintain);
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
        return StackPower(new BlurPower(player, amount));
    }

    public ApplyPower GainCounterAttack(int amount)
    {
        return StackPower(new CounterAttackPower(player, amount));
    }

    public ApplyAffinityPower GainDesecration(int amount)
    {
        return GainDesecration(amount, false);
    }

    public ApplyAffinityPower GainDesecration(int amount, boolean maintain)
    {
        return StackAffinityPower(DesecrationPower.AFFINITY_TYPE, amount, maintain);
    }

    public ApplyPower GainDexterity(int amount)
    {
        return StackPower(new DexterityPower(player, amount));
    }

    public ApplyPower GainResistance(int amount)
    {
        return StackPower(new ResistancePower(player, amount));
    }

    public ApplyPower GainResistance(int amount, boolean temporary)
    {
        return StackPower(temporary ? new TemporaryResistancePower(player, amount) : new ResistancePower(player, amount));
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

    public ApplyPower GainFocus(int amount, boolean temporary)
    {
        return StackPower(temporary ? new TemporaryFocusPower(player, amount) : new FocusPower(player, amount));
    }

    public ApplyAffinityPower GainMight(int amount)
    {
        return GainMight(amount, false);
    }

    public ApplyAffinityPower GainMight(int amount, boolean maintain)
    {
        return StackAffinityPower(MightPower.AFFINITY_TYPE, amount, maintain);
    }

    public GainGold GainGold(int amount)
    {
        return Add(new GainGold(amount, true));
    }

    public ApplyPower GainInspiration(int amount)
    {
        return StackPower(new InspirationPower(player, amount));
    }

    public ApplyAffinityPower GainWisdom(int amount)
    {
        return GainWisdom(amount, false);
    }

    public ApplyAffinityPower GainWisdom(int amount, boolean maintain)
    {
        return StackAffinityPower(WisdomPower.AFFINITY_TYPE, amount, maintain);
    }

    public ApplyPower GainMalleable(int amount)
    {
        return StackPower(new MalleablePower(player, amount));
    }

    public ApplyPower GainMetallicize(int amount)
    {
        return StackPower(new PCLMetallicizePower(player, amount));
    }

    public IncreaseMaxOrbAction GainOrbSlots(int slots)
    {
        return Add(new IncreaseMaxOrbAction(slots));
    }

    public ApplyPower GainPlatedArmor(int amount)
    {
        return StackPower(new PCLPlatedArmorPower(player, amount));
    }

    public ApplyAffinityPower GainRandomAffinityPower(int amount, boolean retain)
    {
        return StackAffinityPower(null, amount, retain);
    }

    public ApplyAffinityPower GainRandomAffinityPower(int amount, boolean retain, PCLAffinity... affinities)
    {
        return StackAffinityPower(PCLGameUtilities.GetRandomElement(affinities), amount, retain);
    }

    public ApplyPower GainStrength(int amount)
    {
        return StackPower(new StrengthPower(player, amount));
    }

    public ApplyPower GainSorcery(int amount)
    {
        return StackPower(new SorceryPower(player, amount));
    }

    public ApplyPower GainSupportDamage(int amount)
    {
        return StackPower(new SupportDamagePower(player, amount));
    }

    public ApplyAffinityPower GainTechnic(int amount)
    {
        return GainTechnic(amount, false);
    }

    public ApplyAffinityPower GainTechnic(int amount, boolean retain)
    {
        return StackAffinityPower(TechnicPower.AFFINITY_TYPE, amount, retain);
    }

    public ApplyPower GainTemporaryArtifact(int amount)
    {
        return StackPower(new TemporaryArtifactPower(player, amount));
    }

    public GainTemporaryHP GainTemporaryHP(int amount)
    {
        return Add(new GainTemporaryHP(player, player, amount));
    }

    public ApplyPower GainTemporaryThorns(int amount)
    {
        return StackPower(new TemporaryThornsPower(player, amount));
    }

    public ApplyPower GainThorns(int amount)
    {
        return StackPower(new ThornsPower(player, amount));
    }

    public ApplyPower GainVigor(int amount)
    {
        return StackPower(new VigorPower(player, amount));
    }

    public ApplyPower GainVitality(int amount)
    {
        return StackPower(new VitalityPower(player, amount));
    }

    public ApplyAffinityPower GainEndurance(int amount)
    {
        return GainEndurance(amount, false);
    }

    public ApplyAffinityPower GainEndurance(int amount, boolean maintain)
    {
        return StackAffinityPower(EndurancePower.AFFINITY_TYPE, amount, maintain);
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
        return Add(new ModifyAffinityScaling(card, PCLAffinity.General, amount, true));
    }

    public ModifyAffinityScaling IncreaseRandomScaling(AbstractCard card, int amount)
    {
        return Add(new ModifyAffinityScaling(card, null, amount, true));
    }

    public ModifyAffinityScaling IncreaseScaling(AbstractCard card, PCLAffinity affinity, int amount)
    {
        return Add(new ModifyAffinityScaling(card, affinity, amount, true));
    }

    public ModifyAffinityScaling IncreaseScaling(CardGroup group, int cards, PCLAffinity affinity, int amount)
    {
        return Add(new ModifyAffinityScaling(group, cards, affinity, amount, true));
    }

    public InduceOrb InduceOrb(AbstractOrb orb, boolean shouldTriggerEvokeEffect)
    {
        return Add(new InduceOrb(orb, shouldTriggerEvokeEffect));
    }

    public InduceOrb InduceOrbs(FuncT0<AbstractOrb> orbConstructor, int amount, boolean shouldTriggerEvokeEffect)
    {
        return Add(new InduceOrb(orbConstructor, amount, shouldTriggerEvokeEffect));
    }

    public LoseHP LoseHP(AbstractCreature source, AbstractCreature target, int amount, AbstractGameAction.AttackEffect effect)
    {
        return Add(new LoseHP(target, source, amount, effect));
    }

    public LoseHP LoseHP(int amount, AbstractGameAction.AttackEffect effect)
    {
        return Add(new LoseHP(player, player, amount, effect));
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

    public GenerateCard ObtainAffinityToken(PCLAffinity affinity, boolean upgraded)
    {
        return MakeCardInHand(AffinityToken.GetCard(affinity)).SetUpgrade(upgraded, false);
    }

    public ModifyAffinityLevel ModifyAffinityLevel(AbstractCard card, PCLAffinity affinity, int amount, boolean relative)
    {
        return Add(new ModifyAffinityLevel(card, affinity, amount, relative));
    }

    public ModifyAffinityLevel ModifyAffinityLevel(CardGroup group, int cards, PCLAffinity affinity, int amount, boolean relative)
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

    public ModifyTag ModifyTag(AbstractCard card, AbstractCard.CardTags tag, boolean value)
    {
        return Add(new ModifyTag(card, tag, value));
    }

    public ModifyTag ModifyTag(CardGroup group, int cards, AbstractCard.CardTags tag, boolean value)
    {
        return Add(new ModifyTag(group, cards, tag, value));
    }

    public MotivateAction Motivate()
    {
        return Add(new MotivateAction(1));
    }

    public MotivateAction Motivate(CardGroup group)
    {
        return Motivate().SetGroup(group);
    }

    public MotivateAction Motivate(AbstractCard card, int amount)
    {
        return Add(new MotivateAction(card, amount));
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

    public ArrayList<MotivateAction> Motivate(CardGroup group, int times)
    {
        ArrayList<MotivateAction> actions = new ArrayList<>();

        for (int i = 0; i < times; i++)
        {
            actions.add(Motivate(group));
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
        return Add(new PlayCard(card, target, false, actionOrder != GameActions.ActionOrder.Top)).SetSourcePile(sourcePile);
    }

    public PlayCard PlayCard(AbstractCard card, AbstractMonster target)
    {
        return Add(new PlayCard(card, target, false, actionOrder != GameActions.ActionOrder.Top));
    }

    public PlayCard PlayCopy(AbstractCard card, AbstractMonster target)
    {
        return Add(new PlayCard(card, target, true, actionOrder != GameActions.ActionOrder.Top))
        .SetCurrentPosition(card.current_x, card.current_y)
        .SpendEnergy(false)
        .SetPurge(true);
    }

    public PlayFromPile PlayFromPile(String sourceName, int amount, AbstractMonster target, CardGroup... groups)
    {
        return Add(new PlayFromPile(sourceName, target, amount, groups));
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

    public ReducePower ReducePower(AbstractCreature target, AbstractCreature source, String powerID, int amount)
    {
        return Add(new ReducePower(target, source, powerID, amount));
    }

    public ReducePower ReducePower(AbstractPower power, int amount)
    {
        return Add(new ReducePower(power.owner, power.owner, power, amount));
    }

    public IncreasePower IncreasePower(AbstractPower power, int amount)
    {
        return Add(new IncreasePower(power.owner, power.owner, power, amount));
    }

    public ReduceStrength ReduceStrength(AbstractCreature target, int amount, boolean temporary)
    {
        return Add(new ReduceStrength(player, target, amount, temporary));
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
        .SetIsReload(true)
        .SetOptions(true, true, true)
        .AddCallback(onReload));
    }

    public ModifyPowers RemoveCommonDebuffs(AbstractCreature target, ListSelection<AbstractPower> selection, int count)
    {
        return Add(new ModifyPowers(target, target, 0, false))
                .SetFilter(PCLGameUtilities::IsCommonDebuff)
                .SetSelection(selection, count);
    }

    public ModifyPowers RemoveDebuffs(AbstractCreature target, ListSelection<AbstractPower> selection, int count)
    {
        return Add(new ModifyPowers(target, target, 0, false))
        .SetFilter(PCLGameUtilities::IsDebuff)
        .SetSelection(selection, count);
    }

    public ModifyPowers ReduceCommonDebuffs(AbstractCreature target, int amount)
    {
        return Add(new ModifyPowers(target, target, -amount, true))
                .SetFilter(PCLGameUtilities::IsCommonDebuff);
    }

    public ModifyPowers ReduceDebuffs(AbstractCreature target, int amount)
    {
        return Add(new ModifyPowers(target, target, -amount, true))
        .SetFilter(PCLGameUtilities::IsDebuff);
    }

    public RemoveSpecificPowerAction RemovePower(AbstractCreature source, AbstractPower power)
    {
        return Add(new RemoveSpecificPowerAction(power.owner, source, power));
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

    public RerollAffinity RerollAffinity(PCLAffinityMeter.Target target)
    {
        return Add(new RerollAffinity(target));
    }

    public ReshuffleDiscardPile ReshuffleDiscardPile(boolean onlyIfEmpty)
    {
        return Add(new ReshuffleDiscardPile(onlyIfEmpty));
    }

    public ApplyAffinityPower RetainPower(PCLAffinity affinity)
    {
        return StackAffinityPower(affinity, 0, true);
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

    public ApplyAffinityPower StackAffinityPower(PCLAffinity affinity, int amount)
    {
        return Add(new ApplyAffinityPower(player, affinity, amount, false));
    }

    public ApplyAffinityPower StackAffinityPower(PCLAffinity affinity, int amount, boolean maintain)
    {
        return Add(new ApplyAffinityPower(player, affinity, amount, maintain));
    }

    public ApplyPower StackPower(AbstractPower power)
    {
        return StackPower(power.owner, power);
    }

    public ApplyPower StackPower(AbstractCreature source, AbstractPower power)
    {
        return Add(new ApplyPower(source, power.owner, power, power.amount));
    }

    public ApplyPowerAuto StackPower(TargetHelper target, PCLPowerHelper power, int stacks)
    {
        return Add(new ApplyPowerAuto(target, power, stacks));
    }

    public ApplyPower DealDamageAtEndOfTurn(AbstractCreature source, AbstractCreature target, int amount)
    {
        return StackPower(source, new DelayedDamagePower(target, amount));
    }

    public ApplyPower DealDamageAtEndOfTurn(AbstractCreature source, AbstractCreature target, int amount, AbstractGameAction.AttackEffect effect)
    {
        return StackPower(source, new DelayedDamagePower(target, amount, effect));
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

    public TriggerOrbPassiveAbility TriggerOrbPassive(int times, boolean random, boolean sequential)
    {
        return Add(new TriggerOrbPassiveAbility(times, random, sequential, null));
    }

    public TriggerOrbPassiveAbility TriggerOrbPassive(AbstractOrb orb, int times)
    {
        return Add(new TriggerOrbPassiveAbility(orb, times));
    }

    public TryChooseAffinity TryChooseAffinity(String sourceName)
    {
        return Add(new TryChooseAffinity(sourceName, 0));
    }

    public TryChooseAffinity TryChooseAffinity(String sourceName, PCLAffinity... affinities)
    {
        return Add(new TryChooseAffinity(sourceName, 0, affinities));
    }

    public TryChooseAffinity TryChooseAffinity(String sourceName, int value)
    {
        return Add(new TryChooseAffinity(sourceName, value));
    }

    public TryChooseAffinity TryChooseAffinity(String sourceName, int gain, PCLAffinity... affinities)
    {
        return Add(new TryChooseAffinity(sourceName, gain, affinities));
    }

    public TryChooseGainAffinity TryChooseGainAffinity(String sourceName, int gain)
    {
        return Add(new TryChooseGainAffinity(sourceName, gain));
    }

    public TryChooseGainAffinity TryChooseGainAffinity(String sourceName, int gain, PCLAffinity... affinities)
    {
        return Add(new TryChooseGainAffinity(sourceName, gain, affinities));
    }

    public TryChooseSpendAffinity TryChooseSpendAffinity(PCLCard sourceCard)
    {
        return (TryChooseSpendAffinity) Add(new TryChooseSpendAffinity(sourceCard.name, -1).SetSourceAffinities(sourceCard.affinities));
    }

    public TryChooseSpendAffinity TryChooseSpendAffinity(PCLCard sourceCard, PCLAffinity... affinities)
    {
        return (TryChooseSpendAffinity) Add(new TryChooseSpendAffinity(sourceCard.name, -1, affinities).SetSourceAffinities(sourceCard.affinities));
    }

    public TryChooseSpendAffinity TryChooseSpendAffinity(String sourceName, int cost)
    {
        return Add(new TryChooseSpendAffinity(sourceName, cost));
    }

    public TryChooseSpendAffinity TryChooseSpendAffinity(String sourceName, int cost, PCLAffinity... affinities)
    {
        return Add(new TryChooseSpendAffinity(sourceName, cost, affinities));
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
            PCLCombatStats.onPhaseChanged.Subscribe(new ExecuteLast(action));
        }

        @Override
        public void OnPhaseChanged(GameActionManager.Phase phase)
        {
            if (phase == GameActionManager.Phase.WAITING_ON_USER)
            {
                PCLActions.Bottom.Add(action);
                PCLCombatStats.onPhaseChanged.Unsubscribe(this);
            }
        }
    }
}