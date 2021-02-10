package eatyourbeets.utilities;

import com.badlogic.gdx.math.Vector2;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.SoulboundField;
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
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.*;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.ChemicalX;
import com.megacrit.cardcrawl.relics.PenNib;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.screens.stats.AchievementGrid;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.interfaces.delegates.FuncT1;
import eatyourbeets.interfaces.subscribers.OnAddingToCardReward;
import eatyourbeets.interfaces.subscribers.OnPhaseChangedSubscriber;
import eatyourbeets.interfaces.subscribers.OnTryApplyPowerSubscriber;
import eatyourbeets.monsters.EnemyMoveDetails;
import eatyourbeets.orbs.animator.Aether;
import eatyourbeets.orbs.animator.Earth;
import eatyourbeets.orbs.animator.Fire;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.PowerHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.UUID;
import java.util.function.Predicate;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.player;

public class GameUtilities
{
    private static final HandLayoutRefresher handLayoutRefresher = new HandLayoutRefresher();
    private static final WeightedList<AbstractOrb> orbs = new WeightedList<>();

    public static void ApplyPowerInstantly(AbstractCreature target, PowerHelper powerHelper, int stacks)
    {
        ApplyPowerInstantly(TargetHelper.Source(target), powerHelper, stacks);
    }

    public static void ApplyPowerInstantly(TargetHelper targetHelper, PowerHelper powerHelper, int stacks)
    {
        for (AbstractCreature target : targetHelper.GetTargets())
        {
            AbstractPower power = GetPower(target, powerHelper.ID);
            if (power != null)
            {
                if ((power.amount += stacks) == 0)
                {
                    target.powers.remove(power);
                }
            }
            else
            {
                target.addPower(powerHelper.Create(target, target, stacks));
                Collections.sort(target.powers);
            }
        }
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean CanApplyPower(AbstractCreature source, AbstractCreature target, AbstractPower powerToApply)
    {
        boolean canApply = true;
        if (target != null)
        {
            for (AbstractPower power : target.powers)
            {
                if (power instanceof OnTryApplyPowerSubscriber)
                {
                    canApply &= ((OnTryApplyPowerSubscriber) power).TryApplyPower(powerToApply, target, source);
                }
            }

            if (canApply && target != source && source != null)
            {
                for (AbstractPower power : source.powers)
                {
                    if (power instanceof OnTryApplyPowerSubscriber)
                    {
                        canApply &= ((OnTryApplyPowerSubscriber) power).TryApplyPower(powerToApply, target, source);
                    }
                }
            }
        }

        return canApply;
    }

    public static boolean CanRemoveFromDeck(AbstractCard card)
    {
        return (card.rarity != AbstractCard.CardRarity.SPECIAL) && !SoulboundField.soulbound.get(card);
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

    public static void DecreaseSecondaryValue(AbstractCard card, int amount, boolean temporary)
    {
        if (card instanceof EYBCard)
        {
            ModifySecondaryValue(card, Math.max(0, ((EYBCard)card).baseSecondaryValue - amount), temporary);
        }
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

    public static ArrayList<AbstractCard> GenerateCardPool(Predicate<AbstractCard> filter)
    {
        ArrayList<AbstractCard> pool = new ArrayList<>();
        for (AbstractCard c : CardLibrary.getAllCards())
        {
            if (filter.test(c))
            {
                pool.add(c);
            }
        }

        return pool;
    }

    public static int GetActualAscensionLevel()
    {
        return AbstractDungeon.isAscensionMode ? AbstractDungeon.ascensionLevel : 0;
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
        HashSet<AbstractCard> cards = new HashSet<>();

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
        HashSet<AbstractCard> cards = GetAllInBattleInstances(uuid);
        AbstractCard masterDeckInstance = GetMasterDeckInstance(uuid);
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

    public static CardGroup GetCardPool(AbstractCard.CardRarity rarity, AbstractCard.CardColor color)
    {
        if (color == AbstractCard.CardColor.COLORLESS)
        {
            return AbstractDungeon.colorlessCardPool;
        }

        switch (rarity)
        {
            case COMMON:
                return AbstractDungeon.commonCardPool;
            case UNCOMMON:
                return AbstractDungeon.uncommonCardPool;
            case RARE:
                return AbstractDungeon.rareCardPool;
            default:
                return null;
        }
    }

    public static CardGroup GetCardPoolSource(AbstractCard.CardRarity rarity, AbstractCard.CardColor color)
    {
        if (color == AbstractCard.CardColor.COLORLESS)
        {
            return AbstractDungeon.srcColorlessCardPool;
        }

        switch (rarity)
        {
            case COMMON:
                return AbstractDungeon.srcCommonCardPool;
            case UNCOMMON:
                return AbstractDungeon.srcUncommonCardPool;
            case RARE:
                return AbstractDungeon.srcRareCardPool;
            default:
                return null;
        }
    }

    public static ArrayList<CardGroup> GetCardPools()
    {
        ArrayList<CardGroup> result = new ArrayList<>();
        result.add(AbstractDungeon.colorlessCardPool);
        result.add(AbstractDungeon.commonCardPool);
        result.add(AbstractDungeon.uncommonCardPool);
        result.add(AbstractDungeon.rareCardPool);
        result.add(AbstractDungeon.curseCardPool);
        return result;
    }

    public static AbstractRoom GetCurrentRoom()
    {
        return (AbstractDungeon.currMapNode == null) ? null : AbstractDungeon.currMapNode.getRoom();
    }

    public static AbstractRoom GetCurrentRoom(boolean notNull)
    {
        AbstractRoom room = GetCurrentRoom();
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

    public static EnemyMoveDetails GetEnemyMove(AbstractMonster enemy)
    {
        return new EnemyMoveDetails(enemy);
    }

    public static float GetHealthPercentage(AbstractCreature creature)
    {
        return creature.currentHealth / (float) creature.maxHealth;
    }

    public static AbstractCard GetLastCardPlayed(boolean currentTurn)
    {
        return GetLastCardPlayed(currentTurn, 0);
    }

    public static AbstractCard GetLastCardPlayed(boolean currentTurn, int offset)
    {
        ArrayList<AbstractCard> cards;
        if (currentTurn)
        {
            cards = AbstractDungeon.actionManager.cardsPlayedThisTurn;
        }
        else
        {
            cards = AbstractDungeon.actionManager.cardsPlayedThisCombat;
        }

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

        return null;
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
        return JUtils.GetRandomElement(GetAllCharacters(aliveOnly), GetRNG());
    }

    public static AbstractMonster GetRandomEnemy(boolean aliveOnly)
    {
        return JUtils.GetRandomElement(GetEnemies(aliveOnly), GetRNG());
    }

    public static AbstractOrb GetRandomOrb()
    {
        if (orbs.Size() == 0)
        {
            orbs.Add(new Lightning(), 7);
            orbs.Add(new Frost(), 7);
            orbs.Add(new Earth(), 6);
            orbs.Add(new Fire(), 6);
            orbs.Add(new Plasma(), 4);
            orbs.Add(new Dark(), 4);
            orbs.Add(new Aether(), 4);
        }

        return orbs.Retrieve(GetRNG(), false).makeCopy();
    }

    public static AbstractCard GetRandomRewardCard(RewardItem rewardItem, boolean includeRares)
    {
        AbstractCard replacement = null;
        boolean searchingCard = true;

        while (searchingCard)
        {
            searchingCard = false;

            AbstractCard temp = GetRandomRewardCard(includeRares);
            if (temp == null)
            {
                break;
            }

            for (AbstractCard c : rewardItem.cards)
            {
                if (temp.cardID.equals(c.cardID))
                {
                    searchingCard = true;
                }
            }

            if (temp instanceof OnAddingToCardReward && ((OnAddingToCardReward) temp).ShouldCancel(rewardItem))
            {
                searchingCard = true;
            }

            if (!searchingCard)
            {
                replacement = temp.makeCopy();
            }
        }

        for (AbstractRelic r : player.relics)
        {
            r.onPreviewObtainCard(replacement);
        }

        return replacement;
    }

    public static AbstractCard GetRandomRewardCard(boolean includeRares)
    {
        ArrayList<AbstractCard> list;

        int roll = AbstractDungeon.cardRng.random(100);
        if (includeRares && (roll <= 4 || GetCurrentRoom() instanceof MonsterRoomBoss))
        {
            list = AbstractDungeon.srcRareCardPool.group;
        }
        else if (roll < 40)
        {
            list = AbstractDungeon.srcUncommonCardPool.group;
        }
        else
        {
            list = AbstractDungeon.srcCommonCardPool.group;
        }

        if (list != null && list.size() > 0)
        {
            return list.get(AbstractDungeon.cardRng.random(list.size() - 1));
        }

        return null;
    }

    public static <T extends AbstractRelic> T GetRelic(String relicID)
    {
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
            case COMMON:
                return AbstractDungeon.commonRelicPool;
            case UNCOMMON:
                return AbstractDungeon.uncommonRelicPool;
            case RARE:
                return AbstractDungeon.rareRelicPool;
            case BOSS:
                return AbstractDungeon.bossRelicPool;
            case SHOP:
                return AbstractDungeon.shopRelicPool;
            default:
                return null;
        }
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

    public static boolean InBattle()
    {
        AbstractRoom room = GetCurrentRoom();
        if (room != null && !room.isBattleOver && !player.isDead)
        {
            return room.phase == AbstractRoom.RoomPhase.COMBAT || (room.monsters != null && !room.monsters.areMonstersBasicallyDead());
        }

        return false;
    }

    public static boolean InBossRoom()
    {
        return GetCurrentRoom() instanceof MonsterRoomBoss;
    }

    public static boolean InEliteRoom()
    {
        AbstractRoom room = GetCurrentRoom();
        return (room != null) && room.eliteTrigger;
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

    public static void IncreaseMagicNumber(AbstractCard card, int amount, boolean temporary)
    {
        ModifyMagicNumber(card, card.baseMagicNumber + amount, temporary);
    }

    public static void IncreaseSecondaryValue(AbstractCard card, int amount, boolean temporary)
    {
        if (card instanceof EYBCard)
        {
            ModifySecondaryValue(card, ((EYBCard)card).baseSecondaryValue + amount, temporary);
        }
    }

    public static boolean IsAttacking(AbstractMonster.Intent intent)
    {
        return (intent == AbstractMonster.Intent.ATTACK_DEBUFF || intent == AbstractMonster.Intent.ATTACK_BUFF ||
                intent == AbstractMonster.Intent.ATTACK_DEFEND || intent == AbstractMonster.Intent.ATTACK);
    }

    public static boolean IsCurseOrStatus(AbstractCard card)
    {
        return card.type == AbstractCard.CardType.CURSE || card.type == AbstractCard.CardType.STATUS;
    }

    public static boolean IsDeadOrEscaped(AbstractCreature target)
    {
        return target.isDeadOrEscaped() || target.currentHealth <= 0;
    }

    public static boolean IsMonster(AbstractCreature c)
    {
        return c != null && !c.isPlayer;
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

    public static void ModifyCostForCombat(AbstractCard card, int amount, boolean relative)
    {
        int previousCost = card.cost;
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
    }

    public static void ModifyCostForTurn(AbstractCard card, int amount, boolean relative)
    {
        card.costForTurn = relative ? Math.max(0, card.costForTurn + amount) : amount;
        card.isCostModifiedForTurn = (card.cost != card.costForTurn);
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

    public static void ModifySecondaryValue(AbstractCard card, int amount, boolean temporary)
    {
        EYBCard c = JUtils.SafeCast(card, EYBCard.class);
        if (c != null)
        {
            c.secondaryValue = amount;
            if (!temporary)
            {
                c.baseSecondaryValue = c.secondaryValue;
            }
            c.isSecondaryValueModified = (c.secondaryValue != c.baseSecondaryValue);
        }
    }

    public static void ObtainBlight(float cX, float cY, AbstractBlight blight)
    {
        GetCurrentRoom(true).spawnBlightAndObtain(cX, cY, blight);
    }

    public static void ObtainRelic(float cX, float cY, AbstractRelic relic)
    {
        GetCurrentRoom(true).spawnRelicAndObtain(cX, cY, relic);
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

    public static boolean Retain(AbstractCard card)
    {
        if (!card.isEthereal && !card.retain && !card.selfRetain)
        {
            card.retain = true;
            return true;
        }

        return false;
    }

    public static boolean TriggerOnKill(AbstractCreature enemy, boolean includeMinions)
    {
        return IsDeadOrEscaped(enemy) && !enemy.hasPower(RegrowPower.POWER_ID) && (includeMinions || !enemy.hasPower(MinionPower.POWER_ID));
    }

    public static Vector2 TryGetPosition(CardGroup group)
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

        return null;
    }

    public static boolean TrySetPosition(CardGroup group, AbstractCard card)
    {
        Vector2 pos = TryGetPosition(group);
        if (pos != null)
        {
            card.current_x = pos.x;
            card.current_y = pos.y;

            return true;
        }

        return false;
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

    public static boolean UseArtifact(AbstractCreature target)
    {
        ArtifactPower artifact = JUtils.SafeCast(target.getPower(ArtifactPower.POWER_ID), ArtifactPower.class);
        if (artifact != null)
        {
            GameActions.Top.Add(new TextAboveCreatureAction(target, ApplyPowerAction.TEXT[0]));
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
        AbstractPlayer p = player;
        if (p.hasPower(PenNibPower.POWER_ID))
        {
            GameActions.Bottom.ReducePower(p, PenNibPower.POWER_ID, 1);

            AbstractRelic relic = p.getRelic(PenNib.ID);
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
            CardGroup hand = player.hand;
            hand.refreshHandLayout();
            hand.applyPowers();
            hand.glowCheck();
        }
    }
}
