package eatyourbeets.powers;

import basemod.DevConsole;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower;
import com.megacrit.cardcrawl.actions.GameActionManager;
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
import com.megacrit.cardcrawl.stances.AbstractStance;
import eatyourbeets.actions.special.HasteAction;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.interfaces.subscribers.*;
import eatyourbeets.powers.common.AgilityPower;
import eatyourbeets.powers.common.ForcePower;
import eatyourbeets.powers.common.IntellectPower;
import eatyourbeets.relics.EYBRelic;
import eatyourbeets.resources.GR;
import eatyourbeets.ui.unnamed.Void;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JavaUtilities;
import patches.CardGlowBorderPatches;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CombatStats extends EYBPower implements InvisiblePower
{
    public static final String POWER_ID = GR.Common.CreateID(CombatStats.class.getSimpleName());

    public static final CombatStats Instance = new CombatStats();

    public static final GameEvent<OnEnemyDyingSubscriber> onEnemyDying = new GameEvent<>();
    public static final GameEvent<OnBlockBrokenSubscriber> onBlockBroken = new GameEvent<>();
    public static final GameEvent<OnBeforeLoseBlockSubscriber> onBeforeLoseBlock = new GameEvent<>();
    public static final GameEvent<OnBattleStartSubscriber> onBattleStart = new GameEvent<>();
    public static final GameEvent<OnBattleEndSubscriber> onBattleEnd = new GameEvent<>();
    public static final GameEvent<OnAfterCardDrawnSubscriber> onAfterCardDrawn = new GameEvent<>();
    public static final GameEvent<OnAfterCardDiscardedSubscriber> onAfterCardDiscarded = new GameEvent<>();
    public static final GameEvent<OnAfterCardExhaustedSubscriber> onAfterCardExhausted = new GameEvent<>();
    public static final GameEvent<OnEvokeOrbSubscriber> onEvokeOrb = new GameEvent<>();
    public static final GameEvent<OnAttackSubscriber> onAttack = new GameEvent<>();
    public static final GameEvent<OnLoseHpSubscriber> onLoseHp = new GameEvent<>();
    public static final GameEvent<OnEndOfTurnSubscriber> onEndOfTurn = new GameEvent<>();
    public static final GameEvent<OnAfterCardPlayedSubscriber> onAfterCardPlayed = new GameEvent<>();
    public static final GameEvent<OnApplyPowerSubscriber> onApplyPower = new GameEvent<>();
    public static final GameEvent<OnSynergySubscriber> onSynergy = new GameEvent<>();
    public static final GameEvent<OnAfterDeathSubscriber> onAfterDeath = new GameEvent<>();
    public static final GameEvent<OnShuffleSubscriber> onShuffle = new GameEvent<>();
    public static final GameEvent<OnStartOfTurnSubscriber> onStartOfTurn = new GameEvent<>();
    public static final GameEvent<OnStartOfTurnPostDrawSubscriber> onStartOfTurnPostDraw = new GameEvent<>();
    public static final GameEvent<OnCostRefreshSubscriber> onCostRefresh = new GameEvent<>();
    public static final GameEvent<OnPhaseChangedSubscriber> onPhaseChanged = new GameEvent<>();
    public static final GameEvent<OnStatsClearedSubscriber> onStatsCleared = new GameEvent<>();
    public static final GameEvent<OnStanceChangedSubscriber> onStanceChanged = new GameEvent<>();

    public static final Void Void = new Void();
    public static boolean LoadingPlayerSave;

    private static final Map<String, Object> combatData = new HashMap<>();
    private static final Map<String, Object> turnData = new HashMap<>();
    private static GameActionManager.Phase currentPhase;
    private static int turnCount = 0;
    private static int cardsDrawnThisTurn = 0;
    private static int cardsExhaustedThisTurn = 0;
    private static int orbsEvokedThisCombat = 0;
    private static int orbsEvokedThisTurn = 0;
    private static int synergiesThisTurn = 0;

    //@Formatter: Off
    public static boolean HasActivatedLimited(String id) { return combatData.containsKey(id); }
    public static boolean HasActivatedSemiLimited(String id) { return turnData.containsKey(id); }
    public static boolean TryActivateLimited(String id) { return combatData.put(id, 1) == null; }
    public static boolean TryActivateSemiLimited(String id) { return turnData.put(id, 1) == null; }
    //@Formatter: On

    protected CombatStats()
    {
        super(null, POWER_ID);

        this.priority = -3000; //it was Integer.MIN_VALUE but it actually breaks the comparator, nice
    }

    @Override
    public AbstractPower makeCopy()
    {
        JavaUtilities.GetLogger(this).error("Do not clone powers which implement InvisiblePower");
        return null;
    }

    public static AbstractPlayer RefreshPlayer()
    {
        EYBCard.rng = EYBPower.rng = EYBRelic.rng = AbstractDungeon.cardRandomRng;
        return EYBCard.player = EYBPower.player = EYBRelic.player = AbstractDungeon.player;
    }

    private static void ClearStats()
    {
        RefreshPlayer();
        JavaUtilities.Log(CombatStats.class, "Clearing Player Stats");

        for (OnStatsClearedSubscriber s : onStatsCleared.GetSubscribers())
        {
            s.OnStatsCleared();
        }

        CardGlowBorderPatches.overrideColor = null;
        Synergies.SetLastCardPlayed(null);

        turnCount = 0;
        cardsDrawnThisTurn = 0;
        cardsExhaustedThisTurn = 0;
        orbsEvokedThisCombat = 0;
        orbsEvokedThisTurn = 0;
        synergiesThisTurn = 0;
        currentPhase = null;
        combatData.clear();
        turnData.clear();

        onSynergy.Clear();
        onEnemyDying.Clear();
        onBlockBroken.Clear();
        onBeforeLoseBlock.Clear();
        onAfterCardDrawn.Clear();
        onAfterCardPlayed.Clear();
        onAfterCardDiscarded.Clear();
        onAfterCardExhausted.Clear();
        onAttack.Clear();
        onLoseHp.Clear();
        onEndOfTurn.Clear();
        onShuffle.Clear();
        onApplyPower.Clear();
        onAfterDeath.Clear();
        onCostRefresh.Clear();
        onStartOfTurn.Clear();
        onStartOfTurnPostDraw.Clear();
        onPhaseChanged.Clear();
        onStatsCleared.Clear();
        onStanceChanged.Clear();

        //Void.Initialize(true);
    }

    public static void EnsurePowerIsApplied()
    {
        if (!player.powers.contains(Instance))
        {
            JavaUtilities.Log(CombatStats.class, "Applied PlayerStatistics");
            player.powers.add(Instance);
        }
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

    public static void OnCostRefresh(AbstractCard card)
    {
        OnCostRefreshSubscriber c = JavaUtilities.SafeCast(card, OnCostRefreshSubscriber.class);
        if (c != null)
        {
            c.OnCostRefresh(card);
        }

        for (OnCostRefreshSubscriber s : onCostRefresh.GetSubscribers())
        {
            if (card != s)
            {
                s.OnCostRefresh(card);
            }
        }
    }

    public static void OnShuffle(boolean triggerRelics)
    {
        for (OnShuffleSubscriber s : onShuffle.GetSubscribers())
        {
            s.OnShuffle(triggerRelics);
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

    public void OnBattleStart()
    {
        ClearStats();

        ArrayList<AbstractCard> cards = new ArrayList<>(player.drawPile.group);
        cards.addAll(player.hand.group);
        cards.addAll(player.discardPile.group);
        cards.addAll(player.exhaustPile.group);

        for (AbstractCard c : cards)
        {
            OnBattleStartSubscriber s = JavaUtilities.SafeCast(c, OnBattleStartSubscriber.class);
            if (s != null)
            {
                s.OnBattleStart();
            }
        }

        for (OnBattleStartSubscriber s : onBattleStart.GetSubscribers())
        {
            s.OnBattleStart();
        }

        onBattleStart.Clear();
        onBattleEnd.Clear();
    }

    public void OnBattleEnd()
    {
        for (OnBattleEndSubscriber s : onBattleEnd.GetSubscribers())
        {
            s.OnBattleEnd();
        }

        onBattleStart.Clear();
        onBattleEnd.Clear();
        ClearStats();
    }

    public void OnEnemyDying(AbstractMonster enemy, boolean triggerRelics)
    {
        for (OnEnemyDyingSubscriber s : onEnemyDying.GetSubscribers())
        {
            s.OnEnemyDying(enemy, triggerRelics);
        }
    }

    public void OnBlockBroken(AbstractCreature creature)
    {
        for (OnBlockBrokenSubscriber s : onBlockBroken.GetSubscribers())
        {
            s.OnBlockBroken(creature);
        }
    }

    public void OnBeforeLoseBlock(AbstractCreature creature, int amount, boolean noAnimation)
    {
        for (OnBeforeLoseBlockSubscriber s : onBeforeLoseBlock.GetSubscribers())
        {
            s.OnBeforeLoseBlock(creature, amount, noAnimation);
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
        else
        {
            return SetCombatData(key, defaultData);
        }
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
        else
        {
            return SetCombatData(key, defaultData);
        }
    }

    public static int CardsExhaustedThisTurn()
    {
        return cardsExhaustedThisTurn;
    }

    public static int SynergiesThisTurn()
    {
        return synergiesThisTurn;
    }

    public static int CardsDrawnThisTurn()
    {
        return cardsDrawnThisTurn;
    }

    public static int OrbsEvokedThisCombat()
    {
        return orbsEvokedThisCombat;
    }

    public static int OrbsEvokedThisTurn()
    {
        return orbsEvokedThisTurn;
    }

    public static int TurnCount()
    {
        return turnCount;
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
            orbsEvokedThisCombat += 1;
            orbsEvokedThisTurn += 1;
        }
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster m)
    {
        super.onPlayCard(card, m);

        AnimatorCard c = JavaUtilities.SafeCast(card, AnimatorCard.class);
        if (c != null && c.HasSynergy())
        {
            for (OnSynergySubscriber s : onSynergy.GetSubscribers())
            {
                s.OnSynergy(c);
            }
        }
    }

    @Override
    public void onAfterCardPlayed(AbstractCard card)
    {
        super.onAfterCardPlayed(card);

        AnimatorCard c = JavaUtilities.SafeCast(card, AnimatorCard.class);
        if (c != null && c.HasSynergy())
        {
            synergiesThisTurn += 1;
        }

        for (OnAfterCardPlayedSubscriber p : onAfterCardPlayed.GetSubscribers())
        {
            p.OnAfterCardPlayed(card);
        }
    }

    @Override
    public void onAfterUseCard(AbstractCard card, UseCardAction action)
    {
        super.onAfterUseCard(card, action);

        Synergies.SetLastCardPlayed(card);
        player.hand.glowCheck();
    }

    @Override
    public void onExhaust(AbstractCard card)
    {
        super.onExhaust(card);

        for (OnAfterCardExhaustedSubscriber p : onAfterCardExhausted.GetSubscribers())
        {
            p.OnAfterCardExhausted(card);
        }
        cardsExhaustedThisTurn += 1;
    }

    public void OnManualDiscard()
    {
        for (OnAfterCardDiscardedSubscriber p : onAfterCardDiscarded.GetSubscribers())
        {
            p.OnAfterCardDiscarded();
        }
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

        if (ForcePower.POWER_ID.equals(power.ID))
        {
            power.priority = -2100;
        }
        else if (AgilityPower.POWER_ID.equals(power.ID))
        {
            power.priority = -2099;
        }
        else if (IntellectPower.POWER_ID.equals(power.ID))
        {
            power.priority = -2098;
        }
        else if (StrengthPower.POWER_ID.equals(power.ID))
        {
            power.priority = -2097;
        }
        else if (DexterityPower.POWER_ID.equals(power.ID))
        {
            power.priority = -2096;
        }
        else if (FocusPower.POWER_ID.equals(power.ID))
        {
            power.priority = -2095;
        }

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
        synergiesThisTurn = 0;
        cardsDrawnThisTurn = 0;
        orbsEvokedThisTurn = 0;
        turnCount += 1;

        Synergies.SetLastCardPlayed(null);
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

    public void OnAfterDraw(AbstractCard card)
    {
        cardsDrawnThisTurn += 1;
        for (OnAfterCardDrawnSubscriber s : onAfterCardDrawn.GetSubscribers())
        {
            s.OnAfterCardDrawn(card);
        }

        if (card instanceof EYBCard && ((EYBCard) card).haste)
        {
            GameActions.Bottom.Add(new HasteAction((EYBCard) card));
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
