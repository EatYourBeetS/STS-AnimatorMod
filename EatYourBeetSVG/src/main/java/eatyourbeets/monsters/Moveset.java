package eatyourbeets.monsters;

import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.JavaUtilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

public class Moveset
{
    public enum Mode
    {
        Random,
        Sequential
    }

    public final ArrayList<AbstractMove> special = new ArrayList<>();
    public final ArrayList<AbstractMove> rotation = new ArrayList<>();
    public final ArrayList<AbstractMove> sequence = new ArrayList<>();
    public Mode mode = Mode.Random;
    public int currentIndex = 0;

    private final AbstractMonster owner;
    private final HashMap<Byte, AbstractMove> moves = new HashMap<>();
    private byte counter = 0;

    public Moveset(AbstractMonster owner)
    {
        this.owner = owner;
    }

    public <T extends AbstractMove> T AddSpecial(T move)
    {
        return AddSpecial(move, -1);
    }

    public <T extends AbstractMove> T AddNormal(T move)
    {
        return AddNormal(move, -1);
    }

    public <T extends AbstractMove> T AddSpecial(T move, int uses)
    {
        move.uses = uses;
        move.Init(counter, owner);
        moves.put(counter, move);

        special.add(move);
        counter += 1;

        return move;
    }

    public <T extends AbstractMove> T AddNormal(T move, int uses)
    {
        move.uses = uses;
        move.Init(counter, owner);
        moves.put(counter, move);

        rotation.add(move);
        counter += 1;

        return move;
    }

    public AbstractMove GetMove(byte code)
    {
        return moves.get(code);
    }

    public <T extends AbstractMove> T GetMove(Class<T> type)
    {
        for (AbstractMove m : moves.values())
        {
            T res = JavaUtilities.SafeCast(m, type);
            if (res != null)
            {
                return res;
            }
        }

        return null;
    }

    public AbstractMove GetNextMove(int roll, Byte previousMove)
    {
        if (sequence.isEmpty())
        {
            sequence.addAll(rotation);

            if (mode == Mode.Random)
            {
                Collections.shuffle(sequence, new Random(roll));
            }
        }

        AbstractMove move = sequence.remove(0);

        if (!move.CanUse(previousMove))
        {
            return GetNextMove(roll, previousMove);
        }

        return move;
    }
}