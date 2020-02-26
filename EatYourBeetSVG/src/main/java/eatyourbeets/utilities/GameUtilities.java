package eatyourbeets.utilities;

import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.TextAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.curses.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.GetAllInBattleInstances;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.*;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.ChemicalX;
import com.megacrit.cardcrawl.relics.PenNib;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import eatyourbeets.cards.animator.curse.Curse_Greed;
import eatyourbeets.cards.animator.curse.Curse_GriefSeed;
import eatyourbeets.cards.animator.curse.Curse_Nutcracker;
import eatyourbeets.interfaces.subscribers.OnAddingToCardReward;
import eatyourbeets.interfaces.subscribers.OnPhaseChangedSubscriber;
import eatyourbeets.orbs.animator.Aether;
import eatyourbeets.orbs.animator.Earth;
import eatyourbeets.orbs.animator.Fire;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.powers.common.TemporaryBiasPower;
import eatyourbeets.powers.unnamed.ResonancePower;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.function.Predicate;

public class GameUtilities
{
    private static final OnPhaseChangedSubscriber handLayoutRefresher = new HandLayoutRefresher();
    private static final WeightedList<AbstractOrb> orbs = new WeightedList<>();

    public static int GetActualAscensionLevel()
    {
        if (AbstractDungeon.isAscensionMode)
        {
            return AbstractDungeon.ascensionLevel;
        }

        return 0;
    }

    public static ArrayList<AbstractCreature> GetAllCharacters(boolean aliveOnly)
    {
        ArrayList<AbstractCreature> characters = new ArrayList<>();
        AbstractRoom room = GetCurrentRoom();
        if (room != null && room.monsters != null)
        {
            for (AbstractMonster m : room.monsters.monsters)
            {
                if (!aliveOnly || (!m.isDeadOrEscaped() && !m.isDying))
                {
                    characters.add(m);
                }
            }
        }

        characters.add(AbstractDungeon.player);

        return characters;
    }

    public static HashSet<AbstractCard> GetAllCopies(AbstractCard card)
    {
        String cardID = card.cardID;
        HashSet<AbstractCard> cards = new HashSet<>();
        AbstractCard c;

        c = AbstractDungeon.player.cardInUse;
        if (c != null && c.cardID.equals(cardID))
        {
            cards.add(c);
        }

        Iterator var2 = AbstractDungeon.player.drawPile.group.iterator();
        while (var2.hasNext())
        {
            c = (AbstractCard) var2.next();
            if (c.cardID.equals(cardID))
            {
                cards.add(c);
            }
        }

        var2 = AbstractDungeon.player.discardPile.group.iterator();
        while (var2.hasNext())
        {
            c = (AbstractCard) var2.next();
            if (c.cardID.equals(cardID))
            {
                cards.add(c);
            }
        }

        var2 = AbstractDungeon.player.exhaustPile.group.iterator();
        while (var2.hasNext())
        {
            c = (AbstractCard) var2.next();
            if (c.cardID.equals(cardID))
            {
                cards.add(c);
            }
        }

        var2 = AbstractDungeon.player.limbo.group.iterator();
        while (var2.hasNext())
        {
            c = (AbstractCard) var2.next();
            if (c.cardID.equals(cardID))
            {
                cards.add(c);
            }
        }

        var2 = AbstractDungeon.player.hand.group.iterator();
        while (var2.hasNext())
        {
            c = (AbstractCard) var2.next();
            if (c.cardID.equals(cardID))
            {
                cards.add(c);
            }
        }

        return cards;
    }

    public static HashSet<AbstractCard> GetAllInBattleInstances(AbstractCard card)
    {
        HashSet<AbstractCard> cards = GetAllInBattleInstances.get(card.uuid);

        cards.add(card);

        return cards;
    }

    public static HashSet<AbstractCard> GetAllInstances(AbstractCard card)
    {
        HashSet<AbstractCard> cards = GetAllInBattleInstances(card);

        AbstractCard masterDeckInstance = GetMasterDeckInstance(card);
        if (masterDeckInstance != null)
        {
            cards.add(masterDeckInstance);
        }

        return cards;
    }

    public static int GetAscensionLevel()
    {
        if (AbstractDungeon.isAscensionMode)
        {
            return Math.max(0, Math.min(20, AbstractDungeon.ascensionLevel));
        }

        return 0;
    }

    public static AbstractCard GetRandomCurse()
    {
        ArrayList<AbstractCard> curses;

        curses = new ArrayList<>();
        curses.add(new Clumsy());
        curses.add(new Decay());
        curses.add(new Doubt());
        curses.add(new Injury());
        curses.add(new Normality());
        curses.add(new Pain());
        curses.add(new Parasite());
        curses.add(new Regret());
        curses.add(new Shame());
        curses.add(new Writhe());
        curses.add(new Curse_Greed());
        curses.add(new Curse_Nutcracker());
        curses.add(new Curse_GriefSeed());
        //curses.add(new Pride());
        //curses.add(new Necronomicurse());

        return JavaUtilities.GetRandomElement(curses).makeCopy();
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

            if (temp instanceof OnAddingToCardReward && ((OnAddingToCardReward)temp).ShouldCancel(rewardItem))
            {
                searchingCard = true;
            }

            if (!searchingCard)
            {
                replacement = temp.makeCopy();
            }
        }

        return replacement;
    }

    public static CardGroup GetCardPool(AbstractCard.CardRarity rarity, AbstractCard.CardColor color)
    {
        if (color == AbstractCard.CardColor.COLORLESS)
        {
            return AbstractDungeon.colorlessCardPool;
        }

        switch (rarity)
        {
            case COMMON: return AbstractDungeon.commonCardPool;
            case UNCOMMON: return AbstractDungeon.uncommonCardPool;
            case RARE: return AbstractDungeon.rareCardPool;

            default:
                return null;
        }
    }

    public static CardGroup GetSourceCardPool(AbstractCard.CardRarity rarity, AbstractCard.CardColor color)
    {
        if (color == AbstractCard.CardColor.COLORLESS)
        {
            return AbstractDungeon.colorlessCardPool;
        }

        switch (rarity)
        {
            case COMMON: return AbstractDungeon.srcCommonCardPool;
            case UNCOMMON: return AbstractDungeon.srcUncommonCardPool;
            case RARE: return AbstractDungeon.srcRareCardPool;

            default:
                return null;
        }
    }

    public static AbstractCard GetRandomRewardCard(boolean includeRares)
    {
        ArrayList<AbstractCard> list;
        int roll = AbstractDungeon.cardRng.random(100);
        if (roll <= 4 && includeRares)
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
        else
        {
            return null;
        }
    }

    public static ArrayList<AbstractMonster> GetCurrentEnemies(boolean aliveOnly)
    {
        ArrayList<AbstractMonster> monsters = new ArrayList<>();
        AbstractRoom room = GetCurrentRoom();
        if (room != null && room.monsters != null)
        {
            for (AbstractMonster m : room.monsters.monsters)
            {
                if (!aliveOnly || (!m.isDeadOrEscaped() && m.currentHealth >0))
                {
                    monsters.add(m);
                }
            }
        }

        return monsters;
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

    public static boolean Retain(AbstractCard card)
    {
        if (!card.isEthereal && !card.retain)
        {
            card.retain = true;
            return true;
        }

        return false;
    }

    public static int GetDebuffsCount(AbstractCreature creature)
    {
        if (creature == null || creature.powers == null)
        {
            return 0;
        }

        return GetDebuffsCount(creature.powers);
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

    public static float GetHealthPercentage(AbstractCreature creature)
    {
        return creature.currentHealth / (float)creature.maxHealth;
    }

    public static AbstractCard GetMasterDeckInstance(String cardID)
    {
        for (AbstractCard c : AbstractDungeon.player.masterDeck.group)
        {
            if (cardID.equals(c.cardID))
            {
                return c;
            }
        }

        return null;
    }

    public static AbstractCard GetMasterDeckInstance(AbstractCard card)
    {
        for (AbstractCard c : AbstractDungeon.player.masterDeck.group)
        {
            if (c.uuid == card.uuid)
            {
                return c;
            }
        }

        return null;
    }

    public static HashSet<AbstractCard> GetOtherCardsInHand(AbstractCard card)
    {
        HashSet<AbstractCard> cards = new HashSet<>();
        for (AbstractCard c : AbstractDungeon.player.hand.group)
        {
            if (c != card)
            {
                cards.add(c);
            }
        }

        return cards;
    }

    public static <T extends AbstractRelic> T GetRelic(String relicID)
    {
        for (AbstractRelic relic : AbstractDungeon.player.relics)
        {
            if (relic != null && relicID.equals(relic.relicId))
            {
                try
                {
                    return (T)relic;
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
        for (AbstractRelic relic : AbstractDungeon.player.relics)
        {
            if (relicType.isInstance(relic))
            {
                return relicType.cast(relic);
            }
        }

        return null;
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
                    return (T)p;
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
        return GetPowerAmount(AbstractDungeon.player, powerID);
    }

    public static int GetPowerAmount(AbstractCreature owner, String powerID)
    {
        AbstractPower power = GetPower(owner, powerID);
        if (power != null)
        {
            return power.amount;
        }

        return 0;
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

    public static AbstractMonster GetRandomEnemy(boolean aliveOnly)
    {
        return JavaUtilities.GetRandomElement(GetCurrentEnemies(aliveOnly));
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

        return orbs.Retrieve(AbstractDungeon.cardRandomRng, false).makeCopy();
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

    public static boolean InBattle()
    {
        AbstractRoom room = GetCurrentRoom();
        if (room != null && !room.isBattleOver && !AbstractDungeon.player.isDead)
        {
            return room.phase == AbstractRoom.RoomPhase.COMBAT || (room.monsters != null && !room.monsters.areMonstersBasicallyDead());
        }

        return false;
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

    public static boolean IsBossRoom()
    {
        return GetCurrentRoom() instanceof MonsterRoomBoss;
    }

    public static boolean IsEliteRoom()
    {
        AbstractRoom room = GetCurrentRoom();
        if (room != null)
        {
            return room.eliteTrigger;
        }

        return false;
    }

    public static boolean TriggerOnKill(AbstractCreature enemy, boolean includeMinions)
    {
        return IsDeadOrEscaped(enemy) && !enemy.hasPower(RegrowPower.POWER_ID) && (includeMinions || !enemy.hasPower(MinionPower.POWER_ID));
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

    public static boolean UseArtifact(AbstractCreature target)
    {
        ArtifactPower artifact = JavaUtilities.SafeCast(target.getPower(ArtifactPower.POWER_ID), ArtifactPower.class);
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

    public static void ApplyTemporaryDexterity(AbstractCreature source, AbstractCreature target, int amount)
    {
        if (UseArtifact(target))
        {
            GameActions.Top.ApplyPowerSilently(source, target, new LoseDexterityPower(target, amount), amount);
        }

        GameActions.Top.ApplyPower(source, target, new DexterityPower(target, amount), amount);
    }

    public static void ApplyTemporaryFocus(AbstractCreature source, AbstractCreature target, int amount)
    {
        if (UseArtifact(target))
        {
            GameActions.Top.ApplyPowerSilently(source, target, new TemporaryBiasPower(target, amount), amount);
        }

        GameActions.Top.ApplyPower(source, target, new FocusPower(target, amount), amount);
    }

    public static void ApplyTemporaryStrength(AbstractCreature source, AbstractCreature target, int amount)
    {
        if (UseArtifact(target))
        {
            GameActions.Top.ApplyPowerSilently(source, target, new LoseStrengthPower(target, amount), amount);
        }

        GameActions.Top.ApplyPower(source, target, new StrengthPower(target, amount), amount);
    }

    public static void ClearPostCombatActions()
    {
        AbstractDungeon.actionManager.clearPostCombatActions();
    }

    public static CardGroup FindCardGroup(AbstractCard card, boolean includeLimbo)
    {
        AbstractPlayer player = AbstractDungeon.player;
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

    public static int GetXCostEnergy(AbstractCard card)
    {
        int amount = EnergyPanel.getCurrentEnergy();

        if (card.energyOnUse != -1)
        {
            amount = card.energyOnUse;
        }

        if (AbstractDungeon.player.hasRelic(ChemicalX.ID))
        {
            amount += ChemicalX.BOOST;
        }

        return amount;
    }

    public static int UseXCostEnergy(AbstractCard card)
    {
        int amount = EnergyPanel.getCurrentEnergy();

        if (card.energyOnUse != -1)
        {
            amount = card.energyOnUse;
        }

        if (AbstractDungeon.player.hasRelic(ChemicalX.ID))
        {
            amount += ChemicalX.BOOST;
            AbstractDungeon.player.getRelic(ChemicalX.ID).flash();
        }

        if (!card.freeToPlayOnce)
        {
            EnergyPanel.useEnergy(card.energyOnUse);
        }

        RefreshHandLayout();

        return amount;
    }

    public static void RefreshHandLayout()
    {
        PlayerStatistics.onPhaseChanged.Subscribe(handLayoutRefresher);
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

    public static boolean IsPlayerClass(AbstractPlayer.PlayerClass playerClass)
    {
        return AbstractDungeon.player != null && AbstractDungeon.player.chosenClass == playerClass;
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

    public static boolean IsValidOrb(AbstractOrb orb)
    {
        return orb != null && !(orb instanceof EmptyOrbSlot);
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

    public static boolean RequiresTarget(AbstractCard card)
    {
        return card.target == AbstractCard.CardTarget.ENEMY || card.target == AbstractCard.CardTarget.SELF_AND_ENEMY;
    }

    public static HashSet<AbstractCard> GetAllCopies(String cardID, CardGroup group)
    {
        HashSet<AbstractCard> result = new HashSet<>();
        for (AbstractCard card : group.group)
        {
            if (cardID.equals(card.cardID))
            {
                result.add(card);
            }
        }

        return result;
    }

    public static boolean IsInGame()
    {
        return CardCrawlGame.GameMode.GAMEPLAY.equals(CardCrawlGame.mode) && AbstractDungeon.player != null;
    }

    private static class HandLayoutRefresher implements OnPhaseChangedSubscriber
    {
        @Override
        public void OnPhaseChanged(GameActionManager.Phase phase)
        {
            if (phase == GameActionManager.Phase.WAITING_ON_USER)
            {
                CardGroup hand = AbstractDungeon.player.hand;
                hand.refreshHandLayout();
                hand.applyPowers();
                hand.glowCheck();

                PlayerStatistics.onPhaseChanged.Unsubscribe(handLayoutRefresher);
            }
        }
    }
}
