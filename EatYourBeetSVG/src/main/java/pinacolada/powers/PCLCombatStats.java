package pinacolada.powers;

import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
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
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.relics.BlueCandle;
import eatyourbeets.interfaces.subscribers.*;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.EYBPower;
import eatyourbeets.powers.GameEvent;
import eatyourbeets.utilities.FieldInfo;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardCooldown;
import pinacolada.interfaces.subscribers.*;
import pinacolada.patches.CardGlowBorderPatches;
import pinacolada.powers.affinity.AbstractPCLAffinityPower;
import pinacolada.powers.common.ResistancePower;
import pinacolada.powers.common.VitalityPower;
import pinacolada.relics.PCLRelic;
import pinacolada.resources.GR;
import pinacolada.ui.combat.PCLAffinitySystem;
import pinacolada.ui.common.ControllableCardPile;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

import java.util.ArrayList;

// This class handles events specific to PCL cards
// Shared events are handled by the existing CombatStats class
public class PCLCombatStats extends EYBPower implements InvisiblePower
{
    protected static final int PRIORITY = -3000;
    private static final FieldInfo<ArrayList<GameEvent<?>>> _eventsGetter = PCLJUtils.GetField("events", CombatStats.class);
    public static final String POWER_ID = GR.PCL.CreateID(CombatStats.class.getSimpleName());

    public static final ArrayList<GameEvent<?>> events = _eventsGetter.Get(null);
    public static final PCLAffinitySystem MatchingSystem = new PCLAffinitySystem();

    public static final GameEvent<OnAfterlifeSubscriber> onAfterlife = RegisterEvent(new GameEvent<>());
    public static final GameEvent<OnCardMovedSubscriber> onCardMoved = RegisterEvent(new GameEvent<>());
    public static final GameEvent<OnCooldownTriggeredSubscriber> onCooldownTriggered = RegisterEvent(new GameEvent<>());
    public static final GameEvent<OnCostChangedSubscriber> onCostChanged = RegisterEvent(new GameEvent<>());
    public static final GameEvent<OnDamageActionSubscriber> onDamageAction = RegisterEvent(new GameEvent<>());
    public static final GameEvent<OnDamageOverrideSubscriber> onDamageOverride = RegisterEvent(new GameEvent<>());
    public static final GameEvent<OnGainAffinitySubscriber> onGainAffinity = RegisterEvent(new GameEvent<>());
    public static final GameEvent<OnGainTempHPSubscriber> onGainTempHP = RegisterEvent(new GameEvent<>());
    public static final GameEvent<OnGainTriggerablePowerBonusSubscriber> onGainTriggerablePowerBonus = RegisterEvent(new GameEvent<>());
    public static final GameEvent<OnMatchBonusSubscriber> onMatchBonus = RegisterEvent(new GameEvent<>());
    public static final GameEvent<OnOrbApplyFocusSubscriber> onOrbApplyFocus = RegisterEvent(new GameEvent<>());
    public static final GameEvent<OnOrbApplyLockOnSubscriber> onOrbApplyLockOn = RegisterEvent(new GameEvent<>());
    public static final GameEvent<OnPCLClickablePowerUsed> onPCLClickablePowerUsed = RegisterEvent(new GameEvent<>());
    public static final GameEvent<OnPurgeSubscriber> onPurge = RegisterEvent(new GameEvent<>());
    public static final GameEvent<OnReloadPostDiscardSubscriber> onReloadPostDiscard = RegisterEvent(new GameEvent<>());
    public static final GameEvent<OnReloadPreDiscardSubscriber> onReloadPreDiscard = RegisterEvent(new GameEvent<>());
    public static final GameEvent<OnSpendEnergySubscriber> onSpendEnergy = RegisterEvent(new GameEvent<>());
    public static final GameEvent<OnTagChangedSubscriber> onTagChanged = RegisterEvent(new GameEvent<>());
    public static final GameEvent<OnTrySpendAffinitySubscriber> onTrySpendAffinity = RegisterEvent(new GameEvent<>());

    public static final GameEvent<OnAfterCardDiscardedSubscriber> onAfterCardDiscarded = CombatStats.onAfterCardDiscarded;
    public static final GameEvent<OnAfterCardDrawnSubscriber> onAfterCardDrawn = CombatStats.onAfterCardDrawn;
    public static final GameEvent<OnAfterCardExhaustedSubscriber> onAfterCardExhausted = CombatStats.onAfterCardExhausted;
    public static final GameEvent<OnAfterCardPlayedSubscriber> onAfterCardPlayed = CombatStats.onAfterCardPlayed;
    public static final GameEvent<OnAfterDeathSubscriber> onAfterDeath = CombatStats.onAfterDeath;
    public static final GameEvent<OnApplyPowerSubscriber> onApplyPower = CombatStats.onApplyPower;
    public static final GameEvent<OnAttackSubscriber> onAttack = CombatStats.onAttack;
    public static final GameEvent<OnBattleEndSubscriber> onBattleEnd = CombatStats.onBattleEnd;
    public static final GameEvent<OnBattleStartSubscriber> onBattleStart = CombatStats.onBattleStart;
    public static final GameEvent<OnBeforeLoseBlockSubscriber> onBeforeLoseBlock = CombatStats.onBeforeLoseBlock;
    public static final GameEvent<OnBlockBrokenSubscriber> onBlockBroken = CombatStats.onBlockBroken;
    public static final GameEvent<OnBlockGainedSubscriber> onBlockGained = CombatStats.onBlockGained;
    public static final GameEvent<OnCardCreatedSubscriber> onCardCreated = CombatStats.onCardCreated;
    public static final GameEvent<OnCardResetSubscriber> onCardReset = CombatStats.onCardReset;
    public static final GameEvent<OnChannelOrbSubscriber> onChannelOrb = CombatStats.onChannelOrb;
    public static final GameEvent<OnClickablePowerUsedSubscriber> onClickablePowerUsed = CombatStats.onClickablePowerUsed;
    public static final GameEvent<OnEndOfTurnSubscriber> onEndOfTurn = CombatStats.onEndOfTurn;
    public static final GameEvent<OnEnemyDyingSubscriber> onEnemyDying = CombatStats.onEnemyDying;
    public static final GameEvent<OnEvokeOrbSubscriber> onEvokeOrb = CombatStats.onEvokeOrb;
    public static final GameEvent<OnHealthBarUpdatedSubscriber> onHealthBarUpdated = CombatStats.onHealthBarUpdated;
    public static final GameEvent<OnLoseHPSubscriber> onLoseHP = CombatStats.onLoseHP;
    public static final GameEvent<OnLosingHPSubscriber> onLosingHP = CombatStats.onLosingHP;
    public static final GameEvent<OnModifyDebuffSubscriber> onModifyDebuff = CombatStats.onModifyDebuff;
    public static final GameEvent<OnOrbPassiveEffectSubscriber> onOrbPassiveEffect = CombatStats.onOrbPassiveEffect;
    public static final GameEvent<OnPhaseChangedSubscriber> onPhaseChanged = CombatStats.onPhaseChanged;
    public static final GameEvent<OnRawDamageReceivedSubscriber> onRawDamageReceived = CombatStats.onRawDamageReceived;
    public static final GameEvent<OnShuffleSubscriber> onShuffle = CombatStats.onShuffle;
    public static final GameEvent<OnStanceChangedSubscriber> onStanceChanged = CombatStats.onStanceChanged;
    public static final GameEvent<OnStartOfTurnPostDrawSubscriber> onStartOfTurnPostDraw = CombatStats.onStartOfTurnPostDraw;
    public static final GameEvent<OnStartOfTurnSubscriber> onStartOfTurn = CombatStats.onStartOfTurn;
    public static final GameEvent<OnStatsClearedSubscriber> onStatsCleared = CombatStats.onStatsCleared;
    public static final GameEvent<OnSynergyBonusSubscriber> onSynergyBonus = CombatStats.onSynergyBonus;
    public static final GameEvent<OnSynergyCheckSubscriber> onSynergyCheck = CombatStats.onSynergyCheck;
    public static final GameEvent<OnSynergySubscriber> onSynergy = CombatStats.onSynergy;
    public static final GameEvent<OnTryUsingCardSubscriber> onTryUsingCard = CombatStats.onTryUsingCard;

    public static final ControllableCardPile ControlPile = new ControllableCardPile();

    private static final ArrayList<AbstractGameAction> cachedActions = new ArrayList<>();

    protected static <T> GameEvent<T> RegisterEvent(GameEvent<T> event) {
        events.add(event);
        return event;
    }

    protected PCLCombatStats()
    {
        super((AbstractCreature)null, POWER_ID);
        this.priority = PRIORITY;
    }

    public static AbstractPlayer RefreshPlayer()
    {
        PCLCard.rng = PCLPower.rng = PCLRelic.rng = AbstractDungeon.cardRandomRng;
        return PCLCard.player = PCLPower.player = PCLRelic.player = AbstractDungeon.player;
    }

    private static void ClearPCLStats()
    {
        // Other stuff is already handled in CombatStats
        RefreshPlayer();
        PCLJUtils.LogInfo(CombatStats.class, "Clearing PCL Player Stats");
        ControlPile.Clear();

        CardGlowBorderPatches.overrideColor = null;
        PCLCombatStats.MatchingSystem.Initialize();
        PCLCombatStats.MatchingSystem.SetLastCardPlayed(null);
    }

    public static void RefreshPCL()
    {
        RefreshPlayer();
    }

    public static void OnStartup()
    {
        RefreshPCL();
        ClearPCLStats();
    }

    public static void OnGameStart()
    {
        ClearPCLStats();

        GR.PCL.Dungeon.Reset();
    }

    public static void OnStartOver()
    {
        ClearPCLStats();

        GR.PCL.Dungeon.Reset();
    }

    public static void OnAfterDeath()
    {
        ClearPCLStats();
    }

    public static float OnDamageOverride(AbstractCreature target, DamageInfo.DamageType type, float damage, AbstractCard card)
    {
        for (OnDamageOverrideSubscriber s : onDamageOverride.GetSubscribers())
        {
            damage = s.OnDamageOverride(target, type, damage, card);
        }

        return damage;
    }

    public static void OnDamageAction(AbstractGameAction action, AbstractCreature target, DamageInfo info, AbstractGameAction.AttackEffect effect)
    {
        for (OnDamageActionSubscriber s : onDamageAction.GetSubscribers())
        {
            s.OnDamageAction(action, target, info, effect);
        }
    }

    public static void OnCardMoved(AbstractCard card, CardGroup source, CardGroup destination)
    {
        for (OnCardMovedSubscriber s : onCardMoved.GetSubscribers())
        {
            s.OnCardMoved(card, source, destination);
        }
    }

    public static void OnPurge(AbstractCard card, CardGroup source)
    {
        for (OnPurgeSubscriber s : onPurge.GetSubscribers())
        {
            s.OnPurge(card, source);
        }
    }

    public static int OnSpendEnergy(int amount)
    {
        int energySpent = amount;
        for (OnSpendEnergySubscriber s : onSpendEnergy.GetSubscribers())
        {
            energySpent = s.OnSpendEnergy(amount);
        }

        return energySpent;
    }

    public static void OnAfterlife(AbstractCard playedCard, AbstractCard fuelCard)
    {
        for (OnAfterlifeSubscriber s : onAfterlife.GetSubscribers())
        {
            s.OnAfterlife(playedCard, fuelCard);
        }
    }

    public static void OnReloadPostDiscard(ArrayList<AbstractCard> cards)
    {
        for (OnReloadPostDiscardSubscriber s : onReloadPostDiscard.GetSubscribers())
        {
            s.OnReloadPostDiscard(cards);
        }
    }

    public static ArrayList<AbstractCard> OnReloadPreDiscard(AbstractCard card)
    {
        ArrayList<AbstractCard> cards = new ArrayList<>();
        for (OnReloadPreDiscardSubscriber s : onReloadPreDiscard.GetSubscribers())
        {
            AbstractCard newCard = s.OnReloadPreDiscard(card);
            if (newCard != null) {
                cards.add(newCard);
            }
        }

        return cards;
    }

    public static boolean OnClickablePowerUsed(PCLClickablePower power, AbstractMonster target)
    {
        boolean shouldPayCost = true;
        for (OnPCLClickablePowerUsed s : onPCLClickablePowerUsed.GetSubscribers())
        {
            shouldPayCost = shouldPayCost & s.OnClickablePowerUsed(power, target);
        }
        return shouldPayCost;
    }

    public static void OnBattleStart()
    {
        RefreshPCL();
    }

    public static void OnBattleEnd()
    {
        ClearPCLStats();
    }

    public static boolean OnCooldownTriggered(AbstractCard card, PCLCardCooldown cooldown)
    {
        boolean canProgress = true;
        for (OnCooldownTriggeredSubscriber s : onCooldownTriggered.GetSubscribers())
        {
            canProgress = canProgress & s.OnCooldownTriggered(card, cooldown);
        }
        return canProgress;
    }

    public static void OnMatchBonus(AbstractCard card, PCLAffinity affinity)
    {
        for (OnMatchBonusSubscriber s : onMatchBonus.GetSubscribers())
        {
            s.OnMatchBonus(card, affinity);
        }
    }

    public static void OnUsingCard(PCLCard card, AbstractPlayer p, AbstractMonster m)
    {
        if (card == null)
        {
            throw new RuntimeException("Card played is null");
        }

        final CardUseInfo info = new CardUseInfo(card);

        card.OnUse(p, m, info);

        if (info.IsSynergizing)
        {
            CombatStats.OnSynergy(card);
        }

        final ArrayList<AbstractGameAction> actions = PCLActions.GetActions();

        cachedActions.clear();
        cachedActions.addAll(actions);

        actions.clear();
        card.OnLateUse(p, m, info);

        if (info.IsSynergizing)
        {
            MatchingSystem.OnSynergy(card);
        }
        else {
            MatchingSystem.OnNotSynergy(card);
        }

        if (actions.isEmpty())
        {
            actions.addAll(cachedActions);
        }
        else for (int i = 0; i < cachedActions.size(); i++)
        {
            PCLActions.Top.Add(cachedActions.get(cachedActions.size() - 1 - i));
        }

        for (AbstractPCLAffinityPower po : PCLCombatStats.MatchingSystem.Powers) {
            po.OnUsingCard(card, p, m);
        }

        if (card.affinities != null && card.cardData.CanGrantAffinity) {
            MatchingSystem.AddAffinities(card.affinities);
        }

        PCLActions.Bottom.Add(new UseCardAction(card, m));
        if (!card.dontTriggerOnUseCard) {
            p.hand.triggerOnOtherCardPlayed(card);
        }

        p.hand.removeCard(card);
        p.cardInUse = card;
        card.target_x = (float)(Settings.WIDTH / 2);
        card.target_y = (float)(Settings.HEIGHT / 2);
        // TODO make a proper subscriber for this
        if (card.costForTurn > 0 && !card.freeToPlay() && !card.isInAutoplay && (!p.hasPower(CorruptionPower.POWER_ID) || card.type != AbstractCard.CardType.SKILL)) {
            p.energy.use(card.costForTurn);
        }

        if (!p.hand.canUseAnyCard() && !p.endTurnQueued) {
            AbstractDungeon.overlayMenu.endTurnButton.isGlowing = true;
        }
    }

    public static int OnGainTriggerablePowerBonus(String powerID, PCLTriggerablePower.Type gainType, int amount)
    {
        for (OnGainTriggerablePowerBonusSubscriber s : onGainTriggerablePowerBonus.GetSubscribers())
        {
            amount = s.OnGainTriggerablePowerBonus(powerID, gainType, amount);
        }
        return amount;
    }

    public static int OnGainTempHP(int amount)
    {
        if (onGainTempHP.Count() > 0)
        {
            for (OnGainTempHPSubscriber s : onGainTempHP.GetSubscribers())
            {
                amount = s.OnGainTempHP(amount);
            }
        }
        return amount;
    }

    public static int OnGainAffinity(PCLAffinity affinity, int amount, boolean isActuallyGaining)
    {
        for (OnGainAffinitySubscriber s : onGainAffinity.GetSubscribers())
        {
            amount = s.OnGainAffinity(affinity, amount, isActuallyGaining);
        }

        return amount;
    }

    public static int OnTrySpendAffinity(PCLAffinity affinity, int amount, boolean isActuallySpending)
    {
        for (OnTrySpendAffinitySubscriber s : onTrySpendAffinity.GetSubscribers())
        {
            amount = s.OnTrySpendAffinity(affinity, amount, isActuallySpending);
        }

        return amount;
    }

    public static boolean OnTryUsingCard(AbstractCard card, AbstractPlayer p, AbstractMonster m, boolean canPlay)
    {
        if (CombatStats.UnplayableCards().contains(card.uuid))
        {
            return false;
        }

        if (PCLGameUtilities.HasRelicEffect(BlueCandle.ID) && card.type == AbstractCard.CardType.CURSE) {
            canPlay = true;
        }

        for (OnTryUsingCardSubscriber s : onTryUsingCard.GetSubscribers())
        {
            canPlay &= s.OnTryUsingCard(card, p, m, canPlay);
        }
        
        return canPlay;
    }

    public static void OnCostChanged(AbstractCard card, int originalCost, int newCost)
    {
        for (OnCostChangedSubscriber s : onCostChanged.GetSubscribers())
        {
            s.OnCostChanged(card, originalCost, newCost);
        }
    }

    public static void OnTagChanged(AbstractCard card, AbstractCard.CardTags tag, boolean value)
    {
        for (OnTagChangedSubscriber s : onTagChanged.GetSubscribers())
        {
            s.OnTagChanged(card, tag, value);
        }
    }

    public static void OnOrbApplyFocus(AbstractOrb orb)
    {
        for (OnOrbApplyFocusSubscriber s : onOrbApplyFocus.GetSubscribers())
        {
            s.OnApplyFocus(orb);
        }
    }

    public static int OnOrbApplyLockOn(int retVal, AbstractCreature target, int dmg)
    {
        for (OnOrbApplyLockOnSubscriber s : onOrbApplyLockOn.GetSubscribers())
        {
            retVal = s.OnOrbApplyLockOn(retVal, target, dmg);
        }
        return retVal;
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
        else if (ResistancePower.POWER_ID.equals(power.ID))
        {
            power.priority = -2097;
        }
        else if (VitalityPower.POWER_ID.equals(power.ID))
        {
            power.priority = -2096;
        }
    }

    public static void OnPlayCardPostActions(AbstractCard card)
    {
        PCLCombatStats.MatchingSystem.TrySynergize(card);
    }

    public static void OnAfterCardPlayedPostActions(AbstractCard card)
    {
        if (card.purgeOnUse || card.hasTag(GR.Enums.CardTags.PURGE)) {
            OnPurge(card, player.hand);
        }
    }

    public static void OnAfterUseCardPostActions(AbstractCard card)
    {
        PCLCombatStats.MatchingSystem.SetLastCardPlayed(card);
        player.hand.glowCheck();
    }

    public static void AtEndOfTurn()
    {
        PCLCombatStats.MatchingSystem.SetLastCardPlayed(null);
    }

    public static void AtStartOfTurn()
    {
        PCLCombatStats.MatchingSystem.OnStartOfTurn();
    }

    public static void OnDeath()
    {
        ClearPCLStats();
    }

    public static void OnVictory()
    {
        GR.PCL.Dungeon.UpdateLongestMatchCombo(MatchingSystem.AffinityMeter.GetLongestMatchCombo());
        ClearPCLStats();
    }
}
