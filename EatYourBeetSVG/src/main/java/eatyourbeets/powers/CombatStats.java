package eatyourbeets.powers;

import basemod.DevConsole;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.patches.core.AbstractCreature.TempHPField;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.utility.UnlimboAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.Calipers;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.stances.AbstractStance;
import eatyourbeets.actions.special.HasteAction;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.cards.base.EYBCardAffinities;
import eatyourbeets.characters.EYBPlayerCharacter;
import eatyourbeets.interfaces.listeners.OnCardResetListener;
import eatyourbeets.interfaces.subscribers.*;
import eatyourbeets.monsters.FakeMonster;
import eatyourbeets.orbs.EYBOrb;
import eatyourbeets.powers.affinity.AbstractAffinityPower;
import eatyourbeets.powers.animator.GeassPower;
import eatyourbeets.powers.common.InvocationPower;
import eatyourbeets.powers.common.VitalityPower;
import eatyourbeets.relics.EYBRelic;
import eatyourbeets.relics.animator.unnamedReign.Ynitaph;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.misc.AnimatorLoadoutData;
import eatyourbeets.ui.animator.combat.EYBCardAffinitySystem;
import eatyourbeets.ui.animator.combat.UnnamedDollManager;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;
import eatyourbeets.utilities.Mathf;
import org.apache.commons.lang3.mutable.MutableInt;
import patches.CardGlowBorderPatches;

import java.util.*;

public class CombatStats extends EYBPower implements InvisiblePower
{
    public static final String POWER_ID = GR.Common.CreateID(CombatStats.class.getSimpleName());

    public static final CombatStats Instance = new CombatStats();
    public static final EYBCardAffinitySystem Affinities = new EYBCardAffinitySystem();
    public static final UnnamedDollManager Dolls = new UnnamedDollManager();
    public static final CardGroup PurgedCards = new CardGroup(GR.Enums.CardGroupType.PURGED_CARDS);
    public static float EnemyVulnerableModifier;
    public static float EnemyWeakModifier;
    public static int BlockRetained;
    public static int MaxHPSinceLastTurn;
    public static boolean LoadingPlayerSave;
    public static boolean IsPlayerTurn;
    public static AbstractRoom Room;
    public static UUID BattleID;

    static final ArrayList<GameEvent<?>> events = new ArrayList<>();
    //
    public static final GameEvent<OnAffinitySealedSubscriber> onAffinitySealed = RegisterEvent(new GameEvent<>());
    public static final GameEvent<OnAffinityThresholdReachedSubscriber> onAffinityThresholdReached = RegisterEvent(new GameEvent<>());
    public static final GameEvent<OnAfterCardDiscardedSubscriber> onAfterCardDiscarded = RegisterEvent(new GameEvent<>());
    public static final GameEvent<OnAfterCardDrawnSubscriber> onAfterCardDrawn = RegisterEvent(new GameEvent<>());
    public static final GameEvent<OnAfterCardExhaustedSubscriber> onAfterCardExhausted = RegisterEvent(new GameEvent<>());
    public static final GameEvent<OnAfterCardPlayedSubscriber> onAfterCardPlayed = RegisterEvent(new GameEvent<>());
    public static final GameEvent<OnAfterDeathSubscriber> onAfterDeath = RegisterEvent(new GameEvent<>());
    public static final GameEvent<OnApplyPowerSubscriber> onApplyPower = RegisterEvent(new GameEvent<>());
    public static final GameEvent<OnAttackSubscriber> onAttack = RegisterEvent(new GameEvent<>());
    public static final GameEvent<OnBeforeLoseBlockSubscriber> onBeforeLoseBlock = RegisterEvent(new GameEvent<>());
    public static final GameEvent<OnBlockBrokenSubscriber> onBlockBroken = RegisterEvent(new GameEvent<>());
    public static final GameEvent<OnBlockGainedSubscriber> onBlockGained = RegisterEvent(new GameEvent<>());
    public static final GameEvent<OnCardCreatedSubscriber> onCardCreated = RegisterEvent(new GameEvent<>());
    public static final GameEvent<OnCardResetSubscriber> onCardReset = RegisterEvent(new GameEvent<>());
    public static final GameEvent<OnCardReshuffledSubscriber> onCardReshuffled = RegisterEvent(new GameEvent<>());
    public static final GameEvent<OnChannelOrbSubscriber> onChannelOrb = RegisterEvent(new GameEvent<>());
    public static final GameEvent<OnClickablePowerUsedSubscriber> onClickablePowerUsed = RegisterEvent(new GameEvent<>());
    public static final GameEvent<OnEndOfTurnFirstSubscriber> onEndOfTurnFirst = RegisterEvent(new GameEvent<>());
    public static final GameEvent<OnEndOfTurnLastSubscriber> onEndOfTurnLast = RegisterEvent(new GameEvent<>());
    public static final GameEvent<OnEnemyDyingSubscriber> onEnemyDying = RegisterEvent(new GameEvent<>());
    public static final GameEvent<OnEnergyRechargeSubscriber> onEnergyRecharge = RegisterEvent(new GameEvent<>());
    public static final GameEvent<OnEvokeOrbSubscriber> onEvokeOrb = RegisterEvent(new GameEvent<>());
    public static final GameEvent<OnHealthBarUpdatedSubscriber> onHealthBarUpdated = RegisterEvent(new GameEvent<>());
    public static final GameEvent<OnLoseHPSubscriber> onLoseHP = RegisterEvent(new GameEvent<>());
    public static final GameEvent<OnLosingHPSubscriber> onLosingHP = RegisterEvent(new GameEvent<>());
    public static final GameEvent<OnModifyDamageFirstSubscriber> onModifyDamageFirst = RegisterEvent(new GameEvent<>());
    public static final GameEvent<OnModifyDamageLastSubscriber> onModifyDamageLast = RegisterEvent(new GameEvent<>());
    public static final GameEvent<OnModifyDebuffSubscriber> onModifyDebuff = RegisterEvent(new GameEvent<>());
    public static final GameEvent<OnOrbPassiveEffectSubscriber> onOrbPassiveEffect = RegisterEvent(new GameEvent<>());
    public static final GameEvent<OnPhaseChangedSubscriber> onPhaseChanged = RegisterEvent(new GameEvent<>());
    public static final GameEvent<OnPlayCardSubscriber> onPlayCard = RegisterEvent(new GameEvent<>());
    public static final GameEvent<OnPlayerMinionActionSubscriber> onPlayerMinionAction = RegisterEvent(new GameEvent<>());
    public static final GameEvent<OnShuffleSubscriber> onShuffle = RegisterEvent(new GameEvent<>());
    public static final GameEvent<OnStanceChangedSubscriber> onStanceChanged = RegisterEvent(new GameEvent<>());
    public static final GameEvent<OnStartOfTurnPostDrawSubscriber> onStartOfTurnPostDraw = RegisterEvent(new GameEvent<>());
    public static final GameEvent<OnStartOfTurnSubscriber> onStartOfTurn = RegisterEvent(new GameEvent<>());
    public static final GameEvent<OnTryUsingCardSubscriber> onTryUsingCard = RegisterEvent(new GameEvent<>());
    //
    public static final GameEvent<OnStatsClearedSubscriber> onStatsCleared = new GameEvent<>();
    public static final GameEvent<OnBattleStartSubscriber> onBattleStart = new GameEvent<>();
    public static final GameEvent<OnBattleEndSubscriber> onBattleEnd = new GameEvent<>();
    //
    private static final Map<String, Object> combatData = new HashMap<>();
    private static final Map<String, Object> turnData = new HashMap<>();
    private static final ArrayList<AbstractGameAction> cachedActions = new ArrayList<>();
    private static final ArrayList<AbstractOrb> orbsEvokedThisCombat = new ArrayList<>();
    private static final ArrayList<AbstractOrb> orbsEvokedThisTurn = new ArrayList<>();
    private static final Map<Integer, ArrayList<AbstractCard>> cardsPlayedThisCombat = new HashMap<>();
    private static final ArrayList<AbstractCard> cardsExhaustedThisCombat = new ArrayList<>();
    private static final ArrayList<AbstractCard> cardsExhaustedThisTurn = new ArrayList<>();
    private static final ArrayList<AbstractCard> synergiesThisCombat = new ArrayList<>();
    private static final ArrayList<AbstractCard> synergiesThisTurn = new ArrayList<>();
    private static final ArrayList<AbstractCard> resetAfterPlay = new ArrayList<>();
    private static final ArrayList<AbstractMonster> exactKills = new ArrayList<>();
    private static final ArrayList<UUID> unplayableCards = new ArrayList<>();
    private static final String LOADOUT_BUFF_KEY = GR.Animator.CreateID("LoadoutBuff");
    private static GameActionManager.Phase currentPhase;
    private static boolean canActivateStarter;
    private static int cardsDrawnThisTurn = 0;
    private static int turnCount = 0;

    //@Formatter: Off
    public static boolean CanActivateLimited(String id) { return !HasActivatedLimited(id); }
    public static boolean HasActivatedLimited(String id) { return combatData.containsKey(id); }
    public static boolean TryActivateLimited(String id) { return combatData.put(id, 1) == null; }
    public static boolean CanActivateSemiLimited(String id) { return !HasActivatedSemiLimited(id); }
    public static boolean HasActivatedSemiLimited(String id) { return turnData.containsKey(id); }
    public static boolean TryActivateSemiLimited(String id) { return turnData.put(id, 1) == null; }
    public static boolean CanActivatedStarter() { return canActivateStarter; } @SuppressWarnings("ConstantConditions")
    public static boolean TryActivateStarter() { return canActivateStarter && !(canActivateStarter = false); }
    //
    public static boolean CanActivateLimited(String id, int cap) { return !HasActivatedLimited(id, cap); }
    public static boolean HasActivatedLimited(String id, int cap) { return combatData.containsKey(id) && (int)combatData.get(id) >= cap; }
    public static boolean TryActivateLimited(String id, int cap) { return JUtils.IncrementMapElement(combatData, id) <= cap; }
    public static boolean CanActivateSemiLimited(String id, int cap) { return !HasActivatedSemiLimited(id, cap); }
    public static boolean HasActivatedSemiLimited(String id, int cap) { return turnData.containsKey(id) && (int)turnData.get(id) >= cap; }
    public static boolean TryActivateSemiLimited(String id, int cap) { return JUtils.IncrementMapElement(turnData, id) <= cap; }
    //@Formatter: On

    protected static <T> GameEvent<T> RegisterEvent(GameEvent<T> event)
    {
        events.add(event);
        return event;
    }

    protected CombatStats()
    {
        super(null, POWER_ID);

        this.priority = -3000; //it was Integer.MIN_VALUE but it actually breaks the comparator, nice
    }

    public static AbstractPlayer RefreshPlayer()
    {
        EYBCard.rng = EYBPower.rng = EYBRelic.rng = AbstractDungeon.cardRandomRng;
        return EYBCard.player = EYBPower.player = EYBRelic.player = AbstractDungeon.player;
    }

    private static void ClearStats()
    {
        RefreshPlayer();
        JUtils.LogInfo(CombatStats.class, "Clearing Player Stats");

        MaxHPSinceLastTurn = AbstractDungeon.player == null ? 0 : AbstractDungeon.player.currentHealth;
        EnemyVulnerableModifier = 0;
        BlockRetained = 0;
        BattleID = null;

        turnCount = 0;
        cardsDrawnThisTurn = 0;
        canActivateStarter = true;
        orbsEvokedThisCombat.clear();
        orbsEvokedThisTurn.clear();
        cardsPlayedThisCombat.clear();
        cardsExhaustedThisCombat.clear();
        cardsExhaustedThisTurn.clear();
        synergiesThisCombat.clear();
        synergiesThisTurn.clear();
        unplayableCards.clear();
        resetAfterPlay.clear();
        currentPhase = null;
        exactKills.clear();
        combatData.clear();
        turnData.clear();

        for (GameEvent event : events)
        {
            event.Clear();
        }

        CardGlowBorderPatches.overrideColor = null;
        CombatStats.PurgedCards.clear();
        CombatStats.Affinities.Initialize();
        CombatStats.Affinities.SetLastCardPlayed(null);
        CombatStats.Dolls.Initialize();

        for (OnStatsClearedSubscriber s : onStatsCleared.GetSubscribers())
        {
            s.OnStatsCleared();
        }
    }

    public static void Refresh()
    {
        RefreshPlayer();

        Room = GameUtilities.GetCurrentRoom(false);

        if (Room == null || player == null)
        {
            BattleID = null;
        }
        else if (Room.isBattleOver || player.isDead)
        {
            if (Room.phase != AbstractRoom.RoomPhase.COMBAT || Room.monsters == null || Room.monsters.areMonstersBasicallyDead())
            {
                BattleID = null;
            }
        }
        else if (BattleID == null && Room.phase == AbstractRoom.RoomPhase.COMBAT)
        {
            BattleID = UUID.randomUUID();
        }

        if (BattleID != null && !player.powers.contains(Instance))
        {
            JUtils.LogInfo(CombatStats.class, "Applied PlayerStatistics");
            player.powers.add(Instance);
        }
    }

    public static void OnStartup()
    {
        Refresh();
        ClearStats();
    }

    public static void OnNewRunStart(boolean startOfAct)
    {
        if (GameUtilities.IsPlayerClass(GR.Animator.PlayerClass))
        {
            final AnimatorLoadoutData data = GR.Animator.Data.SelectedLoadout.GetPreset();
            if (data.Buff != 0)
            {
                GR.Common.Dungeon.SetData(LOADOUT_BUFF_KEY, data.Buff * 3);
            }
        }
        Ynitaph.TryRestoreFromPreviousRun();
    }

    public static void OnGameStart()
    {
        ClearStats();
        onBattleStart.Clear();
        onBattleEnd.Clear();

        DevConsole.enabled = true;
        GR.Animator.Dungeon.Reset();
        GR.Common.Dungeon.Reset();
    }

    public static void OnStartOver()
    {
        final AbstractRoom room = GameUtilities.GetCurrentRoom(false);
        if (room != null && room.monsters != null)
        {
            room.cannotLose = true;

            boolean addFake = !JUtils.IsNullOrEmpty(room.monsters.monsters);
            if (addFake)
            {
                for (AbstractMonster m : room.monsters.monsters)
                {
                    if (FakeMonster.ID.equals(m.id))
                    {
                        addFake = false;
                        break;
                    }
                }

                if (addFake)
                {
                    room.monsters.monsters.add(new FakeMonster(-Settings.WIDTH, -Settings.HEIGHT));
                }
            }
        }

        ClearStats();
        onBattleStart.Clear();
        onBattleEnd.Clear();

        DevConsole.enabled = true;
        GR.Animator.Dungeon.Reset();
        GR.Common.Dungeon.Reset();
    }

    public static void OnAfterDeath()
    {
        for (OnAfterDeathSubscriber s : onAfterDeath.GetSubscribers())
        {
            s.OnAfterDeath();
        }

        ClearStats();
    }

    public static int OnModifyDamageFirst(AbstractCreature target, DamageInfo info, int damage)
    {
        for (OnModifyDamageFirstSubscriber s : onModifyDamageFirst.GetSubscribers())
        {
            damage = s.OnModifyDamageFirst(target, info, damage);
        }

        return damage;
    }

    public static int OnModifyDamageLast(AbstractCreature target, DamageInfo info, int damage)
    {
        for (OnModifyDamageLastSubscriber s : onModifyDamageLast.GetSubscribers())
        {
            damage = s.OnModifyDamageLast(target, info, damage);
        }

        return damage;
    }

    public static void OnModifyDebuff(AbstractPower debuff, int initialAmount, int newAmount)
    {
        for (OnModifyDebuffSubscriber s : onModifyDebuff.GetSubscribers())
        {
            s.OnModifyDebuff(debuff, initialAmount, newAmount);
        }
    }

    public static int OnEnergyRecharge(int previousEnergy, int currentEnergy)
    {
        final MutableInt a = new MutableInt(previousEnergy);
        final MutableInt b = new MutableInt(currentEnergy);
        for (OnEnergyRechargeSubscriber s : onEnergyRecharge.GetSubscribers())
        {
            s.OnEnergyRecharge(a, b);
        }

        return b.getValue();
    }

    public static void OnCardReset(AbstractCard card)
    {
        final OnCardResetListener c = JUtils.SafeCast(card, OnCardResetListener.class);
        if (c != null)
        {
            c.OnReset();
        }

        for (OnCardResetSubscriber s : onCardReset.GetSubscribers())
        {
            s.OnCardReset(card);
        }
    }

    public static void OnCardCreated(AbstractCard card, boolean startOfBattle)
    {
        final EYBCard c = JUtils.SafeCast(card, EYBCard.class);
        if (c != null)
        {
            c.triggerWhenCreated(startOfBattle);
        }

        for (OnCardCreatedSubscriber s : onCardCreated.GetSubscribers())
        {
            s.OnCardCreated(card, startOfBattle);
        }
    }

    public static void OnCardPurged(AbstractCard card)
    {
        if (!PurgedCards.contains(card))
        {
            PurgedCards.group.add(card);
        }
    }

    public static void OnCardReshuffled(AbstractCard card, CardGroup sourcePile)
    {
        for (OnCardReshuffledSubscriber s : onCardReshuffled.GetSubscribers())
        {
            s.OnCardReshuffled(card, sourcePile);
        }
    }

    public static void OnShuffle(boolean triggerRelics)
    {
        for (OnShuffleSubscriber s : onShuffle.GetSubscribers())
        {
            s.OnShuffle(triggerRelics);
        }
    }

    public static void OnClickablePowerUsed(EYBClickablePower power, AbstractMonster target)
    {
        for (OnClickablePowerUsedSubscriber s : onClickablePowerUsed.GetSubscribers())
        {
            s.OnClickablePowerUsed(power, target);
        }
    }

    public static void OnAffinitySealed(EYBCardAffinities affinities, boolean manual)
    {
        affinities.Card.triggerOnAffinitySeal(manual);

        for (OnAffinitySealedSubscriber s : onAffinitySealed.GetSubscribers())
        {
            s.OnAffinitySealed(affinities.Card, manual);
        }
    }

    public static void OnAffinityThresholdReached(AbstractAffinityPower power, int thresholdIndex)
    {
        for (OnAffinityThresholdReachedSubscriber s : onAffinityThresholdReached.GetSubscribers())
        {
            s.OnAffinityThresholdReached(power, thresholdIndex);
        }
    }

    public static void OnCreateDamageAction(AbstractGameAction action, DamageInfo info)
    {
        if (action != null && action.source != null && info != null)
        {
            final GeassPower power = GameUtilities.GetPower(action.source, GeassPower.POWER_ID);
            if (power != null && power.enabled)
            {
                info.applyPowers(action.source, action.source);
                action.target = action.source;
            }
        }
    }

    public static void OnRelicObtained(AbstractRelic relic, OnRelicObtainedSubscriber.Trigger trigger)
    {
        RefreshPlayer();

        for (AbstractCard c : player.masterDeck.group)
        {
            if (c instanceof OnRelicObtainedSubscriber)
            {
                ((OnRelicObtainedSubscriber) c).OnRelicObtained(relic, trigger);
            }
        }

        for (AbstractRelic r : player.relics)
        {
            if (r instanceof OnRelicObtainedSubscriber)
            {
                ((OnRelicObtainedSubscriber) r).OnRelicObtained(relic, trigger);
            }
        }
    }

    public static void OnBattleStart()
    {
        Refresh();

        onBattleEnd.Clear();
        for (OnBattleStartSubscriber s : onBattleStart.GetSubscribers())
        {
            s.OnBattleStart();
        }
        onBattleStart.Clear();

        final ArrayList<AbstractCard> cards = new ArrayList<>(player.drawPile.group);
        cards.addAll(player.hand.group);
        cards.addAll(player.discardPile.group);
        cards.addAll(player.exhaustPile.group);

        for (AbstractCard c : cards)
        {
            OnCardCreated(c, true);
        }

        final int tempBuff = GR.Common.Dungeon.GetInteger(LOADOUT_BUFF_KEY, 0);
        if (tempBuff != 0 && !GameUtilities.InEliteOrBossRoom() && AbstractDungeon.actNum < 2)
        {
            final int amount = tempBuff > 0 ? +1 : -1;
            GameActions.Bottom.GainStrength(amount);
            GameActions.Bottom.GainDexterity(amount);
            GameActions.Bottom.GainFocus(amount);
            GR.Common.Dungeon.SetData(LOADOUT_BUFF_KEY, Mathf.MoveTowards(tempBuff, 0, 1));
        }

        GameUtilities.GetAscensionData(true).OnBattleStart();
    }

    public static void OnBattleEnd()
    {
        for (OnBattleEndSubscriber s : onBattleEnd.GetSubscribers())
        {
            s.OnBattleEnd();
        }

        onBattleStart.Clear();
        onBattleEnd.Clear();
        ClearStats();
    }

    public static void OnUsingCard(AbstractCard c, AbstractPlayer p, AbstractMonster m)
    {
        if (c == null)
        {
            throw new RuntimeException("Card played is null");
        }

        c.unfadeOut();
        c.lighten(true);

        if (c.hasTag(EYBCard.RECAST))
        {
            final AbstractCard copy = c.makeStatEquivalentCopy();
            copy.tags.remove(EYBCard.RECAST);
            GameActions.Last.PlayNextTurn(copy, m);
        }

        final AnimatorCard card = JUtils.SafeCast(c, AnimatorCard.class);
        if (card == null)
        {
            c.use(p, m);
            return;
        }

        if (card.cost == -1 && card.energyOnUse == 0)
        {
            card.freeToPlayOnce = false;
        }

        final CardUseInfo info = new CardUseInfo(card, m);

        card.OnUse(p, m, info);

        final ArrayList<AbstractGameAction> actions = GameActions.GetActions();
        cachedActions.clear();
        cachedActions.addAll(actions);

        actions.clear();
        card.OnLateUse(p, m, info);

        if (actions.isEmpty())
        {
            actions.addAll(cachedActions);
        }
        else for (int i = 0; i < cachedActions.size(); i++)
        {
            GameActions.Top.Add(cachedActions.get(cachedActions.size() - 1 - i));
        }
    }

    public static boolean OnTryUsingCard(AbstractCard card, AbstractPlayer p, AbstractMonster m, boolean canPlay)
    {
        if (unplayableCards.contains(card.uuid))
        {
            return false;
        }

        for (OnTryUsingCardSubscriber s : onTryUsingCard.GetSubscribers())
        {
            canPlay &= s.OnTryUsingCard(card, p, m, canPlay);
        }
        
        return canPlay;
    }

    public static void OnEnemyDying(AbstractMonster enemy, boolean triggerRelics)
    {
        for (OnEnemyDyingSubscriber s : onEnemyDying.GetSubscribers())
        {
            s.OnEnemyDying(enemy, triggerRelics);
        }
    }

    public static void OnHealthBarUpdated(AbstractCreature creature)
    {
        if (creature instanceof EYBPlayerCharacter)
        {
            final int tempHP = TempHPField.tempHp.get(creature);
            if (tempHP > EYBPlayerCharacter.MAX_TEMP_HP)
            {
                TempHPField.tempHp.set(creature, EYBPlayerCharacter.MAX_TEMP_HP);
            }
        }

        if (creature == player && creature.currentHealth > MaxHPSinceLastTurn)
        {
            MaxHPSinceLastTurn = creature.currentHealth;
        }

        for (OnHealthBarUpdatedSubscriber s : onHealthBarUpdated.GetSubscribers())
        {
            s.OnHealthBarUpdated(creature);
        }

        GameUtilities.RefreshHandLayout(true);
    }

    public static void OnBlockGained(AbstractCreature creature, int block)
    {
        if (creature instanceof EYBPlayerCharacter)
        {
            if (creature.currentBlock > EYBPlayerCharacter.MAX_BLOCK)
            {
                creature.currentBlock = EYBPlayerCharacter.MAX_BLOCK;
            }
        }

        for (OnBlockGainedSubscriber s : onBlockGained.GetSubscribers())
        {
            s.OnBlockGained(creature, block);
        }
    }

    public static void OnBlockBroken(AbstractCreature creature)
    {
        for (OnBlockBrokenSubscriber s : onBlockBroken.GetSubscribers())
        {
            s.OnBlockBroken(creature);
        }
    }

    public static void OnBeforeLoseBlock(AbstractCreature creature, int amount, boolean noAnimation)
    {
        for (OnBeforeLoseBlockSubscriber s : onBeforeLoseBlock.GetSubscribers())
        {
            s.OnBeforeLoseBlock(creature, amount, noAnimation);
        }
    }

    public static void OnOrbPassiveEffect(AbstractOrb orb)
    {
        for (OnOrbPassiveEffectSubscriber s : onOrbPassiveEffect.GetSubscribers())
        {
            s.OnOrbPassiveEffect(orb);
        }
    }

    public static void OnManualDiscard()
    {
        for (OnAfterCardDiscardedSubscriber p : onAfterCardDiscarded.GetSubscribers())
        {
            p.OnAfterCardDiscarded();
        }
    }

    public static void OnAfterDraw(AbstractCard card)
    {
        cardsDrawnThisTurn += 1;
        for (OnAfterCardDrawnSubscriber s : onAfterCardDrawn.GetSubscribers())
        {
            s.OnAfterCardDrawn(card);
        }

        if (card.hasTag(GR.Enums.CardTags.HASTE))
        {
            GameActions.Top.Add(new HasteAction(card));
        }
    }

    public static <T> T SetTurnData(String key, T data)
    {
        turnData.put(key, data);
        return data;
    }

    public static <T> T GetTurnData(String key, T defaultData)
    {
        if (turnData.containsKey(key))
        {
            return (T) turnData.get(key);
        }
        else if (defaultData != null)
        {
            return SetCombatData(key, defaultData);
        }

        return defaultData;
    }

    public static <T> T SetCombatData(String key, T data)
    {
        combatData.put(key, data);
        return data;
    }

    public static <T> T GetCombatData(String key, T defaultData)
    {
        if (combatData.containsKey(key))
        {
            return (T) combatData.get(key);
        }
        else if (defaultData != null)
        {
            return SetCombatData(key, defaultData);
        }

        return defaultData;
    }

    public static List<AbstractMonster> ExactKills()
    {
        return exactKills;
    }

    public static List<AbstractCard> CardsExhaustedThisCombat()
    {
        return cardsExhaustedThisCombat;
    }

    public static List<AbstractCard> CardsExhaustedThisTurn()
    {
        return cardsExhaustedThisTurn;
    }

    public static List<AbstractCard> CardsPlayedThisCombat(int turn)
    {
        return cardsPlayedThisCombat.computeIfAbsent(turn, k -> new ArrayList<>());
    }

    public static List<AbstractCard> SynergiesThisCombat()
    {
        return synergiesThisCombat;
    }

    public static List<AbstractCard> SynergiesThisTurn()
    {
        return synergiesThisTurn;
    }

    public static List<AbstractCard> ResetAfterPlay()
    {
        return resetAfterPlay;
    }

    public static List<UUID> UnplayableCards()
    {
        return unplayableCards;
    }

    public static int CardsDrawnThisTurn()
    {
        return cardsDrawnThisTurn;
    }

    public static List<AbstractOrb> OrbsEvokedThisCombat()
    {
        return orbsEvokedThisCombat;
    }

    public static List<AbstractOrb> OrbsEvokedThisTurn()
    {
        return orbsEvokedThisTurn;
    }

    public static int TurnCount(boolean fromZero)
    {
        return fromZero ? turnCount : (turnCount + 1);
    }

    public static void OnApplyPower(AbstractCreature source, AbstractCreature target, AbstractPower power)
    {
        ApplyPowerPriority(power);

        for (OnApplyPowerSubscriber p : onApplyPower.GetSubscribers())
        {
            p.OnApplyPower(power, target, source);
        }
    }

    public static void ApplyPowerPriority(AbstractPower power)
    {
        if (StrengthPower.POWER_ID.equals(power.ID))
        {
            power.priority = -2099;
        }
        else if (DexterityPower.POWER_ID.equals(power.ID))
        {
            power.priority = -2098;
        }
        else if (FocusPower.POWER_ID.equals(power.ID))
        {
            power.priority = -2097;
        }
        else if (VitalityPower.POWER_ID.equals(power.ID))
        {
            power.priority = -2096;
        }
        else if (InvocationPower.POWER_ID.equals(power.ID))
        {
            power.priority = -2095;
        }
    }

    @Override
    public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source)
    {
        //super.onApplyPower(power, target, source);
    }

    @Override
    public void onChannel(AbstractOrb orb)
    {
        super.onChannel(orb);

        if (orb != null && !(orb instanceof EmptyOrbSlot))
        {
            if (orb instanceof EYBOrb)
            {
                ((EYBOrb) orb).onChannel();
            }

            for (OnChannelOrbSubscriber p : onChannelOrb.GetSubscribers())
            {
                p.OnChannelOrb(orb);
            }
        }
    }

    @Override
    public void onEvokeOrb(AbstractOrb orb)
    {
        super.onEvokeOrb(orb);

        if (orb != null && !(orb instanceof EmptyOrbSlot))
        {
            for (OnEvokeOrbSubscriber p : onEvokeOrb.GetSubscribers())
            {
                p.OnEvokeOrb(orb);
            }
            orbsEvokedThisCombat.add(orb);
            orbsEvokedThisTurn.add(orb);
        }
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster m) {
        super.onPlayCard(card, m);

        for (OnPlayCardSubscriber p : onPlayCard.GetSubscribers())
        {
            p.OnPlayCard(card, m);
        }
    }

    @Override
    public void onAfterCardPlayed(AbstractCard card)
    {
        super.onAfterCardPlayed(card);

        for (OnAfterCardPlayedSubscriber p : onAfterCardPlayed.GetSubscribers())
        {
            p.OnAfterCardPlayed(card);
        }

        if (!card.isInAutoplay)
        {
            canActivateStarter = false;
        }

        CardsPlayedThisCombat(turnCount).add(card);

        if (player.limbo.contains(card))
        {
            GameActions.Top.Add(new UnlimboAction(card));
        }
    }

    @Override
    public void onAfterUseCard(AbstractCard card, UseCardAction action)
    {
        super.onAfterUseCard(card, action);

        resetAfterPlay.add(card);
        for (AbstractCard c : resetAfterPlay)
        {
            card.tags.remove(GR.Enums.CardTags.AUTOPLAYED);
            card.tags.remove(GR.Enums.CardTags.AUTOPLAYED_COPY);
        }
        resetAfterPlay.clear();

        CombatStats.Affinities.SetLastCardPlayed(card);
        player.hand.glowCheck();
    }

    @Override
    public void onExhaust(AbstractCard card)
    {
        super.onExhaust(card);

        card.targetDrawScale = 0.75F;
        card.setAngle(0.0F);
        card.lighten(false);
        card.clearPowers();

        for (OnAfterCardExhaustedSubscriber p : onAfterCardExhausted.GetSubscribers())
        {
            p.OnAfterCardExhausted(card);
        }
        cardsExhaustedThisCombat.add(card);
        cardsExhaustedThisTurn.add(card);
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target)
    {
        super.onAttack(info, damageAmount, target);

        for (OnAttackSubscriber p : onAttack.GetSubscribers())
        {
            p.OnAttack(info, damageAmount, target);
        }
    }

    @Override
    public void updateDescription()
    {
        this.description = "";
    }

    @Override
    public void update(int slot)
    {
        super.update(slot);

        if (currentPhase != AbstractDungeon.actionManager.phase)
        {
            currentPhase = AbstractDungeon.actionManager.phase;
            for (OnPhaseChangedSubscriber s : onPhaseChanged.GetSubscribers())
            {
                s.OnPhaseChanged(currentPhase);
            }
        }
    }

    @Override
    public void renderIcons(SpriteBatch sb, float x, float y, Color c)
    {
        super.renderIcons(sb, x, y, c);
    }

    @Override
    public void onChangeStance(AbstractStance oldStance, AbstractStance newStance)
    {
        super.onChangeStance(oldStance, newStance);

        for (OnStanceChangedSubscriber s : onStanceChanged.GetSubscribers())
        {
            s.OnStanceChanged(oldStance, newStance);
        }
    }

    @Override
    public void onDeath()
    {
        super.onDeath();
        ClearStats();
    }

    @Override
    public void onVictory()
    {
        super.onVictory();

        if (GameUtilities.IsPlayerClass(GR.Animator.PlayerClass))
        {
            final AbstractRoom room = GameUtilities.GetCurrentRoom(false);
            if (room != null && room.rewardAllowed && GameUtilities.GetAscensionLevel() >= 13)
            {
                final int bonus = GameUtilities.InEliteRoom() ? 2 : GameUtilities.InBossRoom() ? 3 : 1;
                final int unblockedDamage = GameActionManager.damageReceivedThisCombat - GameActionManager.hpLossThisCombat;
                if (unblockedDamage <= 0)
                {
                    GameUtilities.AddSpecialGoldReward("No Damage", 2 + (bonus * 2));
                }
                if (turnCount < (2 + bonus))
                {
                    GameUtilities.AddSpecialGoldReward("Fast Victory", bonus * 4);
                }
                if (exactKills.size() > 0)
                {
                    GameUtilities.AddSpecialGoldReward("Exact Kill", (1 + Math.min(3, exactKills.size())) + bonus);
                }
            }
        }

        ClearStats();
    }

    @Override
    public void wasHPLost(DamageInfo info, int damageAmount)
    {
        super.wasHPLost(info, damageAmount);

        if (onLosingHP.Count() > 0)
        {
            for (OnLoseHPSubscriber s : onLoseHP.GetSubscribers())
            {
                s.OnLoseHP(info, damageAmount);
            }
        }
    }

    @Override
    public int onLoseHp(int damageAmount)
    {
        int damage = damageAmount;
        if (onLosingHP.Count() > 0)
        {
            for (OnLosingHPSubscriber s : onLosingHP.GetSubscribers())
            {
                damage = s.OnLosingHP(damage);
            }
        }

        return super.onLoseHp(damage);
    }

    @Override
    public void atStartOfTurn()
    {
        super.atStartOfTurn();

        IsPlayerTurn = true;
        MaxHPSinceLastTurn = GameActionManager.playerHpLastTurn;

        if (onStartOfTurn.Count() > 0)
        {
            for (OnStartOfTurnSubscriber s : onStartOfTurn.GetSubscribers())
            {
                s.OnStartOfTurn();
            }
        }

        if (BlockRetained > 0)
        {
            if (!player.hasPower(BarricadePower.POWER_ID) && !player.hasPower(BlurPower.POWER_ID))
            {
                this.ID = BarricadePower.POWER_ID;
            }
            else
            {
                BlockRetained = 0;
            }
        }
    }

    @Override
    public void atStartOfTurnPostDraw()
    {
        super.atStartOfTurnPostDraw();
        if (onStartOfTurnPostDraw.Count() > 0)
        {
            for (OnStartOfTurnPostDrawSubscriber s : onStartOfTurnPostDraw.GetSubscribers())
            {
                s.OnStartOfTurnPostDraw();
            }
        }

        if (BlockRetained > 0)
        {
            int temp = Math.max(0, player.currentBlock - BlockRetained);
            if (temp > 0)
            {
                if (player.hasRelic(Calipers.ID))
                {
                    temp = Math.min(Calipers.BLOCK_LOSS, temp);
                }

                player.loseBlock(temp, true);
            }

            this.ID = POWER_ID;
            BlockRetained = 0;
        }
    }

    @Override
    public void atEndOfTurnPreEndTurnCards(boolean isPlayer)
    {
        super.atEndOfTurnPreEndTurnCards(isPlayer);

        IsPlayerTurn = false;

        for (OnEndOfTurnFirstSubscriber s : onEndOfTurnFirst.GetSubscribers())
        {
            s.OnEndOfTurnFirst(isPlayer);
        }

        for (AbstractCard c : player.hand.group)
        {
            if (GameUtilities.IsHindrance(c) && c.isEthereal)
            {
                final EYBCard card = JUtils.SafeCast(c, EYBCard.class);
                if (card != null && card.playAtEndOfTurn)
                {
                    c.exhaustOnUseOnce = true;
                }
                else
                {
                    GameActions.Delayed.Exhaust(c);
                }
            }
        }
    }

    @Override
    public void atEndOfTurn(boolean isPlayer)
    {
        super.atEndOfTurn(isPlayer);

        for (OnEndOfTurnLastSubscriber s : onEndOfTurnLast.GetSubscribers())
        {
            s.OnEndOfTurnLast(isPlayer);
        }

        turnData.clear();
        cardsExhaustedThisTurn.clear();
        cardsDrawnThisTurn = 0;
        canActivateStarter = true;
        synergiesThisTurn.clear();
        unplayableCards.clear();
        orbsEvokedThisTurn.clear();
        turnCount += 1;

        CombatStats.Affinities.SetLastCardPlayed(null);
    }
}
