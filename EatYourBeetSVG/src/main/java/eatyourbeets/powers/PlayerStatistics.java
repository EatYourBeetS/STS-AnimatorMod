package eatyourbeets.powers;

import basemod.DevConsole;
import basemod.abstracts.CustomSavable;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower;
import com.google.gson.annotations.Expose;
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
import eatyourbeets.powers.common.AgilityPower;
import eatyourbeets.powers.common.ForcePower;
import eatyourbeets.powers.common.IntellectPower;
import eatyourbeets.powers.common.TemporaryBiasPower;
import eatyourbeets.powers.unnamed.ResonancePower;
import eatyourbeets.resources.AbstractResources;
import eatyourbeets.ui.Void;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.utilities.Utilities;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.utilities.RandomizedList;
import patches.CardGlowBorderPatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class PlayerStatistics extends AnimatorPower implements InvisiblePower, CustomSavable<PlayerStatistics.SaveData>
{
    public static final String POWER_ID = CreateFullID(PlayerStatistics.class.getSimpleName());

    public static final PlayerStatistics Instance = new PlayerStatistics();

    public static final HashSet<String> limitedEffects = new HashSet<>();
    public static final HashSet<String> semiLimitedEffects = new HashSet<>();

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
    public static final GameEvent<OnStartOfTurnPostDrawSubscriber> onStartOfTurnPostDraw = new GameEvent<>();
    public static final GameEvent<OnCostRefreshSubscriber> onCostRefresh = new GameEvent<>();

    public static boolean LoadingPlayerSave;
    public static SaveData SaveData = new SaveData();
    public static Void Void = new Void();

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

        limitedEffects.clear();
        semiLimitedEffects.clear();

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
        onStartOfTurnPostDraw.Clear();

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
        SaveData = new SaveData();
    }

    public static void OnStartOver()
    {
        ClearStats();
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

    public static void UnlockAllKeys()
    {
        if (!Settings.isFinalActAvailable)
        {
            Settings.isFinalActAvailable = true;
            CardCrawlGame.playerPref.putBoolean(AbstractPlayer.PlayerClass.IRONCLAD.name() + "_WIN", true);
            CardCrawlGame.playerPref.putBoolean(AbstractPlayer.PlayerClass.THE_SILENT.name() + "_WIN", true);
            CardCrawlGame.playerPref.putBoolean(AbstractPlayer.PlayerClass.DEFECT.name() + "_WIN", true);

            if (UnlockTracker.isAchievementUnlocked("RUBY_PLUS"))
            {
                UnlockTracker.unlockAchievement("RUBY_PLUS");
            }

            if (UnlockTracker.isAchievementUnlocked("EMERALD_PLUS"))
            {
                UnlockTracker.unlockAchievement("EMERALD_PLUS");
            }

            if (UnlockTracker.isAchievementUnlocked("SAPPHIRE_PLUS"))
            {
                UnlockTracker.unlockAchievement("SAPPHIRE_PLUS");
            }
        }
    }

    public static int GetAscensionLevel()
    {
        if (AbstractDungeon.isAscensionMode)
        {
            return Math.max(0, Math.min(20, AbstractDungeon.ascensionLevel));
        }

        return 0;
    }

    public static int GetActualAscensionLevel()
    {
        if (AbstractDungeon.isAscensionMode)
        {
            return AbstractDungeon.ascensionLevel;
        }

        return 0;
    }

    public static void OnCostRefresh(AbstractCard card)
    {
        OnCostRefreshSubscriber c = Utilities.SafeCast(card, OnCostRefreshSubscriber.class);
        if (c != null)
        {
            c.OnCostRefresh(card);
        }

        for (OnCostRefreshSubscriber s : onCostRefresh.GetSubscribers())
        {
            s.OnCostRefresh(card);
        }
    }

    public static <T> T  GetPower(AbstractCreature owner, Class<T> powerType)
    {
        for (AbstractPower power : owner.powers)
        {
            if (powerType.isInstance(power))
            {
                return powerType.cast(power);
            }
        }

        return null;
    }

    public static boolean IsAttacking(AbstractMonster.Intent intent)
    {
        return (intent == AbstractMonster.Intent.ATTACK_DEBUFF || intent == AbstractMonster.Intent.ATTACK_BUFF ||
                intent == AbstractMonster.Intent.ATTACK_DEFEND || intent == AbstractMonster.Intent.ATTACK);
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
            OnBattleStartSubscriber s = Utilities.SafeCast(c, OnBattleStartSubscriber.class);
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

    public static AbstractRoom GetCurrentRoom()
    {
        MapRoomNode mapNode = AbstractDungeon.currMapNode;
        if (mapNode == null)
        {
            return null;
        }
        else
        {
            return mapNode.getRoom();
        }
    }

    public static boolean InBattle()
    {
        AbstractRoom room = GetCurrentRoom();
        if (room != null && !room.isBattleOver)
        {
            return room.phase == AbstractRoom.RoomPhase.COMBAT || (room.monsters != null && !room.monsters.areMonstersBasicallyDead());
        }

        return false;
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
            case "common:ForcePower"://ForcePower.POWER_ID:
            {
                power.priority = -2100;
                break;
            }
            case StrengthPower.POWER_ID:
            {
                power.priority = -2099;
                break;
            }
            case "common:AgilityPower"://AgilityPower.POWER_ID:
            {
                power.priority = -2098;
                break;
            }
            case DexterityPower.POWER_ID:
            {
                power.priority = -2097;
                break;
            }
            case "common:IntellectPower"://IntellectPower.POWER_ID:
            {
                power.priority = -2096;
                break;
            }
            case FocusPower.POWER_ID:
            {
                power.priority = -2095;
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

        AnimatorCard c = Utilities.SafeCast(card, AnimatorCard.class);
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

        semiLimitedEffects.clear();

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

    public static boolean HasActivatedLimited(String effectID)
    {
        return limitedEffects.contains(effectID);
    }

    public static boolean TryActivateLimited(String effectID)
    {
        return limitedEffects.add(effectID);
    }

    public static boolean HasActivatedSemiLimited(String effectID)
    {
        return semiLimitedEffects.contains(effectID);
    }

    public static boolean TryActivateSemiLimited(String effectID)
    {
        return semiLimitedEffects.add(effectID);
    }

    public static AbstractMonster GetRandomEnemy(boolean aliveOnly)
    {
        return Utilities.GetRandomElement(GetCurrentEnemies(aliveOnly));
    }

    public static AbstractCreature GetRandomCharacter(boolean aliveOnly)
    {
        RandomizedList<AbstractMonster> enemies = new RandomizedList<>(GetCurrentEnemies(aliveOnly));

        AbstractCreature result = enemies.Retrieve(AbstractDungeon.cardRandomRng, false);
        if (result == null)
        {
            return AbstractDungeon.player;
        }
        else
        {
            return result;
        }
    }

    public static ArrayList<AbstractCreature> GetAllCharacters(boolean aliveOnly)
    {
        ArrayList<AbstractCreature> characters = new ArrayList<>();
        AbstractRoom room = GetCurrentRoom();
        if (room != null && room.monsters != null)
        {
            for (AbstractMonster m : room.monsters.monsters)
            {
                //logger.info("ENEMY: " + m.name + ", DeadOrEscaped: " + m.isDeadOrEscaped() + ", Dying: " + m.isDying);
                if (!aliveOnly || (!m.isDeadOrEscaped() && !m.isDying))
                {
                    characters.add(m);
                }
            }
        }

        characters.add(AbstractDungeon.player);

        return characters;
    }

    public static ArrayList<AbstractMonster> GetCurrentEnemies(boolean aliveOnly)
    {
        ArrayList<AbstractMonster> monsters = new ArrayList<>();
        AbstractRoom room = GetCurrentRoom();
        if (room != null && room.monsters != null)
        {
            for (AbstractMonster m : room.monsters.monsters)
            {
                //logger.info("ENEMY: " + m.name + ", DeadOrEscaped: " + m.isDeadOrEscaped() + ", Dying: " + m.isDying);
                if (!aliveOnly || (!m.isDeadOrEscaped() && !m.isDying))
                {
                    monsters.add(m);
                }
            }
        }

        return monsters;
    }

    public static int GetUniqueOrbsCount()
    {
        ArrayList<String> orbList = new ArrayList<>();

        for (AbstractOrb o : AbstractDungeon.player.orbs)
        {
            if (o.ID != null && !o.ID.equals(EmptyOrbSlot.ORB_ID) && !orbList.contains(o.ID))
            {
                orbList.add(o.ID);
            }
        }

        return orbList.size();
    }

    public static int GetDexterity()
    {
        return GetDexterity(AbstractDungeon.player);
    }

    public static int GetDexterity(AbstractCreature creature)
    {
        DexterityPower power = (DexterityPower) creature.getPower(DexterityPower.POWER_ID);
        if (power != null)
        {
            return power.amount;
        }

        return 0;
    }

    public static int GetResonance()
    {
        return GetResonance(AbstractDungeon.player);
    }

    public static int GetResonance(AbstractCreature creature)
    {
        ResonancePower power = (ResonancePower) creature.getPower(ResonancePower.POWER_ID);
        if (power != null)
        {
            return power.amount;
        }

        return 0;
    }

    public static int GetStrength()
    {
        return GetStrength(AbstractDungeon.player);
    }

    public static int GetStrength(AbstractCreature creature)
    {
        StrengthPower power = (StrengthPower) creature.getPower(StrengthPower.POWER_ID);
        if (power != null)
        {
            return power.amount;
        }

        return 0;
    }

    public static int GetFocus()
    {
        return GetFocus(AbstractDungeon.player);
    }

    public static int GetFocus(AbstractCreature creature)
    {
        FocusPower power = (FocusPower) creature.getPower(FocusPower.POWER_ID);
        if (power != null)
        {
            return power.amount;
        }

        return 0;
    }

    public static void LoseTemporaryStrength(AbstractCreature source, AbstractCreature target, int amount)
    {
        GameActionsHelper.SetOrder(GameActionsHelper.Order.Top);

        if (UseArtifact(target))
        {
            GameActionsHelper.ApplyPower(source, target, new StrengthPower(target, -amount), -amount);
            GameActionsHelper.ApplyPowerSilently(source, target, new GainStrengthPower(target, amount), amount);
        }

        GameActionsHelper.ResetOrder();
    }

    public static void GainTemporaryStrength(AbstractCreature source, AbstractCreature target, int amount)
    {
        GameActionsHelper.SetOrder(GameActionsHelper.Order.Top);

        if (UseArtifact(target))
        {
            GameActionsHelper.ApplyPowerSilently(source, target, new LoseStrengthPower(target, amount), amount);
        }

        GameActionsHelper.ApplyPower(source, target, new StrengthPower(target, amount), amount);

        GameActionsHelper.ResetOrder();
    }

    public static void ApplyTemporaryFocus(AbstractCreature source, AbstractCreature target, int amount)
    {
        GameActionsHelper.SetOrder(GameActionsHelper.Order.Top);

        if (UseArtifact(target))
        {
            GameActionsHelper.ApplyPowerSilently(source, target, new TemporaryBiasPower(target, amount), amount);
        }

        GameActionsHelper.ApplyPower(source, target, new FocusPower(target, amount), amount);

        GameActionsHelper.ResetOrder();
    }

    public static void ApplyTemporaryDexterity(AbstractCreature source, AbstractCreature target, int amount)
    {
        GameActionsHelper.SetOrder(GameActionsHelper.Order.Top);

        if (UseArtifact(target))
        {
            GameActionsHelper.ApplyPowerSilently(source, target, new LoseDexterityPower(target, amount), amount);
        }

        GameActionsHelper.ApplyPower(source, target, new DexterityPower(target, amount), amount);

        GameActionsHelper.ResetOrder();
    }

    public static boolean UseArtifact(AbstractCreature target)
    {
        ArtifactPower artifact = Utilities.SafeCast(target.getPower(ArtifactPower.POWER_ID), ArtifactPower.class);
        if (artifact != null)
        {
            AbstractDungeon.actionManager.addToTop(new TextAboveCreatureAction(target, ApplyPowerAction.TEXT[0]));
            CardCrawlGame.sound.play("NULLIFY_SFX");
            artifact.flashWithoutSound();
            artifact.onSpecificTrigger();

            return false;
        }
        else
        {
            return true;
        }
    }

    public static void UsePenNib()
    {
        AbstractPlayer p = AbstractDungeon.player;
        if (p.hasPower(PenNibPower.POWER_ID))
        {
            GameActionsHelper.AddToBottom(new ReducePowerAction(p, p, PenNibPower.POWER_ID, 1));
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
