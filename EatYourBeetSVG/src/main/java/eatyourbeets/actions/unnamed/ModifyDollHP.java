package eatyourbeets.actions.unnamed;

import eatyourbeets.actions.EYBActionWithCallback;
import eatyourbeets.interfaces.delegates.FuncT1;
import eatyourbeets.interfaces.delegates.FuncT2;
import eatyourbeets.monsters.PlayerMinions.UnnamedDoll;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GenericCondition;
import eatyourbeets.utilities.Mathf;

import java.util.ArrayList;

public class ModifyDollHP extends EYBActionWithCallback<ArrayList<UnnamedDoll>>
{
    protected final ArrayList<UnnamedDoll> dolls = new ArrayList<>();
    protected GenericCondition<UnnamedDoll> filter;
    protected UnnamedDoll toModify;
    protected boolean relative;

    public ModifyDollHP(int amount)
    {
        this(null, amount);
    }

    public ModifyDollHP(UnnamedDoll doll, int amount)
    {
        super(ActionType.WAIT);

        this.toModify = doll;
        this.relative = true;

        Initialize(amount);
    }

    public ModifyDollHP SetRelative(boolean relative)
    {
        this.relative = relative;

        return this;
    }

    public ModifyDollHP SetFilter(FuncT1<Boolean, UnnamedDoll> filter)
    {
        this.filter = GenericCondition.FromT1(filter);

        return this;
    }

    public <S> ModifyDollHP SetFilter(S state, FuncT2<Boolean, S, UnnamedDoll> filter)
    {
        this.filter = GenericCondition.FromT2(filter, state);

        return this;
    }

    @Override
    protected void FirstUpdate()
    {
        if (toModify != null)
        {
            ModifyMaxHP(toModify);
        }
        else
        {
            for (UnnamedDoll doll : CombatStats.Dolls.GetAll())
            {
                ModifyMaxHP(doll);
            }
        }

        Complete(dolls);
    }

    protected void ModifyMaxHP(UnnamedDoll doll)
    {
        if (filter == null || filter.Check(doll))
        {
            int hpChange = amount;
            if (!relative)
            {
                hpChange -= doll.maxHealth;
            }

            if (hpChange > 0)
            {
                doll.increaseMaxHp(hpChange, true);
            }
            else
            {
                doll.decreaseMaxHealth(Mathf.Abs(hpChange));
            }

            dolls.add(doll);
        }
    }
}
