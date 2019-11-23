package eatyourbeets.monsters;

import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.JavaUtilities;

import java.util.ArrayList;
import java.util.HashMap;

public class Moveset
{
    private final AbstractMonster owner;
    private byte counter = 0;
    private final HashMap<Byte, AbstractMove> moves = new HashMap<>();

    public final ArrayList<AbstractMove> special = new ArrayList<>();
    public final ArrayList<AbstractMove> rotation = new ArrayList<>();

    public Moveset(AbstractMonster owner)
    {
        this.owner = owner;
    }

    public AbstractMove AddSpecial(AbstractMove move)
    {
        return AddSpecial(move, 999);
    }

    public AbstractMove AddNormal(AbstractMove move)
    {
        return AddNormal(move, 999);
    }

    public AbstractMove AddSpecial(AbstractMove move, int uses)
    {
        move.uses = uses;
        move.Init(counter, owner);
        moves.put(counter, move);

        special.add(move);
        counter += 1;

        return move;
    }

    public AbstractMove AddNormal(AbstractMove move, int uses)
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
}
