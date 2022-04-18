package eatyourbeets.actions.unnamed;

import eatyourbeets.actions.EYBActionWithCallback;
import eatyourbeets.interfaces.delegates.FuncT1;
import eatyourbeets.interfaces.delegates.FuncT2;
import eatyourbeets.monsters.PlayerMinions.UnnamedDoll;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GenericCondition;

import java.util.ArrayList;

public class HealDoll extends EYBActionWithCallback<ArrayList<UnnamedDoll>>
{
    protected final ArrayList<UnnamedDoll> dolls = new ArrayList<>();
    protected GenericCondition<UnnamedDoll> filter;
    protected UnnamedDoll toHeal;

    public HealDoll(int amount)
    {
        this(null, amount);
    }

    public HealDoll(UnnamedDoll doll, int amount)
    {
        super(ActionType.WAIT);

        this.toHeal = doll;

        Initialize(amount);
    }

    public HealDoll SetFilter(FuncT1<Boolean, UnnamedDoll> filter)
    {
        this.filter = GenericCondition.FromT1(filter);

        return this;
    }

    public <S> HealDoll SetFilter(S state, FuncT2<Boolean, S, UnnamedDoll> filter)
    {
        this.filter = GenericCondition.FromT2(filter, state);

        return this;
    }

    @Override
    protected void FirstUpdate()
    {
        if (toHeal != null)
        {
            Heal(toHeal);
        }
        else
        {
            for (UnnamedDoll doll : CombatStats.Dolls.GetAll())
            {
                Heal(doll);
            }
        }

        Complete(dolls);
    }

    protected void Heal(UnnamedDoll doll)
    {
        if (doll.currentHealth < doll.maxHealth && (filter == null || filter.Check(doll)))
        {
            GameActions.Bottom.Heal(player, doll, amount);
            dolls.add(doll);
        }
    }
}
