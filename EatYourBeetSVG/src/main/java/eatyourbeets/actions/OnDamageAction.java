package eatyourbeets.actions;

import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.function.BiConsumer;

public class OnDamageAction extends AnimatorAction
{
    private final DamageAction damageAction;
    private final boolean includeMinions;
    private final BiConsumer<Object, AbstractMonster> onDamage;
    private final Object state;

    public OnDamageAction(AbstractCreature target, DamageAction damageAction, BiConsumer<Object, AbstractMonster> onDamage, Object context, boolean includeMinions)
    {
        this.onDamage = onDamage;
        this.state = context;
        this.includeMinions = includeMinions;
        this.target = target;
        this.duration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.DAMAGE;
        this.damageAction = damageAction;
    }

    public void update()
    {
        if (updateDamageAction())
        {
            AbstractMonster monster = ((AbstractMonster)this.target);
            if (includeMinions || !monster.hasPower("Minion"))
            {
                onDamage.accept(state, monster);
            }

            this.isDone = true;
        }
    }

    private boolean updateDamageAction()
    {
        if (!damageAction.isDone)
        {
            damageAction.update();
        }

        return damageAction.isDone;
    }
}
