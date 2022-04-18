package eatyourbeets.actions.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.PowerExpireTextEffect;
import eatyourbeets.actions.EYBActionWithCallback;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameEffects;

public class RemovePower extends EYBActionWithCallback<AbstractPower>
{
    private String powerID;
    private AbstractPower powerInstance;
    private boolean isDebuffInteraction;

    public RemovePower(AbstractCreature target, AbstractCreature source, String powerID)
    {
        super(ActionType.DEBUFF);

        this.powerID = powerID;

        Initialize(source, target, 1);
    }

    public RemovePower(AbstractCreature target, AbstractCreature source, AbstractPower powerInstance)
    {
        super(ActionType.DEBUFF);

        this.powerInstance = powerInstance;

        Initialize(source, target, 1);
    }

    public RemovePower IsDebuffInteraction(boolean value)
    {
        isDebuffInteraction = value;

        return this;
    }

    @Override
    protected void FirstUpdate()
    {
        if (this.target.isDeadOrEscaped())
        {
            Complete(null);
            return;
        }

        AbstractPower toRemove = null;
        if (this.powerID != null)
        {
            toRemove = this.target.getPower(this.powerID);
        }
        else if (this.powerInstance != null && this.target.powers.contains(this.powerInstance))
        {
            toRemove = this.powerInstance;
        }

        if (toRemove != null)
        {
            GameEffects.List.Add(new PowerExpireTextEffect(this.target.hb.cX - this.target.animX, this.target.hb.cY + this.target.hb.height / 2.0F, toRemove.name, toRemove.region128));
            toRemove.onRemove();
            this.target.powers.remove(toRemove);
            AbstractDungeon.onModifyPower();

            for (AbstractOrb o : AbstractDungeon.player.orbs)
            {
                o.updateDescription();
            }

            if (isDebuffInteraction)
            {
                CombatStats.OnModifyDebuff(toRemove, toRemove.amount, 0);
            }

            Complete(toRemove);
        }
        else
        {
            this.duration = 0.0F;
        }
    }
}
