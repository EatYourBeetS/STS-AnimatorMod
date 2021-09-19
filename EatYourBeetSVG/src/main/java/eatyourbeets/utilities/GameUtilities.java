package eatyourbeets.utilities;

import basemod.abstracts.CustomCard;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.SoulboundField;
import com.evacipated.cardcrawl.mod.stslib.patches.core.AbstractCreature.TempHPField;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.TextAboveCreatureAction;
import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.*;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.ChemicalX;
import com.megacrit.cardcrawl.relics.PenNib;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import com.megacrit.cardcrawl.screens.stats.AchievementGrid;
import com.megacrit.cardcrawl.screens.stats.RunData;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import eatyourbeets.blights.animator.UpgradedHand;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.cards.base.EYBCardAffinities;
import eatyourbeets.cards.base.EYBCardAffinity;
import eatyourbeets.effects.SFX;
import eatyourbeets.interfaces.delegates.ActionT1;
import eatyourbeets.interfaces.delegates.ActionT2;
import eatyourbeets.interfaces.delegates.FuncT1;
import eatyourbeets.interfaces.listeners.OnTryApplyPowerListener;
import eatyourbeets.interfaces.subscribers.OnAfterCardPlayedSubscriber;
import eatyourbeets.interfaces.subscribers.OnPhaseChangedSubscriber;
import eatyourbeets.monsters.EnemyIntent;
import eatyourbeets.orbs.animator.Air;
import eatyourbeets.orbs.animator.Earth;
import eatyourbeets.orbs.animator.Fire;
import eatyourbeets.orbs.animator.Water;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.powers.affinity.AbstractAffinityPower;
import eatyourbeets.powers.replacement.TemporaryArtifactPower;
import eatyourbeets.resources.GR;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.actionManager;
import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.player;
import static eatyourbeets.resources.GR.Enums.CardTags.*;

public class GameUtilities
{
    private static final HandLayoutRefresher handLayoutRefresher = new HandLayoutRefresher();
    private static final ArrayList<PowerHelper> commonDebuffs = new ArrayList<>();
    private static final WeightedList<AbstractOrb> orbs = new WeightedList<>();

    public static void ApplyPowerInstantly(AbstractCreature target, PowerHelper powerHelper, int stacks)
    {
        ApplyPowerInstantly(TargetHelper.Source(target), powerHelper, stacks);
    }

    public static void ApplyPowerInstantly(TargetHelper targetHelper, PowerHelper powerHelper, int stacks)
    {
        for (AbstractCreature target : targetHelper.GetTargets())
        {
            final AbstractPower power = GetPower(target, powerHelper.ID);
            if (power != null)
            {
                if ((power.amount += stacks) == 0)
                {
                    target.powers.remove(power);
                }
            }
            else
            {
                target.addPower(powerHelper.Create(target, targetHelper.GetSource(), stacks));
                Collections.sort(target.powers);
            }
        }
    }

    public static void ApplyPowerInstantly(TargetHelper targetHelper, AbstractPower power, int stacks)
    {
        for (AbstractCreature target : targetHelper.GetTargets())
        {
            final AbstractPower p = GetPower(target, power.ID);
            if (p != null)
            {
                if ((p.amount += stacks) == 0)
                {
                    target.powers.remove(p);
                }
            }
            else
            {
                target.addPower(power);
                Collections.sort(target.powers);
            }
        }
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean CanApplyPower(AbstractCreature source, AbstractCreature target, AbstractPower powerToApply, AbstractGameAction action)
    {
        boolean canApply = true;
        if (target != null)
        {
            for (AbstractPower power : target.powers)
            {
                if (power instanceof OnTryApplyPowerListener)
                {
                    canApply &= ((OnTryApplyPowerListener) power).TryApplyPower(powerToApply, target, source, action);
                }
            }

            if (canApply && target != source && source != null)
            {
                for (AbstractPower power : source.powers)
                {
                    if (power instanceof OnTryApplyPowerListener)
                    {
                        canApply &= ((OnTryApplyPowerListener) power).TryApplyPower(powerToApply, target, source, action);
                    }
                }
            }
        }

        return canApply;
    }

    public static boolean CanPlayTwice(AbstractCard card)
    {
        return !card.isInAutoplay && (!card.purgeOnUse || card.hasTag(GR.Enums.CardTags.PURGE));
    }

    public static boolean CanRemoveFromDeck(AbstractCard card)
    {
        return (card.rarity != AbstractCard.CardRarity.SPECIAL) && !SoulboundField.soulbound.get(card);
    }

    public static boolean CanShowUpgrades(boolean isLibrary)
    {
        return SingleCardViewPopup.isViewingUpgrade && (player == null || isLibrary
                || AbstractDungeon.screen == AbstractDungeon.CurrentScreen.COMBAT_REWARD
                || AbstractDungeon.screen == AbstractDungeon.CurrentScreen.CARD_REWARD
                || AbstractDungeon.screen == GR.Enums.Screens.EYB_SCREEN);
    }

    public static void ClearPostCombatActions()
    {
        AbstractDungeon.actionManager.clearPostCombatActions();
    }

    public static void CopyVisualProperties(AbstractCard copy, AbstractCard original)
    {
        copy.current_y = original.current_y;
        copy.current_x = original.current_x;
        copy.target_x = original.target_x;
        copy.target_y = original.target_y;
        copy.targetDrawScale = original.targetDrawScale;
        copy.drawScale = original.drawScale;
        copy.transparency = original.transparency;
        copy.targetTransparency = original.targetTransparency;
        copy.angle = original.angle;
        copy.targetAngle = original.targetAngle;
    }

    public static CardGroup CreateCardGroup(List<AbstractCard> cards)
    {
        return CreateCardGroup(cards, CardGroup.CardGroupType.UNSPECIFIED);
    }

    public static CardGroup CreateCardGroup(List<AbstractCard> cards, CardGroup.CardGroupType type)
    {
        final CardGroup group = new CardGroup(type);
        group.group.addAll(cards);
        return group;
    }

    public static void DecreaseBlock(AbstractCard card, int amount, boolean temporary)
    {
        ModifyBlock(card, Math.max(0, card.baseBlock - amount), temporary);
    }

    public static void DecreaseDamage(AbstractCard card, int amount, boolean temporary)
    {
        ModifyDamage(card, Math.max(0, card.baseDamage - amount), temporary);
    }

    public static void DecreaseMagicNumber(AbstractCard card, int amount, boolean temporary)
    {
        ModifyMagicNumber(card, Math.max(0, card.baseMagicNumber - amount), temporary);
    }

    public static void DecreaseSecondaryValue(EYBCard card, int amount, boolean temporary)
    {
        ModifySecondaryValue(card, Math.max(0, card.baseSecondaryValue - amount), temporary);
    }

    public static CardGroup FindCardGroup(AbstractCard card, boolean includeLimbo)
    {
        if (player.hand.contains(card))
        {
            return player.hand;
        }
        else if (player.drawPile.contains(card))
        {
            return player.drawPile;
        }
        else if (player.discardPile.contains(card))
        {
            return player.discardPile;
        }
        else if (player.exhaustPile.contains(card))
        {
            return player.exhaustPile;
        }
        else if (includeLimbo && player.limbo.contains(card))
        {
            return player.limbo;
        }
        else
        {
            return null;
        }
    }

    public static void Flash(AbstractCard card, boolean superFlash)
    {
        if (superFlash)
        {
            card.superFlash();
        }
        else
        {
            card.flash();
        }
    }

    public static void Flash(AbstractCard card, Color color, boolean superFlash)
    {
        if (superFlash)
        {
            card.superFlash(color.cpy());
        }
        else
        {
            card.flash(color.cpy());
        }
    }

    public static int GetActualAscensionLevel()
    {
        return AbstractDungeon.isAscensionMode ? AbstractDungeon.ascensionLevel : 0;
    }

    public static EYBCardAffinities GetAffinities(AbstractCard card)
    {
        return card instanceof EYBCard ? ((EYBCard) card).affinities : null;
    }

    public static EYBCardAffinity GetAffinity(AbstractCard card, Affinity affinity)
    {
        final EYBCardAffinities a = GetAffinities(card);
        return a != null ? a.Get(affinity, false) : null;
    }

    public static int GetAffinityLevel(AbstractCard card, Affinity affinity, boolean useStarLevel)
    {
        final EYBCardAffinities a = GetAffinities(card);
        return a != null ? a.GetLevel(affinity, useStarLevel) : 0;
    }

    public static AbstractOrb GetFirstOrb(String orbID)
    {
        for (AbstractOrb orb : player.orbs)
        {
            if (orb != null && orbID.equals(orb.ID))
            {
                return orb;
            }
        }

        return null;
    }

    public static int GetHealthRecoverAmount(int amount)
    {
        return Math.min(amount, GameActionManager.playerHpLastTurn - player.currentHealth);
    }

    public static boolean HasArtifact(AbstractCreature creature)
    {
        return creature.hasPower(ArtifactPower.POWER_ID) || creature.hasPower(TemporaryArtifactPower.POWER_ID);
    }

    public static boolean HasRedAffinity(AbstractCard card)
    {
        return GetAffinityLevel(card, Affinity.Red, true) > 0;
    }

    public static boolean HasGreenAffinity(AbstractCard card)
    {
        return GetAffinityLevel(card, Affinity.Green, true) > 0;
    }

    public static boolean HasBlueAffinity(AbstractCard card)
    {
        return GetAffinityLevel(card, Affinity.Blue, true) > 0;
    }

    public static boolean HasOrangeAffinity(AbstractCard card)
    {
        return GetAffinityLevel(card, Affinity.Orange, true) > 0;
    }

    public static boolean HasLightAffinity(AbstractCard card)
    {
        return GetAffinityLevel(card, Affinity.Light, true) > 0;
    }

    public static boolean HasDarkAffinity(AbstractCard card)
    {
        return GetAffinityLevel(card, Affinity.Dark, true) > 0;
    }

    public static boolean HasMulticolorAffinity(AbstractCard card)
    {
        return GetAffinityLevel(card, Affinity.Star, true) > 0;
    }

    public static ArrayList<AbstractCreature> GetAllCharacters(boolean aliveOnly)
    {
        AbstractRoom room = GetCurrentRoom();
        ArrayList<AbstractCreature> characters = new ArrayList<>();
        if (room != null && room.monsters != null)
        {
            for (AbstractMonster m : room.monsters.monsters)
            {
                if (!aliveOnly || !IsDeadOrEscaped(m))
                {
                    characters.add(m);
                }
            }
        }

        if (!aliveOnly || !IsDeadOrEscaped(player))
        {
            characters.add(player);
        }

        return characters;
    }

    public static HashSet<AbstractCard> GetAllCopies(String cardID, CardGroup group)
    {
        return GetAllCopies(new HashSet<>(), cardID, group);
    }

    public static HashSet<AbstractCard> GetAllCopies(HashSet<AbstractCard> cards, String cardID, CardGroup group)
    {
        for (AbstractCard card : group.group)
        {
            if (cardID.equals(card.cardID))
            {
                cards.add(card);
            }
        }

        return cards;
    }

    public static HashSet<AbstractCard> GetAllCopies(String cardID)
    {
        HashSet<AbstractCard> cards = GetAllInBattleCopies(cardID);
        cards.addAll(GetMasterDeckCopies(cardID));

        return cards;
    }

    public static HashSet<AbstractCard> GetAllInBattleCopies(String cardID)
    {
        HashSet<AbstractCard> cards = new HashSet<>();

        if (player.cardInUse != null && player.cardInUse.cardID.equals(cardID))
        {
            cards.add(player.cardInUse);
        }

        GetAllCopies(cards, cardID, player.hand);
        GetAllCopies(cards, cardID, player.drawPile);
        GetAllCopies(cards, cardID, player.discardPile);
        GetAllCopies(cards, cardID, player.exhaustPile);
        GetAllCopies(cards, cardID, player.limbo);

        return cards;
    }

    public static HashSet<AbstractCard> GetAllInBattleInstances(UUID uuid)
    {
        final HashSet<AbstractCard> cards = new HashSet<>();

        if (player.cardInUse != null && player.cardInUse.uuid.equals(uuid))
        {
            cards.add(player.cardInUse);
        }

        GetAllInstances(cards, uuid, player.hand);
        GetAllInstances(cards, uuid, player.drawPile);
        GetAllInstances(cards, uuid, player.discardPile);
        GetAllInstances(cards, uuid, player.exhaustPile);
        GetAllInstances(cards, uuid, player.limbo);

        return cards;
    }

    public static HashSet<AbstractCard> GetAllInstances(UUID uuid)
    {
        final HashSet<AbstractCard> cards = GetAllInBattleInstances(uuid);
        final AbstractCard masterDeckInstance = GetMasterDeckInstance(uuid);
        if (masterDeckInstance != null)
        {
            cards.add(masterDeckInstance);
        }

        return cards;
    }

    public static HashSet<AbstractCard> GetAllInstances(HashSet<AbstractCard> cards, UUID uuid, CardGroup group)
    {
        for (AbstractCard card : group.group)
        {
            if (uuid.equals(card.uuid))
            {
                cards.add(card);
            }
        }

        return cards;
    }

    public static int GetAscensionLevel()
    {
        return AbstractDungeon.isAscensionMode ? Math.max(0, Math.min(20, AbstractDungeon.ascensionLevel)) : 0;
    }

    public static int GetMaxAscensionLevel(AbstractPlayer p)
    {
        return p.getPrefs().getInteger("ASCENSION_LEVEL", 0);
    }

    public static ArrayList<AbstractCard> GetAvailableCards()
    {
        return GetAvailableCards(null);
    }

    public static ArrayList<AbstractCard> GetAvailableCards(FuncT1<Boolean, AbstractCard> filter)
    {
        ArrayList<AbstractCard> result = new ArrayList<>();
        for (CardGroup pool : GetCardPools())
        {
            for (AbstractCard card : pool.group)
            {
                if (filter == null || filter.Invoke(card))
                {
                    result.add(card);
                }
            }
        }

        return result;
    }

    public static RandomizedList<AbstractCard> GetCardPoolInCombat(AbstractCard.CardRarity rarity)
    {
        return GetCardPoolInCombat(GetCardPool(rarity), null);
    }

    public static RandomizedList<AbstractCard> GetCardPoolInCombat(CardGroup group, FuncT1<Boolean, AbstractCard> filter)
    {
        final RandomizedList<AbstractCard> cards = new RandomizedList<>();
        if (group != null)
        {
            for (AbstractCard c : group.group)
            {
                if (IsObtainableInCombat(c) && (filter == null || filter.Invoke(c)))
                {
                    cards.Add(c);
                }
            }
        }

        return cards;
    }

    public static CardGroup GetCardPool(AbstractCard.CardRarity rarity)
    {
        if (rarity == null)
        {
            return AbstractDungeon.colorlessCardPool;
        }

        switch (rarity)
        {
            case CURSE: return AbstractDungeon.curseCardPool;
            case COMMON: return AbstractDungeon.commonCardPool;
            case UNCOMMON: return AbstractDungeon.uncommonCardPool;
            case RARE: return AbstractDungeon.rareCardPool;
            default: return null;
        }
    }

    public static CardGroup GetCardPoolSource(AbstractCard.CardRarity rarity)
    {
        if (rarity == null)
        {
            return AbstractDungeon.srcColorlessCardPool;
        }

        switch (rarity)
        {
            case CURSE: return AbstractDungeon.srcCurseCardPool;
            case COMMON: return AbstractDungeon.srcCommonCardPool;
            case UNCOMMON: return AbstractDungeon.srcUncommonCardPool;
            case RARE: return AbstractDungeon.srcRareCardPool;
            default: return null;
        }
    }

    public static ArrayList<CardGroup> GetCardPools()
    {
        final ArrayList<CardGroup> result = new ArrayList<>();
        result.add(AbstractDungeon.colorlessCardPool);
        result.add(AbstractDungeon.commonCardPool);
        result.add(AbstractDungeon.uncommonCardPool);
        result.add(AbstractDungeon.rareCardPool);
        result.add(AbstractDungeon.curseCardPool);
        return result;
    }

    public static ArrayList<CardGroup> GetSourceCardPools()
    {
        final ArrayList<CardGroup> result = new ArrayList<>();
        result.add(AbstractDungeon.srcColorlessCardPool);
        result.add(AbstractDungeon.srcCommonCardPool);
        result.add(AbstractDungeon.srcUncommonCardPool);
        result.add(AbstractDungeon.srcRareCardPool);
        result.add(AbstractDungeon.srcCurseCardPool);
        return result;
    }

    public static ArrayList<PowerHelper> GetCommonDebuffs()
    {
        if (commonDebuffs.isEmpty())
        {
            commonDebuffs.add(PowerHelper.Weak);
            commonDebuffs.add(PowerHelper.Vulnerable);
            commonDebuffs.add(PowerHelper.Frail);
            commonDebuffs.add(PowerHelper.Poison);
            commonDebuffs.add(PowerHelper.Burning);
            commonDebuffs.add(PowerHelper.Shackles);
            commonDebuffs.add(PowerHelper.LockOn);
            commonDebuffs.add(PowerHelper.DelayedDamage);
        }

        return commonDebuffs;
    }

    public static AbstractRoom GetCurrentRoom()
    {
        return GetCurrentRoom(false);
    }

    public static AbstractRoom GetCurrentRoom(boolean notNull)
    {
        final AbstractRoom room = (AbstractDungeon.currMapNode == null) ? null : AbstractDungeon.currMapNode.getRoom();
        if (room == null && notNull)
        {
            throw new NullPointerException("GetCurrentRoom() returned null");
        }

        return room;
    }

    public static int GetDebuffsCount(AbstractCreature creature)
    {
        return (creature == null || creature.powers == null) ? 0 : GetDebuffsCount(creature.powers);
    }

    public static int GetDebuffsCount(ArrayList<AbstractPower> powers)
    {
        int result = 0;
        for (AbstractPower power : powers)
        {
            if (power.type == AbstractPower.PowerType.DEBUFF)
            {
                result += 1;
            }
        }

        return result;
    }

    public static ArrayList<AbstractMonster> GetEnemies(boolean aliveOnly)
    {
        final AbstractRoom room = GetCurrentRoom();
        final ArrayList<AbstractMonster> monsters = new ArrayList<>();

        if (room != null && room.monsters != null)
        {
            if (!aliveOnly)
            {
                return room.monsters.monsters;
            }

            for (AbstractMonster m : room.monsters.monsters)
            {
                if (!IsDeadOrEscaped(m))
                {
                    monsters.add(m);
                }
            }
        }

        return monsters;
    }

    public static int GetGold()
    {
        return player != null ? player.gold : 0;
    }

    public static float GetHealthPercentage(AbstractCreature creature)
    {
        return creature.currentHealth / (float) creature.maxHealth;
    }

    public static float GetHealthPercentage(AbstractCreature creature, boolean addTempHP, boolean addBlock)
    {
        return GetHP(creature, addTempHP, addBlock) / (float) GetMaxHP(creature, addTempHP, addBlock);
    }

    public static int GetMaxHP(AbstractCreature creature, boolean addTempHP, boolean addBlock)
    {
        return creature.maxHealth + (addTempHP ? TempHPField.tempHp.get(creature) : 0) + (addBlock ? creature.currentBlock : 0);
    }

    public static int GetHP(AbstractCreature creature, boolean addTempHP, boolean addBlock)
    {
        return creature.currentHealth + (addTempHP ? TempHPField.tempHp.get(creature) : 0) + (addBlock ? creature.currentBlock : 0);
    }

    public static EnemyIntent GetIntent(AbstractMonster enemy)
    {
        return EnemyIntent.Get(enemy);
    }

    public static ArrayList<EnemyIntent> GetIntents()
    {
        final ArrayList<EnemyIntent> intents = new ArrayList<>();
        for (AbstractMonster m : GetEnemies(true))
        {
            intents.add(GetIntent(m));
        }

        return intents;
    }

    public static int GetTotalCardsPlayed(AbstractCard ignoreLast, boolean currentTurn)
    {
        final ArrayList<AbstractCard> cards = currentTurn
                ? AbstractDungeon.actionManager.cardsPlayedThisTurn
                : AbstractDungeon.actionManager.cardsPlayedThisCombat;
        return (cards.size() > 0 && (cards.get(cards.size() - 1) == ignoreLast)) ? (cards.size() - 1) : cards.size();
    }

    public static AbstractCard GetLastCardPlayed(boolean currentTurn)
    {
        return GetLastCardPlayed(currentTurn, 0);
    }

    public static AbstractCard GetLastCardPlayed(boolean currentTurn, int offset)
    {
        final ArrayList<AbstractCard> cards = currentTurn
                ? AbstractDungeon.actionManager.cardsPlayedThisTurn
                : AbstractDungeon.actionManager.cardsPlayedThisCombat;
        return cards.size() > offset ? cards.get(cards.size() - 1 - offset) : null;
    }

    public static HashSet<AbstractCard> GetMasterDeckCopies(String cardID)
    {
        final HashSet<AbstractCard> cards = new HashSet<>();
        for (AbstractCard c : player.masterDeck.group)
        {
            if (c.cardID.equals(cardID))
            {
                cards.add(c);
            }
        }

        return cards;
    }

    public static AbstractCard GetMasterDeckInstance(UUID uuid)
    {
        for (AbstractCard c : player.masterDeck.group)
        {
            if (c.uuid == uuid)
            {
                return c;
            }
        }

        return null;
    }

    public static HashSet<AbstractCard> GetOtherCardsInHand(AbstractCard card)
    {
        final HashSet<AbstractCard> cards = new HashSet<>();
        for (AbstractCard c : player.hand.group)
        {
            if (c != card)
            {
                cards.add(c);
            }
        }

        return cards;
    }

    public static <T> T GetPower(AbstractCreature owner, Class<T> powerType)
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

    public static <T extends AbstractPower> T GetPower(AbstractCreature creature, String powerID)
    {
        if (creature != null && creature.powers != null) {
            for (AbstractPower p : creature.powers)
            {
                if (p != null && powerID.equals(p.ID))
                {
                    try
                    {
                        return (T) p;
                    }
                    catch (ClassCastException e)
                    {
                        e.printStackTrace();
                        return null;
                    }
                }
            }
        }

        return null;
    }

    public static <T extends AbstractPower> ArrayList<T> GetPowers(TargetHelper targetHelper, String powerID)
    {
        final ArrayList<T> result = new ArrayList<>();
        for (AbstractCreature c : targetHelper.GetTargets())
        {
            final T t = GetPower(c, powerID);
            if (t != null)
            {
                result.add(t);
            }
        }

        return result;
    }

    public static int GetPowerAmount(Affinity affinity)
    {
        return CombatStats.Affinities.GetPowerAmount(affinity);
    }

    public static int GetPowerAmount(String powerID)
    {
        return GetPowerAmount(player, powerID);
    }

    public static int GetPowerAmount(AbstractCreature owner, String powerID)
    {
        AbstractPower power = GetPower(owner, powerID);
        return (power != null) ? power.amount : 0;
    }

    public static Random GetRNG()
    {
        if (EYBCard.rng == null)
        {
            JUtils.LogInfo(GameUtilities.class, "EYBCard.rng was null");
            return new Random();
        }

        return EYBCard.rng;
    }

    public static AbstractCreature GetRandomCharacter(boolean aliveOnly)
    {
        return GetRandomElement(GetAllCharacters(aliveOnly), GetRNG());
    }

    public static <T> T GetRandomElement(List<T> list)
    {
        return GetRandomElement(list, GetRNG());
    }

    public static <T> T GetRandomElement(T[] arr)
    {
        return GetRandomElement(arr, GetRNG());
    }

    public static <T> T GetRandomElement(T[] arr, Random rng)
    {
        int size = arr.length;
        return (size > 0) ? arr[rng.random(arr.length - 1)] : null;
    }

    public static <T> T GetRandomElement(List<T> list, Random rng)
    {
        int size = list.size();
        return (size > 0) ? list.get(rng.random(list.size() - 1)) : null;
    }

    public static AbstractMonster GetRandomEnemy(boolean aliveOnly)
    {
        return GetRandomElement(GetEnemies(aliveOnly), GetRNG());
    }

    public static AbstractOrb GetRandomOrb()
    {
        if (orbs.Size() == 0)
        {
            orbs.Add(new Lightning(), 7);
            orbs.Add(new Frost(), 7);
            orbs.Add(new Fire(), 6);
            orbs.Add(new Dark(), 6);
            orbs.Add(new Earth(), 5);
            orbs.Add(new Air(), 4);
            orbs.Add(new Plasma(), 3);
            orbs.Add(new Water(), 3);
        }

        return orbs.Retrieve(GetRNG(), false).makeCopy();
    }

    public static <T extends AbstractRelic> T GetRelic(String relicID)
    {
        if (player == null)
        {
            return null;
        }

        for (AbstractRelic relic : player.relics)
        {
            if (relic != null && relicID.equals(relic.relicId))
            {
                try
                {
                    return (T) relic;
                }
                catch (ClassCastException e)
                {
                    e.printStackTrace();
                    return null;
                }
            }
        }

        return null;
    }

    public static <T> T GetRelic(Class<T> relicType)
    {
        for (AbstractRelic relic : player.relics)
        {
            if (relicType.isInstance(relic))
            {
                return relicType.cast(relic);
            }
        }

        return null;
    }

    public static ArrayList<String> GetRelicPool(AbstractRelic.RelicTier tier)
    {
        switch (tier)
        {
            case COMMON: return AbstractDungeon.commonRelicPool;
            case UNCOMMON: return AbstractDungeon.uncommonRelicPool;
            case RARE: return AbstractDungeon.rareRelicPool;
            case BOSS: return AbstractDungeon.bossRelicPool;
            case SHOP: return AbstractDungeon.shopRelicPool;
            default: return null;
        }
    }

    public static int GetTempHP(AbstractCreature creature)
    {
        return creature != null ? TempHPField.tempHp.get(creature) : 0;
    }

    public static int GetTempHP()
    {
        return GetTempHP(player);
    }

    public static int GetTimesPlayedThisTurn(AbstractCard card)
    {
        int result = 0;
        for (AbstractCard c : actionManager.cardsPlayedThisTurn)
        {
            if (c.uuid.equals(card.uuid))
            {
                result += 1;
            }
        }

        return result;
    }

    public static int GetUniqueOrbsCount()
    {
        final HashSet<String> orbs = new HashSet<>();
        for (AbstractOrb orb : player.orbs)
        {
            if (IsValidOrb(orb))
            {
                orbs.add(orb.ID);
            }
        }

        return orbs.size();
    }

    public static int GetXCostEnergy(AbstractCard card)
    {
        int amount = EnergyPanel.getCurrentEnergy();

        if (card.energyOnUse != -1)
        {
            amount = card.energyOnUse;
        }

        if (player.hasRelic(ChemicalX.ID))
        {
            amount += ChemicalX.BOOST;
        }

        return amount;
    }

    public static boolean HasOrb(String orbID)
    {
        return GetOrbCount(orbID) > 0;
    }

    public static int GetOrbCount(String orbID)
    {
        int count = 0;
        if (player != null && player.orbs != null)
        {
            for (AbstractOrb orb : player.orbs)
            {
                if (orbID.equals(orb.ID))
                {
                    count += 1;
                }
            }
        }

        return count;
    }

    public static boolean HasRelic(String relicID)
    {
        return player != null && player.hasRelic(relicID);
    }

    public static boolean HasRelicEffect(String relicID)
    {
        return HasRelic(relicID)
            || CombatStats.GetCombatData(relicID, false)
            || CombatStats.GetTurnData(relicID, false);
    }

    public static void ChangeCardName(AbstractCard card, String newName)
    {
        card.name = card.name.replace(card.originalName, newName);
        card.originalName = newName;
    }

    public static AbstractCard Imitate(AbstractCard card)
    {
        card = card.makeStatEquivalentCopy();

        if (!card.originalName.startsWith("\""))
        {
            ChangeCardName(card, "\"" + card.originalName + "\"");
        }
        if (card.baseDamage >= 0)
        {
            ModifyDamage(card, 1, false);
        }
        if (card.baseBlock >= 0)
        {
            ModifyBlock(card, 1, false);
        }
        if (card.cost >= 0)
        {
            ModifyCostForCombat(card, 0, false);
        }

        card.tags.add(GR.Enums.CardTags.PURGE);
        card.freeToPlayOnce = true;

        return card;
    }

    public static boolean InBattle()
    {
        return InBattle(false);
    }

    public static boolean InBattle(boolean forceRefresh)
    {
        if (forceRefresh)
        {
            CombatStats.Refresh();
        }

        return CombatStats.BattleID != null;
    }

    public static boolean InBossRoom()
    {
        return GetCurrentRoom() instanceof MonsterRoomBoss;
    }

    public static boolean InEliteRoom()
    {
        final AbstractRoom room = GetCurrentRoom();
        return (room != null) && room.eliteTrigger;
    }

    public static boolean InEliteOrBossRoom()
    {
        final AbstractRoom room = GetCurrentRoom();
        return room instanceof MonsterRoomBoss || (room != null && room.eliteTrigger);
    }

    public static boolean InGame()
    {
        return CardCrawlGame.GameMode.GAMEPLAY.equals(CardCrawlGame.mode);
    }

    public static boolean InStance(String stanceID)
    {
        return player != null && player.stance != null && player.stance.ID.equals(stanceID);
    }

    public static void IncreaseBlock(AbstractCard card, int amount, boolean temporary)
    {
        ModifyBlock(card, card.baseBlock + amount, temporary);
    }

    public static void IncreaseDamage(AbstractCard card, int amount, boolean temporary)
    {
        ModifyDamage(card, card.baseDamage + amount, temporary);
    }

    public static void IncreaseHandSizePermanently(float cX, float cY)
    {
        for (AbstractBlight blight : player.blights)
        {
            if (blight instanceof UpgradedHand)
            {
                ((UpgradedHand)blight).addAmount(1);
                return;
            }
        }
        GetCurrentRoom(true).spawnBlightAndObtain(cX, cY, new UpgradedHand());
    }

    public static void IncreaseMagicNumber(AbstractCard card, int amount, boolean temporary)
    {
        ModifyMagicNumber(card, card.baseMagicNumber + amount, temporary);
    }

    public static void IncreaseSecondaryValue(EYBCard card, int amount, boolean temporary)
    {
        ModifySecondaryValue(card, card.baseSecondaryValue + amount, temporary);
    }

    public static boolean IsAttacking(AbstractMonster.Intent intent)
    {
        return (intent == AbstractMonster.Intent.ATTACK_DEBUFF || intent == AbstractMonster.Intent.ATTACK_BUFF ||
                intent == AbstractMonster.Intent.ATTACK_DEFEND || intent == AbstractMonster.Intent.ATTACK);
    }

    public static boolean IsCommonOrb(AbstractOrb orb)
    {
        return IsValidOrb(orb) && (Fire.ORB_ID.equals(orb.ID) || Frost.ORB_ID.equals(orb.ID) || Lightning.ORB_ID.equals(orb.ID) || Dark.ORB_ID.equals(orb.ID));
    }

    public static boolean IsHindrance(AbstractCard card)
    {
        return card.type == AbstractCard.CardType.CURSE || card.type == AbstractCard.CardType.STATUS;
    }

    public static boolean IsPlayable(AbstractCard card)
    {
        final boolean temp = card.freeToPlayOnce;
        card.freeToPlayOnce = true;
        boolean canUse = card.canUse(player, null);

        if (!canUse && (card.target == AbstractCard.CardTarget.ENEMY || card.target == AbstractCard.CardTarget.SELF_AND_ENEMY))
        {
            final ArrayList<AbstractMonster> enemies = GetEnemies(true);
            for (AbstractMonster m : enemies)
            {
                if (card.canUse(player, m))
                {
                    canUse = true;
                    break;
                }
            }
        }

        card.freeToPlayOnce = temp;
        return canUse;
    }

    public static boolean IsPlayable(AbstractCard card, AbstractMonster target)
    {
        final boolean temp = card.freeToPlayOnce;
        card.freeToPlayOnce = true;
        boolean canUse = card.canUse(player, target);
        card.freeToPlayOnce = temp;

        return canUse;
    }

    public static boolean IsHighCost(AbstractCard card)
    {
        return card.costForTurn >= 2;
    }

    public static boolean IsLowCost(AbstractCard card)
    {
        return card.costForTurn == 0 || card.costForTurn == 1;
    }

    public static boolean IsCommonDebuff(AbstractPower power)
    {
        return JUtils.Any(GameUtilities.GetCommonDebuffs(), ph -> ph.ID.equals(power.ID));
    }

    public static boolean IsDebuff(AbstractPower power)
    {
        return power != null && power.type == AbstractPower.PowerType.DEBUFF;
    }

    public static boolean IsDeadOrEscaped(AbstractCreature target)
    {
        return target.isDeadOrEscaped() || target.currentHealth <= 0;
    }

    public static boolean IsFatal(AbstractCreature enemy, boolean includeMinions)
    {
        return (enemy.isDead || enemy.isDying || enemy.currentHealth <= 0)
            && !enemy.hasPower(RegrowPower.POWER_ID)
            && (includeMinions || !enemy.hasPower(MinionPower.POWER_ID));
    }

    public static boolean IsMonster(AbstractCreature c)
    {
        return c != null && !c.isPlayer;
    }

    public static boolean IsNormalRun(boolean checkMods)
    {
        if (checkMods)
        {
            if (!GR.Animator.OfficialName.equals(player.getTitle(player.chosenClass)))
            {
                JUtils.LogInfo(GameUtilities.class, "IsNormalRun: false (0)");
                return false;
            }

            if (GR.Common.Dungeon.IsCheating())
            {
                JUtils.LogInfo(GameUtilities.class, "IsNormalRun: false (1)");
                return false;
            }

//            final String validSeed = GR.Animator.Config.LastSeed.Get();
//            if (StringUtils.isNotEmpty(validSeed) && !String.valueOf(Settings.seed).equals(validSeed))
//            {
//                JUtils.LogInfo(GameUtilities.class, "IsNormalRun: false (2)");
//                return false;
//            }

            for (AbstractCard c : player.masterDeck.group)
            {
                if (c instanceof CustomCard)
                {
                    JUtils.LogInfo(GameUtilities.class, "IsNormalRun: false (3)");
                    return false;
                }
            }
        }

        JUtils.LogInfo(GameUtilities.class, "IsNormalRun: SeedSet: {0}, SpecialSeed: {1}, DailyRun: {2}, IsTrial: {3} IsDemo: {4}, Mods: {5}, Endless: {6}",
                Settings.seedSet, Settings.specialSeed, Settings.isDailyRun, Settings.isTrial, Settings.isDemo,
                JUtils.JoinStrings("," ,ModHelper.getEnabledModIDs()), Settings.isEndless);

        return !Settings.seedSet && JUtils.IsNullOrZero(Settings.specialSeed) && !Settings.isDailyRun && !Settings.isTrial
            && !Settings.isDemo && JUtils.IsNullOrEmpty(ModHelper.enabledMods) && !Settings.isEndless;
    }

    public static boolean IsNormalRun(RunData data, boolean checkMods)
    {
        if (checkMods)
        {
            if (!GR.Animator.OfficialName.equals(data.loadout))
            {
                JUtils.LogInfo(GameUtilities.class, "IsNormalRunData: false (0)");
                return false;
            }

            final String validSeed = GR.Animator.Config.LastSeed.Get();
            if (StringUtils.isNotEmpty(validSeed) && !validSeed.equals(String.valueOf(data.seed_played)))
            {
                JUtils.LogInfo(GameUtilities.class, "IsNormalRunData: false (1)");
                return false;
            }

            for (String cardID : data.master_deck)
            {
                final AbstractCard c = CardLibrary.getCard(JUtils.SplitString("+", cardID)[0]);
                if (c == null || c instanceof CustomCard)
                {
                    JUtils.LogInfo(GameUtilities.class, "IsNormalRunData: false (2), " + (c != null ? c.cardID : "null"));
                    return false;
                }
            }
        }

        JUtils.LogInfo(GameUtilities.class, "IsNormalRunData: chose_seed: {0}, special_seed: {1}, is_daily: {2}, is_trial: {3}, is_endless: {4}, daily_mods: {5}, is_special_run: {6}",
                data.chose_seed, data.special_seed, data.is_daily, data.is_trial,
                (data.daily_mods == null ? "null" : JUtils.JoinStrings(",", data.daily_mods)), data.is_special_run);

        return !data.chose_seed && JUtils.IsNullOrZero(data.special_seed) && !data.is_daily && !data.is_trial
            && !data.is_endless && JUtils.IsNullOrEmpty(data.daily_mods) && !data.is_special_run;
    }

    public static boolean IsObtainableInCombat(AbstractCard c)
    {
        return !c.hasTag(AbstractCard.CardTags.HEALING);
    }

    public static boolean IsPlayer(AbstractCreature c)
    {
        return c != null && c.isPlayer;
    }

    public static boolean IsPlayerClass(AbstractPlayer.PlayerClass playerClass)
    {
        return player != null && player.chosenClass == playerClass;
    }

    public static boolean IsValidOrb(AbstractOrb orb)
    {
        return orb != null && !(orb instanceof EmptyOrbSlot);
    }

    public static boolean IsValidTarget(AbstractMonster enemy)
    {
        return enemy != null && !IsDeadOrEscaped(enemy);
    }

    public static void ModifyBlock(AbstractCard card, int amount, boolean temporary)
    {
        card.block = Math.max(0, amount);
        if (!temporary)
        {
            card.baseBlock = card.block;
        }
        card.isBlockModified = (card.block != card.baseBlock);
    }

    public static int ModifyCardDrawPerTurn(int amount, int minimumCardDraw)
    {
        final int newAmount = player.gameHandSize + amount;
        if (newAmount < minimumCardDraw)
        {
            amount += (minimumCardDraw - newAmount);
        }

        player.gameHandSize += amount;
        return amount;
    }

    public static void ModifyCardTag(AbstractCard card, AbstractCard.CardTags tag, boolean value)
    {
        EYBCard aCard = JUtils.SafeCast(card, EYBCard.class);
        if (aCard != null) {
            if (tag.equals(AUTOPLAY)) {
                aCard.SetAutoplay(value);
            }
            else if (tag.equals(DELAYED)) {
                aCard.SetDelayed(value);
            }
            else if (tag.equals(HARMONIC)) {
                aCard.SetHarmonic(value);
            }
            else if (tag.equals(HASTE)) {
                aCard.SetHaste(value);
            }
            else if (tag.equals(HASTE_INFINITE)) {
                aCard.SetPermanentHaste(value);
            }
            else if (tag.equals(LOYAL)) {
                aCard.SetLoyal(value);
            }
            else if (tag.equals(PROTAGONIST)) {
                aCard.SetHarmonic(value);
            }
            else if (tag.equals(PURGE)) {
                aCard.SetPurge(value);
            }
            else if (value) {
                aCard.tags.add(tag);
            }
            else {
                aCard.tags.remove(tag);
            }
        }
        else if (value) {
            card.tags.add(tag);
        }
        else {
            card.tags.remove(tag);
        }
    }

    public static void ModifyCostForCombat(AbstractCard card, int amount, boolean relative)
    {
        final int previousCost = card.cost;
        if (relative)
        {
            card.costForTurn = Math.max(0, card.costForTurn + amount);
            card.cost = Math.max(0, card.cost + amount);
        }
        else
        {
            card.costForTurn = amount + (card.costForTurn - card.cost);
            card.cost = amount;
        }

        if (card.cost != previousCost)
        {
            card.isCostModified = true;
        }
        CombatStats.OnCostChanged(card, previousCost, card.cost);
    }

    public static void ModifyCostForTurn(AbstractCard card, int amount, boolean relative)
    {
        final int previousCost = card.costForTurn;
        card.costForTurn = relative ? Math.max(0, card.costForTurn + amount) : amount;
        card.isCostModifiedForTurn = (card.cost != card.costForTurn);
        CombatStats.OnCostChanged(card, previousCost, card.costForTurn);
    }

    public static void ModifyDamage(AbstractCard card, int amount, boolean temporary)
    {
        card.damage = Math.max(0, amount);
        if (!temporary)
        {
            card.baseDamage = card.damage;
        }
        card.isDamageModified = (card.damage != card.baseDamage);
    }

    public static void ModifyMagicNumber(AbstractCard card, int amount, boolean temporary)
    {
        card.magicNumber = amount;
        if (!temporary)
        {
            card.baseMagicNumber = card.magicNumber;
        }
        card.isMagicNumberModified = (card.magicNumber != card.baseMagicNumber);
    }

    public static void ModifySecondaryValue(EYBCard card, int amount, boolean temporary)
    {
        card.secondaryValue = amount;
        if (!temporary)
        {
            card.baseSecondaryValue = card.secondaryValue;
        }
        card.isSecondaryValueModified = (card.secondaryValue != card.baseSecondaryValue);
    }

    public static void ObtainBlight(float cX, float cY, AbstractBlight blight)
    {
        GetCurrentRoom(true).spawnBlightAndObtain(cX, cY, blight);
    }

    public static void ObtainRelic(float cX, float cY, AbstractRelic relic)
    {
        GetCurrentRoom(true).spawnRelicAndObtain(cX, cY, relic);
    }

    public static void PlayManually(AbstractCard card, AbstractMonster m)
    {
        card.applyPowers();
        card.calculateCardDamage(m);
        card.use(player, m);
        actionManager.cardsPlayedThisTurn.add(card);
        actionManager.cardsPlayedThisCombat.add(card);
        CombatStats.Affinities.SetLastCardPlayed(card);
    }

    public static void RefreshHandLayout()
    {
        RefreshHandLayout(false);
    }

    public static void RefreshHandLayout(boolean refreshInstantly)
    {
        if (refreshInstantly)
        {
            handLayoutRefresher.Refresh();
        }
        else
        {
            CombatStats.onPhaseChanged.Subscribe(handLayoutRefresher);
        }
    }

    public static boolean RequiresTarget(AbstractCard card)
    {
        return card.target == AbstractCard.CardTarget.ENEMY || card.target == AbstractCard.CardTarget.SELF_AND_ENEMY;
    }

    public static boolean CanRetain(AbstractCard card)
    {
        return !card.isEthereal && !card.retain && !card.selfRetain;
    }

    public static boolean Retain(AbstractCard card)
    {
        if (CanRetain(card))
        {
            card.retain = true;
            return true;
        }

        return false;
    }

    public static AbstractAffinityPower RetainPower(Affinity affinity)
    {
        final AbstractAffinityPower power = CombatStats.Affinities.GetPower(affinity);
        if (power != null)
        {
            power.RetainOnce();
        }

        return power;
    }

    public static void RetainAllPowers()
    {
        for (AbstractAffinityPower p : CombatStats.Affinities.Powers)
        {
            p.RetainOnce();
        }
    }

    public static void SetCardTag(AbstractCard card, AbstractCard.CardTags tag, boolean value)
    {
        if (value)
        {
            if (!card.tags.contains(tag))
            {
                card.tags.add(tag);
            }
        }
        else
        {
            card.tags.remove(tag);
        }
    }

    public static boolean IsTopPanelVisible()
    {
        return AbstractDungeon.topPanel != null && !Settings.hideTopBar;
    }

    public static void SetTopPanelVisible(boolean visible)
    {
        Settings.hideTopBar = !visible;
        Settings.hideRelics = !visible;

        if (AbstractDungeon.topPanel != null)
        {
            AbstractDungeon.topPanel.unhoverHitboxes();
            //AbstractDungeon.topPanel.potionUi.isHidden = !visible;
        }
    }

    public static void TriggerWhenPlayed(AbstractCard card, ActionT1<AbstractCard> onCardPlayed)
    {
        CombatStats.onAfterCardPlayed.Subscribe(new CardPlayedListener(card, onCardPlayed));
    }

    public static <T> void TriggerWhenPlayed(AbstractCard card, T state, ActionT2<T, AbstractCard> onCardPlayed)
    {
        CombatStats.onAfterCardPlayed.Subscribe(new CardPlayedListener(card, state, onCardPlayed));
    }

    public static Vector2 TryGetPosition(CardGroup group, AbstractCard card)
    {
        if (group != null)
        {
            if (group.type == CardGroup.CardGroupType.DRAW_PILE)
            {
                return new Vector2(CardGroup.DRAW_PILE_X, CardGroup.DRAW_PILE_Y);
            }
            else if (group.type == CardGroup.CardGroupType.DISCARD_PILE)
            {
                return new Vector2(CardGroup.DISCARD_PILE_X, CardGroup.DRAW_PILE_Y);
            }
            else if (group.type == CardGroup.CardGroupType.EXHAUST_PILE)
            {
                return new Vector2(CardGroup.DISCARD_PILE_X, CardGroup.DRAW_PILE_Y + (Settings.scale * 30f));
            }
        }

        return card == null ? null : new Vector2(card.current_x, card.current_y);
    }

    public static void SetUnplayableThisTurn(AbstractCard card)
    {
        CombatStats.UnplayableCards().add(card.uuid);
    }

    public static boolean IsUnplayableThisTurn(AbstractCard card)
    {
        return CombatStats.UnplayableCards().contains(card.uuid);
    }

    public static boolean TrySetPosition(CardGroup group, AbstractCard card)
    {
        Vector2 pos = TryGetPosition(group, null);
        if (pos == null)
        {
            return false;
        }

        card.current_x = pos.x;
        card.current_y = pos.y;

        return true;
    }

    public static void UnlockAllKeys()
    {
        if (!Settings.isFinalActAvailable)
        {
            Settings.isFinalActAvailable = true;
            CardCrawlGame.playerPref.putBoolean(AbstractPlayer.PlayerClass.IRONCLAD.name() + "_WIN", true);
            CardCrawlGame.playerPref.putBoolean(AbstractPlayer.PlayerClass.THE_SILENT.name() + "_WIN", true);
            CardCrawlGame.playerPref.putBoolean(AbstractPlayer.PlayerClass.DEFECT.name() + "_WIN", true);

            if (UnlockTracker.isAchievementUnlocked(AchievementGrid.RUBY_PLUS_KEY))
            {
                UnlockTracker.unlockAchievement(AchievementGrid.RUBY_PLUS_KEY);
            }
            if (UnlockTracker.isAchievementUnlocked(AchievementGrid.EMERALD_PLUS_KEY))
            {
                UnlockTracker.unlockAchievement(AchievementGrid.EMERALD_PLUS_KEY);
            }
            if (UnlockTracker.isAchievementUnlocked(AchievementGrid.SAPPHIRE_PLUS_KEY))
            {
                UnlockTracker.unlockAchievement(AchievementGrid.SAPPHIRE_PLUS_KEY);
            }
        }
    }

    public static void UpdatePowerDescriptions()
    {
        for (AbstractCreature c : GameUtilities.GetAllCharacters(true))
        {
            for (AbstractPower p : c.powers)
            {
                p.updateDescription();
            }
        }
    }

    public static boolean UseArtifact(AbstractCreature target)
    {
        final AbstractPower artifact = GetPower(target, ArtifactPower.POWER_ID);
        if (artifact != null)
        {
            GameActions.Top.Add(new TextAboveCreatureAction(target, ApplyPowerAction.TEXT[0]));
            SFX.Play(SFX.NULLIFY_SFX);
            artifact.flashWithoutSound();
            artifact.onSpecificTrigger();

            return false;
        }

        return true;
    }

    public static void UsePenNib()
    {
        if (player.hasPower(PenNibPower.POWER_ID))
        {
            GameActions.Bottom.ReducePower(player, PenNibPower.POWER_ID, 1);

            final AbstractRelic relic = player.getRelic(PenNib.ID);
            if (relic != null)
            {
                relic.counter = 0;
                relic.flash();
                relic.stopPulse();
            }
        }
    }

    public static int UseXCostEnergy(AbstractCard card)
    {
        int amount = EnergyPanel.getCurrentEnergy();

        if (card.energyOnUse != -1)
        {
            amount = card.energyOnUse;
        }
        else
        {
            card.energyOnUse = amount;
        }

        if (player.hasRelic(ChemicalX.ID))
        {
            amount += ChemicalX.BOOST;
            player.getRelic(ChemicalX.ID).flash();
        }

        if (!card.freeToPlayOnce)
        {
            player.energy.use(card.energyOnUse);
        }

        RefreshHandLayout();

        return amount;
    }

    private static class CardPlayedListener implements OnAfterCardPlayedSubscriber
    {
        private final Object state;
        private final AbstractCard card;
        private final ActionT1<AbstractCard> onCardPlayedT1;
        private final ActionT2<?, AbstractCard> onCardPlayedT2;

        public CardPlayedListener(AbstractCard card, ActionT1<AbstractCard> onCardPlayed)
        {
            this.state = null;
            this.card = card;
            this.onCardPlayedT1 = onCardPlayed;
            this.onCardPlayedT2 = null;
        }

        public <T> CardPlayedListener(AbstractCard card, T state, ActionT2<T, AbstractCard> onCardPlayed)
        {
            this.state = state;
            this.card = card;
            this.onCardPlayedT1 = null;
            this.onCardPlayedT2 = onCardPlayed;
        }

        @Override
        public void OnAfterCardPlayed(AbstractCard card)
        {
            if (this.card.uuid.equals(card.uuid))
            {
                if (this.onCardPlayedT1 != null)
                {
                    this.onCardPlayedT1.Invoke(card);
                }

                if (this.onCardPlayedT2 != null)
                {
                    this.onCardPlayedT2.CastAndInvoke(state, card);
                }

                CombatStats.onAfterCardPlayed.Unsubscribe(this);
            }
        }
    }

    private static class HandLayoutRefresher implements OnPhaseChangedSubscriber
    {
        @Override
        public void OnPhaseChanged(GameActionManager.Phase phase)
        {
            if (phase == GameActionManager.Phase.WAITING_ON_USER)
            {
                Refresh();

                CombatStats.onPhaseChanged.Unsubscribe(handLayoutRefresher);
            }
        }

        public void Refresh()
        {
            player.hand.refreshHandLayout();
            player.hand.applyPowers();
            player.hand.glowCheck();
        }
    }
}
