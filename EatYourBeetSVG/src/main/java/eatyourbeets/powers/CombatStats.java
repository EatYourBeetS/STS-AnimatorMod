package eatyourbeets.powers;

import basemod.DevConsole;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.utility.UnlimboAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.FocusPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.stances.AbstractStance;
import eatyourbeets.actions.special.HasteAction;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.interfaces.listeners.OnCardResetListener;
import eatyourbeets.interfaces.subscribers.*;
import eatyourbeets.orbs.EYBOrb;
import eatyourbeets.powers.common.VitalityPower;
import eatyourbeets.relics.EYBRelic;
import eatyourbeets.resources.GR;
import eatyourbeets.ui.animator.combat.EYBCardAffinitySystem;
import eatyourbeets.ui.common.ControllableCardPile;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;
import patches.CardGlowBorderPatches;

import java.util.*;

public class CombatStats extends EYBPower implements InvisiblePower
{
    public static final String POWER_ID = GR.Common.CreateID(CombatStats.class.getSimpleName());

    public static final CombatStats Instance = new CombatStats();
    public static final EYBCardAffinitySystem Affinities = new EYBCardAffinitySystem();
    public static float EnemyVulnerableModifier;
    public static boolean LoadingPlayerSave;
    public static AbstractRoom Room;
    public static UUID BattleID;

    static final ArrayList<GameEvent<?>> events = new ArrayList<>();
    //
    public static final GameEvent<OnSynergySubscriber> onSynergy = RegisterEvent(new GameEvent<>());
    public static final GameEvent<OnEnemyDyingSubscriber> onEnemyDying = RegisterEvent(new GameEvent<>());
    public static final GameEvent<OnBlockBrokenSubscriber> onBlockBroken = RegisterEvent(new GameEvent<>());
    public static final GameEvent<OnBeforeLoseBlockSubscriber> onBeforeLoseBlock = RegisterEvent(new GameEvent<>());
    public static final GameEvent<OnTryUsingCardSubscriber> onTryUsingCard = RegisterEvent(new GameEvent<>());
    public static final GameEvent<OnAfterCardDrawnSubscriber> onAfterCardDrawn = RegisterEvent(new GameEvent<>());
    public static final GameEvent<OnAfterCardPlayedSubscriber> onAfterCardPlayed = RegisterEvent(new GameEvent<>());
    public static final GameEvent<OnAfterCardDiscardedSubscriber> onAfterCardDiscarded = RegisterEvent(new GameEvent<>());
    public static final GameEvent<OnAfterCardExhaustedSubscriber> onAfterCardExhausted = RegisterEvent(new GameEvent<>());
    public static final GameEvent<OnOrbPassiveEffectSubscriber> onOrbPassiveEffect = RegisterEvent(new GameEvent<>());
    public static final GameEvent<OnClickablePowerUsed> onClickablePowerUsed = RegisterEvent(new GameEvent<>());
    public static final GameEvent<OnChannelOrbSubscriber> onChannelOrb = RegisterEvent(new GameEvent<>());
    public static final GameEvent<OnEvokeOrbSubscriber> onEvokeOrb = RegisterEvent(new GameEvent<>());
    public static final GameEvent<OnAttackSubscriber> onAttack = RegisterEvent(new GameEvent<>());
    public static final GameEvent<OnLoseHpSubscriber> onLoseHp = RegisterEvent(new GameEvent<>());
    public static final GameEvent<OnEndOfTurnSubscriber> onEndOfTurn = RegisterEvent(new GameEvent<>());
    public static final GameEvent<OnShuffleSubscriber> onShuffle = RegisterEvent(new GameEvent<>());
    public static final GameEvent<OnApplyPowerSubscriber> onApplyPower = RegisterEvent(new GameEvent<>());
    public static final GameEvent<OnAfterDeathSubscriber> onAfterDeath = RegisterEvent(new GameEvent<>());
    public static final GameEvent<OnRawDamageReceived> onRawDamageReceived = RegisterEvent(new GameEvent<>());
    public static final GameEvent<OnCardResetSubscriber> onCardReset = RegisterEvent(new GameEvent<>());
    public static final GameEvent<OnCardCreatedSubscriber> onCardCreated = RegisterEvent(new GameEvent<>());
    public static final GameEvent<OnStartOfTurnSubscriber> onStartOfTurn = RegisterEvent(new GameEvent<>());
    public static final GameEvent<OnStartOfTurnPostDrawSubscriber> onStartOfTurnPostDraw = RegisterEvent(new GameEvent<>());
    public static final GameEvent<OnPhaseChangedSubscriber> onPhaseChanged = RegisterEvent(new GameEvent<>());
    public static final GameEvent<OnStanceChangedSubscriber> onStanceChanged = RegisterEvent(new GameEvent<>());
    public static final GameEvent<OnSynergyCheckSubscriber> onSynergyCheck = RegisterEvent(new GameEvent<>());
    //
    public static final GameEvent<OnStatsClearedSubscriber> onStatsCleared = new GameEvent<>();
    public static final GameEvent<OnBattleStartSubscriber> onBattleStart = new GameEvent<>();
    public static final GameEvent<OnBattleEndSubscriber> onBattleEnd = new GameEvent<>();

    public static final ControllableCardPile ControlPile = new ControllableCardPile();

    private static final Map<String, Object> combatData = new HashMap<>();
    private static final Map<String, Object> turnData = new HashMap<>();
    private static final ArrayList<AbstractGameAction> cachedActions = new ArrayList<>();
    private static final ArrayList<AbstractOrb> orbsEvokedThisCombat = new ArrayList<>();
    private static final ArrayList<AbstractOrb> orbsEvokedThisTurn = new ArrayList<>();
    private static final ArrayList<AbstractCard> synergiesThisCombat = new ArrayList<>();
    private static final ArrayList<AbstractCard> synergiesThisTurn = new ArrayList<>();
    private static final ArrayList<UUID> unplayableCards = new ArrayList<>();
    private static GameActionManager.Phase currentPhase;
    private static int turnCount = 0;
    private static int cardsDrawnThisTurn = 0;
    private static int cardsExhaustedThisTurn = 0;

    private static HashMap<String, Integer> amountIncreasedOnOrbs = new HashMap<>();
    //@Formatter: Off
    public static boolean CanActivateLimited(String id) { return !HasActivatedLimited(id); }
    public static boolean HasActivatedLimited(String id) { return combatData.containsKey(id); }
    public static boolean TryActivateLimited(String id) { return combatData.put(id, 1) == null; }
    public static boolean CanActivateSemiLimited(String id) { return !HasActivatedSemiLimited(id); }
    public static boolean HasActivatedSemiLimited(String id) { return turnData.containsKey(id); }
    public static boolean TryActivateSemiLimited(String id) { return turnData.put(id, 1) == null; }
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

        EnemyVulnerableModifier = 0;
        BattleID = null;

        turnCount = 0;
        cardsDrawnThisTurn = 0;
        cardsExhaustedThisTurn = 0;
        orbsEvokedThisCombat.clear();
        orbsEvokedThisTurn.clear();
        synergiesThisCombat.clear();
        synergiesThisTurn.clear();
        currentPhase = null;
        combatData.clear();
        turnData.clear();

        for (GameEvent event : events)
        {
            event.Clear();
        }

        CardGlowBorderPatches.overrideColor = null;
        CombatStats.Affinities.Initialize();
        CombatStats.Affinities.SetLastCardPlayed(null);

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

    public static int OnModifyDamage(AbstractCreature target, DamageInfo info, int damage)
    {
        for (OnRawDamageReceived s : onRawDamageReceived.GetSubscribers())
        {
            damage = s.OnRawDamageReceived(target, info, damage);
        }

        return damage;
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

    public static void OnShuffle(boolean triggerRelics)
    {
        for (OnShuffleSubscriber s : onShuffle.GetSubscribers())
        {
            s.OnShuffle(triggerRelics);
        }
    }

    public static void OnClickablePowerUsed(EYBClickablePower power, AbstractMonster target)
    {
        for (OnClickablePowerUsed s : onClickablePowerUsed.GetSubscribers())
        {
            s.OnClickablePowerUsed(power, target);
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

    public static void OnSynergy(AbstractCard card)
    {
        for (OnSynergySubscriber s : onSynergy.GetSubscribers())
        {
            s.OnSynergy(card);
        }

        synergiesThisTurn.add(card);
        synergiesThisCombat.add(card);
    }

    public static void OnUsingCard(AbstractCard c, AbstractPlayer p, AbstractMonster m)
    {
        if (c == null)
        {
            throw new RuntimeException("Card played is null");
        }

        final AnimatorCard card = JUtils.SafeCast(c, AnimatorCard.class);
        if (card == null)
        {
            c.use(p, m);
            return;
        }

        final boolean isSynergizing = Affinities.IsSynergizing(c);

        card.OnUse(p, m, isSynergizing);

        if (isSynergizing)
        {
            OnSynergy(c);
        }

        final ArrayList<AbstractGameAction> actions = GameActions.GetActions();

        cachedActions.clear();
        cachedActions.addAll(actions);

        actions.clear();
        card.OnLateUse(p, m, isSynergizing);

        if (isSynergizing)
        {
            Affinities.OnSynergy(card);
        }

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

    public static int CardsExhaustedThisTurn()
    {
        return cardsExhaustedThisTurn;
    }

    public static List<AbstractCard> SynergiesThisCombat()
    {
        return synergiesThisCombat;
    }

    public static List<AbstractCard> SynergiesThisTurn()
    {
        return synergiesThisTurn;
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

    public static HashMap<String, Integer> AmountIncreasedOnOrbs(boolean fromZero)
    {
        return amountIncreasedOnOrbs;
    }

    public static int TurnCount(boolean fromZero)
    {
        return fromZero ? turnCount : (turnCount + 1);
    }

    public static void AddAmountIncreasedOnOrbs(String orbType, int amount)
    {
        if (amountIncreasedOnOrbs.containsKey(orbType))
        {
            int currentAmount = amountIncreasedOnOrbs.get(orbType);

            amountIncreasedOnOrbs.put(orbType, currentAmount + amount);
        }
        else
        {
            amountIncreasedOnOrbs.put(orbType, amount);
        }
    }

    public static int GetAmountIncreasedOnOrb(String orbType)
    {
        for (String orbID : amountIncreasedOnOrbs.keySet())
        {
            if (orbID.equals(orbType))
            {
                return amountIncreasedOnOrbs.get(orbID);
            }
        }

        return 0;
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
    public void onPlayCard(AbstractCard card, AbstractMonster m)
    {
        super.onPlayCard(card, m);

        CombatStats.Affinities.TrySynergize(card);
    }

    @Override
    public void onAfterCardPlayed(AbstractCard card)
    {
        super.onAfterCardPlayed(card);

        for (OnAfterCardPlayedSubscriber p : onAfterCardPlayed.GetSubscribers())
        {
            p.OnAfterCardPlayed(card);
        }

        if (player.limbo.contains(card))
        {
            GameActions.Top.Add(new UnlimboAction(card));
        }
    }

    @Override
    public void onAfterUseCard(AbstractCard card, UseCardAction action)
    {
        super.onAfterUseCard(card, action);

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
        cardsExhaustedThisTurn += 1;
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
    public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source)
    {
        super.onApplyPower(power, target, source);

        ApplyPowerPriority(power);

        for (OnApplyPowerSubscriber p : onApplyPower.GetSubscribers())
        {
            p.OnApplyPower(power, target, source);
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
    public void atEndOfTurn(boolean isPlayer)
    {
        super.atEndOfTurn(isPlayer);

        for (OnEndOfTurnSubscriber s : onEndOfTurn.GetSubscribers())
        {
            s.OnEndOfTurn(isPlayer);
        }

        turnData.clear();
        cardsExhaustedThisTurn = 0;
        cardsDrawnThisTurn = 0;
        synergiesThisTurn.clear();
        unplayableCards.clear();
        orbsEvokedThisTurn.clear();
        turnCount += 1;

        CombatStats.Affinities.SetLastCardPlayed(null);
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
        ClearStats();
    }

    @Override
    public int onLoseHp(int damageAmount)
    {
        int damage = damageAmount;
        if (onLoseHp.Count() > 0)
        {
            for (OnLoseHpSubscriber s : onLoseHp.GetSubscribers())
            {
                damage = s.OnLoseHp(damage);
            }
        }

        return super.onLoseHp(damage);
    }

    @Override
    public void atStartOfTurn()
    {
        super.atStartOfTurn();

        if (onStartOfTurn.Count() > 0)
        {
            for (OnStartOfTurnSubscriber s : onStartOfTurn.GetSubscribers())
            {
                s.OnStartOfTurn();
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
    }
}
