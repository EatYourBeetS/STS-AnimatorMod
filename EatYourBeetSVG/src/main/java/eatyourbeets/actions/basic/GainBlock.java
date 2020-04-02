package eatyourbeets.actions.basic;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import eatyourbeets.actions.EYBActionWithCallback;
import eatyourbeets.utilities.GameEffects;

public class GainBlock extends EYBActionWithCallback<AbstractCreature>
{
    protected boolean mute;

    public GainBlock(AbstractCreature target, AbstractCreature source, int amount)
    {
        this(target, source, amount, false);
    }

    public GainBlock(AbstractCreature target, AbstractCreature source, int amount, boolean superFast)
    {
        super(ActionType.BLOCK, superFast ? Settings.ACTION_DUR_XFAST : Settings.ACTION_DUR_FAST);

        Initialize(source, target, amount);

        if (amount <= 0)
        {
            Complete();
        }
    }

    public GainBlock SetVFX(boolean mute, boolean superFast)
    {
        this.mute = mute;
        this.startDuration = this.duration = superFast ? Settings.ACTION_DUR_XFAST : Settings.ACTION_DUR_FAST;

        return this;
    }

    @Override
    protected void FirstUpdate()
    {
        if (!this.target.isDying && !this.target.isDead && amount > 0)
        {
            GameEffects.List.Add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, AttackEffect.SHIELD, mute));

            this.target.addBlock(amount);

            for (AbstractCard c : player.hand.group)
            {
                c.applyPowers();
            }
        }
    }

    @Override
    protected void UpdateInternal(float deltaTime)
    {
        if (TickDuration(deltaTime))
        {
            Complete(target);
        }
    }
}
