package eatyourbeets.monsters;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.interfaces.delegates.FuncT1;
import eatyourbeets.utilities.JUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

public class EYBMoveset
{
    public enum Mode
    {
        Random,
        Sequential,
        Repeat
    }

    public final AbstractMonster owner;
    public final HashMap<Byte, EYBAbstractMove> moves = new HashMap<>();
    public final ArrayList<EYBAbstractMove> sequence = new ArrayList<>();
    public final EYBMovesCollection Normal = new EYBMovesCollection(this, false);
    public final EYBMovesCollection Special = new EYBMovesCollection(this, true);

    public FuncT1<EYBAbstractMove, Integer> findSpecialMove;
    public Mode mode = Mode.Random;
    public AbstractGameAction.AttackEffect attackEffect;
    public AbstractCreature.CreatureAnimation attackAnimation;

    protected transient byte counter;

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
        return (T)Normal.Add(move).SetUses(uses);
    }

    public <T extends EYBAbstractMove> T AddSpecial(T move, int uses)
    {
        return (T)Special.Add(move).SetUses(uses);
    }

    public <T extends EYBAbstractMove> T AddSpecial(T move)
    {
        return AddSpecial(move, -1);
    }

    public EYBAbstractMove FindNextMove(int roll, Byte previousMove)
    {
        if (sequence.isEmpty())
        {
            sequence.addAll(Normal.rotation);

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
            if (mode == Mode.Repeat && previousMove != null)
            {
                final ArrayList<Byte> history = owner.moveHistory;
                for (int i = history.size() - 1; i >= 0; i--)
                {
                    move = GetMove(history.get(i));

                    if (Normal.rotation.contains(move) && move.CanUse(null))
                    {
                        return move;
                    }
                }
            }

            move = sequence.remove(0);

            if (!move.CanUse(previousMove))
            {
                return FindNextMove(roll, previousMove);
            }
        }

        return move;
    }

    public <T extends EYBAbstractMove> T GetMove(Class<T> type)
    {
        for (EYBAbstractMove m : moves.values())
        {
            T res = JUtils.SafeCast(m, type);
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

    public EYBMoveset SetAttackEffect(AbstractGameAction.AttackEffect effect)
    {
        this.attackEffect = effect;

        return this;
    }

    public EYBMoveset SetAttackAnimation(AbstractCreature.CreatureAnimation animation)
    {
        this.attackAnimation = animation;

        return this;
    }
}