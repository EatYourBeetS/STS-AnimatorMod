package eatyourbeets.actions.basic;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import eatyourbeets.actions.EYBActionWithCallback;

public class GainBlock extends EYBActionWithCallback<AbstractCreature>
{
    public GainBlock(AbstractCreature target, AbstractCreature source, int amount)
    {
        super(ActionType.BLOCK, Settings.ACTION_DUR_FAST);

        Initialize(source, target, amount);
    }

    public GainBlock(AbstractCreature target, AbstractCreature source, int amount, boolean superFast)
    {
        super(ActionType.BLOCK, superFast ? Settings.ACTION_DUR_XFAST : Settings.ACTION_DUR_FAST);

        Initialize(source, target, amount);
    }

    @Override
    protected void FirstUpdate()
    {
        if (!this.target.isDying && !this.target.isDead)
        {
            AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, AttackEffect.SHIELD));

            this.target.addBlock(this.amount);

            for (AbstractCard c : player.hand.group)
            {
                c.applyPowers();
            }
        }
    }

    @Override
    protected void UpdateInternal()
    {
        tickDuration();

        if (isDone)
        {
            Complete(target);
        }
    }
}
