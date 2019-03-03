package eatyourbeets.powers;

import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import eatyourbeets.Utilities;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.subscribers.*;

import java.util.ArrayList;

public class PlayerStatistics extends AnimatorPower implements InvisiblePower
{
    public static final String POWER_ID = CreateFullID(PlayerStatistics.class.getSimpleName());

    public static final PlayerStatistics Instance = new PlayerStatistics();

    public static final GameEvent<OnAttackSubscriber> onAttack = new GameEvent<>();
    public static final GameEvent<OnApplyPowerSubscriber> onApplyPower = new GameEvent<>();
    public static final GameEvent<OnBattleStartSubscriber> onBattleStart = new GameEvent<>();
    public static final GameEvent<OnBattleEndSubscriber> onBattleEnd = new GameEvent<>();
    public static final GameEvent<OnAfterCardDrawnSubscriber> onAfterCardDrawn = new GameEvent<>();
    public static final GameEvent<OnEndOfTurnSubscriber> onEndOfTurn = new GameEvent<>();
    public static final GameEvent<OnLoseHpSubscriber> onLoseHp = new GameEvent<>();
    public static final GameEvent<OnStartOfTurnPostDrawSubscriber> onStartOfTurnPostDraw = new GameEvent<>();

    private static int turnCount = 0;
    private static int cardsDrawnThisTurn = 0;

    protected PlayerStatistics()
    {
        super(null, POWER_ID);
    }

    private static void ClearStats()
    {
        logger.info("Clearing Player Stats");

        AnimatorCard.SetLastCardPlayed(null);
        cardsDrawnThisTurn = 0;
        turnCount = 0;

        onAttack.Clear();
        onLoseHp.Clear();
        onLoseHp.Clear();
        onEndOfTurn.Clear();
        onApplyPower.Clear();
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

    public void OnBattleStart()
    {
        ClearStats();
        onBattleEnd.Clear();
        onBattleStart.Clear();
        for (AbstractCard c : AbstractDungeon.player.drawPile.group)
        {
            OnBattleStartSubscriber s = Utilities.SafeCast(c, OnBattleStartSubscriber.class);
            if (s != null)
            {
                onBattleStart.Subscribe(s);
                s.OnBattleStart();
            }
        }
    }

    public void OnBattleEnd()
    {
        for (OnBattleEndSubscriber s : onBattleEnd.GetSubscribers())
        {
            s.OnBattleEnd();
        }
        onBattleEnd.Clear();
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

    public static int getCardsDrawnThisTurn()
    {
        if(!InBattle() && cardsDrawnThisTurn != 0)
        {
            ClearStats();
        }

        return cardsDrawnThisTurn;
    }

    public static int getTurnCount()
    {
        if(!InBattle() && turnCount != 0)
        {
            ClearStats();
        }

        return turnCount;
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

        for (OnApplyPowerSubscriber p : onApplyPower.GetSubscribers())
        {
            p.OnApplyPower(power, target, source);
        }
    }

    @Override
    public void onAfterUseCard(AbstractCard card, UseCardAction action)
    {
        super.onAfterUseCard(card, action);

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

    //@Override
    //public void onRemove()
    //{
    //    super.onRemove();
    //    if (AbstractDungeon.getCurrRoom() != null && !AbstractDungeon.getCurrRoom().isBattleOver)
    //    {
    //        if (!AbstractDungeon.player.powers.contains(this))
    //        {
    //            AbstractDungeon.player.powers.add(this);
    //        }
    //    }
    //}

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

    public static ArrayList<AbstractMonster> GetCurrentEnemies(boolean aliveOnly)
    {
        ArrayList<AbstractMonster> monsters = new ArrayList<>();
        AbstractRoom room = GetCurrentRoom();
        if (room != null && room.monsters != null)
        {
            for (AbstractMonster m : room.monsters.monsters)
            {
                if (!aliveOnly || (!m.isDeadOrEscaped() && !m.isDying))
                {
                    monsters.add(m);
                }
            }
        }

        return monsters;
    }
}
