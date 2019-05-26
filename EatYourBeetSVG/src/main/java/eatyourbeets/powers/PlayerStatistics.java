package eatyourbeets.powers;

import basemod.DevConsole;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.Utilities;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.misc.RandomizedList;
import eatyourbeets.subscribers.*;

import java.util.ArrayList;

public class PlayerStatistics extends AnimatorPower implements InvisiblePower
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
    public static final GameEvent<OnAttackSubscriber> onAttack = new GameEvent<>();
    public static final GameEvent<OnLoseHpSubscriber> onLoseHp = new GameEvent<>();
    public static final GameEvent<OnEndOfTurnSubscriber> onEndOfTurn = new GameEvent<>();
    public static final GameEvent<OnAfterCardPlayedSubscriber> onAfterCardPlayed = new GameEvent<>();
    public static final GameEvent<OnApplyPowerSubscriber> onApplyPower = new GameEvent<>();
    public static final GameEvent<OnSynergySubscriber> onSynergy = new GameEvent<>();
    public static final GameEvent<OnAfterDeathSubscriber> onAfterDeath = new GameEvent<>();
    public static final GameEvent<OnStartOfTurnPostDrawSubscriber> onStartOfTurnPostDraw = new GameEvent<>();

    public static boolean LoadingPlayerSave;

    private static int turnDamageMultiplier = 0;
    private static int turnCount = 0;
    private static int cardsDrawnThisTurn = 0;
    private static int cardsExhaustedThisTurn = 0;
    private static int synergiesThisTurn = 0;

    protected PlayerStatistics()
    {
        super(null, POWER_ID);
    }

    private static void ClearStats()
    {
        logger.info("Clearing Player Stats");

        AnimatorCard.SetLastCardPlayed(null);
        synergiesThisTurn = 0;
        cardsExhaustedThisTurn = 0;
        cardsDrawnThisTurn = 0;
        turnDamageMultiplier = 0;
        turnCount = 0;

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
        onApplyPower.Clear();
        onAfterDeath.Clear();
        onStartOfTurnPostDraw.Clear();
    }

    public static void EnsurePowerIsApplied()
    {
        if (!AbstractDungeon.player.powers.contains(Instance))
        {
            logger.info("Applied PlayerStatistics");

            AbstractDungeon.player.powers.add(Instance);
        }
    }

    public static void OnStartOver()
    {
        ClearStats();
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

    public static int getTurnCount()
    {
        return turnCount;
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
        logger.info("ON APPLY POWER: " + onApplyPower.Count());
        super.onApplyPower(power, target, source);

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

    @Override
    public void atEndOfTurn(boolean isPlayer)
    {
        super.atEndOfTurn(isPlayer);

        for (OnEndOfTurnSubscriber s : onEndOfTurn.GetSubscribers())
        {
            s.OnEndOfTurn(isPlayer);
        }

        turnDamageMultiplier = 0;
        cardsExhaustedThisTurn = 0;
        synergiesThisTurn = 0;
        cardsDrawnThisTurn = 0;
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

    public static AbstractMonster GetRandomEnemy(boolean aliveOnly)
    {
        return Utilities.GetRandomElement(GetCurrentEnemies(aliveOnly));
    }

    public static AbstractCreature GetRandomCharacter(boolean aliveOnly)
    {
        RandomizedList<AbstractMonster> enemies = new RandomizedList<>(GetCurrentEnemies(aliveOnly));

        AbstractCreature result = enemies.Retrieve(AbstractDungeon.miscRng, false);
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

    public static int GetDexterity(AbstractCreature creature)
    {
        DexterityPower power = (DexterityPower) creature.getPower(DexterityPower.POWER_ID);
        if (power != null)
        {
            return power.amount;
        }

        return 0;
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

    public static int GetFocus(AbstractCreature creature)
    {
        FocusPower power = (FocusPower) creature.getPower(FocusPower.POWER_ID);
        if (power != null)
        {
            return power.amount;
        }

        return 0;
    }

    public static void UsePenNib()
    {
        AbstractPlayer p = AbstractDungeon.player;
        if (p.hasPower(PenNibPower.POWER_ID))
        {
            GameActionsHelper.AddToBottom(new ReducePowerAction(p, p, PenNibPower.POWER_ID, 1));
        }
    }
}
