package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import patches.AbstractEnums;

public class PinaCoLadaPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(PinaCoLadaPower.class.getSimpleName());

    private int baseAmount;

    public PinaCoLadaPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        this.baseAmount = amount;
        this.amount = amount;

        updateDescription();
    }

    public void atStartOfTurn()
    {
        this.amount = this.baseAmount;
        updateDescription();
    }

    @Override
    public void stackPower(int stackAmount)
    {
        super.stackPower(stackAmount);

        this.baseAmount += stackAmount;
    }

    public void onUseCard(AbstractCard card, UseCardAction action)
    {
        if ((card.costForTurn == 0 || card.freeToPlayOnce) && this.amount > 0 && !card.purgeOnUse)
        {
            this.amount -= 1;
            this.flash();
            updateDescription();

            AbstractMonster m = null;
            if (action.target != null)
            {
                m = (AbstractMonster) action.target;
            }

            AbstractCard tmp = card.makeSameInstanceOf();
            AbstractDungeon.player.limbo.addToBottom(tmp);
            tmp.current_x = card.current_x;
            tmp.current_y = card.current_y;
            tmp.target_x = (float) Settings.WIDTH / 2.0F - 300.0F * Settings.scale;
            tmp.target_y = (float) Settings.HEIGHT / 2.0F;
            tmp.freeToPlayOnce = true;
            if (m != null)
            {
                tmp.calculateCardDamage(m);
            }

            tmp.purgeOnUse = true;
            AbstractDungeon.actionManager.cardQueue.add(new CardQueueItem(tmp, m, card.energyOnUse));
        }
    }
}
