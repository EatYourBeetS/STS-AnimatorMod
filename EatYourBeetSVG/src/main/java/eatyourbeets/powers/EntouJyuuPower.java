package eatyourbeets.powers;

import com.megacrit.cardcrawl.actions.common.ModifyDamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.GameActionsHelper;

public class EntouJyuuPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(EntouJyuuPower.class.getSimpleName());

    private int baseAmount;
    private int damageBonus;

    public EntouJyuuPower(AbstractCreature owner, int damageBonus)
    {
        super(owner, POWER_ID);

        this.baseAmount = 1;
        this.amount = 1;
        this.damageBonus = damageBonus;

        updateDescription();
    }

    @Override
    public void updateDescription()
    {
        String[] desc = powerStrings.DESCRIPTIONS;

        this.description = desc[0] + amount + desc[1] + damageBonus + desc[2];
    }

    @Override
    public void atStartOfTurn()
    {
        super.atStartOfTurn();

        this.amount = this.baseAmount;
        updateDescription();
    }

    @Override
    public void stackPower(int stackAmount)
    {
        this.baseAmount += stackAmount;
        this.amount += stackAmount;
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster m)
    {
        if (amount > 0)
        {
            if (card.type == AbstractCard.CardType.ATTACK)
            {
                GameActionsHelper.AddToBottom(new ModifyDamageAction(card.uuid, this.damageBonus));
                GameActionsHelper.DrawCard(AbstractDungeon.player, 1);

                this.flash();

                amount -= 1;
                updateDescription();
            }
        }

        super.onPlayCard(card, m);
    }
}
