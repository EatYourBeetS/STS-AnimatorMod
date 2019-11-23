package eatyourbeets.utilities;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.utility.TextAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
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
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import eatyourbeets.powers.common.TemporaryBiasPower;
import eatyourbeets.powers.unnamed.ResonancePower;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class GameUtilities
{
    public static final Logger Logger = LogManager.getLogger(GameUtilities.class.getName());

    public static boolean IsAttacking(AbstractMonster.Intent intent)
    {
        return (intent == AbstractMonster.Intent.ATTACK_DEBUFF || intent == AbstractMonster.Intent.ATTACK_BUFF ||
                intent == AbstractMonster.Intent.ATTACK_DEFEND || intent == AbstractMonster.Intent.ATTACK);
    }

    public static float GetHealthPercentage(AbstractCreature creature)
    {
        return creature.currentHealth / (float)creature.maxHealth;
    }

    public static boolean IsCurseOrStatus(AbstractCard card)
    {
        return card.type == AbstractCard.CardType.CURSE || card.type == AbstractCard.CardType.STATUS;
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

    public static AbstractMonster GetRandomEnemy(boolean aliveOnly)
    {
        return JavaUtilities.GetRandomElement(GetCurrentEnemies(aliveOnly));
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
            GameActionsHelper.AddToBottom(new ReducePowerAction(p, p, PenNibPower.POWER_ID, 1));
        }
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
}
