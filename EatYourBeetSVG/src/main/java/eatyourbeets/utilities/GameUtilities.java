package eatyourbeets.utilities;

import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.QueueCardAction;
import com.megacrit.cardcrawl.actions.utility.TextAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UnlimboAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GetAllInBattleInstances;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.*;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.relics.ChemicalX;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import eatyourbeets.interfaces.OnPhaseChangedSubscriber;
import eatyourbeets.orbs.Aether;
import eatyourbeets.orbs.Earth;
import eatyourbeets.orbs.Fire;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.powers.common.*;
import eatyourbeets.powers.unnamed.ResonancePower;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class GameUtilities
{
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

    public static ArrayList<AbstractMonster> GetCurrentEnemies(boolean aliveOnly)
    {
        ArrayList<AbstractMonster> monsters = new ArrayList<>();
        AbstractRoom room = GetCurrentRoom();
        if (room != null && room.monsters != null)
        {
            for (AbstractMonster m : room.monsters.monsters)
            {
                //logger.info("ENEMY: " + m.name + ", DeadOrEscaped: " + m.isDeadOrEscaped() + ", Dying: " + m.isDying);
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

    @SuppressWarnings("unchecked")
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
        if (orbs.Count() == 0)
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
        if (room != null && !room.isBattleOver)
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

    public static void PlayCard(AbstractCard card, AbstractMonster m)
    {
        PlayCard(card, m, false, false);
    }

    public static void PlayCard(AbstractCard card, AbstractMonster m, boolean purge, boolean exhaust)
    {
        RefreshHandLayout();

        AbstractDungeon.getCurrRoom().souls.remove(card);
        AbstractDungeon.player.limbo.group.add(card);

        card.freeToPlayOnce = true;
//        card.current_x = (float) Settings.WIDTH / 2.0F;
//        card.current_y = (float) Settings.HEIGHT / 2.0F;
        card.target_x = (float) Settings.WIDTH / 2.0F - 300.0F * Settings.scale;
        card.target_y = (float) Settings.HEIGHT / 2.0F;
        card.targetAngle = 0.0F;
        card.unfadeOut();
        card.lighten(true);
        card.drawScale = 0.5F;
        card.targetDrawScale = 0.75F;

        if (!card.canUse(AbstractDungeon.player, m))
        {
            if (purge)
            {
                GameActions.Top.Add(new UnlimboAction(card));
            }
            else if (exhaust)
            {
                GameActions.Top.Exhaust(card);
            }
            else
            {
                GameActions.Top.Discard(card, AbstractDungeon.player.limbo);
                GameActions.Top.Add(new WaitAction(0.4F));
            }
        }
        else
        {
            card.exhaustOnUseOnce = exhaust;
            card.purgeOnUse = purge;
            card.applyPowers();

            GameActions.Top.Add(new QueueCardAction(card, m));
            GameActions.Top.Add(new UnlimboAction(card));

            if (!Settings.FAST_MODE)
            {
                GameActions.Top.Add(new WaitAction(Settings.ACTION_DUR_MED));
            }
            else
            {
                GameActions.Top.Add(new WaitAction(Settings.ACTION_DUR_FASTER));
            }
        }
    }

    public static CardQueueItem PlayCopy(AbstractCard source, AbstractMonster m, boolean applyPowers)
    {
        RefreshHandLayout();

        AbstractCard temp = source.makeSameInstanceOf();
        AbstractDungeon.player.limbo.addToBottom(temp);
        temp.current_x = source.current_x;
        temp.current_y = source.current_y;
        temp.target_x = (float) Settings.WIDTH / 2.0F - 300.0F * Settings.scale;
        temp.target_y = (float) Settings.HEIGHT / 2.0F;
        temp.freeToPlayOnce = true;

        if (applyPowers)
        {
            temp.applyPowers();
        }

        temp.calculateCardDamage(m);
        temp.purgeOnUse = true;

        CardQueueItem item = new CardQueueItem(temp, m, source.energyOnUse, true);
        AbstractDungeon.actionManager.cardQueue.add(item);

        return item;
    }

    public static int UseEnergyXCost(AbstractCard card)
    {
        int amount = card.energyOnUse = EnergyPanel.getCurrentEnergy();

        if (AbstractDungeon.player.hasRelic(ChemicalX.ID))
        {
            amount += ChemicalX.BOOST;
        }

        if (!card.freeToPlayOnce && !card.ignoreEnergyOnUse)
        {
            EnergyPanel.useEnergy(card.energyOnUse);
        }

        RefreshHandLayout();

        return amount;
    }

    public static void RefreshHandLayout()
    {
        PlayerStatistics.onPhaseChanged.SubscribeOnce(handLayoutRefresher);
    }

    private final static OnPhaseChangedSubscriber handLayoutRefresher = phase ->
    {
        if (phase == GameActionManager.Phase.WAITING_ON_USER)
        {
            CardGroup hand = AbstractDungeon.player.hand;
            hand.refreshHandLayout();
            hand.applyPowers();
            hand.glowCheck();
        }
    };
}
