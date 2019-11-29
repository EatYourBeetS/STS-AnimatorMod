package eatyourbeets.powers;

import basemod.DevConsole;
import basemod.abstracts.CustomSavable;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower;
import com.google.gson.annotations.Expose;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.utility.TextAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import eatyourbeets.interfaces.*;
import eatyourbeets.powers.common.TemporaryBiasPower;
import eatyourbeets.powers.unnamed.ResonancePower;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.ui.Void;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.utilities.JavaUtilities;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.utilities.RandomizedList;
import patches.CardGlowBorderPatch;

import java.util.ArrayList;

public class PlayerStatistics extends AnimatorPower implements InvisiblePower, CustomSavable<PlayerStatistics.SaveData>
{
    public static final String POWER_ID = CreateFullID(PlayerStatistics.class.getSimpleName());

    public static final PlayerStatistics Instance = new PlayerStatistics();

    public static final GameEvent<OnEnemyDyingSubscriber> onEnemyDying  = new GameEvent<>();
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

    public static boolean LoadingPlayerSave;
    public static SaveData SaveData = new SaveData();
    public static Void Void = new Void();

    private static final EffectHistory effectHistory = new EffectHistory();
    private static GameActionManager.Phase currentPhase;
    private static int turnDamageMultiplier = 0;
    private static int turnCount = 0;
    private static int cardsDrawnThisTurn = 0;
    private static int cardsExhaustedThisTurn = 0;
    private static int orbsEvokedThisTurn = 0;
    private static int synergiesThisTurn = 0;

    protected PlayerStatistics()
    {
        super(null, POWER_ID);
    }

    private static void ClearStats()
    {
        logger.info("Clearing Player Stats");

        CardGlowBorderPatch.overrideColor = null;
        AnimatorCard.SetLastCardPlayed(null);
        turnDamageMultiplier = 0;
        turnCount = 0;
        cardsDrawnThisTurn = 0;
        cardsExhaustedThisTurn = 0;
        orbsEvokedThisTurn = 0;
        synergiesThisTurn = 0;
        currentPhase = null;

        EffectHistory.limitedEffects.clear();
        EffectHistory.semiLimitedEffects.clear();

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

        Void.Initialize(true);
    }

    public static void EnsurePowerIsApplied()
    {
        if (!AbstractDungeon.player.powers.contains(Instance))
        {
            logger.info("Applied PlayerStatistics");

            AbstractDungeon.player.powers.add(Instance);
        }
    }

    public static void OnGameStart()
    {
        ClearStats();
        onBattleStart.Clear();
        onBattleEnd.Clear();
        SaveData = new SaveData();
    }

    public static void OnStartOver()
    {
        ClearStats();
        onBattleStart.Clear();
        onBattleEnd.Clear();
        SaveData = new SaveData();
        DevConsole.enabled = true;
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

    public void OnBattleStart()
    {
        ClearStats();

        AbstractPlayer p = AbstractDungeon.player;
        ArrayList<AbstractCard> cards = new ArrayList<>(p.drawPile.group);
        cards.addAll(p.hand.group);
        cards.addAll(p.discardPile.group);
        cards.addAll(p.exhaustPile.group);

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

    public static void AddTurnDamageMultiplier(int value)
    {
        turnDamageMultiplier += value;
    }

    public static int getCardsExhaustedThisTurn()
    {
        return cardsExhaustedThisTurn;
    }

    public static int getSynergiesThisTurn()
    {
        return synergiesThisTurn;
    }

    public static int getCardsDrawnThisTurn()
    {
        return cardsDrawnThisTurn;
    }

    public static int getOrbsEvokedThisTurn()
    {
        return orbsEvokedThisTurn;
    }

    public static int getTurnCount()
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
            orbsEvokedThisTurn += 1;
        }
    }

    @Override
    public void onAfterCardPlayed(AbstractCard usedCard)
    {
        super.onAfterCardPlayed(usedCard);

        for (OnAfterCardPlayedSubscriber p : onAfterCardPlayed.GetSubscribers())
        {
            p.OnAfterCardPlayed(usedCard);
        }
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

        switch (power.ID)
        {
            case "EYB:ForcePower"://ForcePower.POWER_ID:
            {
                power.priority = -2100;
                break;
            }
            case StrengthPower.POWER_ID:
            {
                power.priority = -2099;
                break;
            }
            case "EYB:AgilityPower"://AgilityPower.POWER_ID:
            {
                power.priority = -2098;
                break;
            }
            case DexterityPower.POWER_ID:
            {
                power.priority = -2097;
                break;
            }
            case "EYB:IntellectPower"://IntellectPower.POWER_ID:
            {
                power.priority = -2096;
                break;
            }
            case FocusPower.POWER_ID:
            {
                power.priority = -2096;
                break;
            }
        }

        for (OnApplyPowerSubscriber p : onApplyPower.GetSubscribers())
        {
            p.OnApplyPower(power, target, source);
        }
    }

    @Override
    public void onAfterUseCard(AbstractCard card, UseCardAction action)
    {
        super.onAfterUseCard(card, action);

        AnimatorCard c = JavaUtilities.SafeCast(card, AnimatorCard.class);
        if (c != null && c.HasActiveSynergy())
        {
            synergiesThisTurn += 1;

            for (OnSynergySubscriber s : onSynergy.GetSubscribers())
            {
                s.OnSynergy(c);
            }
        }

        AnimatorCard.SetLastCardPlayed(card);
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

    //    @Override
//    public void atEndOfRound()
//    {
//        super.atEndOfRound();
//
//        turnDamageMultiplier = 0;
//        cardsExhaustedThisTurn = 0;
//        synergiesThisTurn = 0;
//        cardsDrawnThisTurn = 0;
//    }

    @Override
    public void atEndOfTurn(boolean isPlayer)
    {
        super.atEndOfTurn(isPlayer);

        for (OnEndOfTurnSubscriber s : onEndOfTurn.GetSubscribers())
        {
            s.OnEndOfTurn(isPlayer);
        }

        EffectHistory.semiLimitedEffects.clear();

        turnDamageMultiplier = 0;
        cardsExhaustedThisTurn = 0;
        synergiesThisTurn = 0;
        cardsDrawnThisTurn = 0;
        orbsEvokedThisTurn = 0;

        turnCount += 1;

        AnimatorCard.SetLastCardPlayed(null);
    }

    public void OnAfterDraw(AbstractCard abstractCard)
    {
        cardsDrawnThisTurn += 1;
        for (OnAfterCardDrawnSubscriber s : onAfterCardDrawn.GetSubscribers())
        {
            s.OnAfterCardDrawn(abstractCard);
        }
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type)
    {
        if (turnDamageMultiplier != 0 && type == DamageInfo.DamageType.NORMAL)
        {
            return Math.round(damage * (1 + turnDamageMultiplier / 100f));
        }
        else
        {
            return damage;
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

    @Override
    public SaveData onSave()
    {
        if (SaveData != null)
        {
            SaveData.OnBeforeSaving();
        }

        return SaveData;
    }

    @Override
    public void onLoad(SaveData saveData)
    {
        if (saveData != null)
        {
            SaveData = saveData;
        }
        else
        {
            SaveData = new SaveData();
        }

        SaveData.ValidateFields();
    }

    public static class SaveData
    {
        public Boolean EnteredUnnamedReign = false;
        public Integer RNGCounter = 0;

        @Expose(serialize = false, deserialize = false)
        private Random rng;

        protected void OnBeforeSaving()
        {
            if (rng != null)
            {
                RNGCounter = rng.counter;
            }
            else
            {
                RNGCounter = 0;
            }
        }

        protected void Reset()
        {

        }

        protected void ValidateFields()
        {
            if (EnteredUnnamedReign == null)
            {
                EnteredUnnamedReign = false;
            }

            if (RNGCounter == null)
            {
                RNGCounter = 0;
            }
        }

        public Random GetRNG()
        {
            if (rng == null)
            {
                rng = new Random(Settings.seed);
                rng.setCounter(RNGCounter);
            }

            return rng;
        }
    }


}
