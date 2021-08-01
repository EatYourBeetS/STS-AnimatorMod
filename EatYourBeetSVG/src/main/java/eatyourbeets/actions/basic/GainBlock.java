package eatyourbeets.actions.basic;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import eatyourbeets.actions.EYBActionWithCallback;
import eatyourbeets.effects.SFX;
import eatyourbeets.effects.vfx.FlashAttackEffect;
import eatyourbeets.utilities.GameEffects;

public class GainBlock extends EYBActionWithCallback<AbstractCreature>
{
    protected boolean mute;
    protected boolean useFrostSound;

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

    public GainBlock SetFrostSound(boolean value)
    {
        this.useFrostSound = value;

        return this;
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
        if (!target.isDying && !target.isDead && amount > 0)
        {
            FlashAttackEffect effect = GameEffects.List.Add(new FlashAttackEffect(target.hb.cX, target.hb.cY, AttackEffect.SHIELD, mute));
            if (useFrostSound)
            {
                effect.OverrideSound(SFX.GetRandom(SFX.ORB_FROST_DEFEND_1, SFX.ORB_FROST_DEFEND_2, SFX.ORB_FROST_DEFEND_3));
            }

            target.addBlock(amount);

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
