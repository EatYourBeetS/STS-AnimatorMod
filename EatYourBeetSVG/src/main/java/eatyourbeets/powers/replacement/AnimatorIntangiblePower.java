package eatyourbeets.powers.replacement;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import eatyourbeets.interfaces.subscribers.OnModifyDamageFirstSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;

public class AnimatorIntangiblePower extends IntangiblePlayerPower implements OnModifyDamageFirstSubscriber, CloneablePowerInterface
{
    public AnimatorIntangiblePower(AbstractCreature owner, int amount)
    {
        super(owner, amount);

        this.ID = "Animator" + ID;
    }

    @Override
    public void onInitialApplication()
    {
        super.onInitialApplication();

        CombatStats.onModifyDamageFirst.Subscribe(this);
    }

    @Override
    public void onRemove()
    {
        super.onRemove();

        CombatStats.onModifyDamageFirst.Unsubscribe(this);
    }

    @Override
    public void updateDescription()
    {
        this.description = GR.Tooltips.Intangible.description;
    }

    @Override
    public int OnModifyDamageFirst(AbstractCreature target, DamageInfo info, int damage)
    {
        return (target == owner && info.type != DamageInfo.DamageType.NORMAL) ? damage / 2 : damage;
    }

    @Override
    public float atDamageFinalReceive(float damage, DamageInfo.DamageType type)
    {
        return type == DamageInfo.DamageType.NORMAL ? damage * 0.5f : damage;
    }

    @Override
    public float atDamageFinalGive(float damage, DamageInfo.DamageType type)
    {
        return type == DamageInfo.DamageType.NORMAL ? damage * 0.5f : damage;
    }

    @Override
    public void atEndOfRound()
    {
        GameActions.Bottom.ReducePower(this, 1);
    }

    @Override
    public AbstractPower makeCopy()
    {
        return new AnimatorIntangiblePower(owner, amount);
    }
}
