package eatyourbeets.monsters;

import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.interfaces.delegates.FuncT1;
import eatyourbeets.utilities.JavaUtilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

public class EYBMoveset
{
    public enum Mode
    {
        Random,
        Sequential
    }

    public final ArrayList<EYBAbstractMove> special = new ArrayList<>();
    public final ArrayList<EYBAbstractMove> rotation = new ArrayList<>();
    public final ArrayList<EYBAbstractMove> sequence = new ArrayList<>();
    public FuncT1<EYBAbstractMove, Integer> findSpecialMove;
    public Mode mode = Mode.Random;
    public int currentIndex;

    private final AbstractMonster owner;
    private final HashMap<Byte, EYBAbstractMove> moves = new HashMap<>();
    private transient byte counter;

    public EYBMoveset(AbstractMonster owner)
    {
        this.owner = owner;
    }

    public <T extends EYBAbstractMove> T AddNormal(T move)
    {
        return AddNormal(move, -1);
    }

    public <T extends EYBAbstractMove> T AddNormal(T move, int uses)
    {
        move.uses = uses;
        move.Initialize(counter, owner);
        moves.put(counter, move);

        rotation.add(move);
        counter += 1;

        return move;
    }

    public <T extends EYBAbstractMove> T AddSpecial(T move, int uses)
    {
        move.uses = uses;
        move.Initialize(counter, owner);
        moves.put(counter, move);

        special.add(move);
        counter += 1;

        return move;
    }

    public <T extends EYBAbstractMove> T AddSpecial(T move)
    {
        return AddSpecial(move, -1);
    }

    public EYBAbstractMove FindNextMove(int roll, Byte previousMove)
    {
        if (sequence.isEmpty())
        {
            sequence.addAll(rotation);

            if (mode == Mode.Random)
            {
                Collections.shuffle(sequence, new Random(roll));
            }
        }

        EYBAbstractMove move = null;

        if (findSpecialMove != null)
        {
            move = findSpecialMove.Invoke(roll);
        }

        if (move == null)
        {
            move = sequence.remove(0);
        }

        if (!move.CanUse(previousMove))
        {
            return FindNextMove(roll, previousMove);
        }

        return move;
    }

    public <T extends EYBAbstractMove> T GetMove(Class<T> type)
    {
        for (EYBAbstractMove m : moves.values())
        {
            T res = JavaUtilities.SafeCast(m, type);
            if (res != null)
            {
                return res;
            }
        }

        return null;
    }

    public EYBAbstractMove GetMove(byte code)
    {
        return moves.get(code);
    }

    public EYBMoveset SetFindSpecialMove(FuncT1<EYBAbstractMove, Integer> findSpecialMove)
    {
        this.findSpecialMove = findSpecialMove;

        return this;
    }
}