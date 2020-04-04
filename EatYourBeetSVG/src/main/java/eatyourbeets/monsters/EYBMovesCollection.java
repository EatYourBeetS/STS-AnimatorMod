package eatyourbeets.monsters;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import eatyourbeets.monsters.SharedMoveset.*;
import eatyourbeets.monsters.SharedMoveset.special.EYBMove_ShuffleCard;
import eatyourbeets.powers.PowerHelper;

import java.util.ArrayList;

public class EYBMovesCollection
{
    public final ArrayList<EYBAbstractMove> rotation = new ArrayList<>();
    public final EYBMoveset moveset;
    public final boolean isSpecial;

    public EYBMovesCollection(EYBMoveset moveset, boolean isSpecial)
    {
        this.isSpecial = isSpecial;
        this.moveset = moveset;
    }

    public <T extends EYBAbstractMove> T Add(T move)
    {
        if (move.attackEffect == null)
        {
            move.attackEffect = moveset.attackEffect;
        }
        if (move.attackAnimation == null)
        {
            move.attackAnimation = moveset.attackAnimation;
        }

        move.Initialize(moveset.counter, moveset.owner);
        moveset.moves.put(moveset.counter, move);
        rotation.add(move);
        moveset.counter += 1;
        return move;
    }

    public EYBMove_Attack Attack(int damage)
    {
        return Add(new EYBMove_Attack(damage));
    }

    public EYBMove_Attack Attack(int damage, int times)
    {
        return Add(new EYBMove_Attack(damage, times));
    }

    public EYBMove_AttackDebuff AttackDebuff(int damage, PowerHelper power, int debuff)
    {
        return Add(new EYBMove_AttackDebuff(damage, power, debuff));
    }

    public EYBMove_AttackDebuff AttackDebuff(int damage, int times, PowerHelper power, int debuff)
    {
        return Add(new EYBMove_AttackDebuff(damage, times, power, debuff));
    }

    public EYBMove_AttackDefend AttackDefend(int damage, int block)
    {
        return Add(new EYBMove_AttackDefend(damage, block));
    }

    public EYBMove_AttackDefend AttackDefend(int damage, int times, int block)
    {
        return Add(new EYBMove_AttackDefend(damage, times, block));
    }

    public EYBMove_AttackBuff AttackBuff(int damage, PowerHelper power, int buff)
    {
        return Add(new EYBMove_AttackBuff(damage, power, buff));
    }

    public EYBMove_AttackBuff AttackBuff(int damage, int times, PowerHelper power, int buff)
    {
        return Add(new EYBMove_AttackBuff(damage, times, power, buff));
    }

    public EYBMove_Defend Defend(int block)
    {
        return Add(new EYBMove_Defend(block));
    }

    public EYBMove_DefendBuff DefendBuff(int block, PowerHelper power, int buff)
    {
        return Add(new EYBMove_DefendBuff(block, power, buff));
    }

    public EYBMove_DefendBuff DefendDebuff(int block, PowerHelper power, int debuff)
    {
        return Add(new EYBMove_DefendBuff(block, power, debuff));
    }

    public EYBMove_Buff Buff(PowerHelper power, int amount)
    {
        return Add(new EYBMove_Buff(power, amount));
    }

    public EYBMove_Debuff Debuff(PowerHelper power, int amount)
    {
        return Add(new EYBMove_Debuff(power, amount));
    }

    public EYBMove_StrongDebuff StrongDebuff(PowerHelper power, int amount)
    {
        return Add(new EYBMove_StrongDebuff(power, amount));
    }

    public EYBMove_ShuffleCard ShuffleCard(AbstractCard card, int amount)
    {
        return Add(new EYBMove_ShuffleCard(card, amount));
    }

    public EYBMove_ShuffleCard ShuffleCard(AbstractCard card, int amount, CardGroup.CardGroupType destination)
    {
        return Add(new EYBMove_ShuffleCard(card, amount)).SetDestination(destination);
    }
}
